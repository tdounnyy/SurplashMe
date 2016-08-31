package duan.felix.wallpaper.scaffold.dagger;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Felix.Duan.
 */
@Singleton
@Component(
        modules = {
                Modules.class,
        }
)
public interface Injection {
}
