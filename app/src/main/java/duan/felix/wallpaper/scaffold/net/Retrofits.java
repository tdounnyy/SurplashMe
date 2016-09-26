package duan.felix.wallpaper.scaffold.net;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author Felix.Duan.
 */

public class Retrofits {

    public static Retrofit.Builder defaultBuilder() {
        return new Retrofit.Builder().baseUrl(
                new HttpUrl.Builder()
                        .scheme("https")
                        .host(Host.DEFAULT)
                        .build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(Moshis.FACTORY);
    }
}
