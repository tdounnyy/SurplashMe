package duan.felix.wallpaper.core.client;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import duan.felix.wallpaper.core.list.Portion;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.client.Client;
import duan.felix.wallpaper.scaffold.net.OkHttpClients;
import duan.felix.wallpaper.scaffold.net.Retrofits;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Felix.Duan.
 */

public class FeedClient extends Client {

    private static final String TAG = "FeedClient";

    private static final int PER_PAGE = 30;

    private static final Retrofit retrofit = Retrofits.defaultBuilder()
            .client(OkHttpClients.DEFAULT)
            .build();

    private static final FeedEndpoint endpoint = retrofit.create(FeedEndpoint.class);

    public Observable<Portion<Photo>> getPhotoList(@NonNull String feedId, Integer page) {
        return endpoint.getPhotoList(feedId, page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<Photo>>() {
                    @Override
                    public List<Photo> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                })
                .flatMap(new Func1<List<Photo>, Observable<Portion<Photo>>>() {
                    @Override
                    public Observable<Portion<Photo>> call(List<Photo> photos) {
                        return Observable.just(new Portion<>(photos));
                    }
                });
    }

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
