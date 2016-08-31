package duan.felix.wallpaper.feed;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.Photo;
import duan.felix.wallpaper.core.net.PhotosClient;
import duan.felix.wallpaper.view.PhotoListContainer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author Felix.Duan.
 */

public class FeedListActivity extends Activity {

    //    @Inject
    PhotosClient client = new PhotosClient();

    private PhotoListContainer container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: use dagger2
//        DaggerInjection.builder().build();
        setContentView(R.layout.feed_list);
        // TODO: use butterknife
        container = (PhotoListContainer) findViewById(R.id.container);
    }

    @Override
    protected void onResume() {
        super.onResume();

        client.getPhotos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Photo>>() {
                    @Override
                    public void call(List<Photo> photos) {
                        container.setItems(photos);
                    }
                });
    }

}
