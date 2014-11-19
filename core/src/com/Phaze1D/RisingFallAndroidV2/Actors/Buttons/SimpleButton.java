package com.Phaze1D.RisingFallAndroidV2.Actors.Buttons;

import com.Phaze1D.RisingFallAndroidV2.Controllers.SoundControllerDelegate;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class SimpleButton extends ImageTextButton {

    public SimpleButtonDelegate delegate;

    public int type;

    private SimpleButtonListener listener;
    
    public SoundControllerDelegate soundDelegate;

    public SimpleButton(String text, Skin skin) {
        super(text, skin);
        listener = new SimpleButtonListener();
        addListener(listener);
       
    }

    public SimpleButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        listener = new SimpleButtonListener();
        addListener(listener);
       
    }

    public SimpleButton(String text, ImageTextButtonStyle style) {
        super(text, style);
        listener = new SimpleButtonListener();
        addListener(listener);
        
    }

    public void setAlpha(float alpha){
        AlphaAction alphaAction = Actions.alpha(alpha);
        addAction(alphaAction);
    }

    private class SimpleButtonListener extends ClickListener{

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            setAlpha(.5f);
            soundDelegate.playButtonSound();
            return super.touchDown(event, x, y, pointer, button);

        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            setAlpha(1);
            delegate.buttonPressed(type);
            super.touchUp(event, x, y, pointer, button);

        }
    }
    
    @Override
    public boolean remove(){
    	
    	return super.remove();
    }

    public interface SimpleButtonDelegate{
        public void buttonPressed(int type);
    }

    public static final int PLAY_BUTTON = 0;
    public static final int STORE_BUTTON = 1;
    public static final int LEVEL_BACK_BUTTON = 2;
    public static final int QUIT_BUTTON = 3;
    public static final int RESUME_BUTTON = 4;
    public static final int NEXT_LEVEL_BUTTON = 5;
    public static final int PAY_BUTTON = 7;
    public static final int RESTART_BUTTON = 8;

}
