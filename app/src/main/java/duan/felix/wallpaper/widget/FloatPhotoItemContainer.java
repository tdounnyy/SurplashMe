package duan.felix.wallpaper.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import butterknife.OnClick;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.scaffold.utils.LogUtils;

/**
 * @author Felix.Duan.
 */

public class FloatPhotoItemContainer extends PhotoItemContainer {

    private static final String TAG = "FloatPhotoItemContainer";

    public FloatPhotoItemContainer(Context context) {
        this(context, null, 0);
    }

    public FloatPhotoItemContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatPhotoItemContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = new View(context);
        v.setBackgroundColor(Color.RED);
        v.setAlpha(0.2f);
        addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @OnClick(R.id.photo_item_container)
    public void clickOnItemView() {
        // ignore click events
    }

    public View getPhotoView() {
        return mDraweeView;
    }

    public void selfDetach() {
        LogUtils.d(TAG, "selfDetach");
        if (isAttachedToWindow()) {
            WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            manager.removeView(this);
        }
    }
}

