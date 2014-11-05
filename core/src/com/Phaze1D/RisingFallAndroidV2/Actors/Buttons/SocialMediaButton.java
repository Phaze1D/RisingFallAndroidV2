package com.Phaze1D.RisingFallAndroidV2.Actors.Buttons;

import com.Phaze1D.RisingFallAndroidV2.Controllers.SocialMediaControl;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

            System.out.println((currentTime < playerTime) + "  " + !isOpen + "  " +  (type == SOCIAL_BUTTON));
            if (currentTime < playerTime && !isOpen && type == SOCIAL_BUTTON) {

                socialMediaButton.displayTimeLeft();
            } else {
                if (type == SOCIAL_BUTTON) {
                    delegate.socialButtonPressed();
                } else {

                    SocialMediaControl control = SocialMediaControl.sharedInstance();
                    control.delegate = this.socialMediaButton;
                    didShare = false;

                    if (subType == FACEBOOK){
                    	System.out.println("Facebook pressed");
                        control.facebookClicked();
                    }else if (subType == CONTACTS){
                    	System.out.println("Contacts pressed");
                        control.contactsClicked();
                    }else if (subType == GOOGLE){
                    	System.out.println("Google pressed");
                        control.googleClicked();
                    }else if (subType == WEIBO){
                    	System.out.println("Weibo pressed");
                        control.weiboClicked();
                    }else if (subType == TWITTER){
                    	System.out.println("Twitter pressed");
                        control.twitterClicked();
                    }else if (subType == VK){
                    	System.out.println("Vk pressed");
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


        final double time = System.currentTimeMillis()/1000;

        Player player = Player.shareInstance();
        int timeLeft= (int)(player.getTimeLeftOnSocialMedia() - time);
        int seconds = timeLeft % 60;
        int minutes = (timeLeft /60) %60;
        int hours = timeLeft/3600;

        Label.LabelStyle style = new Label.LabelStyle(BitmapFontSizer.getFontWithSize(13), Color.BLACK);

        final Label title = new Label(LocaleStrings.getOurInstance().getValue("TimeLeftK"), style);
        title.setPosition((int)(getWidth()/2 - title.getWidth()/2), (int)getHeight() );
        title.addAction(Actions.alpha(0));

        final Label timeL = new Label(String.format("%02d:%02d:%02d",hours,minutes, seconds), style);
        timeL.setPosition((int)(getWidth()/2 - timeL.getWidth()/2), (int) -timeL.getHeight());
        timeL.addAction(Actions.alpha(0));

        AlphaAction fadeIn = Actions.alpha(1, 1);
        AlphaAction fadeOut = Actions.alpha(0,1);
        Action complete = new Action() {
            @Override
            public boolean act(float delta) {
                timeL.remove();
                title.remove();

                return true;
            }
        };

        title.addAction(Actions.sequence(fadeIn,fadeOut,complete));

        AlphaAction fadeIn1 = Actions.alpha(1, 1);
        AlphaAction fadeOut1 = Actions.alpha(0,1);
        Action complete1 = new Action() {
            @Override
            public boolean act(float delta) {
                timeL.remove();
                title.remove();

                return true;
            }
        };

        timeL.addAction(Actions.sequence(fadeIn1,fadeOut1,complete1));

        addActor(timeL);
        addActor(title);

    }


    @Override
    public void sharedCalledBack(boolean didShare) {
    	if(didShare){
    		Player player = Player.shareInstance();
    		player.calculateNextShareTime();
    		delegate.subSocialButtonPressed(didShare);
    	}
    	
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
