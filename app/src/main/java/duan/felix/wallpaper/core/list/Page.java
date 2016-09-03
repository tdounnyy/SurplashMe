package duan.felix.wallpaper.core.list;

import java.util.List;

/**
 * @author Felix.Duan.
 */

// TODO: ** useless?
public class Page<T> extends Portion<T> {
    int page;

    public Page(List<T> items, int page) {
        super(items);
        this.page = page;
    }
}
