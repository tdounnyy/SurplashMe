package duan.felix.wallpaper.scaffold.event;

/**
 * @author Felix.Duan.
 */

public class ToastEvent extends Event {

    public final String msg;

    public ToastEvent(String msg) {
        this.msg = msg;
    }
}
