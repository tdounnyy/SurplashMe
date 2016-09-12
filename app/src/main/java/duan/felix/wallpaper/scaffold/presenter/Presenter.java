package duan.felix.wallpaper.scaffold.presenter;

import android.view.View;

import duan.felix.wallpaper.scaffold.model.Model;

/**
 * @author Felix.Duan.
 */

public abstract class Presenter<M extends Model, V extends View> {

    public void onStart() {
    }

    public void onStop() {
    }

    public void onResume() {
    }

    public void onPause() {
    }
}
