package com.Phaze1D.RisingFallAndroidV2.Actors;


import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class LevelButton extends ImageTextButton {

    public LevelButton(String text, Skin skin) {
        super(text, skin);
    }

    public LevelButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public LevelButton(String text, ImageTextButtonStyle style) {
        super(text, style);
    }

}
