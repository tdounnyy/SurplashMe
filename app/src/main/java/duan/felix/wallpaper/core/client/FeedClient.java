package duan.felix.wallpaper.core.client;

import android.support.annotation.NonNull;

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

    private static final Retrofit retrofit = Retrofits.defaultBuilder()
            .client(OkHttpClients.DEFAULT)
            .build();

    private static final FeedEndpoint endpoint = retrofit.create(FeedEndpoint.class);

    public Observable<Portion<Photo>> getPhotos(@NonNull String feedId, Integer page) {
        // TODO: make a call executor?
        return endpoint.getPhotos(feedId, page).subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<Photo>, Observable<Portion<Photo>>>() {
                    @Override
                    public Observable<Portion<Photo>> call(List<Photo> photos) {
                        return Observable.just(new Portion<>(photos));
                    }

                });
    }
}
