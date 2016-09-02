package duan.felix.wallpaper.core.worker;

import android.app.WallpaperManager;
import android.graphics.Rect;
import android.net.Uri;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.helper.DisplayInfo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class WallpaperWorker {

    private static final String TAG = "WallpaperWorker";

    CloseableReference<PooledByteBuffer> result = null;

    @Inject
    WallpaperManager mWallpaperManager;

    @Inject
    RetrofitFeedClient mFeedClient;

    public WallpaperWorker() {
        Global.Injector.inject(this);
    }

    // TODO: ** two size of wallpaper
    // TODO: scale & crop wallpaper
    public void setWallpaper(Photo photo, DisplayInfo info) {
        Uri imageUri = pickImageResolution(photo, info);
        Rect rect = info.getScreenRect();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(rect.width(), rect.height())).build();
        DataSource<CloseableReference<PooledByteBuffer>> source = Fresco.getImagePipeline()
                .fetchEncodedImage(imageRequest, WallpaperWorker.this);
        BaseDataSubscriber<CloseableReference<PooledByteBuffer>> subscriber =
                new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {

                    @Override
                    protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                        if (!dataSource.isFinished()) {
                            return;
                        }
                        result = dataSource.getResult();
                        InputStream inputStream = new PooledByteBufferInputStream(result.get());
                        try {
                            mWallpaperManager.setStream(inputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtils.e(TAG, "setWallpaper() setStream fail", e);
                        } finally {
                            result.close();
                            result = null;
                        }
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                        LogUtils.e(TAG, "setWallpaper() fetch photo fail", dataSource.getFailureCause());
                    }
                };
        source.subscribe(subscriber, new DefaultExecutorSupplier(2).forBackgroundTasks());
    }

    private Uri pickImageResolution(Photo photo, DisplayInfo info) {
        if (info.getScreenRect().width() * info.getScreenRect().height() < 1080 * 1920) {
            return Uri.parse(photo.urls.regular);
        } else {
            return Uri.parse(photo.urls.full);
        }
    }

}
