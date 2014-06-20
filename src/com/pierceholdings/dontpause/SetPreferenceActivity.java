package com.pierceholdings.dontpause;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;

public class SetPreferenceActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setDisplayShowTitleEnabled(true);
	    getSupportActionBar().setTitle("Settings");
	    
	    SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean unlocked = mySharedPreferences.getBoolean("unlocked", false);
        if (unlocked == true){
        	getFragmentManager().beginTransaction().replace(android.R.id.content,
                    new PrefsFragment()).commit();
        } else{
        	 getFragmentManager().beginTransaction().replace(android.R.id.content,
                     new PrefsFragmentLocked()).commit();
        }
	    
	//	}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
