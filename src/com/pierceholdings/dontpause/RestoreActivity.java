package com.pierceholdings.dontpause;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.pierceholdings.dontpauseiap.utils.IabHelper;
import com.pierceholdings.dontpauseiap.utils.IabResult;
import com.pierceholdings.dontpauseiap.utils.Inventory;
import com.pierceholdings.dontpauseiap.utils.Purchase;
/**
 * Implements Google Play in-app billing v3 for the donate feature. Plenty of interesting stuff here.
 * Scroll down for details
 */
/**
 * Implements Google Play in-app billing v3 for the donate feature. Plenty of interesting stuff here.
 * Scroll down for details
 */


public class RestoreActivity extends Activity {

	
	
	static final String SKU_SMALL = "unlock";
	String TAG = "Dont Pause";
	
	
	boolean unlocked = false;
	boolean adsenabled = false;

	private Toast toast = null;

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	// the helper object
	IabHelper mHelper;

	// Button setups
	Button button_small;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.donate);
	
		
		// In-app purchase stuff
		//Remember to copy your application's specific license key from google play here
		//for security purposes, save it to an xml if it needs to be on github
		String base64EncodedPublicKey = getString(R.string.app_license);
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(false);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					toast(getString(R.string.in_app_bill_error) + result);
					return;
				}
				// makeDonation(1);
				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;

				// IAB is fully set up. Now, let's get an inventory of stuff we own.
				//   --commented out here as we didn't need it for donation purposes.
				 Log.d(TAG, "Setup successful. Querying inventory.");
				 mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}
		// Listener that's called when we finish querying the items and subscriptions we own
	    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
	        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
	            Log.d(TAG, "Query inventory finished.");

	            // Have we been disposed of in the meantime? If so, quit.
	            if (mHelper == null) return;

	            // Is it a failure?
	            if (result.isFailure()) {
	            	  Log.d(TAG, "Query inventory failed.");
	                return;
	            }

	            Log.d(TAG, "Query inventory was successful.");

	            /*
	             * Check for items we own. Notice that for each purchase, we check
	             * the developer payload to see if it's correct! See
	             * verifyDeveloperPayload().
	             */

	            // Do we have the premium upgrade?
	            Purchase premiumPurchase = inventory.getPurchase(SKU_SMALL);
	            unlocked = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
	            Log.d(TAG, "User is " + (unlocked ? "PREMIUM" : "NOT PREMIUM"));
	            if(unlocked = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase))) {
	          
	            savePreferences(TAG, unlocked);
	           
	            Log.d(TAG, "Initial inventory query finished; enabling Pro UI.");
	            } else {
	            	toast("Upgrade was not previously purchased, scroll up to purchase.");
	            finish();
	            }
	        }

	    };
	

	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	//DO NOT SKIP THIS METHOD
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);
		if (mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
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
	 

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/**Follow google guidelines to create your own payload string here, in case it is needed.
		*Remember it is recommended to store the keys on your own server for added protection
		USE as necessary*/

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				toast(getString(R.string.purchase_error) + result);
				// setWaitScreen(false);
				finish();
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				toast(getString(R.string.error_verification));
				// setWaitScreen(false);
				finish();
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU_SMALL))
					 {

				 Log.d(TAG, "Unlock IAP");
				 toast(getString(R.string.thank_you));
				 savePreferences(TAG, unlocked);
			//	mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}

		}
	};


	private void toast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(getApplicationContext(), "",
							Toast.LENGTH_SHORT);
				}
				toast.setText(msg);
				toast.show();
			}
		});
	}

}
