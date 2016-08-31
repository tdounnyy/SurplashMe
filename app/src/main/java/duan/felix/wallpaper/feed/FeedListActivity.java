package duan.felix.wallpaper.feed;

import android.app.Activity;
import android.os.Bundle;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Feed;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class FeedListActivity extends Activity {

    private static final String TAG = "FeedListActivity";
    private FeedListPresenter mListPresenter;
    private FeedListView mListView;
    private Feed mFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);
        // TODO: use butterknife
        mFeed = new Feed();
        mListView = (FeedListView) findViewById(R.id.container);
        mListPresenter = new FeedListPresenter(mFeed, mListView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
        mListPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mListPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop");
        mListPresenter.onStart();
    }
}
