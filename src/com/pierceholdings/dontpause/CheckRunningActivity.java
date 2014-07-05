package com.pierceholdings.dontpause;


import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class CheckRunningActivity extends Service {
    private static final String TAG = "CheckRunningActivity";
    boolean checkApps;
    private Timer mTimer = null;
    private Handler mHandler = new Handler();
    private ActivityManager am;
    boolean vibenabled;
    private Context context;
  //  public String selectedPkg;
    private SharedPreferences prefs;
    
    public static final long NOTIFY_INTERVAL = 1000; // 1 second
   
        @Override
        public void onDestroy() {
        	mTimer.cancel();
        }
    	@Override
    	public IBinder onBind(Intent intent) {
    		// TODO Auto-generated method stub
    		return null;
    	}
    	
    	private boolean isEnabled(String pkgName) {
    		//prefs.reload();
            return prefs.getBoolean(pkgName, false);
        }
    @Override
    public void onCreate() { 
    	
    	 //Get preferences and check if persistent status bar notification is enabled
   // 	ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
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
                	SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                     vibenabled = mySharedPreferences.getBoolean("vib_preference", false);
                	ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                	Log.d(TAG, "Its running");   
            
            String lastPkg = am.getRunningTasks(1).get(0).topActivity.getPackageName();
            
           if (isEnabled(lastPkg)) {
            	if (vibenabled) {
            	startService(new Intent(CheckRunningActivity.this, MyService.class));
 	  		    	  } else {
 	  		    startService(new Intent(CheckRunningActivity.this, MyService2.class));
 	  		    	  } 
           } else {
 	  		if (vibenabled) {
 	  		    stopService(new Intent(CheckRunningActivity.this, MyService2.class));
 	    	  		  } else {
 	    	    stopService(new Intent(CheckRunningActivity.this, MyService.class));
 	    	  		    	  }
 	  		    	  }
            
            
            	//Toast.makeText(getBaseContext(), packageName, Toast.LENGTH_LONG).show();
            	//Log.d(TAG, "Make Toast");           
               }
      	    });
	}
}
}
