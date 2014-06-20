package com.pierceholdings.dontpause;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pierceholdings.dontpauseiap.utils.IabHelper;
/**
 * Implements Google Play in-app billing v3 for the donate feature. Plenty of interesting stuff here.
 * Scroll down for details
 */
/**
 * Implements Google Play in-app billing v3 for the donate feature. Plenty of interesting stuff here.
 * Scroll down for details
 */


public class PromoActivity extends Activity {

	
	
	static final String SKU_SMALL = "unlock";
	String TAG = "Dont Pause";
	
	boolean unlocked = false;
	
	String answer;
	

	private Toast toast = null;

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	// the helper object
	IabHelper mHelper;

	// Button setups
	Button button_small;
	private String AppOfTheDay = "Xda";
	private String appoftheday = "XDA";
	private String Appoftheday = "TPD";
	private String APPOFTHEDAY = "DONTPAUSE";
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.donate);
	
		showDialog(0);
		
			
	}
	@Override
	protected Dialog onCreateDialog(int id) {
	    // We have only one dialog.
		AlertDialog.Builder alert = new AlertDialog.Builder(this); 
    	LinearLayout layout = new LinearLayout(this);
    	TextView tvMessage = new TextView(this); 
    	final EditText FNBox = new EditText(this);
    	tvMessage.setText("Enter a promo code to unlock the Pro features"); 
    	FNBox.setSingleLine();
    	FNBox.setHint("Promo Code");
    	layout.setOrientation(LinearLayout.VERTICAL); 
    	layout.addView(tvMessage); 
    	layout.addView(FNBox);
    	alert.setTitle("Promo"); alert.setView(layout);
    				
    		
    		          alert.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						dialog.dismiss();
    						finish();
    						
    					}
    				  });
    				  alert.setPositiveButton("Submit",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						answer = FNBox.getText().toString().trim();
    			          
    						if (answer.equals(AppOfTheDay)) {
    							savePreferences(TAG, unlocked);
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Valid Promo Code. Don't Pause Pro Unlocked...", Toast.LENGTH_LONG).show();
    						}
    						else if (answer.equals(Appoftheday)) {
    							savePreferences(TAG, unlocked);
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Valid Promo Code. Don't Pause Pro Unlocked...", Toast.LENGTH_LONG).show();
    						}
    						else if (answer.equals(APPOFTHEDAY)) {
    							savePreferences(TAG, unlocked);
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Valid Promo Code. Don't Pause Pro Unlocked...", Toast.LENGTH_LONG).show();
    						}
    						else if (answer.equals(appoftheday)) {
    							savePreferences(TAG, unlocked);
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Valid Promo Code. Don't Pause Pro Unlocked...", Toast.LENGTH_LONG).show();
    						}
    						else {
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Invalid Promo Code.", Toast.LENGTH_LONG).show();
    							dialog.dismiss();
        						finish();
    						}
    			            dialog.dismiss();
    			            finish();    			        
    			           
    					}
    				  });
    			
    			
    				// create alert dialog
    			
     
    				return alert.show();
			
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
	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		
		
	}
}

	
	

