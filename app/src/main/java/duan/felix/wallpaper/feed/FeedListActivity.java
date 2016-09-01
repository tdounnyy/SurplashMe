package duan.felix.wallpaper.feed;

import android.app.Activity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Feed;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * TODO make fullscreen & float
 *
 * @author Felix.Duan.
 */

public class FeedListActivity extends Activity {

    private static final String TAG = "FeedListActivity";
    private FeedListPresenter mListPresenter;
    @BindView(R.id.container)
    FeedListView mListView;
    private Feed mFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
        mFeed = new Feed();
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
