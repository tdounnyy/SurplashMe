package duan.felix.wallpaper.core.model;

import duan.felix.wallpaper.scaffold.model.Model;

/**
 * @author Felix.Duan.
 */

public class Photo implements Model {
    public String id;
    public Urls urls;
    public int height;
    public int width;

    public class Urls {
        public String raw;
        public String full;
        public String regular;
        public String small;
        public String thumb;

        @Override
        public String toString() {
            return "Urls{" +
                    "raw='" + raw + '\'' +
                    ", full='" + full + '\'' +
                    ", regular='" + regular + '\'' +
                    ", small='" + small + '\'' +
                    ", thumb='" + thumb + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", urls=" + urls +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
