package duan.felix.wallpaper.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.event.InvokeHomeEvent;
import duan.felix.wallpaper.core.event.RequestRandomToastEvent;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.worker.WallpaperWorker;
import duan.felix.wallpaper.feed.FeedSource;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.widget.FloatPhotoItemContainer;
import rx.functions.Action1;

// TODO: random next
// TODO: sequence next
public class FloatService extends Service {

    private static final String TAG = "FloatService";

    public static final String EXTRA_PHOTO = "extra_photo";

    private WindowManager manager;

    @Inject
    WallpaperWorker mWallpaperWorker;

    private FloatPhotoItemContainer mFloatView;

    private FeedSource mFeedSource;

    @Override
    public void onCreate() {
        super.onCreate();
        Global.Injector.inject(this);
        Bus.register(this);
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mFeedSource = new FeedSource(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Photo photo = intent.getParcelableExtra(EXTRA_PHOTO);
        addFloatView(photo);
        mWallpaperWorker.setWallpaper(photo);

        return super.onStartCommand(intent, flags, startId);
    }

    private View addFloatView(Photo photo) {
        mFloatView = (FloatPhotoItemContainer) LayoutInflater
                .from(this).inflate(R.layout.a_float_photo_item, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                PixelFormat.TRANSLUCENT);
        manager.addView(mFloatView, params);
        mFloatView.setPhoto(photo);
        return mFloatView;
    }

    private View addRandomPhotoToast() {
        final Button button = new Button(this);
        button.setText("Random change wallpaper");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d(TAG, "button click");
                mFeedSource.getRandomPhoto()
                        .subscribe(new Action1<Photo>() {
                            @Override
                            public void call(Photo photo) {
                                mWallpaperWorker.setWallpaper(photo);
                            }
                        });
//                manager.removeViewImmediate(button);
            }
        });
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                PixelFormat.TRANSLUCENT);
        manager.addView(button, params);
        return button;
    }

    @Subscribe
    public void onHomeInvoked(InvokeHomeEvent e) {
        if (mFloatView.isAttachedToWindow()) {
            mFloatView.fadeOut();
        }
    }

    @Subscribe
    public void onRandomToastRequested(RequestRandomToastEvent e) {
        addRandomPhotoToast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFloatView.selfDetach();
        Bus.unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
