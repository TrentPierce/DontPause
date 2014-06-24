package com.pierceholdings.dontpause;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
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

//This is the main service that makes Don't Pause do what it does. MyService.java is the standard service, and MyService2.java is "Tablet Mode"

	public class MyService extends Service {
		private AudioManager myAudioManager;
		
		
	    boolean notifyenabled, vibenabled;
	    boolean lastWasElse = false;
	    
	        //How often should I check if music is playing?
	        public static final long NOTIFY_INTERVAL = 1000; // 1 second
	     
	        // run on another Thread to avoid crash
	        private Handler mHandler = new Handler();
	        // timer handling
	        private Timer mTimer = null;
			private NotificationManager mNotificationManager;
	     
	        @Override
	        public IBinder onBind(Intent intent) {
	            return null;
	        }
	        
	        @Override
	        public void onDestroy() {
	        	//Decide what needs to be canceled
	        	SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		         boolean notifyenabled = mySharedPreferences.getBoolean("notif_preference", false);
		         boolean ringdef = mySharedPreferences.getBoolean("ringdef", false);

	        	mTimer.cancel();
	        	
	        	//Return to default ringer, or just release control of the ringer settings?
	        	if (ringdef) {
	        		myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	        } else {	
	       }
	      }
	        
	        @Override
	        public void onCreate() {
	         myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	         
	         //Get preferences and check if persistent status bar notification is enabled
	         SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	         boolean notifyenabled = mySharedPreferences.getBoolean("notif_preference", false);
	         
	     if (notifyenabled) {
	    	 //Don't Show notification
	    	}
	     else{
	    	 //Show notification
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
	                    	//Check if music is playing
	                    	if(myAudioManager.isMusicActive() == true) {
	                    		//Music is playing, so set ringer to silent
	                    		if(myAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
	                    		} else {
	    	            		  myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	                    		}
	                    		//Manages the boolean so you can decide whether you prefer vibrate or default ringer while the service is running
	                    		lastWasElse = false;
	    	            	  } else if (!lastWasElse) {
	    	            		  if(myAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
		                    			
		                    		} else {
	    	            		  myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		                    		}
	    	            		lastWasElse = true;  
	    	            	  }
	  	            	    }
	                     });
	                   }
	                 }        	
	               }   
	