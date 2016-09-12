package duan.felix.wallpaper.scaffold.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import duan.felix.wallpaper.scaffold.utils.LogUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Felix.Duan.
 */

public class OkHttpClients {

    private static final String TAG = "OkHttpClients";

    private static final int RETRY_COUNT = 5;

    public static final OkHttpClient DEFAULT = defaultClientBuilder()
            .addInterceptor(new TokenInterceptor())
            .addNetworkInterceptor(new StethoInterceptor())
            .addInterceptor(new RetryInterceptor())
            .build();

    private static OkHttpClient.Builder defaultClientBuilder() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
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

    private static class RetryInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            Response response = chain.proceed(request);
            int retryCount = 0;
            while (!response.isSuccessful() && retryCount < RETRY_COUNT) {
                LogUtils.d(TAG, "retry request " + retryCount);
                retryCount++;
                response = chain.proceed(request);
            }

            return response;
        }
    }
}
