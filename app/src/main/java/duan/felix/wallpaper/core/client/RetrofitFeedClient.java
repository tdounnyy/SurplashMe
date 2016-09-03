package duan.felix.wallpaper.core.client;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.net.OkHttpClients;
import duan.felix.wallpaper.scaffold.net.Retrofits;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static duan.felix.wallpaper.scaffold.utils.Constant.LOAD_PER_PAGE;

/**
 * @author Felix.Duan.
 */

public class RetrofitFeedClient extends FeedClient {

    private static final String TAG = "RetrofitFeedClient";

    private static final Retrofit retrofit = Retrofits.defaultBuilder()
            .client(OkHttpClients.DEFAULT)
            .build();

    private static final FeedEndpoint endpoint = retrofit.create(FeedEndpoint.class);

    @Override
    public Observable<List<Photo>> getPhotoList(@NonNull String feedId, Integer page) {
        return endpoint.getPhotoList(feedId, page, LOAD_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<Photo>>() {
                    @Override
                    public List<Photo> call(Throwable throwable) {
                        LogUtils.e(TAG, "getPhotoList fail", throwable);
                        return new ArrayList<>();
                    }
                });
    }

    @Override
    public Observable<Photo> getPhoto(@NonNull String photoId) {
        return endpoint.getPhoto(photoId)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Photo>() {
                    @Override
                    public Photo call(Throwable throwable) {
                        return Photo.NULL;
                    }
                });
    }

}
