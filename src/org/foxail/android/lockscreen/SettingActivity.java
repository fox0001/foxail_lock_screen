package org.foxail.android.lockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends Activity {
	
	private final static int REQUEST_CODE_ENABLE_ADMIN = 1;
	
	DevicePolicyManager mDPM;
    ComponentName mDeviceAdmin;

    Button buttonOp;
    Button buttonQuit;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdmin = new ComponentName(SettingActivity.this, LockScreenReceiver.class);

        setContentView(R.layout.setting);
        
        buttonOp = (Button)findViewById(R.id.button_op);
        buttonOp.setOnClickListener(buttonOpListener);
        buttonQuit = (Button)findViewById(R.id.button_quit);
        buttonQuit.setOnClickListener(buttonQuitListener);
        
        updateButtonOp();
	}
	
	private void updateButtonOp() {
		boolean active = mDPM.isAdminActive(mDeviceAdmin);
        if(active){
        	buttonOp.setText(R.string.button_disable);
        } else {
        	buttonOp.setText(R.string.button_enable);
        }
	}

    private OnClickListener buttonOpListener = new OnClickListener() {
        public void onClick(View v) {
    		boolean active = mDPM.isAdminActive(mDeviceAdmin);
            if(active){
                mDPM.removeActiveAdmin(mDeviceAdmin);
            } else {
            	Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
            	intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, R.string.permission_description);
            	startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
            }
            System.exit(0);
        }
    };
    
    private OnClickListener buttonQuitListener = new OnClickListener() {
        public void onClick(View v) {
        	System.exit(0);
        }
    };

}
