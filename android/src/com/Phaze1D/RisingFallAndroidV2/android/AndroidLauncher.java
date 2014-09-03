package com.Phaze1D.RisingFallAndroidV2.android;

import android.os.Bundle;

import com.Phaze1D.RisingFallAndroidV2.Controllers.ApplicationController;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

    private ApplicationController appControl;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        appControl = new ApplicationController();
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
