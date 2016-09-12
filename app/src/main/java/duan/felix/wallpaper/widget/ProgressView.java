package duan.felix.wallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Progress;

/**
 * @author Felix.Duan.
 */

public class ProgressView extends TextView {

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void update(Progress.State state) {
        switch (state) {
            case IDLE:
                setText(null);
                break;
            case FETCHING:
                setText(R.string.progress_fetching);
                break;
            case MEASURING:
                setText(R.string.progress_measuring);
                break;
            case DOWNLOADING:
                setText(R.string.progress_downloading);
                break;
            case RESIZING:
                setText(R.string.progress_resizing);
                break;
            case SETTING:
                setText(R.string.progress_setting);
                break;
            case SUCCESS:
                setText(R.string.progress_success);
                break;
            case FAIL:
                setText(R.string.progress_fail);
                break;
        }

        setVisibility(state == Progress.State.IDLE ? GONE : VISIBLE);
    }

    public void reset() {
        setText(null);
        setVisibility(GONE);
    }
}
