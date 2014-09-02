package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
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


        Label levelLabel = new Label("  " + levelID, new Label.LabelStyle(BitmapFontSizer.getFontWithSize(11), Color.BLACK));
        levelLabel.setPosition((int)(getWidth()/2 - levelLabel.getWidth()/2), (int)(getHeight()*3/4f - levelLabel.getHeight()/2));
        levelLabel.setAlignment(Align.center);
        addActor(levelLabel);

        Label levelTitle = new Label("LabelK", new Label.LabelStyle(BitmapFontSizer.getFontWithSize(11), Color.BLACK));
        levelTitle.setPosition((int)(getWidth()/2 - levelTitle.getWidth()/2), (int)(getHeight()*3f/4f + levelLabel.getHeight()/1.5f - levelTitle.getHeight()/2));
        levelTitle.setAlignment(Align.center);
        addActor(levelTitle);

        Label highTitle = new Label("Highk", new Label.LabelStyle(BitmapFontSizer.getFontWithSize(11), Color.BLACK));
        highTitle.setPosition((int)(getWidth()/2 - highTitle.getWidth()/2),(int)( getHeight()/2 - highTitle.getHeight()/2));
        highTitle.setAlignment(Align.center);
        addActor(highTitle);

        Label scoreTitle = new Label("ScoreK", new Label.LabelStyle(BitmapFontSizer.getFontWithSize(11), Color.BLACK));
        scoreTitle.setPosition((int)(getWidth()/2 - scoreTitle.getWidth()/2),(int)( getHeight()/2 - highTitle.getHeight()/1.5f - scoreTitle.getHeight()/2));
        scoreTitle.setAlignment(Align.center);
        addActor(scoreTitle);

        Label highScore = new Label("  " + passedScore, new Label.LabelStyle(BitmapFontSizer.getFontWithSize(11), Color.BLACK));
        highScore.setPosition((int)(getWidth()/2 - highScore.getWidth()/2), (int)(scoreTitle.getCenterY() - scoreTitle.getHeight()/1.5f - highScore.getHeight()/2));
        highScore.setAlignment(Align.center);
        addActor(highScore);
    }
}
