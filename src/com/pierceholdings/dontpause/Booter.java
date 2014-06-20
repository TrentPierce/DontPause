package com.pierceholdings.dontpause;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Booter extends BroadcastReceiver {

	  @Override
	public void onReceive(Context context, Intent intent) {
	    String action = intent.getAction();

	    if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
		      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		      boolean startatboot = prefs.getBoolean("startatboot", false);
		      boolean vibenabled = prefs.getBoolean("vib_preference", false);
		      boolean headenabled = prefs.getBoolean("headenabled", false);
		      Intent serviceIntent = new Intent(context, HeadsetObserverService.class);           
	            context.startService(serviceIntent);
		      if (startatboot) {
		    	  if (vibenabled) {
		    	  context.startService(new Intent(context, MyService2.class));
		    	  } else {
		   		  context.startService(new Intent(context, MyService.class));
		    	  }
		    	  if (headenabled) {
		          	
			    	  context.startService(new Intent(context, HeadsetObserverService.class));
		        } else {
		        	
			  }
		      
	    }
	  }
	  }
}
	