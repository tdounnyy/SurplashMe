package duan.felix.wallpaper.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.event.ServiceStartedEvent;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.ToastEvent;
import duan.felix.wallpaper.scaffold.utils.ToastUtils;
import duan.felix.wallpaper.widget.FloatButtonView;

public class FloatService extends Service {

    private static final String TAG = "FloatService";

    public static final String EXTRA_PHOTO = "extra_photo";

    private WindowManager manager = null;

    private FloatButtonView mButtonView = null;

    private static boolean mRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Bus.register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (intent == null) {
            stopSelf();
        } else {
            if (!mRunning) {
                mRunning = true;
                addFloatButtonView();
                Bus.post(new ServiceStartedEvent());
            }
        }

        return START_STICKY_COMPATIBILITY;
    }

    private View addFloatButtonView() {
        mButtonView = (FloatButtonView) LayoutInflater
                .from(this).inflate(R.layout.a_float_button_view, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        manager.addView(mButtonView, params);
        return mButtonView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void performToast(ToastEvent e) {
        ToastUtils.toast(this, e.msg);
    }

    public static boolean running() {
        return mRunning;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mButtonView != null) {
            mButtonView.selfDetach();
        }
        Bus.unregister(this);
        mRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
