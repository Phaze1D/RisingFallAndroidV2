package com.Phaze1D.RisingFallAndroidV2.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by davidvillarreal on 8/28/14.
 * Rising Fall Android Version
 */
public class CustomLabel extends Label {
    public CustomLabel(CharSequence text, Skin skin) {
        super(text, skin);
    }

    public CustomLabel(CharSequence text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public CustomLabel(CharSequence text, Skin skin, String fontName, Color color) {
        super(text, skin, fontName, color);
    }

    public CustomLabel(CharSequence text, Skin skin, String fontName, String colorName) {
        super(text, skin, fontName, colorName);
    }

    public CustomLabel(CharSequence text, LabelStyle style) {
        super(text, style);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        setFontScale(scaleX, scaleY);
        super.setScale(scaleX, scaleY);
    }

    @Override
    public void setScaleY(float scaleY) {
        setFontScaleY(scaleY);
        super.setScaleY(scaleY);
    }

    @Override
    public void setScale(float scaleXY) {
        setFontScale(scaleXY);
        super.setScale(scaleXY);
    }

    @Override
    public void setScaleX(float scaleX) {
        setFontScaleX(scaleX);
        super.setScaleX(scaleX);
    }
}
