package duan.felix.wallpaper.feed;

import org.greenrobot.eventbus.Subscribe;

import duan.felix.wallpaper.core.event.LoadAfterEvent;
import duan.felix.wallpaper.core.event.RefreshEvent;
import duan.felix.wallpaper.core.list.Portion;
import duan.felix.wallpaper.core.model.Feed;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.presenter.Presenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author Felix.Duan.
 */

public class FeedListPresenter extends Presenter<Feed, FeedListView> {

    private static final String TAG = "FeedListPresenter";
    private Feed mFeed;
    private int page = 1;
    private FeedListView mListView;
    private FeedSource mFeedSource;
    private boolean mInit = true;

    public FeedListPresenter(Feed feed, FeedListView listView) {
        this.mFeed = feed;
        this.mListView = listView;
        mFeedSource = new FeedSource(mFeed.id);
    }

    @Subscribe
    public void performRefresh(RefreshEvent e) {
        mFeedSource.refresh().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Portion<Photo>>() {
                    @Override
                    public void call(Portion<Photo> photos) {
                        mListView.setItems(photos.items);
                        page = 1;
                    }
                });
    }

    @Subscribe
    public void performLoadAfter(LoadAfterEvent e) {
        mFeedSource.loadAfter(++page).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Portion<Photo>>() {
                    @Override
                    public void call(Portion<Photo> photos) {
                        mListView.appendItems(photos.items);
                    }
                });
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
        mListView = null;
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
