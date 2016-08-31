package duan.felix.wallpaper.scaffold.app;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;
import com.squareup.otto.Bus;

import duan.felix.wallpaper.scaffold.net.OkHttpClients;

/**
 * @author Felix.Duan.
 */

public class Global {

    public static Bus bus;

    public Global(Context context) {

        // Fresco
        ImagePipelineConfig config =
                OkHttpImagePipelineConfigFactory
                        .newBuilder(context, OkHttpClients.DEFAULT).build();
        Fresco.initialize(context, config);

        // Otto
        bus = new Bus();

        // Stetho
        Stetho.initializeWithDefaults(context);
    }
}
