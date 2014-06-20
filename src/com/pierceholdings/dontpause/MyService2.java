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


	public class MyService2 extends Service {
		private static final String TAG = "MyService2";
		private AudioManager myAudioManager;
	    int period = 500; // repeat every sec.
	    boolean notifyenabled, vibenabled;
	    boolean lastWasElse = false;
	    int defvolume;
	    
	    
	        // constant
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
	        	SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		         mySharedPreferences.getBoolean("notif_preference", false);
		         mySharedPreferences.getBoolean("ringdef", false);

	        	mTimer.cancel();
	        	
	        	
	        	myAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, defvolume, 0);
	        }
	        @Override
	        public void onCreate() {
	         myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	         SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	         boolean notifyenabled = mySharedPreferences.getBoolean("notif_preference", false);
	         defvolume = myAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
	         Log.d(TAG, "Default volume =" + defvolume);
	         
	     if (notifyenabled) {
	     		 
	   	     
	    	}
	     else{
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
	     
	                    @SuppressWarnings("unused")
						@Override
	                    public void run() {
	                        // display toast
	                    	
	                    	if(myAudioManager.isMusicActive() == true) {
                    			myAudioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_LOWER, 0);
                    			lastWasElse = false;
                    	} else if (!lastWasElse) {
  	            		 
  	            	
						
  	            			  myAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, defvolume, 0);
  	            			  lastWasElse = true;
  	            	  }
	                    }
	     
	                });
	            }        	
	}
	}