package duan.felix.wallpaper.feed;

import duan.felix.wallpaper.core.client.FeedClient;
import duan.felix.wallpaper.core.list.ListSource;
import duan.felix.wallpaper.core.list.Portion;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import rx.Observable;

/**
 * @author Felix.Duan.
 */

public class FeedSource extends ListSource<Photo> {

    private static final String TAG = "FeedSource";

    private FeedClient mClient = new FeedClient();
    private String feedId = null;

    public FeedSource(String feedId) {
        setFeedId(feedId);
    }

    @Override
    public Observable<Portion<Photo>> refresh() {
        LogUtils.d(TAG, "refresh");
        return mClient.getPhotos(feedId, null);
    }

    @Override
    public Observable<Portion<Photo>> loadAfter(int page) {
        LogUtils.d(TAG, "loadAfter" + page);
        return mClient.getPhotos(feedId, page);
    }

    @Override
    public Observable<Portion<Photo>> loadBefore(int page) {
        LogUtils.d(TAG, "loadBefore" + page);
        return mClient.getPhotos(feedId, page);
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId == null ? "" : feedId;
    }
}
