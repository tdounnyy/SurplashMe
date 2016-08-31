package duan.felix.wallpaper.scaffold.dagger2;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import duan.felix.wallpaper.core.client.FeedClient;
import duan.felix.wallpaper.feed.FeedSource;

/**
 * @author Felix.Duan.
 */
@Module
public class DIModule {

    public DIModule() {
    }

    @Provides
    @Singleton
    FeedClient provideFeedClient() {
        return new FeedClient();
    }

    /**
     * @author Felix.Duan.
     */
    @Singleton
    @Component(modules = DIModule.class)
    public interface DIComponent {
        void inject(FeedSource feedSource);
    }
}
