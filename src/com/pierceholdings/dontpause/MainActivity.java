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

public class MainActivity extends SherlockActivity  implements OnClickListener, BastionOfferListener {
	  private static final String TAG = "MyService";
	  ImageButton buttonStart, buttonStop;
	  boolean vibenabled;
	  boolean adsenabled;
//	  private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsy3vjCpmsJwN37FR2lM5QxWWIrMgcvEUcUzCQOJq2W6jtsRC5njDX68kVDltm0EKeKzVw9HhhsNnfDccBvtz20o2fDOQq/uR7+0JOF0/pVTG3twG5AGZpKn++/ZE6/8JOVmcU2qkoUvL65QnezDmoe2Fr8k4YfRUQACqxg7IJtVoNKnesbo68jTvjL9F0mkJUj4+NNxBFb4lY3kQPqMi3XSe/j/p/IJ5uru/wbckdlGokqmv3bU46+9n/26MC5h4mqo+VueTBVM2qzhzSVy+5d/p5frByA7Ka8I180W1bWmGX8FXVK7J9veTYNMCW/Oz1GRP2Ji0rxrf96bJaaBE/QIDAQAB";
//	  private static final byte[] SALT = new byte[] {90, 03, 86, 82, 16, 27, 87, 57, 78, 29, 98, 29, 86, 55, 79, 95, 87, 55, 06, 87};
	  NotificationManager mNotificationManager;
//	  private Handler mHandler;
//	  private LicenseChecker mChecker;
//	  private LicenseCheckerCallback mLicenseCheckerCallback;
	  boolean licensed;
	  boolean checkingLicense;
	  boolean didCheck;
	  boolean unlocked = false;
	  
	  private InterstitialAd interstitial;
	  
	 
	 

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean unlocked = mySharedPreferences.getBoolean("unlocked", false);
        if (unlocked == true){
            setContentView(R.layout.activity_main);
        } else{
            setContentView(R.layout.activity_mainlocked);
        }
        
        SharedPreferences mySharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        boolean adsenabled = mySharedPreferences1.getBoolean("ad_pref", false);
  	    
        Bastion.onStart(
                this,  /* Give the current activity */
                this); /* Pass this as second parameter since  we're implementing BastionOfferListener */
        
        
  	 // Create the interstitial.
  	    interstitial = new InterstitialAd(this);
  	    interstitial.setAdUnitId("ca-app-pub-5038706716632727/8458526186");

  	    // Create ad request.
  	    AdRequest adRequest = new AdRequest.Builder().build();

  	    // Begin loading your interstitial.
  	    interstitial.loadAd(adRequest);

        
//	    setContentView(R.layout.activity_main);
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	    
		String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        Log.i("Device Id", deviceId); 
        
		
	    ImageView rocketImage = (ImageView) findViewById(R.id.img);
	    rocketImage.setBackgroundResource(R.drawable.dont_pause_icon);
	    
	   
	    buttonStart = (ImageButton) findViewById(R.id.buttonStart);
	    buttonStop = (ImageButton) findViewById(R.id.buttonStop);
        
	    buttonStart.setOnClickListener(this);
	    buttonStop.setOnClickListener(this);
	    
        boolean vibenabled = mySharedPreferences1.getBoolean("vib_preference", false);
        boolean headenabled = mySharedPreferences1.getBoolean("headenabled", false);
        
//        mHandler = new Handler();
//        mLicenseCheckerCallback = new MyLicenseCheckerCallback();
//        mChecker = new LicenseChecker(this, new ServerManagedPolicy(this, new   AESObfuscator(SALT, getPackageName(), deviceId)), BASE64_PUBLIC_KEY);
//        
//        doCheck();
        
        if (headenabled) {
        	
	    	  startService(new Intent(this, HeadsetObserverService.class));
        } else {
        	stopService(new Intent(this, HeadsetObserverService.class));
	  }
	  }
	  

	  @Override
	public void onClick(View src) {
		  
		  LayoutInflater inflater = getLayoutInflater();
	        View view = inflater.inflate(R.layout.cust_toast_layout,
	                                       (ViewGroup) findViewById(R.id.relativeLayout1));
	        
	        LayoutInflater inflater1 = getLayoutInflater();
	        View view1 = inflater1.inflate(R.layout.cust_toast_layout2,
	                                       (ViewGroup) findViewById(R.id.relativeLayout2));
	        
		  SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	         boolean vibenabled = mySharedPreferences.getBoolean("vib_preference", false);
	         SharedPreferences mySharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
	         boolean adsenabled = mySharedPreferences1.getBoolean("ad_pref", false);
	         
	         ImageView rocketImage = (ImageView) findViewById(R.id.img);
	    switch (src.getId()) {
	    case R.id.buttonStart:
	      Log.d(TAG, "onClick: starting service");
	   // use this to start and trigger a service
	      rocketImage.setImageResource(R.drawable.dont_pause_icon_selected);
	      if (vibenabled) {
	    	  startService(new Intent(this, MyService2.class));
	    	  
	      } else {
	    	  startService(new Intent(this, MyService.class));
	      }
	      Toast toast = new Toast(this);
	        toast.setView(view);
	        toast.show();
	      AppRater.app_launched(this);
			Log.d(TAG, "onStart");
			AdRequest adRequest = new AdRequest.Builder().build();
			interstitial.loadAd(adRequest);
				
			
	      break;
	    case R.id.buttonStop:
	      Log.d(TAG, "onClick: stopping service");
	      rocketImage.setImageResource(R.drawable.dont_pause_icon);
	      if (vibenabled) {
	    	  stopService(new Intent(this, MyService2.class));
	      } else {
	    	  stopService(new Intent(this, MyService.class));
	      }
	      Toast toast1 = new Toast(this);
	        toast1.setView(view1);
	        toast1.show();
			Log.d(TAG, "onDestroy");
			Bastion.onStop(this);
if (adsenabled) {
} else {
				if (interstitial.isLoaded()) {
				      interstitial.show();
			    }
			}
	      break;
	    }
	  }
	  
		         
	  
	  @Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			// Inflate the menu; this adds items to the action bar if it is present.
			getSupportMenuInflater().inflate(R.menu.main_activity_actions, menu);
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
				getSupportMenuInflater().inflate(R.menu.mainmenu, menu);
				}
			
			return true;
		}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	LayoutInflater inflater2 = getLayoutInflater();
    View view2 = inflater2.inflate(R.layout.cust_toast_layout3,
                                   (ViewGroup) findViewById(R.id.relativeLayout3));
  switch (item.getItemId()) {
  // Respond to the action bar's Up/Home button
  case android.R.id.home:
	        finish();
	        return true;
 case R.id.share:
     Intent shareIntent = new Intent(Intent.ACTION_SEND);
     shareIntent.setType("text/plain");
     shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.shareText);
     shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.pierceholdings.dontpause");
     startActivity(Intent.createChooser(shareIntent, "Share Via"));
     return true;
 case R.id.settings:
	 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	   Intent intent = new Intent();
     intent.setClass(MainActivity.this, SetPreferenceActivity.class);
     startActivityForResult(intent, 0); 
	 } else { 
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
  super.onPause();  // Always call the superclass method first
 
 
      
  }

@Override
public void onResume() {
	
  super.onResume();  // Always call the superclass method first

  //do something
      
  }

//private void doCheck() {
//
//    didCheck = false;
//    checkingLicense = true;
//    setProgressBarIndeterminateVisibility(true);
//
//    mChecker.checkAccess(mLicenseCheckerCallback);
//}
//
//
//private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
//
//    @Override
//    public void allow(int reason) {
//        // TODO Auto-generated method stub
//        if (isFinishing()) {
//            // Don't update UI if Activity is finishing.
//            return;
//        }               
//        Log.i("License","Accepted!");       
//
//            //You can do other things here, like saving the licensed status to a
//            //SharedPreference so the app only has to check the license once.
//
//        licensed = true;
//        checkingLicense = false;
//        didCheck = true;
//
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public void dontAllow(int reason) {
//        // TODO Auto-generated method stub
//         if (isFinishing()) {
//                // Don't update UI if Activity is finishing.
//                return;
//            }
//            Log.i("License","Denied!");
//            Log.i("License","Reason for denial: "+reason);                                                                              
//
//                    //You can do other things here, like saving the licensed status to a
//                    //SharedPreference so the app only has to check the license once.
//
//            licensed = false;
//            checkingLicense = false;
//            didCheck = true;               
//
//            showDialog(0);
//
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public void applicationError(int reason) {
//        // TODO Auto-generated method stub
//        Log.i("License", "Error: " + reason);
//        if (isFinishing()) {
//            // Don't update UI if Activity is finishing.
//            return;
//        }
//        licensed = true;
//        checkingLicense = false;
//        didCheck = false;
//
//        showDialog(0);
//    }
//
//
//}
//
//@Override
//protected Dialog onCreateDialog(int id) {
//    // We have only one dialog.
//    return new AlertDialog.Builder(this)
//            .setTitle("Don't Pause")
//            .setMessage("This version is not licensed, please download it from the play store to ensure compatibility with your device")
//            .setPositiveButton("Download", new DialogInterface.OnClickListener() {
//                @Override
//				public void onClick(DialogInterface dialog, int which) {
//                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
//                            "http://market.android.com/details?id=" + getPackageName()));
//                    startActivity(marketIntent);
//                    finish();
//                }
//            })
//            .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                @Override
//				public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            })
//            .setNeutralButton("Re-Check", new DialogInterface.OnClickListener() {
//                @Override
//				public void onClick(DialogInterface dialog, int which) {
//                    doCheck();
//                }
//            })
//
//            .setCancelable(false)
//            .setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//				public boolean onKey(DialogInterface dialogInterface,
//                        int i, KeyEvent keyEvent) {
//                    Log.i("License", "Key Listener");
//                    finish();
//                    return true;
//                }
//            })
//            .create();
//
//}

public static class AppRater {
  private final static String APP_TITLE = "Don't Pause";
  private final static String APP_PNAME = "com.pierceholdings.dontpause";
  
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
private void savePreferences(String key, boolean value) {
	 
    SharedPreferences sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(this);
    Editor editor = sharedPreferences.edit();
    editor.putBoolean("unlocked", true);
    editor.putBoolean("ad_pref", true);
    editor.commit();
    Intent i = getBaseContext().getPackageManager()
            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
startActivity(i);
    
}
}




	
			
		
                       
           