package org.foxail.android.lockscreen;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

public class LockScreenActivity extends Activity {
	
	private final static int REQUEST_CODE_ENABLE_ADMIN = 1;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {		
        super.onCreate(savedInstanceState);

        int sdkVer = Build.VERSION.SDK_INT;
        if(sdkVer < Build.VERSION_CODES.FROYO) {
            // unsupported
            exit();
            return;
        }

        if(sdkVer < Build.VERSION_CODES.M) {
            doLock();
            return;
        }

        if(SettingsUtil.isFirstRun(this)) {
            boolean isGranted = PermissionUtil.grantPermission(this);
            if(isGranted) {
                SettingsUtil.setNotFirstRun(this);
                sendPowerKey();
            } else {
                exit();
            }
        } else {
            sendPowerKey();
        }
	}

    /**
     * Stop running
     */
	private void exit() {
        finish();
        System.exit(0);
    }

    /**
     * lock screen using API
     */
	private void doLock() {
        DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mDeviceAdmin = new ComponentName(LockScreenActivity.this, LockScreenReceiver.class);

        boolean active = mDPM.isAdminActive(mDeviceAdmin);
        if (active) {
            mDPM.lockNow();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, R.string.permission_description);
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        }

        exit();
    }

    /**
     * lock screen using KeyEvent
     */
    private void sendPowerKey() {
        Runnable task = () -> {
            Instrumentation mInst = new Instrumentation();
            mInst.sendKeyDownUpSync(KeyEvent.KEYCODE_POWER);
        };
        new Thread(task).start();

        exit();
    }

    /*
    private void sendPowerKey() {
        InputManager im = (InputManager) getSystemService(Context.INPUT_SERVICE);

        Class[] paramTypes = new Class[2];
        paramTypes[0] = InputEvent.class;
        paramTypes[1] = Integer.TYPE;

        Object[] paramsDown = new Object[2];
        paramsDown[0] = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_POWER);
        paramsDown[1] = 0;

        Object[] paramsUp = new Object[2];
        paramsUp[0] = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_POWER);
        paramsUp[1] = 0;

        try {
            Method hiddenMethod = im.getClass().getMethod("injectInputEvent", paramTypes);
            hiddenMethod.invoke(im, paramsDown);
            hiddenMethod.invoke(im, paramsUp);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    */
}
