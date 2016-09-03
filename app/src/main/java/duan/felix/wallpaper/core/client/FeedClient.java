package duan.felix.wallpaper.core.client;

import android.support.annotation.NonNull;

import java.util.List;

import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.client.Client;
import rx.Observable;

/**
 * @author Felix.Duan.
 */

abstract class FeedClient extends Client {

    public abstract Observable<List<Photo>> getPhotoList(@NonNull String feedId, Integer page);

    public abstract Observable<Photo> getPhoto(@NonNull String photoId);
}
