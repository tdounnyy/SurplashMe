package duan.felix.wallpaper.core.worker;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
 * Resize(origin', reference)->resized
 * Crop(resized, reference)->cropped
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

    private Observable<Bitmap> getResizedBitmap(Photo photo, final Rect rect) {
        return Observable.just(photo.urls.full)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(String uri) {

                        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
                        if (rect != null) {
                            ResizeOptions resizeOptions =
                                    new ResizeOptions(rect.width(), rect.height());
                            builder.setResizeOptions(resizeOptions);
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
                                            || (bitmap.getWidth() == rect.width()
                                            && bitmap.getHeight() == rect.height())) {
                                        subject.onNext(bitmap);
                                    } else {
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

                        return subject.onErrorReturn(new Func1<Throwable, Bitmap>() {
                            @Override
                            public Bitmap call(Throwable throwable) {
                                LogUtils.e(TAG, "subject onErrorReturn", throwable);
                                return null;
                            }
                        });
                    }
                });
    }

    private Observable<Bitmap> getViewSizeBitmap(Photo photo, DisplayInfo info) {
        Rect rect = info.getRootViewRect();
        return getResizedBitmap(photo, rect);
    }

    public void storeViewPhoto(final Photo photo, final DisplayInfo info) {
        getViewSizeBitmap(photo, info)
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        LogUtils.d(TAG, "view subscribe called");
                        persistBitmap(bitmap, Constant.File.VIEW_SIZE);
                    }
                });
    }

    private Observable<Bitmap> getWallpaperSizeBitmap(Photo photo) {
        int width = mWallpaperManager.getDesiredMinimumWidth();
        int height = mWallpaperManager.getDesiredMinimumHeight();
        return getResizedBitmap(photo, new Rect(0, 0, width, height));
    }

    public void storeWallpaperSizePhoto(Photo photo) {
        getWallpaperSizeBitmap(photo)
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        LogUtils.d(TAG, "wallpaper subscribe called");
                        persistBitmap(bitmap, Constant.File.WALLPAPER_SIZE);

                    }
                });

    }

    public Observable<Bitmap> getOriginBitmap(Photo photo) {
        return getResizedBitmap(photo, null);
    }

    public void storeOriginPhoto(Photo photo) {
        getOriginBitmap(photo)
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        LogUtils.d(TAG, "origin size subscribe called");
                        persistBitmap(bitmap, Constant.File.ORIGIN_SIZE);
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
    public void setWallpaper(Photo photo, final DisplayInfo info) {
        LogUtils.d(TAG, "setWallpaper: " + photo);

//        clearPersisted();
//        storeViewPhoto(photo, displayInfo);
//        storeWallpaperSizePhoto(photo);
//        storeOriginPhoto(photo);

        getWallpaperSizeBitmap(photo)
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        try {
                            clearPersisted();
                            persistBitmap(bitmap, Constant.File.WALLPAPER_SIZE);
                            mWallpaperManager.setBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void clearPersisted() {
        File dir = new File(Environment.getExternalStorageDirectory(), Constant.File.DIR);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        }
    }

    private Uri pickImageResolution(Photo photo, DisplayInfo info) {
        if (info.getScreenRect().width() * info.getScreenRect().height() < 1080 * 1920) {
            return Uri.parse(photo.urls.regular);
        } else {
            return Uri.parse(photo.urls.full);
        }
    }

}
