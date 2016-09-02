package duan.felix.wallpaper.scaffold.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * @author Felix.Duan.
 */

public class ActivityStarter {

    public static void launchHomeApp(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
