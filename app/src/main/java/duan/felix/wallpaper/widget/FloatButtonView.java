package duan.felix.wallpaper.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.worker.WallpaperWorker;
import duan.felix.wallpaper.feed.FeedSource;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.service.FloatService;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * TODO ** work status
 * TODO *** one task per time
 * TODO *** close button
 * TODO *** local disk cache demo mode
 * TODO * drag float view
 * @author Felix.Duan.
 */

public class FloatButtonView extends RelativeLayout {

    private static final String TAG = "FloatButtonView";

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
                .flatMap(new Func1<Photo, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Photo photo) {
                        return mWallpaperWorker.setWallpaper(photo);
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        LogUtils.d(TAG, "random photo set");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(TAG, "random photo fail", throwable);
                    }
                });
    }

    @OnLongClick(R.id.btn_float)
    boolean onButtonLongClick() {
        Context context = getContext();
        context.stopService(new Intent(context, FloatService.class));
        return true;
    }

    public void selfDetach() {
        if (isAttachedToWindow()) {
            WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            manager.removeView(this);
        }
    }
}
