package com.Phaze1D.RisingFallAndroidV2.Actors.Buttons;

import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class SocialMediaButton extends ImageButton implements SocialMediaControl.SocialMediaControlDelegate{

    public boolean isOpen;
    public boolean didShare;

    public SocialMediaButtonDelegate delegate;

    public int type;
    public int indexInSubArray;
    public int subType;


    public SocialMediaButton(Drawable imageUp) {
        super(imageUp);
        addListener(new SocialMediaListener(this));
    }

    public SocialMediaButton(Drawable imageUp, Drawable imageDown) {
        super(imageUp, imageDown);
        addListener(new SocialMediaListener(this));
    }



    public void setAlpha(float alpha) {
        AlphaAction alphaAction = Actions.alpha(alpha);
        addAction(alphaAction);
    }


    private class SocialMediaListener extends InputListener{
        private SocialMediaButton socialMediaButton;

        public SocialMediaListener(SocialMediaButton button){
            this.socialMediaButton = button;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            setAlpha(.5f);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            setAlpha(1);
            Player player = Player.shareInstance();
            double playerTime = player.getTimeLeftOnSocialMedia();
            double currentTime = System.currentTimeMillis() / 1000;

            if (currentTime < playerTime && !isOpen && type == SOCIAL_BUTTON) {
                socialMediaButton.displayTimeLeft();
            } else {
                if (type == SOCIAL_BUTTON) {
                    delegate.socialButtonPressed();
                } else {

                    SocialMediaControl control = new SocialMediaControl();
                    control.delegate = this.socialMediaButton;
                    didShare = false;

                    if (subType == FACEBOOK){
                        control.facebookClicked();
                    }else if (subType == CONTACTS){
                        control.contactsClicked();
                    }else if (subType == GOOGLE){
                        control.googleClicked();
                    }else if (subType == WEIBO){
                        control.weiboClicked();
                    }else if (subType == TWITTER){
                        control.twitterClicked();
                    }else if (subType == VK){
                        control.vkClicked();
                    }

                    if (didShare){
                        player.calculateNextShareTime();
                    }
                }
            }
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {

        }
    }

    public void displayTimeLeft(){

    }


    @Override
    public void sharedCalledBack(boolean didShare) {

    }

    @Override
    public void disableOther() {

    }

    @Override
    public void enableOther() {

    }

    public interface SocialMediaButtonDelegate{

        public void socialButtonPressed();
        public void subSocialButtonPressed(boolean didShare);
        public void disableChild();
        public void enableChild();
    }

    public static final int SOCIAL_BUTTON = 1;
    public static final int FACEBOOK = 1;
    public static final int CONTACTS = 0;
    public static final int WEIBO = 5;
    public static final int GOOGLE = 2;
    public static final int TWITTER = 4;
    public static final int VK = 3;



}
