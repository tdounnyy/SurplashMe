package duan.felix.wallpaper.feed;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Feed;
import duan.felix.wallpaper.scaffold.app.BaseActivity;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.service.FloatService;

/**
 * @author Felix.Duan.
 */

public class FeedGalleryActivity extends BaseActivity {

    private static final String TAG = "FeedGalleryActivity";

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    FeedPagerPresenter mFeedPagerPresenter;
    private Feed mFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_gallery);
        ButterKnife.bind(this);
        mFeed = new Feed();
        mFeedPagerPresenter = new FeedPagerPresenter(mFeed, mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
        mFeedPagerPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFeedPagerPresenter.onResume();

//        showDialog();

        Intent intent = new Intent(this, FloatService.class);
        startService(intent);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this, R.style.FullscreenDialog);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0,
                0,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                0
//                |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                ,
                PixelFormat.TRANSLUCENT);
        View view = dialog.getLayoutInflater().inflate(R.layout.dummy, null);
        dialog.setContentView(view, params);
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFeedPagerPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop");
        mFeedPagerPresenter.onStop();
    }

}
