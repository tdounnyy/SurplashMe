package duan.felix.wallpaper.core.model;

import duan.felix.wallpaper.scaffold.model.Model;

/**
 * @author Felix.Duan.
 */

public class Progress implements Model {


    public enum State {IDLE, FETCHING, MEASURING, DOWNLOADING, RESIZING, SETTING, SUCCESS, FAIL;}

    private State mState = State.IDLE;

    public State getState() {
        return mState;
    }

    public void reset() {
        mState = State.IDLE;
    }

    public void setState(State state) {
        if (mState.ordinal() > state.ordinal()) {
            throw new IllegalStateException("State can not switch from " + mState + " to " + state);
        }
        mState = state;
    }
}
