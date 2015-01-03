package com.Phaze1D.RisingFallAndroidV2.android;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.Phaze1D.RisingFallAndroidV2.Controllers.ApplicationController;
import com.Phaze1D.RisingFallAndroidV2.Controllers.GameController.AdDelegate;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl;
import com.Phaze1D.RisingFallAndroidV2.Scenes.LevelsScene;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.Session;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.plus.Plus;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;

public class AndroidLauncher extends AndroidApplication implements AdDelegate {

	public static final int GOOGLE_RC_SIGN_IN = 28883;
	public static final int GOOGLE_RC_SHARE = 23973;
	public static final int EMAIL_RC = 231;

	public ApplicationController appControl;
	public AndroidSocialMediaControl androidSocialControl;

	private AdView adView;
	private AdRequest adRequest;
	
	private int currentSceneID = 0;
	
	private AndroidPaymentClass payDelegate;
	private AndroidSoundHandler soundDelegate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("DAVID VILLARREAL", Locale.getDefault().getLanguage());
		
		payDelegate = AndroidPaymentClass.shareInstance(this);
		payDelegate.setUpAndroidPayment();
		
		soundDelegate = new AndroidSoundHandler();
		soundDelegate.loadSounds(this);

		
		RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


		adView = new AdView(this);
		adView.setAdUnitId("ca-app-pub-2067437180761728/9895905098");
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdListener(new MyAdListener());

		adRequest = new AdRequest.Builder().addTestDevice("B72D993C5DCCA63ACBB020D4991E1503").build();
		adView.loadAd(adRequest);
		adView.setVisibility(View.INVISIBLE);
		
		SocialMediaControl smc = SocialMediaControl.sharedInstance();
		androidSocialControl = new AndroidSocialMediaControl(this, smc);
		smc.setAndroidDelegate(androidSocialControl);

		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		Field[] fields = R.string.class.getFields();
		for (Field field : fields) {
			int id = getResources().getIdentifier(field.getName(), "string",
					getPackageName());
			hashtable.put(field.getName(), getResources().getString(id));
		}

		
		
		appControl = new ApplicationController(hashtable, payDelegate, soundDelegate);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(appControl, config);
		appControl.setAdDelegate(this);
		
		layout.addView(gameView);
		

		
		RelativeLayout.LayoutParams adParams = 
	            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT);
	        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

	        layout.addView(adView, adParams);

	        
	        setContentView(layout);
	}
	

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();
		VKUIHelper.onResume(this);
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	protected void onDestroy() {
		payDelegate.dispose();
		payDelegate = null;
		soundDelegate.disposeSounds();
		soundDelegate = null;
		
		if (adView != null) {
			adView.destroy();
		}

		VKUIHelper.onDestroy(this);
		if (androidSocialControl.googleClient != null
				&& androidSocialControl.googleClient.isConnected()) {

			Plus.AccountApi
					.clearDefaultAccount(androidSocialControl.googleClient);
			androidSocialControl.googleClient.disconnect();

		}

		if (Session.getActiveSession() != null) {
			Session.getActiveSession().closeAndClearTokenInformation();
		}
		if (VKSdk.instance() != null && VKSdk.isLoggedIn()) {
			VKSdk.logout();
		}
		androidSocialControl.logoutTwitter();
		finish();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("DAVID VILLARREAL", "ON activity result");

		if (requestCode == GOOGLE_RC_SIGN_IN) {
			if (resultCode == Activity.RESULT_CANCELED) {
				Log.d("DAVID VILLARREAL", "sign in canceled");
				androidSocialControl.googleDidNotShare();
			} else if (resultCode == Activity.RESULT_OK) {
				Log.d("DAVID VILLARREAL", "sign in ok");
				androidSocialControl.newGoogleConnect();
				androidSocialControl.googleConnect.execute(GOOGLE_RC_SHARE);

			}
		}

		if (requestCode == GOOGLE_RC_SHARE) {
			if (resultCode == Activity.RESULT_CANCELED) {
				Log.d("DAVID VILLARREAL", "shared canceled");
				androidSocialControl.googleDidNotShare();
			} else if (resultCode == Activity.RESULT_OK) {
				Log.d("DAVID VILLARREAL", "shared ok");
				androidSocialControl.googleDidShare();
			}
		}

		if (requestCode == EMAIL_RC) {
			androidSocialControl.smcEnable();
			androidSocialControl.postSuccessUpdate();
		}
		
		if(requestCode == AndroidPaymentClass.ANDROID_PAYMENT_BUY_RC){
			payDelegate.myHandleActivityResult(requestCode, resultCode, data);
		}

		if (requestCode == VKSdk.VK_SDK_REQUEST_CODE) {
			VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
		} else if (requestCode != GOOGLE_RC_SIGN_IN
				&& requestCode != GOOGLE_RC_SHARE && requestCode != EMAIL_RC && requestCode != AndroidPaymentClass.ANDROID_PAYMENT_BUY_RC) {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
		}

	}

	@Override
	public void hideAd() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				adView.setVisibility(View.INVISIBLE);
				
			}
		});
		
	}

	@Override
	public void showAd() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//adView.loadAd(adRequest);
		        adView.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	

	@Override
	public void currentScene(int sceneID) {
		currentSceneID = sceneID;
		
	}
	
	public void displayInAppError(String message){
		complain(message);
	}


	void complain(String message) {
        Log.e("DAVID VILLARREAL", message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d("DAVID VILLARREAL", "Showing alert dialog: " + message);
        bld.create().show();
    }


	private class MyAdListener extends AdListener {

		@Override
		public void onAdClosed() {
			super.onAdClosed();
		}

		@Override
		public void onAdFailedToLoad(int errorCode) {
			super.onAdFailedToLoad(errorCode);
			AndroidLauncher.this.hideAd();
		}

		@Override
		public void onAdLeftApplication() {
			super.onAdLeftApplication();
		}

		@Override
		public void onAdLoaded() {
			super.onAdLoaded();
			if(currentSceneID == 1 || currentSceneID == 2){
				AndroidLauncher.this.showAd();
			}
		}

		@Override
		public void onAdOpened() {
			super.onAdOpened();
		}

	}

}
