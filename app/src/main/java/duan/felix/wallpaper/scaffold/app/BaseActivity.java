package duan.felix.wallpaper.scaffold.app;

import android.app.Activity;
import android.os.Bundle;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void performToast(ToastEvent e) {
        ToastUtils.toast(this, e.msg);
    }

}
