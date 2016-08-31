package duan.felix.wallpaper.scaffold.presenter;

import android.view.View;

import duan.felix.wallpaper.scaffold.model.Model;

/**
 * @author Felix.Duan.
 */

public abstract class Presenter<M extends Model, V extends View> {

    public abstract void bind();

    public abstract void unbind();

    public abstract void onStart();

    public abstract void onStop();

    public abstract void onResume();

    public abstract void onPause();
}
