package duan.felix.wallpaper.core.net;

import java.util.List;

import duan.felix.wallpaper.core.Photo;
import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Felix.Duan.
 */

interface PhotoEndpoint {

    @GET("photos")
    Observable<List<Photo>> getPhotos();

}
