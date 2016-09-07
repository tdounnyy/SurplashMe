package duan.felix.wallpaper.feed;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Feed;
import duan.felix.wallpaper.helper.DisplayInfo;
import duan.felix.wallpaper.scaffold.app.BaseActivity;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * TODO style
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
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                DisplayInfo displayInfo = new DisplayInfo();
                displayInfo.update(v);
                if (displayInfo.getType() == DisplayInfo.Type.NAV) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    int screenHeight = displayInfo.getScreenRect().height();
                    int parentHeight = v.getHeight();
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mViewPager.getLayoutParams();
                    params.setMargins(0, 0, 0, parentHeight - screenHeight);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mViewPager.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFeedPagerPresenter.onResume();
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
