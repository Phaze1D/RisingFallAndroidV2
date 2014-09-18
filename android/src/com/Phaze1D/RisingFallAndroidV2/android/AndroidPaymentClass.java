package com.Phaze1D.RisingFallAndroidV2.android;

import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;



public class AndroidPaymentClass implements CorePaymentDelegate{

	private static AndroidPaymentClass instance;
	
	private AndroidPaymentClass(){
		
	}
	
	public static synchronized AndroidPaymentClass shareInstance(){
		if(instance == null){
			instance = new AndroidPaymentClass();
		}
		return instance;
	}

	
}
