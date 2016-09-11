package duan.felix.wallpaper.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.event.ServiceStartedEvent;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.widget.FloatButtonView;

// TODO: random next
// TODO: sequence next
public class FloatService extends Service {

    private static final String TAG = "FloatService";

    public static final String EXTRA_PHOTO = "extra_photo";

    private WindowManager manager = null;

//    @Inject
//    WallpaperWorker mWallpaperWorker;

//    private FloatPhotoItemContainer mPhotoView = null;

    private FloatButtonView mButtonView = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        Global.Injector.inject(this);
//        Bus.register(this);
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            stopSelf();
        } else {
//            Photo photo = intent.getParcelableExtra(EXTRA_PHOTO);
//            addFloatView(photo);
//            mWallpaperWorker.setWallpaper(photo);
            addFloatButtonView();
            Bus.post(new ServiceStartedEvent());
       }

        return START_NOT_STICKY;
    }

//    private View addFloatView(Photo photo) {
//        mPhotoView = (FloatPhotoItemContainer) LayoutInflater
//                .from(this).inflate(R.layout.a_float_photo_item, null);
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT,
//                0, 0,
//                WindowManager.LayoutParams.TYPE_TOAST,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                PixelFormat.TRANSLUCENT);
//        manager.addView(mPhotoView, params);
//        mPhotoView.setPhoto(photo);
//        return mPhotoView;
//    }

    private View addFloatButtonView() {
        mButtonView = (FloatButtonView) LayoutInflater
                .from(this).inflate(R.layout.a_float_button_view, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        manager.addView(mButtonView, params);
        return mButtonView;
    }

//    @Subscribe
//    public void onHomeInvoked(InvokeHomeEvent e) {
//        if (mPhotoView.isAttachedToWindow()) {
//            mPhotoView.fadeOut();
//        }
//    }

//    @Subscribe
//    public void onShowFloatButtonEvent(ShowFloatButtonEvent e) {
//        addFloatButtonView();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mPhotoView != null) {
//            mPhotoView.selfDetach();
//        }
        if (mButtonView != null) {
            mButtonView.selfDetach();
        }
        Bus.unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
