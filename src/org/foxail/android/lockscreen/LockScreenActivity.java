package org.foxail.android.lockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LockScreenActivity extends Activity {
	
	private final static int REQUEST_CODE_ENABLE_ADMIN = 1;
	
	DevicePolicyManager mDPM;
    ComponentName mDeviceAdmin;
    public static final int EXIT_APPLICATION = 0x0001;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {		
        super.onCreate(savedInstanceState);
		mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdmin = new ComponentName(LockScreenActivity.this, LockScreenReceiver.class);

        boolean active = mDPM.isAdminActive(mDeviceAdmin);
        if (active) {
            mDPM.lockNow();
        } else {
        	Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
        	intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, R.string.permission_description);
        	startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        }
        finish();
        System.exit(0);
	}

}
