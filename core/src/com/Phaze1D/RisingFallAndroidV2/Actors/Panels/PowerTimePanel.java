package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class PowerTimePanel extends Panel {

    public double time;
    public double currentTime;
    public double targetTime;

    public int constantTime;
    public int ballsLeft;
    public int powerType;

    public Image titlePower;

    public Label timeLeftLabel;
    public Label ballsLeftLabel;

    public TextureAtlas powerBallAtlas;


    public PowerTimePanel(Sprite panelSprite) {
        super(panelSprite);
    }

    public void createPanelWithTimer(){
        Sprite powerBallSprite = powerBallAtlas.createSprite("powerBall" + powerType);

        titlePower = new Image(powerBallSprite);
        titlePower.setPosition((int)(getWidth()/2 - titlePower.getWidth()/2), (int)(getHeight()/2));
        titlePower.setScale(titlePower.getWidth()/1.5f, titlePower.getHeight()/1.5f);
        addActor(titlePower);


        constantTime = 11;
        currentTime = System.currentTimeMillis()/1000;
        String timeString = String.format("%02d:%02d",0, constantTime);
        timeLeftLabel = new Label(timeString, new Label.LabelStyle(BitmapFontSizer.getFontWithSize(0), Color.BLACK));
        timeLeftLabel.setPosition((int)(getWidth()/2 - timeLeftLabel.getWidth()/2), (int)(getHeight()/7 - timeLeftLabel.getHeight()/2));
        targetTime = constantTime + currentTime;
        addActor(timeLeftLabel);
    }


    public void createPanelWithBalls(){
        Sprite powerBallSprite = powerBallAtlas.createSprite("powerBall" + powerType);
        titlePower = new Image(powerBallSprite);
        titlePower.setPosition((int)(getWidth()/2 - titlePower.getWidth()/2), (int)(getHeight()/2));
        titlePower.setScale(titlePower.getWidth()/1.5f, titlePower.getHeight()/1.5f);
        addActor(titlePower);

        ballsLeft = 10;
        ballsLeftLabel = new Label(ballsLeft + "", new Label.LabelStyle(BitmapFontSizer.getFontWithSize(0), Color.BLACK));
        ballsLeftLabel.setPosition((int)(getWidth()/2 - ballsLeftLabel.getWidth()/2), (int)(getHeight()/7 - ballsLeftLabel.getHeight()/2));
        addActor(ballsLeftLabel);

    }

    public boolean updateBallsLeft(){
        ballsLeft--;

        if (ballsLeft <= 0){
            ballsLeft = 0;
            ballsLeftLabel.setText(ballsLeft+"");
            return true;
        }else {
            ballsLeftLabel.setText(ballsLeft+"");
            return false;
        }

    }

    public boolean updatetimer(){
        time = targetTime - currentTime;
        if (time <= 0){
            time = 0;
            String timeString = String.format("%02d:%02f",0, time);
            if(timeLeftLabel != null) {
                timeLeftLabel.setText(timeString);
            }
            return true;
        }else {
            String timeString = String.format("%02d:%02f",0, time);
            timeLeftLabel.setText(timeString);
            return false;
        }
    }

    public void resetTimer(){
        if (timeLeftLabel != null){
            targetTime = constantTime + currentTime;
            clear();
            createPanelWithTimer();
        }else {
            clear();
            titlePower = null;
            createPanelWithTimer();
        }
        ballsLeftLabel = null;
    }

    public void resetBalls(){
        if (ballsLeftLabel != null){
            ballsLeft = 11;
            clear();
            createPanelWithBalls();
        }else {
            clear();
            titlePower = null;
            createPanelWithBalls();
        }

        timeLeftLabel = null;
    }


}
