package com.Phaze1D.RisingFallAndroidV2.Controllers;


/**
 * Created by davidvillarreal on 9/5/14.
 * Rising Fall Android Version
 */
public class SocialMediaControl {

	private static SocialMediaControl ourInstance;
	
    public SocialMediaControlDelegate delegate;
    
    private SocialMediaConnectionDelegate androidDelegate;
    
    private SocialMediaControl(){
    	
    }
    
    public void setAndroidDelegate(SocialMediaConnectionDelegate androidDelegate){
    	if(this.androidDelegate == null){
    		this.androidDelegate = androidDelegate;
    	}
    }
    
    public static SocialMediaControl sharedInstance(){
    	if(ourInstance == null){
    		ourInstance = new SocialMediaControl();
    	}
    	return ourInstance;
    }


    public void facebookClicked(){
    	androidDelegate.androidFacebookClicked();
    }

    public void contactsClicked(){
    	androidDelegate.androidContactsClicked();
    }

    public void googleClicked(){
    	androidDelegate.androidGoogleClicked();
    }

    public void twitterClicked(){	
    	androidDelegate.androidTwitterClicked();
    }

    public void vkClicked(){
    	androidDelegate.androidVKClicked();
    }

    public void weiboClicked(){
    	androidDelegate.androidWeiboClicked();
    }

    public interface SocialMediaControlDelegate{
        public void sharedCalledBack(boolean didShare);
        public void disableOther();
        public void enableOther();
    }
    
    /** Delegate that handles all the social media code from the android package*/
    public interface SocialMediaConnectionDelegate{
    	public void androidFacebookClicked();
    	public void androidContactsClicked();
    	public void androidGoogleClicked();
    	public void androidTwitterClicked();
    	public void androidVKClicked( );
    	public void androidWeiboClicked(  );
    }
}
