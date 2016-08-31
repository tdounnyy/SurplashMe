package duan.felix.wallpaper.scaffold.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import duan.felix.wallpaper.core.net.PhotosClient;

/**
 * @author Felix.Duan.
 */

@Module
public class Modules {

    @Provides
    @Singleton
    public PhotosClient providesPhotosClient() {
        return new PhotosClient();
    }
}
