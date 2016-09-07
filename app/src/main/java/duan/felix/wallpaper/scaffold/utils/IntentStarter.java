package duan.felix.wallpaper.scaffold.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author Felix.Duan.
 */

public class IntentStarter {

    private static final String TAG = "IntentStarter";

    public static void launchHomeApp(Activity activity) {
        LogUtils.d(TAG, "launchHomeApp");
        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void startService(Context context, Intent intent) {
        LogUtils.d(TAG, "startService");
        context.startService(intent);
    }
}
