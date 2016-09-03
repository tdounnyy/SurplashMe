package duan.felix.wallpaper.core.list;

import java.util.List;

import rx.Observable;

/**
 * @author Felix.Duan.
 */

public abstract class ListSource<T> {

    public abstract Observable<List<T>> refresh();

    public abstract Observable<List<T>> loadAfter();

    // TODO: * buggy
    public abstract Observable<List<T>> loadBefore();

}
