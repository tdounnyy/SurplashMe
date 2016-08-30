package duan.felix.wallpaper.scaffold.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Felix.Duan.
 */
class Gsons {

    static final GsonConverterFactory DEFAULT =
            GsonConverterFactory.create(
                    new GsonBuilder()
                            .setFieldNamingPolicy(
                                    FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create()
            );


}
