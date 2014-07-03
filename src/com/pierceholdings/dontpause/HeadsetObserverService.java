package com.pierceholdings.dontpause;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

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


public class HeadsetObserverService extends Service {
	
    private static final String TAG = "HeadsetObserverService";

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