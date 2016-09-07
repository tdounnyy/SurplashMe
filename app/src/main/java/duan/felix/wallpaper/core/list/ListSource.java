package duan.felix.wallpaper.core.list;

import java.util.List;

import duan.felix.wallpaper.core.model.Photo;
import rx.Observable;

/**
 * @author Felix.Duan.
 */

public abstract class ListSource<T> {

    protected abstract Observable<List<T>> refresh(boolean forceRemote);

    protected abstract Observable<List<Photo>> loadFromCache();

    protected abstract Observable<List<Photo>> loadFromRemote();

    protected abstract Observable<List<T>> loadAfter();

    protected abstract Observable<Photo> getRandomPhoto();

}
