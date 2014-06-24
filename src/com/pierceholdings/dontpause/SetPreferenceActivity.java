package com.pierceholdings.dontpause;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;

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

public class SetPreferenceActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Setup actionBar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setDisplayShowTitleEnabled(true);
	    getSupportActionBar().setTitle("Settings");
	    
	    //Check my prefs
	    SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean unlocked = mySharedPreferences.getBoolean("unlocked", false);
        //Decide whether we are using pro or standard version.
        if (unlocked == true){
        	//Using Pro. Load pro settings
        	getFragmentManager().beginTransaction().replace(android.R.id.content,
                    new PrefsFragment()).commit();
        } else{
        	//Using standard. Load standard settings
        	 getFragmentManager().beginTransaction().replace(android.R.id.content,
                     new PrefsFragmentLocked()).commit();
        }
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	//Take me back to the main activity
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}