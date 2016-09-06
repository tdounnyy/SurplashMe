package duan.felix.wallpaper.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.worker.WallpaperWorker;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.StringUtils;
import duan.felix.wallpaper.service.FloatService;

/**
 * @author Felix.Duan.
 */

public class PhotoItemContainer extends FrameLayout {

    private static final String TAG = "PhotoItemContainer";

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

    public PhotoItemContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Global.Injector.inject(this);
        LayoutInflater.from(context).inflate(R.layout.photo_view, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.photo_item_container)
    public void clickOnItemView() {
        Intent intent = new Intent(getContext(), FloatService.class);
        intent.putExtra(FloatService.EXTRA_PHOTO, mPhoto);
        getContext().startService(intent);
        setClickable(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setClickable(true);
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
        mDraweeView.setImageURI(mPhoto.urls.regular);
        if (!StringUtils.isEmpty(photo.color)) {
            ColorDrawable drawable = new ColorDrawable(Color.parseColor(photo.color));
            mDraweeView.getHierarchy().setPlaceholderImage(drawable);
        }
    }

}
