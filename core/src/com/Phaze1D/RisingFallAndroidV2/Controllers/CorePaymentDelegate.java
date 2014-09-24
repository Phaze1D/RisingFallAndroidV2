package com.Phaze1D.RisingFallAndroidV2.Controllers;

import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;


public interface CorePaymentDelegate{
	
	public static final String POWER1_ID = "power_type_1";
	public static final String POWER2_ID = "power_type_2";
	public static final String POWER3_ID = "power_type_3";
	public static final String POWER4_ID = "power_type_4";
	public static final String POWER5_ID = "power_type_5";
	public static final String KEEP_PLAYING_ID = "keep_playing" ;
	public static final String MORE_LIFES_ID = "lifes_5";
	
	public void setPlayer(Player player);
	public boolean buyItem(String itemID);
	public void setPaymentFlowCompletionListener(PaymentFlowCompletionListener lis);
	
}

