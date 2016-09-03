package duan.felix.wallpaper.feed;

import java.util.ArrayList;
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

    FeedSource(String feedId) {
        Global.Injector.inject(this);
        setFeedId(feedId);
        realm = Realm.getDefaultInstance();
    }

    private void updateCache(List<Photo> photos) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(Photo.class);
        realm.copyToRealmOrUpdate(photos);
        realm.commitTransaction();
    }

    @Override
    protected Observable<List<Photo>> refresh(boolean forceRemote) {
        LogUtils.d(TAG, "refresh");
        page = 1;
        if (forceRemote || realm.where(Photo.class).count() == 0) {
            LogUtils.d(TAG, "no cached");
            return loadFromRemote();
        } else {
            LogUtils.d(TAG, "cached");
            return loadFromCache();
        }
    }

    @Override
    public Observable<List<Photo>> loadFromCache() {
        return realm.where(Photo.class).findAll().asObservable()
                .limit(LOAD_PER_PAGE)
                .map(new Func1<RealmResults<Photo>, List<Photo>>() {
                    @Override
                    public List<Photo> call(RealmResults<Photo> photos) {
                        List<Photo> result = new ArrayList<>(photos.size());
                        for (Photo p : photos) {
                            result.add(p);
                        }
                        return result;
                    }
                });

    }

    @Override
    protected Observable<List<Photo>> loadFromRemote() {
        return mClient.getPhotoList(feedId, page)
                .map(new Func1<List<Photo>, List<Photo>>() {
                    @Override
                    public List<Photo> call(List<Photo> photos) {
                        updateCache(photos);
                        return photos;
                    }
                });
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
