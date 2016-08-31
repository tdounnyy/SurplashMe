package duan.felix.wallpaper.core.model;

import duan.felix.wallpaper.scaffold.model.Model;

/**
 * @author Felix.Duan.
 */

public class Feed implements Model {
    public String id;
    public String title;
    public Photo coverPhoto;

    public Feed() {
        this(null, null, null);
    }

    public Feed(String id, String title, Photo coverPhoto) {
        this.id = id;
        this.title = title;
        this.coverPhoto = coverPhoto;
    }
}
