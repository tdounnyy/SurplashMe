package duan.felix.wallpaper.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import duan.felix.wallpaper.R;
import duan.felix.wallpaper.core.model.Photo;
import duan.felix.wallpaper.core.model.Progress;
import duan.felix.wallpaper.core.worker.WallpaperWorker;
import duan.felix.wallpaper.feed.FeedSource;
import duan.felix.wallpaper.scaffold.app.Global;
import duan.felix.wallpaper.scaffold.event.Bus;
import duan.felix.wallpaper.scaffold.event.ProgressEvent;
import duan.felix.wallpaper.scaffold.utils.LogUtils;
import duan.felix.wallpaper.scaffold.utils.ToastUtils;
import duan.felix.wallpaper.service.FloatService;
import duan.felix.wallpaper.service.presenter.ProgressPresenter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * TODO *** local disk cache demo mode
 * TODO * drag float view
 *
 * @author Felix.Duan.
 */

public class FloatButtonView extends RelativeLayout {

    private static final String TAG = "FloatButtonView";

    private FeedSource mFeedSource;

    private ProgressPresenter mProgressPresenter;

    @Inject
    WallpaperWorker mWallpaperWorker;

    @BindView(R.id.progress_view)
    ProgressView mProgressView;

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
        mProgressPresenter = new ProgressPresenter(mProgressView);
    }

    @OnClick(R.id.btn_random)
    void onRandomButtonClick() {
        if (mProgressPresenter.working()) {
            ToastUtils.toast(getContext(), "ignore click");
            return;//ignore
        }
        mProgressPresenter.reset();
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
                        Bus.post(new ProgressEvent(Progress.State.SUCCESS));
                        LogUtils.d(TAG, "random photo set");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Bus.post(new ProgressEvent(Progress.State.FAIL));
                        LogUtils.e(TAG, "random photo fail", throwable);
                    }
                });
    }

    @OnClick(R.id.btn_close)
    void onCloseButtonClick() {
        Context context = getContext();
        context.stopService(new Intent(context, FloatService.class));
        return;
    }

    public void selfDetach() {
        if (isAttachedToWindow()) {
            WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            manager.removeView(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        mProgressPresenter.onStart();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mProgressPresenter.onStop();
        super.onDetachedFromWindow();
    }
}
