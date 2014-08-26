package com.Phaze1D.RisingFallAndroidV2.Actors;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class SocialMediaButton extends ImageButton {

    public SocialMediaButton(Skin skin) {
        super(skin);
    }

    public SocialMediaButton(Skin skin, String styleName) {
        super(skin, styleName);
    }

    public SocialMediaButton(ImageButtonStyle style) {
        super(style);
    }

    public SocialMediaButton(Drawable imageUp) {
        super(imageUp);
    }

    public SocialMediaButton(Drawable imageUp, Drawable imageDown) {
        super(imageUp, imageDown);
    }

    public SocialMediaButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
        super(imageUp, imageDown, imageChecked);
    }



}
