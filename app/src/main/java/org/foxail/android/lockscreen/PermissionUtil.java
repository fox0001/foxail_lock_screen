package org.foxail.android.lockscreen;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class PermissionUtil {

    // Permssion: android.permission.INJECT_EVENTS
    private final static String PMS_INJECT_EVENTS = "android.permission.INJECT_EVENTS";

    /**
     * grant permission
     *
     * @return
     */
    static boolean grantPermission(Context context){
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        int permissionGranted = pm.checkPermission(PMS_INJECT_EVENTS, packageName);
        if(PackageManager.PERMISSION_DENIED == permissionGranted){
            String cmd = "pm grant " + packageName + " " + PMS_INJECT_EVENTS;
            return execRootCmd(cmd);
        } else {
            return true;
        }
    }

    /**
     * check if this device have been rooted
     *
     */
    static boolean isRooted() {
        return execRootCmd("echo test");
    }

    /**
     * execute Linux command
     *
     * @param command
     * @return If the command is executed successfully
     */
    private static boolean execRootCmd(String command) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            command = command + "\n";
            dataOutputStream.writeBytes(command);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            int result = process.exitValue();
            return (result != -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
