package duan.felix.wallpaper.core.model;

import io.realm.RealmObject;

/**
 * @author Felix.Duan.
 */
public class PhotoUrls extends RealmObject {

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
