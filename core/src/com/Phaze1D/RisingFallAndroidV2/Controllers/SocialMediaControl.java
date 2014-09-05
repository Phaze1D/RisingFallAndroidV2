package com.Phaze1D.RisingFallAndroidV2.Controllers;

/**
 * Created by davidvillarreal on 9/5/14.
 * Rising Fall Android Version
 */
public class SocialMediaControl {

    public SocialMediaControlDelegate delegate;


    public void facebookClicked(){

    }

    public void contactsClicked(){

    }

    public void googleClicked(){

    }

    public void twitterClicked(){

    }

    public void vkClicked(){

    }

    public void weiboClicked(){

    }

    public interface SocialMediaControlDelegate{

        public void sharedCalledBack(boolean didShare);
        public void disableOther();
        public void enableOther();

    }
}
