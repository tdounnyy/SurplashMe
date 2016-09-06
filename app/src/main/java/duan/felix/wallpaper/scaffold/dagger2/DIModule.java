package duan.felix.wallpaper.scaffold.dagger2;

import android.app.WallpaperManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import duan.felix.wallpaper.core.client.RetrofitFeedClient;
import duan.felix.wallpaper.core.worker.WallpaperWorker;
import duan.felix.wallpaper.feed.FeedSource;
import duan.felix.wallpaper.service.FloatService;
import duan.felix.wallpaper.widget.PhotoItemContainer;

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

    @Provides
    @Singleton
    WallpaperWorker provideWallpaperWorker() {
        return new WallpaperWorker();
    }

    /**
     * @author Felix.Duan.
     */
    @Singleton
    @Component(modules = DIModule.class)
    public interface DIComponent {
        void inject(FeedSource feedSource);

        void inject(WallpaperWorker op);

        void inject(PhotoItemContainer container);

        void inject(FloatService service);
    }
}
