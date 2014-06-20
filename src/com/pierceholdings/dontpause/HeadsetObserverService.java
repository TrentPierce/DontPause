package com.pierceholdings.dontpause;

import java.util.Collections;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

	public class HeadsetObserverService extends Service {
		
	    private static final String TAG = "HeadsetObserverService";
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    @Override
	    public IBinder onBind(Intent arg0) {
	        return null;
	    }

	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {

	        // Create a filter. We are interested in the Intent.ACTION_HEADSET_PLUG action
	        IntentFilter filter = new IntentFilter();
	        filter.addAction(Intent.ACTION_HEADSET_PLUG);

	        // Register a new HeadsetReceiver
	        registerReceiver(new HeadsetReceiver(), filter);

	        // We return START_STICKY. If our service gets destroyed, Android will try to restart it when resources are available.      
	        return START_STICKY;
	    }

	    private class HeadsetReceiver extends BroadcastReceiver {
	    	
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
	            	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	            	boolean vibenabled = prefs.getBoolean("vib_preference", false);
	                int state = intent.getIntExtra("state", -1);
	                switch (state) {
	                
	                case 0:
	                     Log.d("unplugged", "Headset was unplugged");
	                     
	                     if (vibenabled) {
	   	  		    	  context.stopService(new Intent(context, MyService2.class));
	   	  		    	  } else {
	   	  		   		  context.stopService(new Intent(context, MyService.class));
	   	  		    	  }
	                     // Cancel notification here

	                break;
	                case 1:
	                     Log.d("plugged", "Headset is plugged");
	                     if (vibenabled) {
	   	  		    	  context.startService(new Intent(context, MyService2.class));
	   	  		    	  } else {
	   	  		   		  context.startService(new Intent(context, MyService.class));
	   	  		    	  }
	                     // Show notification

	                break;
	                default:
	                    Log.w("uh", "I have no idea what the headset state is");
	                }
	            }
	        }
	    }
	}