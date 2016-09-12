package duan.felix.wallpaper.feed;

import android.content.Intent;
import android.os.Bundle;
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
        IntentStarter.startService(this, new Intent(this, FloatService.class));
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceStartedEvent(ServiceStartedEvent e) {
        LogUtils.d(TAG, "onServiceStartedEvent");
        IntentStarter.launchHomeApp(this);
    }
}
