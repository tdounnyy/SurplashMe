package duan.felix.wallpaper.feed;

import java.util.List;

import javax.inject.Inject;

import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.list.ListSource;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/**
 * TODO: *** load from cache
 *
 * @author Felix.Duan.
 */

public class FeedSource extends ListSource<Photo> {

    private static final String TAG = "FeedSource";

    public static final int PER_PAGE = 30;

    @Inject
    RetrofitFeedClient mClient;
    private String feedId = null;
    private int page = 1;

    public FeedSource(String feedId) {
        Global.Injector.inject(this);
        setFeedId(feedId);
    }

    @Override
    public Observable<List<Photo>> refresh() {
        LogUtils.d(TAG, "refresh");
        page = 1;
        RealmResults<Photo> realmResults = Realm.getDefaultInstance().where(Photo.class).findAll();
        if (false&&!realmResults.isEmpty()) {
            LogUtils.d(TAG, "cached");
            return realmResults.asObservable()
                    .limit(PER_PAGE)
                    .map(new Func1<RealmResults<Photo>, List<Photo>>() {
                        @Override
                        public List<Photo> call(RealmResults<Photo> photos) {
                            return photos;
                        }
                    });
        } else {
            LogUtils.d(TAG, "no cached");
            return mClient.getPhotoList(feedId, page);
        }
    }

    @Override
    public Observable<List<Photo>> loadAfter() {
        LogUtils.d(TAG, "loadAfter" + ++page);
        return mClient.getPhotoList(feedId, page);
    }

    @Override
    public Observable<List<Photo>> loadBefore() {
        LogUtils.d(TAG, "loadBefore" + page);
        return mClient.getPhotoList(feedId, page);
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId == null ? "" : feedId;
    }
}
