package com.pierceholdings.dontpause;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

public class Booter extends BroadcastReceiver {

	  @Override
	public void onReceive(Context context, Intent intent) {
	    String action = intent.getAction();
	    
        // This Class starts the service when your device finishes booting.
	    if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
	    	
	    	//Get Shared Preferences
		      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		      
		      //Define Booleans to get
		      boolean startatboot = prefs.getBoolean("startatboot", false);
		      boolean vibenabled = prefs.getBoolean("vib_preference", false);
		      boolean headenabled = prefs.getBoolean("headenabled", false);
		      
		     
		//     Intent serviceIntent = new Intent(context, HeadsetObserverService.class);           
	    //        context.startService(serviceIntent);
	            
	          //Decide which service to start, and start Don't Pause service
		      if (startatboot && vibenabled) {
		    	  context.startService(new Intent(context, MyService2.class));
		    	  } else {
		   		  context.startService(new Intent(context, MyService.class));
		       } 	
		      
		      //If the headset detection is enabled, then lets make sure we always start it at every boot.
		      if (headenabled) {
		    	  context.startService(new Intent(context, HeadsetObserverService.class));
		      }
		      }
		     }
	       }  
	     