package duan.felix.wallpaper.core.list;

import java.util.List;

/**
 * @author Felix.Duan.
 */

public class Portion<T> {
    public List<T> items;

    public Portion(List<T> items) {
        this.items = items;
    }
}
