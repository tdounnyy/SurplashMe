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

//    @BindView(R.id.view_pager)
//    ViewPager mViewPager;
//    FeedPagerPresenter mFeedPagerPresenter;
//    private Feed mFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dummy);
        setContentView(new View(this));
//        ButterKnife.bind(this);
//        mFeed = new Feed();
//        mFeedPagerPresenter = new FeedPagerPresenter(mFeed, mViewPager);
        IntentStarter.startService(this, new Intent(this, FloatService.class));
//        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
//        mFeedPagerPresenter.onStart();
//        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                DisplayInfo displayInfo = new DisplayInfo();
//                displayInfo.update(v);
//                if (displayInfo.getType() == DisplayInfo.Type.NAV) {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//                    int screenHeight = displayInfo.getScreenRect().height();
//                    int parentHeight = v.getHeight();
//                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mViewPager.getLayoutParams();
//                    params.setMargins(0, 0, 0, parentHeight - screenHeight);
//                } else {
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mViewPager.getLayoutParams();
//                    params.setMargins(0, 0, 0, 0);
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        IntentStarter.launchHomeApp(this);
//        mFeedPagerPresenter.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceStartedEvent(ServiceStartedEvent e) {
        LogUtils.d(TAG, "onServiceStartedEvent");
        IntentStarter.launchHomeApp(this);
//        finish();
    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mFeedPagerPresenter.onPause();
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        LogUtils.d(TAG, "onStop");
//        mFeedPagerPresenter.onStop();
//    }

}
