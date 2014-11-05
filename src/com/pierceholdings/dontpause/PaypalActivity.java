package com.pierceholdings.dontpause;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

/**
 * Basic sample using the SDK to make a payment or consent to future payments.
 * 
 * For sample mobile backend interactions, see
 * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
 */
public class PaypalActivity extends SherlockActivity {
    private static final String TAG = "paypalActivity";
    boolean unlocked = false;
	boolean adsenabled = false;
    /**
     * - Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
     * 
     * - Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    // note that these credentials will differ between live & sandbox environments.
    //TODO: Set his code
    private static final String CONFIG_CLIENT_ID = "Ad-TLxDEeut5lwT1ISLtU-kipei8yc3XDEXeSlOX9RHFj2FVAmEmg9U7OHZC";

    private static final int REQUEST_CODE_PAYMENT = 1;
	

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Don't Pause!")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        DisplayMetrics metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
		getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent1 = new Intent(PaypalActivity.this, PaymentActivity.class);

        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent1, REQUEST_CODE_PAYMENT);
        
    }
//	public void onBuyPressed(View pressed) {
//       // Animation vanish = AnimationUtils.loadAnimation(this,R.drawable.vanish);;
//		/* 
//         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
//         * Change PAYMENT_INTENT_SALE to 
//         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
//         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
//         *     later via calls from your server.
//         * 
//         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
//         */
//
//        /*
//         * See getStuffToBuy(..) for examples of some available payment options.
//         */
//		PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent = new Intent(PaypalActivity.this, PaymentActivity.class);
//
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//
//        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
//	}
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("01.43"), "USD", "Upgrade to Don't Pause Pro",
      //For testing
      // return new PayPalPayment(new BigDecimal("00.01"), "USD", "Upgrade to Don't Pause Pro",
                paymentIntent);
    }
    /*
     * Enable retrieval of shipping addresses from buyer's PayPal account
     */
//    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
//        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
//    }

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.thank_you, Toast.LENGTH_LONG)
                                .show();
                        
       				    savePreferences(TAG, unlocked);

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
  //Save the boolean from the purchase, then restart app in pro mode.
  	 private void savePreferences(String key, boolean value) {
  		         //Save boolean
  		         SharedPreferences sharedPreferences = PreferenceManager
  		                 .getDefaultSharedPreferences(this);
  		         Editor editor = sharedPreferences.edit();
  		         editor.putBoolean("unlocked", true);
  		         editor.putBoolean("ad_pref", true);
  		         editor.commit();
  		         //Restart App in Pro
  		         Intent i = getBaseContext().getPackageManager()
  		                 .getLaunchIntentForPackage( getBaseContext().getPackageName() );
  		    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
  		    startActivity(i);
  		         
  		     }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case (android.R.id.home):
			Intent intentHome=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intentHome);
		default:
            return super.onOptionsItemSelected(item);
	}
  }
}
