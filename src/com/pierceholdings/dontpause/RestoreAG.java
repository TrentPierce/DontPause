package com.pierceholdings.dontpause;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Toast;

import com.bastionsdk.android.Bastion;
import com.bastionsdk.android.BastionRestoreListener;
import com.bastionsdk.android.FailReason;
import com.bastionsdk.android.Feature;
import com.bastionsdk.android.Resource;

public class RestoreAG extends Activity {

	protected static final String TAG = "Restore";
	boolean unlocked = false;
	boolean adsenabled = false;

	private Toast toast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.donate);
		Bastion.restore(new BastionRestoreListener()
	    {

	        @Override
	        public void onRestoreSucceed(List<Feature> features, List<Resource> resources)
	        {
	        	 savePreferences(TAG, unlocked);
	        	 toast("Restore Sucessful. Don't Pause Pro Unlocked");
	        }

	        @Override
	        public void onRestoreFailed(FailReason reason)
	        {
	        	toast("Restore Unsucessful. Please purchase the in app upgrade...");
	        }
	    });
	}
	private void savePreferences(String key, boolean value) {
		 
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("unlocked", true);
        editor.putBoolean("ad_pref", true);
        editor.commit();
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   startActivity(i);
        
    }

	private void toast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(getApplicationContext(), "",
							Toast.LENGTH_LONG);
				}
				toast.setText(msg);
				toast.show();
			}
		});
	}	
}
