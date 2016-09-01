package duan.felix.wallpaper.scaffold.event;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Felix.Duan.
 */

public class Bus {

    public static void register(Object obj) {
        EventBus.getDefault().register(obj);
    }

    public static void unregister(Object obj) {
        EventBus.getDefault().unregister(obj);
    }

    public static void post(Event e) {
        EventBus.getDefault().post(e);
    }
}
