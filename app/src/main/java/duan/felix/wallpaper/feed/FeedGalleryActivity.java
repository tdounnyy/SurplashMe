package duan.felix.wallpaper.feed;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import duan.felix.wallpaper.core.event.ServiceStartedEvent;
import duan.felix.wallpaper.scaffold.app.BaseActivity;
import duan.felix.wallpaper.scaffold.utils.IntentStarter;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.service.FloatService;

/**
 * TODO style
 *
 * @author Felix.Duan.
 */

public class FeedGalleryActivity extends BaseActivity {

    private static final String TAG = "FeedGalleryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            // TODO: make a button and nice description, before asking
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else if (FloatService.running()) {
            finish();
        } else {
            IntentStarter.startService(this, new Intent(this, FloatService.class));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceStartedEvent(ServiceStartedEvent e) {
        LogUtils.d(TAG, "onServiceStartedEvent");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentStarter.launchHomeApp(FeedGalleryActivity.this);
                finish();
            }
        }, 1000);
    }
}
