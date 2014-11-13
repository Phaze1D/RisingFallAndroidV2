package com.Phaze1D.RisingFallAndroidV2.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.Phaze1D.RisingFallAndroid.IAB.IabHelper;
import com.Phaze1D.RisingFallAndroid.IAB.IabHelper.OnConsumeMultiFinishedListener;
import com.Phaze1D.RisingFallAndroid.IAB.IabHelper.OnIabPurchaseFinishedListener;
import com.Phaze1D.RisingFallAndroid.IAB.IabHelper.QueryInventoryFinishedListener;
import com.Phaze1D.RisingFallAndroid.IAB.Inventory;
import com.Phaze1D.RisingFallAndroid.IAB.IabHelper.OnIabSetupFinishedListener;
import com.Phaze1D.RisingFallAndroid.IAB.IabResult;
import com.Phaze1D.RisingFallAndroid.IAB.Purchase;
import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Controllers.PaymentFlowCompletionListener;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.android.vending.billing.IInAppBillingService;

public class AndroidPaymentClass implements CorePaymentDelegate {

	public static final int ANDROID_PAYMENT_BUY_RC = 37;
	
	private String TAG = "DAVID VILLARREAL";

	private static AndroidPaymentClass myInstance;
	private Context context;
	private IabHelper mHelper;
	private Player myPlayer;
	private PaymentFlowCompletionListener paymentCompleteListener;

	
	private AndroidPaymentClass(Context context) {
		this.context = context;
	}

	public static AndroidPaymentClass shareInstance(Context context) {
		if (myInstance == null) {
			myInstance = new AndroidPaymentClass(context);
		}
		return myInstance;
	}

	@Override
	public void setPlayer(Player player) {
		if (myPlayer == null) {
			myPlayer = player;
		}
	}
	
	@Override
	public void setPaymentFlowCompletionListener(PaymentFlowCompletionListener lis){
		this.paymentCompleteListener = null;
		this.paymentCompleteListener = lis;
	}

	public void consumeOwnedItems(Inventory inv) {
		ArrayList<Purchase> ownedPurchase = new ArrayList<Purchase>();
		if (inv.hasPurchase(KEEP_PLAYING_ID)) {
			ownedPurchase.add(inv.getPurchase(KEEP_PLAYING_ID));
		}

		if (inv.hasPurchase(MORE_LIFES_ID)) {
			ownedPurchase.add(inv.getPurchase(MORE_LIFES_ID));
		}

		if (inv.hasPurchase(POWER1_ID)) {
			ownedPurchase.add(inv.getPurchase(POWER1_ID));
		}

		if (inv.hasPurchase(POWER2_ID)) {
			ownedPurchase.add(inv.getPurchase(POWER2_ID));
		}

		if (inv.hasPurchase(POWER3_ID)) {
			ownedPurchase.add(inv.getPurchase(POWER3_ID));
		}

		if (inv.hasPurchase(POWER4_ID)) {
			ownedPurchase.add(inv.getPurchase(POWER4_ID));
		}

		if (inv.hasPurchase(POWER5_ID)) {
			ownedPurchase.add(inv.getPurchase(POWER5_ID));
		}

		Log.d(TAG, "OWNED ITEMS " + ownedPurchase.size() + " ITEMS " + ownedPurchase.toString());
		
		if (ownedPurchase.size() > 0) {
			AndroidPaymentClass.this.mHelper.consumeAsync(ownedPurchase,
					new OnConsumeMultiFinishedListener() {

						@Override
						public void onConsumeMultiFinished(
								List<Purchase> purchases,
								List<IabResult> results) {

							for (int i = 0; i < purchases.size(); i++) {

								if (results.get(i).isSuccess()) {
									myPlayer.itemBought(purchases.get(i)
											.getSku());
									
									if(paymentCompleteListener != null){
										paymentCompleteListener.paymentComplete(true);
									}
									
								} else {
									((AndroidLauncher) context)
											.displayInAppError("Error while consuming: " + results.get(i));
									if(paymentCompleteListener != null){
										paymentCompleteListener.paymentComplete(false);
									}
								}
							}

						}
					});
		}

	}

	public void setUpAndroidPayment() {
		mHelper = new IabHelper(
				context,
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjRVCl5f8AbL"
						+ "/owCj/d0p6mLGfmt3CipMQkMG5sl"
						+ "cyVFsQjn1LN3W0JlX8dns0wq9K7Z+J0ibfpoR4"
						+ "7PYMDZlK8fATHSCXla4h88NpZ/4DObXXepA3EmVjSyhfgJ"
						+ "aIVCJC3W8HssnrMkzgVtV8/gH0+iXIWZI/tS6iTaZZ421BgDCrCa"
						+ "b8Uz3orpN7l26BEWuqlkUvQCOaJRzFAjJ7mp7vUL52jlcps5gH5Nfn7"
						+ "riFIJ5ewW2Db0E3v7+TgkNffXrwRB0LFhPHneQRypntAtaKnuPRmpbth"
						+ "BsyRntyvhg6KbEmXyaQG/DdmMNMKGJZh0NbnP/zNGcJr/gvyHfprsibQIDAQAB");
		mHelper.enableDebugLogging(true, "DAVID VILLARREAL");
		mHelper.startSetup(new OnIabSetupFinishedListener() {

			@Override
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					((AndroidLauncher) context).displayInAppError(context.getString(R.string.PaymentFailed));
					
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;

				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				ArrayList<String> myList = new ArrayList<String>();
				myList.add(KEEP_PLAYING_ID);
				myList.add(POWER1_ID);
				myList.add(POWER2_ID);
				myList.add(POWER3_ID);
				myList.add(POWER4_ID);
				myList.add(POWER5_ID);
				myList.add(MORE_LIFES_ID);
				mHelper.queryInventoryAsync(false, null, new MyInventoryListener());
			}
		});

	}

	@Override
	public boolean buyItem(String itemID) {
		
		Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        mHelper.launchPurchaseFlow((AndroidLauncher)context, itemID, ANDROID_PAYMENT_BUY_RC, new MyPurcheseListener(), payload);

		
		return false;
	}
	
	 /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }
    
    public boolean myHandleActivityResult(int requestCode, int resultCode, Intent data){
    	return mHelper.handleActivityResult(requestCode, resultCode, data);
    }

	public void dispose(){
		 Log.d(TAG, "Destroying helper.");
	        if (mHelper != null) {
	            mHelper.dispose();
	            mHelper = null;
	        }
	        
	        paymentCompleteListener = null;
	}
    
	private class MyInventoryListener implements QueryInventoryFinishedListener {

		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inv) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {
				((AndroidLauncher) context).displayInAppError(context.getString(R.string.PaymentFailed));
				return;
			}

			Log.d(TAG, "Query inventory was successful.");
			consumeOwnedItems(inv);
		}

	}

	private class MyPurcheseListener implements OnIabPurchaseFinishedListener{

		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			 Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

	            // if we were disposed of in the meantime, quit.
	            if (mHelper == null) return;

	            if (result.isFailure()) {
	                //complain("Error purchasing: " + result);
	            	((AndroidLauncher) context).displayInAppError(context.getString(R.string.PaymentFailed));
	            	if(paymentCompleteListener != null){
						paymentCompleteListener.paymentComplete(false);
					}
	                return;
	            }
	            if (!verifyDeveloperPayload(purchase)) {
	                //complain("Error purchasing. Authenticity verification failed.");
	            	((AndroidLauncher) context).displayInAppError(context.getString(R.string.PaymentFailed));
	            	if(paymentCompleteListener != null){
						paymentCompleteListener.paymentComplete(false);
					}
	                return;
	            }

	            Log.d(TAG, "Purchase successful.");
	            ArrayList<String> myList = new ArrayList<String>();
				myList.add(KEEP_PLAYING_ID);
				myList.add(POWER1_ID);
				myList.add(POWER2_ID);
				myList.add(POWER3_ID);
				myList.add(POWER4_ID);
				myList.add(POWER5_ID);
				myList.add(MORE_LIFES_ID);
				myList.add("test_product");
				mHelper.queryInventoryAsync(true, myList, new MyInventoryListener());
			
		}
		
	}
}
