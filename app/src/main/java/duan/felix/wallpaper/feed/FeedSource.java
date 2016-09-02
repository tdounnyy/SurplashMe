package duan.felix.wallpaper.feed;

import javax.inject.Inject;

import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.list.ListSource;
import duan.felix.wallpaper.core.list.Portion;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import rx.Observable;

/**
 * TODO: ** load from cache
 *
 * @author Felix.Duan.
 */

public class FeedSource extends ListSource<Photo> {

    private static final String TAG = "FeedSource";

    @Inject
    RetrofitFeedClient mClient;
    private String feedId = null;
    private int page = 1;

    public FeedSource(String feedId) {
        Global.Injector.inject(this);
        setFeedId(feedId);
    }

    @Override
    public Observable<Portion<Photo>> refresh() {
        LogUtils.d(TAG, "refresh");
        page = 1;
        return mClient.getPhotoList(feedId, page);
    }

    @Override
    public Observable<Portion<Photo>> loadAfter() {
        LogUtils.d(TAG, "loadAfter" + ++page);
        return mClient.getPhotoList(feedId, page);
    }

    @Override
    public Observable<Portion<Photo>> loadBefore() {
        LogUtils.d(TAG, "loadBefore" + page);
        return mClient.getPhotoList(feedId, page);
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId == null ? "" : feedId;
    }
}
