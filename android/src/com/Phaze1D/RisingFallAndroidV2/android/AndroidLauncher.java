package com.Phaze1D.RisingFallAndroidV2.android;

import java.lang.reflect.Field;
import java.util.Hashtable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.Phaze1D.RisingFallAndroidV2.Controllers.ApplicationController;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;



public class AndroidLauncher extends AndroidApplication {
	
	public static final int RC_SIGN_IN = 28883;
	public static final int RC_SHARE = 23973;

    public ApplicationController appControl;
    public AndroidSocialMediaControl androidSocialControl; 

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SocialMediaControl smc = SocialMediaControl.sharedInstance();
		androidSocialControl = new AndroidSocialMediaControl(this, smc);
		smc.setAndroidDelegate(androidSocialControl);	
		
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        Field[] fields = R.string.class.getFields();
        for(Field field: fields){
            int id = getResources().getIdentifier(field.getName(), "string", getPackageName());
            hashtable.put(field.getName(), getResources().getString(id));
        }

        appControl = new ApplicationController(hashtable);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(appControl, config);
		
		
		
		
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
        super.onPause();
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        finish();
        VKUIHelper.onDestroy(this);
        if (androidSocialControl.googleClient.isConnected()) {
	        Plus.AccountApi.clearDefaultAccount(androidSocialControl.googleClient);
	        androidSocialControl.googleClient.disconnect();
	        
	    }
        
        if(Session.getActiveSession() != null){
        	Session.getActiveSession().closeAndClearTokenInformation();
        }
        if(VKSdk.instance() != null && VKSdk.isLoggedIn()){
        	VKSdk.logout();
        }
        androidSocialControl.logoutTwitter();
        super.onDestroy();
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("DAVID VILLARREAL", "ON activity result");

		if (requestCode == RC_SIGN_IN) {
			Log.d("DAVID VILLARREAL", "testinga resutl code" + resultCode);
	        if (resultCode != ConnectionResult.SUCCESS) {
	           androidSocialControl.smcEnable();
	           Log.d("DAVID VILLARREAL", "testinga resutl code");
	        }
	        
	        androidSocialControl.googleClient.connect();
	    }
		
		if(requestCode == RC_SHARE){
			 Log.d("DAVID", resultCode + " ---- ");
		        if (resultCode == -1) {
					androidSocialControl.googleDidShare();
				}else {
					androidSocialControl.googleDidNotShare();
				}
		}
	
		
		if (requestCode == VKSdk.VK_SDK_REQUEST_CODE) {
			VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
        }else if(requestCode != RC_SIGN_IN && requestCode != RC_SHARE){
        	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }
		
	}
    
   
    
    
}
