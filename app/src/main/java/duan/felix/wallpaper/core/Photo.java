package duan.felix.wallpaper.core;

/**
 * @author Felix.Duan.
 */

public class Photo {
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
    }
}
