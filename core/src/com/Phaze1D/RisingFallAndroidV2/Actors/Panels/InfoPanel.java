package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class InfoPanel extends Panel {

    public InfoPanel(Sprite panelSprite) {
        super(panelSprite);
    }


    public void createPanel(int levelID, int passedScore){

        LocaleStrings strings = LocaleStrings.getOurInstance();
      

        Label levelLabel = new Label("  " + levelID, new Label.LabelStyle(BitmapFontSizer.getFontWithSize(31), new Color(.969f, .576f, .118f, 1)));
        levelLabel.setPosition((int)(getWidth()/2 - levelLabel.getWidth()/2), (int)(getHeight()/2 - levelLabel.getHeight()/2));
        levelLabel.setAlignment(Align.center);
        addActor(levelLabel);
    }
}
