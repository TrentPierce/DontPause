package com.pierceholdings.dontpause;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



public class USBReceiver extends BroadcastReceiver {
	

		  @Override
		public void onReceive(Context context, Intent intent) {
		    String action = intent.getAction();

		    if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
			      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			      boolean startatUSB = prefs.getBoolean("usbpref", false);
			      boolean vibenabled = prefs.getBoolean("vib_preference", false);
			      Intent serviceIntent = new Intent(context, USBReceiver.class);           
		            context.startService(serviceIntent);
			      if (startatUSB) {
			    	  if (vibenabled) {
			    	  context.startService(new Intent(context, MyService2.class));
			    	  } else {
			   		  context.startService(new Intent(context, MyService.class));
			    	  }
			      }
		    }
			    	  
			      
		    
			      if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
			    	  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				      boolean startatUSB = prefs.getBoolean("usbpref", false);
				      boolean vibenabled = prefs.getBoolean("vib_preference", false);
				      Intent serviceIntent = new Intent(context, USBReceiver.class);           
			            context.stopService(serviceIntent);
				      if (startatUSB) {
				    	  if (vibenabled) {
				    	  context.stopService(new Intent(context, MyService2.class));
				    	  } else {
				   		  context.stopService(new Intent(context, MyService.class));
				    	  }
				    	  
				       
				        	
					  }
		  }
		  }
{}
		  }

	
	
		