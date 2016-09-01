package duan.felix.wallpaper.scaffold.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import duan.felix.wallpaper.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Felix.Duan.
 */

public class OkHttpClients {

    public static final OkHttpClient DEFAULT = defaultClientBuilder()
            .addInterceptor(new TokenInterceptor())
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    private static OkHttpClient.Builder defaultClientBuilder() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (BuildConfig.DEBUG) {
//            builder.readTimeout(5, TimeUnit.SECONDS)
//                    .writeTimeout(5, TimeUnit.SECONDS)
//                    .connectTimeout(5, TimeUnit.SECONDS);
//        }
        return builder;
    }

    private static class TokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Authorization", Auth.AUTH_ID);
            builder.addHeader("Accept-Version", Host.VERSION);
            return chain.proceed(builder.build());
        }
    }
}
