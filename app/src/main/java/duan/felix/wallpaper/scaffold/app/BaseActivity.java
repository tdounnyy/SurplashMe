package duan.felix.wallpaper.scaffold.app;

import android.app.Activity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.ToastEvent;
import duan.felix.wallpaper.scaffold.utils.ToastUtils;

/**
 * @author Felix.Duan.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        Bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void performToast(ToastEvent e) {
        ToastUtils.toast(this, e.msg);
    }

}
