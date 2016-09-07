package duan.felix.wallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.worker.WallpaperWorker;
import duan.felix.wallpaper.feed.FeedSource;
import duan.felix.wallpaper.scaffold.app.Global;
import rx.functions.Action1;

/**
 * @author Felix.Duan.
 */

public class FloatButtonView extends RelativeLayout {

    private FeedSource mFeedSource;

    @Inject
    WallpaperWorker mWallpaperWorker;

    @BindView(R.id.btn_float)
    Button mButton;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    public FloatButtonView(Context context) {
        this(context, null, 0);
    }

    public FloatButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.float_button_view, this);
        ButterKnife.bind(this);
        Global.Injector.inject(this);
        mFeedSource = new FeedSource(null);
    }

    @OnClick(R.id.btn_float)
    void onButtonClick() {
        mFeedSource.getRandomPhoto()
                .subscribe(new Action1<Photo>() {
                    @Override
                    public void call(Photo photo) {
                        mWallpaperWorker.setWallpaper(photo);
                    }
                });
//                manager.removeViewImmediate(button);
    }

}
