package com.Phaze1D.RisingFallAndroidV2.Actors.Buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class SocialMediaButton extends ImageButton {

    public boolean isOpen;

    public SocialMediaButtonDelegate delegate;

    public int type;
    public int indexInSubArray;


    public SocialMediaButton(Drawable imageUp) {
        super(imageUp);
        addListener(new SocialMediaListener());
    }

    public SocialMediaButton(Drawable imageUp, Drawable imageDown) {
        super(imageUp, imageDown);
        addListener(new SocialMediaListener());
    }



    public void setAlpha(float alpha) {
        AlphaAction alphaAction = Actions.alpha(alpha);
        addAction(alphaAction);
    }


    private class SocialMediaListener extends InputListener{
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            setAlpha(.5f);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            setAlpha(1);

            if (type == SOCIAL_BUTTON){
                delegate.socialButtonPressed();
            }else {

            }

        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {

        }
    }

    public interface SocialMediaButtonDelegate{

        public void socialButtonPressed();
        public void subSocialButtonPressed(boolean didShare);
        public void disableChild();
        public void enableChild();
    }

    public static final int SOCIAL_BUTTON = 1;



}
