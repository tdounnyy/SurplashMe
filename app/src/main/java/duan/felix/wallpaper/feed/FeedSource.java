package duan.felix.wallpaper.feed;

import duan.felix.wallpaper.core.client.FeedClient;
import duan.felix.wallpaper.core.list.ListSource;
import duan.felix.wallpaper.core.list.Portion;
import duan.felix.wallpaper.core.model.Photo;
import rx.Observable;

/**
 * @author Felix.Duan.
 */

public class FeedSource extends ListSource<Photo> {

    private FeedClient mClient = new FeedClient();
    private String feedId = null;

    public FeedSource(String feedId) {
        setFeedId(feedId);
    }

    @Override
    public Observable<Portion<Photo>> refresh() {
        return mClient.getPhotos(feedId, null);
    }

    @Override
    public Observable<Portion<Photo>> loadAfter(int page) {
        return mClient.getPhotos(feedId, page);
    }

    @Override
    public Observable<Portion<Photo>> loadBefore(int page) {
        return mClient.getPhotos(feedId, page);
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId == null ? "" : feedId;
    }
}
