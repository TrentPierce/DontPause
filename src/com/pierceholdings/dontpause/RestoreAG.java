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

/**
 * Developed by Trent Pierce for Pierce Holdings LLC
 *
 *Copyright 2014 Pierce Holdings LLC
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */

public class RestoreAG extends Activity {

	//This activity is for restoring a purchase through Bastion.
	//This only works if Bastion was used to unlock thr pro version through the AppGratis Promotion.
	
	protected static final String TAG = "Restore";
	boolean unlocked = false;
	boolean adsenabled = false;

	private Toast toast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
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
