package duan.felix.wallpaper.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.scaffold.utils.StringUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author Felix.Duan.
 */

public class BrowserActivity extends Activity {

    private static final String TAG = "BrowserActivity";

    private static final String PHOTO_ID = "photo_id";

    private Photo mPhoto;

    @BindView(R.id.photo_view)
    SimpleDraweeView mPhotoView;

    @Inject
    RetrofitFeedClient mFeedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.Injector.inject(this);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent invokeIntent = getIntent();
        loadPhoto(invokeIntent.getStringExtra(PHOTO_ID));
    }

    // TODO: ** Make a cancelable Action
    private void loadPhoto(final String photoId) {
        if (!StringUtils.isEmpty(photoId)) {
            mFeedClient.getPhoto(photoId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Photo>() {
                        @Override
                        public void call(Photo photo) {
                            setPhoto(photo);
                        }
                    });
        }
    }

    private void setPhoto(Photo photo) {
        LogUtils.d(TAG, "setPhoto:" + photo.toString());
        mPhoto = photo;
        showPhoto();
    }

    private void showPhoto() {
        mPhotoView.setImageURI(mPhoto.urls.regular);
    }

    public static void startWith(Context context, String photoId) {
        if (!StringUtils.isEmpty(photoId)) {
            Intent intent = new Intent(context, BrowserActivity.class);
            intent.putExtra(PHOTO_ID, photoId);
            context.startActivity(intent);
        }
    }
}
