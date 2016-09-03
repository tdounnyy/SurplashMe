package duan.felix.wallpaper.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import duan.felix.wallpaper.scaffold.model.Model;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Felix.Duan.
 */

public class Photo extends RealmObject implements Model, Parcelable {

    public static final Photo NULL = new Photo();

    @PrimaryKey
    public String id;

    public PhotoUrls urls;

    public int height;

    public int width;

    public String color;

    public Photo() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        return id != null ? id.equals(photo.id) : photo.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    protected Photo(Parcel in) {
        id = in.readString();
        height = in.readInt();
        width = in.readInt();
        urls = new PhotoUrls();
        urls.raw = in.readString();
        urls.full = in.readString();
        urls.regular = in.readString();
        urls.small = in.readString();
        urls.thumb = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeString(urls.raw);
        dest.writeString(urls.full);
        dest.writeString(urls.regular);
        dest.writeString(urls.small);
        dest.writeString(urls.thumb);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
