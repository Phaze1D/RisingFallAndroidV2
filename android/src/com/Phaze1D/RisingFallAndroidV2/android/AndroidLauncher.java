package com.Phaze1D.RisingFallAndroidV2.android;

import android.os.Bundle;

import com.Phaze1D.RisingFallAndroidV2.Controllers.ApplicationController;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.lang.reflect.Field;
import java.util.Hashtable;

public class AndroidLauncher extends AndroidApplication {

    private ApplicationController appControl;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SocialMediaControl smc = SocialMediaControl.sharedInstance();
		smc.setAndroidDelegate(new AndroidSocialMediaControl());
		
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
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
