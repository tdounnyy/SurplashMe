package duan.felix.wallpaper.feed;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Feed;
import duan.felix.wallpaper.scaffold.app.BaseActivity;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 *
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
