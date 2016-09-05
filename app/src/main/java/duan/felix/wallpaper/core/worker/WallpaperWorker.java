package duan.felix.wallpaper.core.worker;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;
import javax.inject.Inject;

import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.helper.DisplayInfo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.BitmapUtils;
import duan.felix.wallpaper.scaffold.utils.Constant;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Decode(origin)->origin'
 * Crop(origin', reference)->cropped
 * Resize(cropped, reference)->resized
 *
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

    public void storeViewPhoto(final Photo photo, final DisplayInfo info) {
        Rect rect = info.getRootViewRect();
        LogUtils.d(TAG, "view size: " + rect.width() + " " + rect.height());
        Observable.just(photo.urls.full)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(String uri) {
                        return getBitmap(ImageRequest.fromUri(uri), info.getRootViewRect());
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        LogUtils.d(TAG, "view subscribe called");
                        persistBitmap(bitmap, Constant.File.VIEW_SIZE);
                    }
                });
    }

    public void storeWallpaperSizePhoto(Photo photo) {
        Observable.just(photo.urls.full)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(String uri) {
                        int width = mWallpaperManager.getDesiredMinimumWidth();
                        int height = mWallpaperManager.getDesiredMinimumHeight();
                        return getBitmap(ImageRequest.fromUri(uri), new Rect(0, 0, width, height));
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        LogUtils.d(TAG, "wallpaper subscribe called");
                        persistBitmap(bitmap, Constant.File.WALLPAPER_SIZE);

                    }
                });

    }

    public void storeFullSizePhoto(Photo photo) {
        LogUtils.d(TAG, "fullsize");
        Observable.just(ImageRequest.fromUri(photo.urls.full))
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<ImageRequest, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(ImageRequest imageRequest) {
                        // TODO: *** fullsize bitmap OOM?
                        return getBitmap(imageRequest, null);

                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        LogUtils.d(TAG, "fullsize subscribe called");
                        persistBitmap(bitmap, Constant.File.FULL_SIZE);
                    }
                });
    }

    private Observable<Bitmap> getBitmap(ImageRequest imageRequest, @Nullable final Rect size) {

        final PublishSubject<Bitmap> subject = PublishSubject.create();
        DataSource<CloseableReference<CloseableImage>> dataSource =
                Fresco.getImagePipeline()
                        .fetchDecodedImage(
                                imageRequest, this);

        BaseBitmapDataSubscriber bitmapDataSubscriber = new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    if (size == null) {
                        subject.onNext(bitmap);
                    } else {
                        Bitmap resizedBmp = BitmapUtils.resizeOuterFit(bitmap, size);
                        Bitmap croppedBmp = BitmapUtils.cropCenterInside(resizedBmp, size);
                        resizedBmp.recycle();
                        subject.onNext(croppedBmp);
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

        return subject.onErrorReturn(new Func1<Throwable, Bitmap>() {
            @Override
            public Bitmap call(Throwable throwable) {
                LogUtils.e(TAG, "subject onErrorReturn", throwable);
                return null;
            }
        });
    }

    private void persistBitmap(Bitmap bitmap, String filename) {
        if (bitmap == null) {
            return;
        }
        File dir = new File(Environment.getExternalStorageDirectory(), Constant.File.DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, filename);
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedOutputStream bufferedOutputStream =
                    new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG, "saveBitmapToFile", e);
        }

    }

    // TODO: ** two size of wallpaper
    // TODO: scale & crop wallpaper
    public void setWallpaper(Photo photo, final DisplayInfo info) {
        int desiredMinimumHeight = mWallpaperManager.getDesiredMinimumHeight();
        int desiredMinimumWidth = mWallpaperManager.getDesiredMinimumWidth();
        LogUtils.d(TAG, "setWallpaper " + desiredMinimumHeight + " " + desiredMinimumWidth);
        Uri imageUri = pickImageResolution(photo, info);
        Rect rect = info.getScreenRect();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(desiredMinimumWidth, desiredMinimumHeight))
                .build();
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
                            inputStream.close();
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
