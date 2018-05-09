package org.foxail.android.lockscreen;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsUtil {

    private final static String TAG = "SettingsUtil";

    // The file name of settings xml
    public final static String FILE_NAME = "settings";

    public final static String ITEM_FIRST_RUN = "first_run";


    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(SettingsUtil.FILE_NAME, Context.MODE_MULTI_PROCESS);
    }

    public static boolean isFirstRun(Context context) {
        return getPreferences(context).getBoolean(SettingsUtil.ITEM_FIRST_RUN, true);
    }

    public static void setNotFirstRun(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(SettingsUtil.ITEM_FIRST_RUN, false);
        editor.commit();
    }

}
