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


public class BTReceiver extends BroadcastReceiver {
	
	//This class catches the Bluetooth connected and disconnected broadcast and enables or disables the service.
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    String action = intent.getAction();
	    
        // If Bluetooth connected, get preferences
	    if (action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
	    	
	    	//Get shared preferences
		      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		      boolean startatBT = prefs.getBoolean("btpref", false);
		      boolean vibenabled = prefs.getBoolean("vib_preference", false);
		      Intent serviceIntent = new Intent(context, USBReceiver.class);           
	            context.startService(serviceIntent);
		      if (startatBT) {
		    	  
		    	  //Decide whether to start tablet mode or standard mode.
		    	  if (vibenabled) {
		    	  context.startService(new Intent(context, MyService2.class));
		    	  } else {
		   		  context.startService(new Intent(context, MyService.class));
		    	  }
		      }
	    }
		    	  
		      
	    //If bluetooth device is disconnected
	    if(action.equals("android.bluetooth.device.action.ACL_DISCONNECTED") ||action.equals("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED")) {
	    	
	           	//Get shared preferences
		    	  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			      boolean startatBT = prefs.getBoolean("btpref", false);
			      boolean vibenabled = prefs.getBoolean("vib_preference", false);
			      Intent serviceIntent = new Intent(context, USBReceiver.class);           
		            context.stopService(serviceIntent);
			      if (startatBT) {
			    	  
			    	  //Decide whether to start tablet mode or standard mode
			    	  if (vibenabled) {
			    	  context.stopService(new Intent(context, MyService2.class));
			    	  } else {
			   		  context.stopService(new Intent(context, MyService.class));
			    	  } 	
				  }
	          }
	      }
	  }



	