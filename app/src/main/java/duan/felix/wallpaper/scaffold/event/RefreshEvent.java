package duan.felix.wallpaper.scaffold.event;

/**
 * @author Felix.Duan.
 */
public class RefreshEvent extends Event {

    public final boolean remote;

    public RefreshEvent(boolean remote) {
        this.remote = remote;
    }

}
