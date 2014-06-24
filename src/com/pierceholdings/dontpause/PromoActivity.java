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

public class PromoActivity extends Activity {

	//This activity is for a previous promo code unlock I offered. It is currently unused, but I left it in the app so I can easily reactivate it in the future.
	
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
	
	//Unlock Codes
	private String answer1 = "Xda";
	private String answer2 = "XDA";
	private String answer3 = "TPD";
	private String answer4 = "DONTPAUSE";
	
	
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
	    // Create promo code dialog box
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
    				
    		          //Exit button to close box
    		          alert.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						dialog.dismiss();
    						finish();
    						
    					}
    				  });
    		          //Positive button submits answer and checks for activation.
    				  alert.setPositiveButton("Submit",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						//Get text from editText box, and trim any excess space off the end.
    						answer = FNBox.getText().toString().trim();
    			          
    						//Check if the entered text is equal to answer 1
    						if (answer.equals(answer1)) {
    							//Save boolean and restart app
    							savePreferences(TAG, unlocked);
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Valid Promo Code. Don't Pause Pro Unlocked...", Toast.LENGTH_LONG).show();
    						}
    						//Check if the entered text is equal to answer 2
    						else if (answer.equals(answer2)) {
    							//Save boolean and restart app
    							savePreferences(TAG, unlocked);
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Valid Promo Code. Don't Pause Pro Unlocked...", Toast.LENGTH_LONG).show();
    						}
    						//Check if the entered text is equal to answer 3
    						else if (answer.equals(answer3)) {
    							//Save boolean and restart app
    							savePreferences(TAG, unlocked);
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Valid Promo Code. Don't Pause Pro Unlocked...", Toast.LENGTH_LONG).show();
    						}
    						//Check if the entered text is equal to answer 4
    						else if (answer.equals(answer4)) {
    							//Save boolean and restart app
    							savePreferences(TAG, unlocked);
    							//display in long period of time
    							Toast.makeText(getApplicationContext(), "Valid Promo Code. Don't Pause Pro Unlocked...", Toast.LENGTH_LONG).show();
    						}
    						else {
    							//The user entered an invalid code. Show a toast to tell them.
    							Toast.makeText(getApplicationContext(), "Invalid Promo Code. Unlock failed.", Toast.LENGTH_LONG).show();
    							dialog.dismiss();
        						finish();
    						}
    			            dialog.dismiss();
    			            finish();    			        
    					}
    				  });
    				return alert.show();
	}

	
	//Save and restart the app
	 private void savePreferences(String key, boolean value) {
		 //Save booleans to sharedPreferences
         SharedPreferences sharedPreferences = PreferenceManager
                 .getDefaultSharedPreferences(this);
         Editor editor = sharedPreferences.edit();
         editor.putBoolean("unlocked", true);
         editor.putBoolean("ad_pref", true);
         editor.commit();
         //Restart app
         Intent i = getBaseContext().getPackageManager()
                 .getLaunchIntentForPackage( getBaseContext().getPackageName() );
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(i);
         
     }
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}