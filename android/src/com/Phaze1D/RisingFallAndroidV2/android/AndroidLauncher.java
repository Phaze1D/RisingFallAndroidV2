package com.Phaze1D.RisingFallAndroidV2.android;

import android.os.Bundle;

import com.Phaze1D.RisingFallAndroidV2.Controllers.ApplicationController;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ApplicationController(), config);
	}
}
