package duan.felix.wallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.worker.WallpaperWorker;
import duan.felix.wallpaper.helper.DisplayInfo;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class PhotoItemContainer extends LinearLayout {

    private static final String TAG = "PhotoItemContainer";

    private static DisplayInfo displayInfo = null;

    @Inject
    WallpaperWorker mWallpaperWorker;

    @BindView(R.id.photo_item)
    SimpleDraweeView mDraweeView;

    private Photo mPhoto;

    public PhotoItemContainer(Context context) {
        this(context, null, 0);
    }

    public PhotoItemContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // TODO: ** recycle this view
    public PhotoItemContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Global.Injector.inject(this);
        LayoutInflater.from(context).inflate(R.layout.photo_view, this);
        ButterKnife.bind(this);
        if (displayInfo == null) {
            displayInfo = new DisplayInfo(this);
        }
    }

    @OnClick(R.id.photo_item_container)
    public void clickOnItemView() {
        mWallpaperWorker.setWallpaper(mPhoto, displayInfo);
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
        LogUtils.d(TAG, "setPhoto\n" + photo.urls.regular + "\n" + photo.urls.full);
        mDraweeView.setImageURI(mPhoto.urls.regular);
    }

}
