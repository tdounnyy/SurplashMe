package duan.felix.wallpaper.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

    @BindView(R.id.photo_view)
    SimpleDraweeView mDraweeView;

    private Photo mPhoto;

    private static CoolDownTimer mTimer = new CoolDownTimer();

    private boolean mIgnoreClick = false;

    public PhotoItemContainer(Context context) {
        this(context, null, 0);
    }

    public PhotoItemContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoItemContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Global.Injector.inject(this);
        LayoutInflater.from(context).inflate(R.layout.photo_item, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.photo_item_container)
    public void clickOnItemView() {
        if (mPhoto != null && !mIgnoreClick && mTimer.tryFire()) {
            Intent intent = new Intent(getContext(), FloatService.class);
            intent.putExtra(FloatService.EXTRA_PHOTO, mPhoto);
            getContext().startService(intent);
        }
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
        mDraweeView.setImageURI(mPhoto.urls.regular);
        if (!StringUtils.isEmpty(photo.color)) {
            ColorDrawable drawable = new ColorDrawable(Color.parseColor(photo.color));
            mDraweeView.getHierarchy().setPlaceholderImage(drawable);
        }
    }

    public View getPhotoView() {
        return mDraweeView;
    }

    public void ignoreClick(boolean ignoreClick) {
        mIgnoreClick = ignoreClick;
    }


    private static class CoolDownTimer {

        static final long DEFAULT = 5000;
        long coolDownMs = 0;
        long lastFireMs = 0;

        CoolDownTimer() {
            this(DEFAULT);
        }

        CoolDownTimer(long coolDownMs) {
            this.coolDownMs = coolDownMs;
        }

        void fire() {
            lastFireMs = System.currentTimeMillis();
        }

        boolean tryFire() {
            if (isCooled()) {
                fire();
                return true;
            } else {
                return false;
            }
        }

        boolean isCooled() {
            return System.currentTimeMillis() - lastFireMs > coolDownMs;
        }
    }
}
