package duan.felix.wallpaper.scaffold.app;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;

import duan.felix.wallpaper.scaffold.dagger2.DIModule;
import duan.felix.wallpaper.scaffold.dagger2.DaggerDIModule_DIComponent;
import duan.felix.wallpaper.scaffold.net.OkHttpClients;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Felix.Duan.
 */

public class Global {

    public static DIModule.DIComponent Injector;
    public static Context App;

    public Global(Context context) {

        // App context
        App = context;
        // Fresco
        ImagePipelineConfig frescoConfig =
                OkHttpImagePipelineConfigFactory
                        .newBuilder(context, OkHttpClients.DEFAULT).build();
        Fresco.initialize(context, frescoConfig);

        // Stetho
        Stetho.initializeWithDefaults(context);

        // Dagger2
        // TODO: * can be simpler using create()
        Injector = DaggerDIModule_DIComponent
                .builder()
                .dIModule(new DIModule(context)).build();

        // Realm
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
