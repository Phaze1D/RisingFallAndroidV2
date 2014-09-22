package com.Phaze1D.RisingFallAndroidV2.android;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private boolean areItemsCreated;
	private boolean billingAvalible;
	
	private Player myPlayer;
	
	private ArrayList<String> skuResponeList;

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
					mService = null;
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
							onStartConsume();
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
		return billingAvalible && areItemsCreated;
	}

	private void initItems() {
		new SKUDetailsGet().execute("null");
	}

	public void onStartConsume() {
		
		Thread consume = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Bundle ownedItems = mService.getPurchases(3, context.getPackageName(), "inapp", null);
					
					int response = ownedItems.getInt("RESPONSE_CODE");
					if (response == 0) {
					   ArrayList<String> ownedSkus =
					      ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
					   ArrayList<String>  purchaseDataList =
					      ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
					   ArrayList<String>  signatureList =
					      ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
					   String continuationToken = 
					      ownedItems.getString("INAPP_CONTINUATION_TOKEN");
					   
					   for (int i = 0; i < purchaseDataList.size(); ++i) {
						   
					      String purchaseData = purchaseDataList.get(i);
					      String signature = signatureList.get(i);
					      String sku = ownedSkus.get(i);
					      JSONObject jobject = new JSONObject(purchaseData);
					      mService.consumePurchase(3, context.getPackageName(), jobject.getString("purchaseToken"));
					      
					   } 
					   
					   
					}

				} catch (RemoteException e) {
					e.printStackTrace();
					
				} catch (JSONException e) {
					e.printStackTrace();
					
				}
				
			}
		});
		
		consume.start();
		
	}

	@Override
	public void setPlayer(Player player) {
		if(myPlayer == null){
			myPlayer = player;
		}
		
	}
	
	
	

	private class SKUDetailsGet extends AsyncTask<String, String, String>{

		Bundle skuDetails;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			ArrayList<String> skuList = new ArrayList<String>();
			skuList.add("power_type_1");
			skuList.add("power_type_2");
			skuList.add("power_type_3");
			skuList.add("power_type_4");
			skuList.add("power_type_5");
			skuList.add("keep_playing");
			skuList.add("lifes_5");
		
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			
			try {
				skuDetails = mService.getSkuDetails(3,context.getPackageName(), "inapp", querySkus);
				
			} catch (RemoteException e) {
				
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			int respone = skuDetails.getInt("RESPONSE_CODE");
			
			if(respone == BILLING_OK){
			     AndroidPaymentClass.this.skuResponeList = skuDetails.getStringArrayList("DETAILS_LIST");
			     AndroidPaymentClass.this.areItemsCreated = true;
			}else{
				AndroidPaymentClass.this.areItemsCreated = false;
			}
			
			
		}
		
	}




	@Override
	public void onClickBuy(int itemID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getItemPrice(int itemID) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
