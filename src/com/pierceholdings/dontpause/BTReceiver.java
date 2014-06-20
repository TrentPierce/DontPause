package com.pierceholdings.dontpause;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BTReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
	    String action = intent.getAction();

	    if (action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
		      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		      boolean startatBT = prefs.getBoolean("btpref", false);
		      boolean vibenabled = prefs.getBoolean("vib_preference", false);
		      Intent serviceIntent = new Intent(context, USBReceiver.class);           
	            context.startService(serviceIntent);
		      if (startatBT) {
		    	  if (vibenabled) {
		    	  context.startService(new Intent(context, MyService2.class));
		    	  } else {
		   		  context.startService(new Intent(context, MyService.class));
		    	  }
		      }
	    }
		    	  
		      
	    
	    if(action.equals("android.bluetooth.device.action.ACL_DISCONNECTED") ||action.equals("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED")) {
		    	  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			      boolean startatBT = prefs.getBoolean("btpref", false);
			      boolean vibenabled = prefs.getBoolean("vib_preference", false);
			      Intent serviceIntent = new Intent(context, USBReceiver.class);           
		            context.stopService(serviceIntent);
			      if (startatBT) {
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



	