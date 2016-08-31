package duan.felix.wallpaper;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import duan.felix.wallpaper.scaffold.net.OkHttpClients;

/**
 * @author Felix.Duan.
 */

public class WApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ImagePipelineConfig config =
                OkHttpImagePipelineConfigFactory
                        .newBuilder(this, OkHttpClients.DEFAULT).build();
        Fresco.initialize(this, config);
    }
}
