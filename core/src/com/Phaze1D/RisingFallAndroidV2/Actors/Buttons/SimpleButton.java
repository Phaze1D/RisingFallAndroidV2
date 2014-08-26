package com.Phaze1D.RisingFallAndroidV2.Actors.Buttons;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class SimpleButton extends ImageTextButton {

    public SimpleButton(String text, Skin skin) {
        super(text, skin);
    }

    public SimpleButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public SimpleButton(String text, ImageTextButtonStyle style) {
        super(text, style);
    }
}
