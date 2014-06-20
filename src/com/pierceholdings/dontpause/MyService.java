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


	public class MyService extends Service {
		private AudioManager myAudioManager;
	    int period = 1000; // repeat every sec.
	    boolean notifyenabled, vibenabled;
	    boolean lastWasElse = false;
	    
	        // constant
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
	        	SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		         boolean notifyenabled = mySharedPreferences.getBoolean("notif_preference", false);
		         boolean ringdef = mySharedPreferences.getBoolean("ringdef", false);

	        	mTimer.cancel();
	        	
	        	
	        	if (ringdef) {
	        		myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	        } else {
	        	
	        }
	        }
	           
	        
	           
	     
	        @Override
	        public void onCreate() {
	    //    	String pkgName = lpparam.packageName;

	            // only run on packages selected
	     //       if (isEnabled(pkgName)) {
	         myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	         SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	         boolean notifyenabled = mySharedPreferences.getBoolean("notif_preference", false);
	         
	
	         
	         
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
	     
	                    @Override
	                    public void run() {
	                        // display toast
	                    	if(myAudioManager.isMusicActive() == true) {
	                    		if(myAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
	                    			
	                    		} else {
	    	            		  myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	    	            		
	                    		}
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
	     
	                }{;
	            }        	
	}
	}
	