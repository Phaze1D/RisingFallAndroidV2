package com.Phaze1D.RisingFallAndroidV2.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.android.vending.billing.IInAppBillingService;

public class AndroidPaymentClass implements CorePaymentDelegate {

	public static final int BILLING_OK = 0;

	private static AndroidPaymentClass instance;

	public IInAppBillingService mService;
	public ServiceConnection mServiceConn;

	private Context context;

	private boolean hasInited;
	private boolean billingAvalible;
	
	private Player myPlayer;

	private AndroidPaymentClass(Context context) {
		this.context = context;
	}

	public static synchronized AndroidPaymentClass shareInstance(Context context) {
		if (instance == null) {
			instance = new AndroidPaymentClass(context);
			instance.initServiceVariables();
		}
		return instance;
	}

	public void bindPaymentService() {
		Intent serviceIntent = new Intent(
				"com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		context.bindService(serviceIntent, mServiceConn,
				Context.BIND_AUTO_CREATE);
	}

	public void unBindPaymentService() {
		context.unbindService(mServiceConn);
	}

	public void initServiceVariables() {
		if (!hasInited) {
			mServiceConn = new ServiceConnection() {

				@Override
				public void onServiceDisconnected(ComponentName name) {

				}

				@Override
				public void onServiceConnected(ComponentName name,
						IBinder service) {
					mService = IInAppBillingService.Stub.asInterface(service);
					try {
						int respone = mService.isBillingSupported(3,
								context.getPackageName(), "inapp");
						if (respone == BILLING_OK) {
							billingAvalible = true;
							initItems();
						} else {
							billingAvalible = false;
							((AndroidLauncher) context).displayInAppError();
						}

					} catch (RemoteException e) {

						e.printStackTrace();
					}

				}
			};

			hasInited = true;
		}

	}

	public boolean isBillingAvalible() {
		return billingAvalible;
	}

	private void initItems() {

	}

	public void consumeItems() {

	}

	@Override
	public void setPlayer(Player player) {
		if(myPlayer == null){
			myPlayer = player;
		}
		
	}

	
}
