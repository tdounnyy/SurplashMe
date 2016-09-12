package duan.felix.wallpaper.service.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import duan.felix.wallpaper.core.model.Progress;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.ProgressEvent;
import duan.felix.wallpaper.scaffold.presenter.Presenter;
import duan.felix.wallpaper.widget.ProgressView;

/**
 * @author Felix.Duan.
 */

public class ProgressPresenter extends Presenter<Progress, ProgressView> {


    private Progress mProgress = new Progress();
    private ProgressView mView;

    public ProgressPresenter(ProgressView view) {
        mView = view;
    }

    @Override
    public void onStart() {
        Bus.register(this);
    }

    @Override
    public void onStop() {
        Bus.unregister(this);
    }

    private synchronized Progress getProgress() {
        return mProgress;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgressEvent(ProgressEvent e) {
        getProgress().setState(e.state);
        mView.update(getProgress().getState());
    }

    public void reset() {
        getProgress().reset();
        mView.reset();
    }

    public boolean working() {
        Progress.State state = getProgress().getState();
        return state != Progress.State.IDLE && state != Progress.State.SUCCESS
                && state != Progress.State.FAIL;
    }
}
