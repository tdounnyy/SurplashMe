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
    Observable<List<Photo>> getPhotoList(@Path("feedId") String feedId,
                                         @Query("page") Integer page,
                                         @Query("per_page") Integer perPage);


    @GET("photos/{photoId}")
    Observable<Photo> getPhoto(@Path("photoId") String photoId);

}
