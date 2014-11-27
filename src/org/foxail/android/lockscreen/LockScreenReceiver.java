package org.foxail.android.lockscreen;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class LockScreenReceiver extends DeviceAdminReceiver {

    void showToast(Context context, int msgTextId) {
    	Toast.makeText(context, msgTextId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, R.string.msg_permission_enabled);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, R.string.msg_permission_disabled);
    }
	
}
