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

public class FeedPagerPresenter extends Presenter<Feed, ViewPager> {

    private FeedSource mFeedSource;
    private Context mContext;
    private Feed mFeed;
    private ViewPager mViewPager;
    private FeedPagerAdapter mPagerAdapter;
    private boolean mInit = true;

    public FeedPagerPresenter(Feed feed, ViewPager viewPager) {
        mContext = viewPager.getContext();
        mFeed = feed;
        mViewPager = viewPager;
        mPagerAdapter = new FeedPagerAdapter();
        mPagerAdapter.setViewPager(mViewPager);
        mFeedSource = new FeedSource(mFeed.id);
    }

    @Subscribe
    public void performRefresh(RefreshEvent e) {
        mFeedSource.refresh().observeOn(AndroidSchedulers.mainThread())
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
    public void performHomeApp(InvokeHomeEvent e) {
        ToastUtils.toast(mContext, "launch home");
        // TODO: *** make seamless transition
        ActivityStarter.launchHomeApp((Activity) mContext);
    }

    public void tryInit() {
        if (mInit) {
            mInit = false;
            Bus.post(new RefreshEvent());
        }
    }

    @Override
    public void bind() {

    }

    @Override
    public void unbind() {
        mFeed = null;
        mViewPager.setAdapter(null);
        mViewPager = null;
        mPagerAdapter = null;
        mFeedSource = null;
        mInit = true;

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {
        Bus.register(this);
        tryInit();
    }

    @Override
    public void onPause() {
        Bus.unregister(this);
    }
}
