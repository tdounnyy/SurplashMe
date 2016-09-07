package duan.felix.wallpaper.feed;

import java.util.List;

import javax.inject.Inject;

import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.list.ListSource;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.CollectionUtils;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

import static duan.felix.wallpaper.scaffold.utils.Constant.LOAD_PER_PAGE;

/**
 * @author Felix.Duan.
 */

public class FeedSource extends ListSource<Photo> {

    private static final String TAG = "FeedSource";

    private Realm realm;


    @Inject
    RetrofitFeedClient mClient;
    private String feedId = null;
    private int page = 1;

    public FeedSource(String feedId) {
        Global.Injector.inject(this);
        setFeedId(feedId);
        realm = Realm.getDefaultInstance();
    }

    private List<Photo> updateCache(List<Photo> photos) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(Photo.class);
        List<Photo> updated = realm.copyToRealmOrUpdate(photos);
        realm.commitTransaction();
        return updated;
    }

    // TODO: remote remote after cache load
    @Override
    protected Observable<List<Photo>> refresh(boolean forceRemote) {
        page = 1;
        if (forceRemote || realm.where(Photo.class).count() == 0) {
            return loadFromRemote();
        } else {
            return loadFromCache();
        }
    }

    @Override
    public Observable<List<Photo>> loadFromCache() {
        LogUtils.d(TAG, "loadFromCache");
        return realm.where(Photo.class).findAll().asObservable()
                .limit(LOAD_PER_PAGE)
                .map(new Func1<RealmResults<Photo>, List<Photo>>() {
                    @Override
                    public List<Photo> call(RealmResults<Photo> photos) {
                        return CollectionUtils.listCopy(photos);
                    }
                });
    }

    @Override
    protected Observable<List<Photo>> loadFromRemote() {
        LogUtils.d(TAG, "loadFromRemote");
        return mClient.getPhotoList(feedId, page)
                .map(new Func1<List<Photo>, List<Photo>>() {
                    @Override
                    public List<Photo> call(List<Photo> photos) {
                        updateCache(photos);
                        return CollectionUtils.listCopy(photos);
                    }
                });
    }

    @Override
    public Observable<List<Photo>> loadAfter() {
        LogUtils.d(TAG, "loadAfter" + ++page);
        return mClient.getPhotoList(feedId, page);
    }

    @Override
    protected Observable<Photo> getRandomPhoto() {
        return mClient.getRandomPhoto();
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId == null ? "" : feedId;
    }
}
