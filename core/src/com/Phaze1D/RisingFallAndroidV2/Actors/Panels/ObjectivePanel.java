package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class ObjectivePanel extends Panel {

    public float time;
    public float futureTime;

    public int ballsLeft;
    public int gameType;
    public int fontSize;

    public Label titleNode;
    public Label objectiveNode;

    public BitmapFont font;


    public ObjectivePanel(Sprite panelSprite) {
        super(panelSprite);
    }

    public void createPanel(){

        String titleString;
        if (gameType == 2){
            titleString = "BallsLeft";
        }else {
            titleString = "TimeLeft";
        }

        font = BitmapFontSizer.getFontWithSize(0);
        titleNode = new Label(titleString, new Label.LabelStyle(font, Color.BLACK));
        titleNode.setPosition((int)(getWidth() /2 - titleNode.getWidth()/2),(int)(getHeight() * 4/7 - titleNode.getHeight()/4));
        addActor(titleNode);

        String objectString;

        if (gameType == 2){
            objectString = "" + ballsLeft;
        }else{
            int minutes = (int)time/(60);
            int seconds =(int) (time - minutes*60);
            String formatted = String.format("%02d:%02d",minutes, seconds);
            objectString = formatted;
        }

        objectiveNode = new Label(objectString, new Label.LabelStyle(font, Color.BLACK));
        objectiveNode.setPosition((int)(getWidth()/2 - objectiveNode.getWidth()/2), (int)(getHeight()/7f + objectiveNode.getHeight()/4 - objectiveNode.getHeight()/4));
        addActor(objectiveNode);

    }


    public boolean updateObjective(){
        if (gameType == 2){
            objectiveNode.setText("" + ballsLeft);
        }else{

            int minutes = (int)time/(60);
            int seconds =(int) (time - minutes*60);
            String formatted = String.format("%02d:%02d",minutes, seconds);
            objectiveNode.setText(formatted);
            float date = System.currentTimeMillis()/1000;
            time = futureTime - date;

        }

        if (gameType == 2 && ballsLeft <= 0){
            ballsLeft = 0;
            objectiveNode.setText("" + ballsLeft);
            return true;
        }else if (gameType == 1 && time < 0){
            time = 0;
            int minutes = (int)time/(60);
            int seconds =(int) (time - minutes*60);
            String formatted = String.format("%02d:%02d",minutes, seconds);
            objectiveNode.setText(formatted);
            return true;
        }
        return false;
    }
}
