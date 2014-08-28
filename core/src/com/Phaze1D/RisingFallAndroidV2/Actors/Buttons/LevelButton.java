package com.Phaze1D.RisingFallAndroidV2.Actors.Buttons;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class LevelButton extends ImageTextButton {

    public LevelButtonDelegate delegate;

    public int parentNumber;
    public int levelNumber;

    public boolean isChild;


    public LevelButton(String text, Skin skin) {
        super(text, skin);
    }

    public LevelButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public LevelButton(String text, ImageTextButtonStyle style) {
        super(text, style);
        addListener(new LevelButtonListener());
    }

    public void setAlpha(float alpha){
        AlphaAction alphaAction = Actions.alpha(alpha);
        addAction(alphaAction);
    }

    private class LevelButtonListener extends InputListener{
        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {

        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            setAlpha(1);
            if (!isChild){
                delegate.parentPressed(parentNumber);
            }else {
                delegate.childPressed(levelNumber);
            }
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            setAlpha(.5f);
            return true;
        }
    }

    public interface LevelButtonDelegate{

        public void parentPressed(int parentNumber);
        public void childPressed(int levelNumber);

    }
}
