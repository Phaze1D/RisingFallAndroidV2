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
import com.vk.sdk.VKUIHelper;



public class AndroidLauncher extends AndroidApplication {

    public ApplicationController appControl;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		SocialMediaControl smc = SocialMediaControl.sharedInstance();
		smc.setAndroidDelegate(new AndroidSocialMediaControl(this, smc));	
		
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
        super.onDestroy();
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		Log.d("DAVID VILLARREAL", "ON activity result");
	}
    
   
    
    
}
