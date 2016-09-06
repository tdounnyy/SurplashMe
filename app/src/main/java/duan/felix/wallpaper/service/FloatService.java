package duan.felix.wallpaper.service;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.event.InvokeHomeEvent;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.worker.WallpaperWorker;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.widget.FloatPhotoItemContainer;

// TODO: random next
// TODO: sequence next
public class FloatService extends Service {

    private static final String TAG = "FloatService";

    public static final String EXTRA_PHOTO = "extra_photo";

    private WindowManager manager;

    @Inject
    WallpaperWorker mWallpaperWorker;

    private FloatPhotoItemContainer mFloatView;

    @Override
    public void onCreate() {
        super.onCreate();
        Global.Injector.inject(this);
        Bus.register(this);
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);
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

    @Subscribe
    public void onHomeInvoked(InvokeHomeEvent e) {

        ValueAnimator animator = ValueAnimator.ofFloat(1, 0)
                .setDuration(1000);

        final View photoView = mFloatView.getPhotoView();
        final View progressBar = mFloatView.getProgressBar();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                photoView.setAlpha(alpha);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                stopSelf();
            }
        });
        animator.start();
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
