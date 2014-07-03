package com.pierceholdings.dontpause;


import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bastionsdk.android.Bastion;
import com.bastionsdk.android.BastionOfferListener;
import com.bastionsdk.android.Feature;
import com.bastionsdk.android.Offer;
import com.bastionsdk.android.Resource;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

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

public class MainActivity extends SherlockActivity  implements OnClickListener, BastionOfferListener {
	
	  private static final String TAG = "MyService";
	  ImageButton buttonStart, buttonStop;
	  boolean vibenabled;
	  boolean adsenabled;
	  NotificationManager mNotificationManager;
	  boolean unlocked = false;
	  private InterstitialAd interstitial;

	  
	  //This class is the UI of the app. This is where I define buttons and decide whether you are using the free or pro version
	  
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    //OnCreate check my shared preferences
	    SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    //Was the app previously unlocked through in app purchase? Set the contentView accordingly
        boolean unlocked = mySharedPreferences.getBoolean("unlocked", false);
        if (unlocked == true){
        	//It was unlocked
            setContentView(R.layout.activity_main);
        } else{
        	//It was not unlocked
            setContentView(R.layout.activity_mainlocked);
        }
        
        //Are ads enabled?
        SharedPreferences mySharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        boolean adsenabled = mySharedPreferences1.getBoolean("ad_pref", false);
  	    
        //Load Bastion
        Bastion.onStart(
                this,  /* Give the current activity */
                this); /* Pass this as second parameter since  we're implementing BastionOfferListener */
        
        
  	 // Create the interstitial ad.
  	    interstitial = new InterstitialAd(this);
  	    interstitial.setAdUnitId("ca-app-pub-5038706716632727/8458526186");

  	    // Create ad request.
  	    AdRequest adRequest = new AdRequest.Builder().build();

  	    // Begin loading your interstitial.
  	    interstitial.loadAd(adRequest);

  	    //Setup the actionbar sherlock action bar
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	    
		String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        Log.i("Device Id", deviceId); 
        
        //Set image in the middle of the activity. This will change depending on whether the service is running or not.
	    ImageView DPIcon = (ImageView) findViewById(R.id.img);
	    DPIcon.setBackgroundResource(R.drawable.dont_pause_icon);
	    
	    //Define my Buttons
	    buttonStart = (ImageButton) findViewById(R.id.buttonStart);
	    buttonStop = (ImageButton) findViewById(R.id.buttonStop);
        
	    //Set onClick listeners for those buttons
	    buttonStart.setOnClickListener(this);
	    buttonStop.setOnClickListener(this);
	    
	  //  startService(new Intent(this, CheckRunningActivity.class));
	    
	    //Get those booleans
        boolean vibenabled = mySharedPreferences1.getBoolean("vib_preference", false);
        boolean headenabled = mySharedPreferences1.getBoolean("headenabled", false);
        
        //Check whether headset service is enabled
        if (headenabled) {
        	//Start headset observer
	    	  startService(new Intent(this, HeadsetObserverService.class));
        } else {
        	//Stop headset observer
        	stopService(new Intent(this, HeadsetObserverService.class));
	  }
 }

	  //What shall we do with all those clicks?
	  @Override
	public void onClick(View src) {
		  //We shall design those fancy toasts (Those fancy toasts are just an inflated relativeLayout)
		  LayoutInflater inflater = getLayoutInflater();
	        View view = inflater.inflate(R.layout.cust_toast_layout,
	                                       (ViewGroup) findViewById(R.id.relativeLayout1));
	        
	        LayoutInflater inflater1 = getLayoutInflater();
	        View view1 = inflater1.inflate(R.layout.cust_toast_layout2,
	                                       (ViewGroup) findViewById(R.id.relativeLayout2));
	        
	        //Check those preferences again. You never know when someone will change a preference...
		  SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	         boolean vibenabled = mySharedPreferences.getBoolean("vib_preference", false);
	         SharedPreferences mySharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
	         boolean adsenabled = mySharedPreferences1.getBoolean("ad_pref", false);
	         
	         //Heres the icon switcher I was talking about. Are you ready?
	         ImageView DPIcon = (ImageView) findViewById(R.id.img);
	         
	    //Use a switch for Start/Stop buttons
	    switch (src.getId()) {
	    
	    //Start Button
	    case R.id.buttonStart:
	    	
	      //Logging always helps with debugging
	      Log.d(TAG, "onClick: starting service");
	      
	      //Set that main image to the green "selected" version. See what I did there?
	      DPIcon.setImageResource(R.drawable.dont_pause_icon_selected);
	      
	      //What boolean did we get from the most recent check of shared preferences?
	      //This determines whether we start standard mode or tablet mode
	      if (vibenabled) {
	    	  //Start tablet mode
	    	  startService(new Intent(this, MyService2.class));
	      } else {
	    	  //Start standard mode
	    	  startService(new Intent(this, MyService.class));
	      }
	      
	      //Its time to show that fancy starting service toast
	      Toast toast = new Toast(this);
	        toast.setView(view);
	        toast.show();
	        
	      //Count those launches for AppRater
	      AppRater.app_launched(this);
	      
	        //You know I love to log
			Log.d(TAG, "onStart");
			
			//Build and load that Interstitial ad
			AdRequest adRequest = new AdRequest.Builder().build();
			interstitial.loadAd(adRequest);
	      break;
	      
	      //Stop Button
	    case R.id.buttonStop:
	    	
	      //Simplify your debugging with logs
	      Log.d(TAG, "onClick: stopping service");
	      
	      //Set the main image to the red version
	      DPIcon.setImageResource(R.drawable.dont_pause_icon);
	      
	      //DO I need to stop tablet mode or standard mode?
	      if (vibenabled) {
	    	  //Stop Tablet mode
	    	  stopService(new Intent(this, MyService2.class));
	      } else {
	    	  //Stop standard mode
	    	  stopService(new Intent(this, MyService.class));
	      }
	      
	      //Show that fancy stop toast
	      Toast toast1 = new Toast(this);
	        toast1.setView(view1);
	        toast1.show();
	        
	        //You know I log everything
			Log.d(TAG, "onDestroy");
			
			//Stop Bastion
			Bastion.onStop(this);
			
			//Check whether I have ads enabled? We already loaded it anyway, but should I show it?
          if (adsenabled) {
	//Don't show me an ad
               } else {
//I love to support hard working developers. Show me that beautiful interstitial ad; if it actually loaded.	
			   if (interstitial.isLoaded()) {
				      //Show it!
				      interstitial.show();
			    }
			}
	      break;
	    }
	  }
	  
	  //Now it is time for an options menu
	  @Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			// Inflate the menu; this adds items to the action bar if it is present.
			getSupportMenuInflater().inflate(R.menu.main_activity_actions, menu);
			
			//Make sure I am using android 3.0+ to support the actionbar
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
				getSupportMenuInflater().inflate(R.menu.mainmenu, menu);
				}
			return true;
		}
	  
      //Think of this as an onClick listener for your action bar
      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
    	  
      //Define the toast that we will show to anyone using a version of Android below 3.0.
	  LayoutInflater inflater2 = getLayoutInflater();
      View view2 = inflater2.inflate(R.layout.cust_toast_layout3,
                                   (ViewGroup) findViewById(R.id.relativeLayout3));
      
      //Here I used another switch to decide which button was clicked
      switch (item.getItemId()) {
      
      // The action bar's home (Up) button was clicked
     case android.R.id.home:
    	    //You're on the home screen, so let's just close the app. 
	        finish();
	        return true;
	        
	 //The share button was clicked       
     case R.id.share:
     //Create a share intent	 
     Intent shareIntent = new Intent(Intent.ACTION_SEND);
     shareIntent.setType("text/plain");
     //Get the subject from strings.xml
     shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.shareText);
     //Share the url for Don't Pause
     shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.pierceholdings.dontpause");
     //Start the share chooser
     startActivity(Intent.createChooser(shareIntent, "Share Via"));
     return true;
     
     //The settings button was clicked
 case R.id.settings:
	 //If your Android version is over 3.0 then show SetPreferenceActivity
	 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 Intent intent = new Intent();
     intent.setClass(MainActivity.this, SetPreferenceActivity.class);
     startActivityForResult(intent, 0); 
	 } else { 
	 //If your Android version was below 3.0 then you cannot use these settings, so we tell you with a toast
	 Toast toast2 = new Toast(this);
	     toast2.setView(view2);
	     toast2.show();
     }
	        return true;
 case R.id.settings1:
	 Intent intent = new Intent();
   intent.setClass(MainActivity.this, SetPreferenceActivity.class);
   startActivityForResult(intent, 0); 
	   return true;
 default:
     return super.onOptionsItemSelected(item);
	}
}

@Override
public void onPause() {
  super.onPause();  
  }

@Override
public void onResume() {
  super.onResume(); 
  }

//I use AppRater to ask for your review on google plus. 
public static class AppRater {
  private final static String APP_TITLE = "Don't Pause";
  private final static String APP_PNAME = "com.pierceholdings.dontpause";
  
  //Show AppRater after you have been using the app for 3 days and any amount of launches
  private final static int DAYS_UNTIL_PROMPT = 3;
  private final static int LAUNCHES_UNTIL_PROMPT = 0;
  
  public static void app_launched(Context mContext) {
      SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
      if (prefs.getBoolean("dontshowagain", false)) { return ; }
      
      SharedPreferences.Editor editor = prefs.edit();
      
      // Increment launch counter
      long launch_count = prefs.getLong("launch_count", 0) + 1;
      editor.putLong("launch_count", launch_count);

      // Get date of first launch
      Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
      if (date_firstLaunch == 0) {
          date_firstLaunch = System.currentTimeMillis();
          editor.putLong("date_firstlaunch", date_firstLaunch);
      }
      
      // Wait at least n days before opening
      if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
          if (System.currentTimeMillis() >= date_firstLaunch + 
                  (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
              showRateDialog(mContext, editor);
          }
      }
      editor.commit();
  }   
  
  public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
      final Dialog dialog = new Dialog(mContext);
      dialog.setTitle("Rate " + APP_TITLE);

      LinearLayout ll = new LinearLayout(mContext);
      ll.setOrientation(LinearLayout.VERTICAL);
      
      TextView tv = new TextView(mContext);
      tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
      tv.setWidth(240);
      tv.setPadding(4, 0, 4, 10);
      ll.addView(tv);
      
      Button b1 = new Button(mContext);
      b1.setText("Rate " + APP_TITLE);
      b1.setOnClickListener(new OnClickListener() {
          @Override
		public void onClick(View v) {
          	editor.putBoolean("dontshowagain", true);
              mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
              dialog.dismiss();
          }
      });        
      ll.addView(b1);

      Button b2 = new Button(mContext);
      b2.setText("Remind me later");
      b2.setOnClickListener(new OnClickListener() {
          @Override
		public void onClick(View v) {
              dialog.dismiss();
          }
      });
      ll.addView(b2);

      Button b3 = new Button(mContext);
      b3.setText("No, thanks");
      b3.setOnClickListener(new OnClickListener() {
          @Override
		public void onClick(View v) {
              if (editor != null) {
                  editor.putBoolean("dontshowagain", true);
                  editor.commit();
              }
              dialog.dismiss();
          }
      });
      ll.addView(b3);

      dialog.setContentView(ll);        
      dialog.show();        
  }
}

//Bastion offer redeemer for AppGratis.
@Override
public void onRedeemOffer(Offer offer) {
	// TODO Auto-generated method stub
	for(Feature feature : offer.getFeatures())
    {
        String featureRef = feature.getReference();
        String value = feature.getValue();

    // Provide the feature to the user
	savePreferences(TAG, unlocked);
    
	//display for a long period of time
	Toast.makeText(getApplicationContext(), "Don't Pause Pro Unlocked by AppGratis...", Toast.LENGTH_LONG).show();
    }
}

//Set that boolean just like an in app purchase, and then restart the app in pro version.
private void savePreferences(String key, boolean value) {
	 
	//Set booleans
    SharedPreferences sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(this);
    Editor editor = sharedPreferences.edit();
    editor.putBoolean("unlocked", true);
    editor.putBoolean("ad_pref", true);
    //Commit changes
    editor.commit();
    //Restart app
    Intent i = getBaseContext().getPackageManager()
            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
startActivity(i);   
  }
}