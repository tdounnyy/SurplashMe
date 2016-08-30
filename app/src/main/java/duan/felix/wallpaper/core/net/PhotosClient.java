package duan.felix.wallpaper.core.net;

import java.util.List;

import duan.felix.wallpaper.core.Photo;
import duan.felix.wallpaper.scaffold.net.OkHttpClients;
import duan.felix.wallpaper.scaffold.net.Retrofits;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * @author Felix.Duan.
 */

public class PhotosClient {

    private static final Retrofit retrofit = Retrofits.defaultBuilder()
            .client(OkHttpClients.DEFAULT)
            .build();

    private static final PhotoEndpoint endpoint = retrofit.create(PhotoEndpoint.class);

    public Observable<List<Photo>> getPhotos() {
        return endpoint.getPhotos();
    }
}
