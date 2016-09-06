package duan.felix.wallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;

/**
 * @author Felix.Duan.
 */

public class FloatPhotoItemContainer extends RelativeLayout {

    private static final String TAG = "FloatPhotoItemContainer";

    @BindView(R.id.photo_item_container)
    PhotoItemContainer mPhotoContainer;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    public FloatPhotoItemContainer(Context context) {
        this(context, null, 0);
    }

    public FloatPhotoItemContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatPhotoItemContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.float_photo_item, this);
        ButterKnife.bind(this);
        mPhotoContainer.ignoreClick(true);
    }

    public void selfDetach() {
        if (isAttachedToWindow()) {
            WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            manager.removeView(this);
        }
    }

    public void setPhoto(Photo photo) {
        mPhotoContainer.setPhoto(photo);
    }

    public View getPhotoView() {
        return mPhotoContainer.getPhotoView();
    }

    public View getProgressBar() {
        return mProgressBar;
    }
}

