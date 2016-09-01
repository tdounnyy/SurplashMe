package duan.felix.wallpaper.core.worker;

import android.app.WallpaperManager;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Felix.Duan.
 */

public class WallpaperOperator {

    private static final String TAG = "WallpaperOperator";

    @Inject
    WallpaperManager mWallpaperManager;

    @Inject
    RetrofitFeedClient mFeedClient;

    public WallpaperOperator() {
        Global.Injector.inject(this);
    }

    public void setWallpaper(Photo photo) {
        Observable.just(photo.urls.full)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<InputStream>>() {
                    @Override
                    public Observable<InputStream> call(String urlString) {
                        LogUtils.d(TAG, urlString);
                        return mFeedClient.downloadPhoto(urlString)
                                .map(new Func1<ResponseBody, InputStream>() {
                                    @Override
                                    public InputStream call(ResponseBody response) {
                                        return response.byteStream();
                                    }
                                });
                    }
                })
                .subscribe(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        try {
                            mWallpaperManager.setStream(inputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(TAG, "setWallpaper err:", throwable);
                    }
                });
    }

}
