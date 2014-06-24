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

public class USBReceiver extends BroadcastReceiver {
	
	//This class detects a broadcast from your usb port. At the time of implementation, I could only detect power connected/disconnected broadcasts.

		  @Override
		public void onReceive(Context context, Intent intent) {
		    String action = intent.getAction();
            //If power (USB) was connected
		    if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
		    	//Check prefs to see what is enabled
			      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			      boolean startatUSB = prefs.getBoolean("usbpref", false);
			      boolean vibenabled = prefs.getBoolean("vib_preference", false);
			      Intent serviceIntent = new Intent(context, USBReceiver.class);           
		            context.startService(serviceIntent);
		            //If usb start enabled, start service.
			      if (startatUSB) {
			    	  //Determine whether to start standard mode or tablet mode.
			    	  if (vibenabled) {
			    	  context.startService(new Intent(context, MyService2.class));
			    	  } else {
			   		  context.startService(new Intent(context, MyService.class));
			    	  }
			      }
		    }
		          //Power (USB) was disconnected
			      if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
			    	  //CHeck prefs to see what to stop
			    	  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				      boolean startatUSB = prefs.getBoolean("usbpref", false);
				      boolean vibenabled = prefs.getBoolean("vib_preference", false);
				      Intent serviceIntent = new Intent(context, USBReceiver.class);           
			            context.stopService(serviceIntent);
			            //If USB enabled, stop it.
				      if (startatUSB) {
				    	  //Decide whether to stop tablet or standard mode.
				    	  if (vibenabled) {
				    	  context.stopService(new Intent(context, MyService2.class));
				    	  } else {
				   		  context.stopService(new Intent(context, MyService.class));
				    	  }	
					  }
		          }
		      }
           }