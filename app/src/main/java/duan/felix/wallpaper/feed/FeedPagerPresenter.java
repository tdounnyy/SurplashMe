package duan.felix.wallpaper.feed;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import duan.felix.wallpaper.core.event.InvokeHomeEvent;
import duan.felix.wallpaper.core.model.Feed;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.LoadAfterEvent;
import duan.felix.wallpaper.scaffold.event.RefreshEvent;
import duan.felix.wallpaper.scaffold.presenter.Presenter;
import duan.felix.wallpaper.scaffold.utils.ActivityStarter;
import duan.felix.wallpaper.scaffold.utils.ToastUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author Felix.Duan.
 */

// TODO: * composite presenter
class FeedPagerPresenter extends Presenter<Feed, ViewPager> {

    private FeedSource mFeedSource;
    private Context mContext;
    private Feed mFeed;
    private ViewPager mViewPager;
    private FeedPagerAdapter mPagerAdapter;
    private FeedLoadMorePresenter mLoadMorePresenter;
    private boolean mInit = true;

    FeedPagerPresenter(Feed feed, ViewPager viewPager) {
        mContext = viewPager.getContext();
        mFeed = feed;
        mViewPager = viewPager;
        mPagerAdapter = new FeedPagerAdapter();
        mFeedSource = new FeedSource(mFeed.id);
        mLoadMorePresenter = new FeedLoadMorePresenter(mViewPager);
    }

    @Subscribe
    public void performRefresh(RefreshEvent e) {
        mFeedSource.refresh(false).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Photo>>() {
                    @Override
                    public void call(List<Photo> photos) {
                        mPagerAdapter.setItems(photos);
                    }
                });
    }

    @Subscribe
    public void performLoadAfter(LoadAfterEvent e) {
        mFeedSource.loadAfter().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Photo>>() {
                    @Override
                    public void call(List<Photo> photos) {
                        mPagerAdapter.appendItems(photos);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHomeInvoked(InvokeHomeEvent e) {
        ToastUtils.toast(mContext, "launch home");
        ActivityStarter.launchHomeApp((Activity) mContext);
    }

    @Override
    public void onStart() {
        mLoadMorePresenter.onStart();
        mPagerAdapter.setViewPager(mViewPager);
    }

    @Override
    public void onStop() {
        mLoadMorePresenter.onStop();
        mViewPager.setAdapter(null);
    }

    @Override
    public void onResume() {
        mLoadMorePresenter.onResume();
        Bus.register(this);
        if (mInit) {
            mInit = false;
            Bus.post(new RefreshEvent());
        }
    }

    @Override
    public void onPause() {
        mLoadMorePresenter.onPause();
        Bus.unregister(this);
    }
}
