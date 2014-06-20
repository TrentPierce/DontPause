package com.pierceholdings.dontpause;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsFragmentLocked extends PreferenceFragment {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		// Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferenceslocked);
        
	}
}


