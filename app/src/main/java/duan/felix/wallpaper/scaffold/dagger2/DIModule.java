package duan.felix.wallpaper.scaffold.dagger2;

import android.app.WallpaperManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import duan.felix.wallpaper.browser.FloatActivity;
import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.worker.WallpaperOperator;
import duan.felix.wallpaper.feed.FeedSource;

/**
 * @author Felix.Duan.
 */
@Module
public class DIModule {

    private final Context baseContext;

    public DIModule(Context context) {
        baseContext = context;
    }

    @Provides
    @Singleton
    RetrofitFeedClient provideFeedClient() {
        return new RetrofitFeedClient();
    }

    @Provides
    @Singleton
    WallpaperManager provideWallPaperManager() {
        return WallpaperManager.getInstance(baseContext);
    }

    /**
     * @author Felix.Duan.
     */
    @Singleton
    @Component(modules = DIModule.class)
    public interface DIComponent {
        void inject(FeedSource feedSource);

        void inject(WallpaperOperator op);

        void inject(FloatActivity activity);
    }
}
