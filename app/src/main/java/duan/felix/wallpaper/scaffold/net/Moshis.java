package duan.felix.wallpaper.scaffold.net;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

import java.lang.annotation.Retention;

import retrofit2.converter.moshi.MoshiConverterFactory;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Felix.Duan.
 */

public class Moshis {

    @Retention(RUNTIME)
    @JsonQualifier
    public @interface HexColor {

    }

    private static class ColorAdapter {
        @ToJson
        String toJson(@HexColor int rgb) {
            return String.format("#%06x", rgb);
        }

        @FromJson
        @HexColor
        int fromJson(String rgb) {
            return Integer.parseInt(rgb.substring(1), 16);
        }
    }

    static final MoshiConverterFactory FACTORY = MoshiConverterFactory.create(
            new Moshi.Builder()
                    .add(new ColorAdapter())
                    .build());

}
