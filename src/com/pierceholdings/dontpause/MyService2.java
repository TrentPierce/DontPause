package com.pierceholdings.dontpause;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
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

//This is "Tablet mode". It only silences the notification stream because many tablets don't have a vibrate mode.
	public class MyService2 extends Service {
		private static final String TAG = "MyService2";
		private AudioManager myAudioManager;
	    boolean notifyenabled, vibenabled;
	    boolean lastWasElse = false;
	    
	    //This int saves your volume level so it can be restored
	    int defvolume;
	    
	        //How often should we check for music playing? Every half second here, but once every second in standard mode. 
	        public static final long NOTIFY_INTERVAL = 500; // .50 second
	     
	        // run on another Thread to avoid crash
	        private Handler mHandler = new Handler();
	        // timer handling
	        private Timer mTimer = null;
			@Override
	        public IBinder onBind(Intent intent) {
	            return null;
	        }
	        
	        @Override
	        public void onDestroy() {
	        	//Get preferences to decide what should be destroyed
	        	SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		         mySharedPreferences.getBoolean("notif_preference", false);
		         mySharedPreferences.getBoolean("ringdef", false);

	        	mTimer.cancel();
	        	
	        	//When you destroy the service, restore the volume back to what it was before the service ran.
	        	myAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, defvolume, 0);
	        }
	        
	        
	        @Override
	        public void onCreate() {
	         myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	         //Check preferences
	         SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	         //Are notifications enabled?
	         boolean notifyenabled = mySharedPreferences.getBoolean("notif_preference", false);
	         defvolume = myAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
	         
	         //Log that default volume
	         Log.d(TAG, "Default volume =" + defvolume);
	         
	         
	     if (notifyenabled) {
	     		 //Notification disabled
	    	}
	     else{
	    	 //Notification enabled...Show notification
	    	 Notification notification = new Notification(R.drawable.notification2, "Service Started", System.currentTimeMillis());
	         
	         Intent main = new Intent(this, MainActivity.class);
	         main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	         PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, main,  PendingIntent.FLAG_UPDATE_CURRENT);
	      
	         notification.setLatestEventInfo(this, "Don't Pause", "monitoring your music...", pendingIntent);
	         notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
	      
	         startForeground(2, notification);
	     }
	            // cancel if already existed
	            if(mTimer != null) {
	                mTimer.cancel();
	            } else {
	                // recreate new
	                mTimer = new Timer();
	            }
	            // schedule task
	            mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
	        }
	        
			class TimeDisplayTimerTask extends TimerTask {
	     
	            @Override
	            public void run() {
	                // run on another thread
	                mHandler.post(new Runnable() {
	     
	                    @Override
	                    public void run() {
	                    	//Check if music is playing every .5 second
	                    	if(myAudioManager.isMusicActive() == true) {
	                    		//Music is playing. Lower that Notification stream all the way
                    			myAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_LOWER, 0);
                    			lastWasElse = false;
                    	} else if (!lastWasElse) {
                    		//Music is no longer playing. Restore the volume.
  	            			  myAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, defvolume, 0);
  	            			  lastWasElse = true;
  	            	    }
	                  }
	               });
	             }        	
	           }
          	}