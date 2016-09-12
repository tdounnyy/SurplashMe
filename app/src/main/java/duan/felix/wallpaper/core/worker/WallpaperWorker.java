package duan.felix.wallpaper.core.worker;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;

import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.model.Progress;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.ProgressEvent;
import duan.felix.wallpaper.scaffold.utils.BitmapUtils;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Decode(origin)->origin'
 * Resize(origin', reference)->resized
 * Crop(resized, reference)->cropped
 *
 * @author Felix.Duan.
 */

public class WallpaperWorker {

    private static final String TAG = "WallpaperWorker";

    @Inject
    WallpaperManager mWallpaperManager;

    @Inject
    RetrofitFeedClient mFeedClient;

    public WallpaperWorker() {
        Global.Injector.inject(this);
    }

    private Observable<Bitmap> getResizedBitmap(final Photo photo, final Rect rect) {
        LogUtils.d(TAG, "getResizedBitmap");
        return Observable.just(photo.urls.full)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(String uri) {
                        Bus.post(new ProgressEvent(Progress.State.DOWNLOADING));
                        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
                        if (rect != null
                                && photo.width > rect.width()
                                && photo.height > rect.height()) {
                            builder.setResizeOptions(new ResizeOptions(rect.width(), rect.height()));
                        }
                        final PublishSubject<Bitmap> subject = PublishSubject.create();
                        DataSource<CloseableReference<CloseableImage>> dataSource =
                                Fresco.getImagePipeline()
                                        .fetchDecodedImage(
                                                builder.build(), WallpaperWorker.this);

                        BaseBitmapDataSubscriber bitmapDataSubscriber = new BaseBitmapDataSubscriber() {
                            @Override
                            protected void onNewResultImpl(@Nullable Bitmap bitmap) {
                                if (bitmap != null) {
                                    if (rect == null
                                            || (bitmap.getWidth() <= rect.width()
                                            || bitmap.getHeight() <= rect.height())) {
                                        subject.onNext(bitmap);
                                    } else {
                                        Bus.post(new ProgressEvent(Progress.State.RESIZING));
                                        Bitmap resizedBmp = BitmapUtils.resizeOuterFit(bitmap, rect);
                                        Bitmap croppedBmp = BitmapUtils.cropCenterInside(resizedBmp, rect);
                                        subject.onNext(Bitmap.createBitmap(croppedBmp));
                                        resizedBmp.recycle();
                                        croppedBmp.recycle();
                                    }
                                }
                            }

                            @Override
                            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                                subject.onError(dataSource.getFailureCause());
                            }
                        };

                        dataSource.subscribe(bitmapDataSubscriber, new DefaultExecutorSupplier(2).forBackgroundTasks());

                        return subject;
                    }
                });
    }


    private Observable<Bitmap> getWallpaperSizeBitmap(Photo photo) {
        Bus.post(new ProgressEvent(Progress.State.MEASURING));
        int width = mWallpaperManager.getDesiredMinimumWidth();
        int height = mWallpaperManager.getDesiredMinimumHeight();
        return getResizedBitmap(photo, new Rect(0, 0, width, height));
    }

    public Observable<Boolean> setWallpaper(Photo photo) {
        LogUtils.d(TAG, "setWallpaper: " + photo);
        return getWallpaperSizeBitmap(photo)
                .map(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        try {
                            Bus.post(new ProgressEvent(Progress.State.SETTING));
                            mWallpaperManager.setBitmap(bitmap);
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Observable.error(e);
                            return false;
                        } finally {
                            System.gc();
                        }
                    }
                });
    }


}
