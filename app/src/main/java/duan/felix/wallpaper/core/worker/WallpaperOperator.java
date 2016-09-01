package duan.felix.wallpaper.core.worker;

import android.app.WallpaperManager;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class WallpaperOperator {

    private static final String TAG = "WallpaperOperator";

    CloseableReference<PooledByteBuffer> result = null;

    @Inject
    WallpaperManager mWallpaperManager;

    @Inject
    RetrofitFeedClient mFeedClient;

    public WallpaperOperator() {
        Global.Injector.inject(this);
    }

    public void setWallpaper(Photo photo) {
        ImagePipeline ipp = Fresco.getImagePipeline();
        DataSource<CloseableReference<PooledByteBuffer>> source =
                ipp.fetchEncodedImage(ImageRequest.fromUri(photo.urls.full), WallpaperOperator.this);
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

}
