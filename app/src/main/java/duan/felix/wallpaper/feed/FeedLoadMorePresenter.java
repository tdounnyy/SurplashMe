package duan.felix.wallpaper.feed;

import android.support.v4.view.ViewPager;

import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.LoadAfterEvent;
import duan.felix.wallpaper.scaffold.model.Model;
import duan.felix.wallpaper.scaffold.presenter.Presenter;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

import static duan.felix.wallpaper.scaffold.utils.Constant.LOAD_MORE_THRESHOLD;

/**
 * @author Felix.Duan.
 */
class FeedLoadMorePresenter extends Presenter<Model, ViewPager> implements LoadMoreObserver {

    private static final String TAG = "FeedLoadMorePresenter";

    private ViewPager mViewPager;

    private ViewPager.SimpleOnPageChangeListener mListener;

    FeedLoadMorePresenter(ViewPager viewPager) {
        mViewPager = viewPager;

        mListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int count = mViewPager.getAdapter().getCount();
                LogUtils.d(TAG, "onPageSelected count:" + count + " pos:" + position);
                if (needLoadMore(count, position)) {
                    loadMore();
                }
            }
        };

    }

    @Override
    public boolean needLoadMore(int total, int currentIndex) {
        if (total > currentIndex && currentIndex >= 0) {
            return total - currentIndex < LOAD_MORE_THRESHOLD;
        } else {
            return false;
        }
    }

    @Override
    public void loadMore() {
        Bus.post(new LoadAfterEvent());
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
        mViewPager.addOnPageChangeListener(mListener);
    }

    @Override
    public void onPause() {
        mViewPager.removeOnPageChangeListener(mListener);
    }
}
