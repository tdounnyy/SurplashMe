package duan.felix.wallpaper.scaffold.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Felix.Duan.
 */

public class CollectionUtils {

    public static <T> List<T> listCopy(List<T> list) {
        List<T> result = new ArrayList<>(list.size());
        for (T p : list) {
            result.add(p);
        }
        return result;

    }
}
