package duan.felix.wallpaper.scaffold.event;

import duan.felix.wallpaper.core.model.Progress;

/**
 * @author Felix.Duan.
 */

public class ProgressEvent extends Event {

    public final Progress.State state;

    public ProgressEvent(Progress.State state) {
        this.state = state;
    }
}
