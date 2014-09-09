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
    	androidDelegate.androidFacebookClicked(this);
    }

    public void contactsClicked(){
    	androidDelegate.androidContactsClicked(this);
    }

    public void googleClicked(){
    	androidDelegate.androidGoogleClicked(this);
    }

    public void twitterClicked(){	
    	androidDelegate.androidTwitterClicked(this);
    }

    public void vkClicked(){
    	androidDelegate.androidVKClicked(this);
    }

    public void weiboClicked(){
    	androidDelegate.androidWeiboClicked(this);
    }

    public interface SocialMediaControlDelegate{
        public void sharedCalledBack(boolean didShare);
        public void disableOther();
        public void enableOther();
    }
    
    /** Delegate that handles all the social media code from the android package*/
    public interface SocialMediaConnectionDelegate{
    	public void androidFacebookClicked(final SocialMediaControl smc);
    	public void androidContactsClicked(final SocialMediaControl smc);
    	public void androidGoogleClicked(final SocialMediaControl smc);
    	public void androidTwitterClicked(final SocialMediaControl smc);
    	public void androidVKClicked(final SocialMediaControl smc);
    	public void androidWeiboClicked(final SocialMediaControl smc);
    }
}
