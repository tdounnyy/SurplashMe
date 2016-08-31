package duan.felix.wallpaper.core.list;

import rx.Observable;

/**
 * @author Felix.Duan.
 */

public abstract class ListSource<T> {

    public abstract Observable<Portion<T>> refresh();

    public abstract Observable<Portion<T>> loadAfter(int page);

    public abstract Observable<Portion<T>> loadBefore(int page);

}
