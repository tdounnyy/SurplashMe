package duan.felix.wallpaper.scaffold.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Felix.Duan.
 */

public class OkHttpClients {

    public static final OkHttpClient DEFAULT = defaultClientBuilder()
            .addInterceptor(new TokenInterceptor()).build();

    private static OkHttpClient.Builder defaultClientBuilder() {

        return new OkHttpClient.Builder();
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
