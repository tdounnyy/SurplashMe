package duan.felix.wallpaper.core.client;

import java.util.List;

import duan.felix.wallpaper.core.model.Photo;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Felix.Duan.
 */

interface FeedEndpoint {

    @GET("photos/{feedId}")
    Observable<List<Photo>> getPhotos(@Path("feedId") String feedId,
                                      @Query("page") Integer page,
                                      @Query("per_page") Integer perPage);

}
