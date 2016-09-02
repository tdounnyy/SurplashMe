package duan.felix.wallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.worker.WallpaperWorker;

/**
 * @author Felix.Duan.
 */

public class PhotoItemContainer extends LinearLayout {

    private static final String TAG = "FeedListItemView";

    // TODO: *** make aspect ratio as screen ratio
    @BindView(R.id.photo_item)
    SimpleDraweeView mDraweeView;

    private Photo mPhoto;

    public PhotoItemContainer(Context context) {
        this(context, null, 0);
    }

    public PhotoItemContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoItemContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.photo_view, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.photo_item_container)
    public void clickOnItemView() {
        new WallpaperWorker().setWallpaper(mPhoto);
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
        mDraweeView.setImageURI(photo.urls.regular);
    }

}
