package duan.felix.wallpaper.feed;

import android.app.Activity;
import android.os.Bundle;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Feed;

/**
 * @author Felix.Duan.
 */

public class FeedListActivity extends Activity {

    private FeedListPresenter mListPresenter;
    private FeedListView mListView;
    private Feed mFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: use dagger2
//        DaggerInjection.builder().build();
        setContentView(R.layout.feed_activity);
        // TODO: use butterknife
        mFeed = new Feed();
        mListView = (FeedListView) findViewById(R.id.container);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mListPresenter = new FeedListPresenter(mFeed, mListView);
        mListPresenter.bind();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        client.getPhotos("", null)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Portion<Photo>>() {
//                    @Override
//                    public void call(Portion<Photo> photos) {
//                        mListView.setItems(photos.items);
//                    }
//                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mListPresenter.unbind();
    }
}
