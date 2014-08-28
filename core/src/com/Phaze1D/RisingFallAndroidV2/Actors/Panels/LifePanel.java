package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class LifePanel extends Panel  implements SimpleButton.SimpleButtonDelegate{


    public double timeLeft;

    private SimpleButton buyButton;

    private Label timeLabel;

    private BitmapFont font;

    public LifePanel(Sprite panelSprite){
        super(panelSprite);
        font = BitmapFontSizer.getFontWithSize(0);


    }

    /** Creates the life panel showing how many lives the player has left*/
    public void createLifePanel(){
        Player playerInfo = Player.shareInstance();

        Label titleLabel = new Label("LifesK", new Label.LabelStyle(font, Color.BLACK));
        titleLabel.setPosition(getWidth()/2 - titleLabel.getWidth()/2, getHeight()*2f/3f - titleLabel.getHeight()/2);
        addActor(titleLabel);

        Label livesLeft = new Label("" + playerInfo.livesLeft, new Label.LabelStyle(font, Color.BLACK));
        livesLeft.setPosition(getWidth()/2 - livesLeft.getWidth()/2, getHeight()/3f - livesLeft.getHeight()/2);
        addActor(livesLeft);


    }

    /** Creates time panel showing how much time the player has for the next 5 lives*/
    public void createTimePanel(Sprite buttonSprite){
        Player playerInfo = Player.shareInstance();
        playerInfo.calculateNextLifeTime();
        timeLeft = playerInfo.getTimeLeftOnLifes() - System.currentTimeMillis()/1000;

        Label titleLabel = new Label("LifesK", new Label.LabelStyle(font, Color.BLACK));
        titleLabel.setPosition(getWidth()/2 - titleLabel.getWidth()/2, getHeight() - titleLabel.getHeight()/2);
        addActor(titleLabel);

        SpriteDrawable up = new SpriteDrawable(buttonSprite);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up,null,null,font);
        style.fontColor = Color.BLACK;

        buyButton = new SimpleButton(".99k", style);
        buyButton.setPosition(getWidth()/2 - buyButton.getWidth()/2, buttonSprite.getHeight()/2f);
        buyButton.delegate = this;
        addActor(buyButton);

        int minutes = (int)timeLeft/60;
        int seconds = (int) (timeLeft - minutes*60);
        String formatted = String.format("%02d:%02d",minutes, seconds);
        timeLabel = new Label(formatted, new Label.LabelStyle(font, Color.BLACK));
        timeLabel.setPosition(getWidth()/2 - timeLabel.getWidth()/2, getHeight()/2 - timeLabel.getHeight()/2);
       // addActor(timeLabel);

    }

    /** Runs an action warning the user that he/she has no lives left*/
    public void runActionWarning(){

    }

    /** Updates the time left in the time Panel*/
    public void updateTime(){
        Player playerInfo = Player.shareInstance();

        timeLeft = playerInfo.getTimeLeftOnLifes() - System.currentTimeMillis()/1000;
        if (timeLeft > 0){
            int minutes = (int)timeLeft/60;
            int seconds =(int) (timeLeft - minutes*60);
            String formatted = String.format("%02d:%02d",minutes, seconds);
            timeLabel.setText(formatted);
        }else {
            playerInfo.livesLeft = 5;
            clear();
            addActor(panelActor);
            createLifePanel();
        }

    }

    /** Dispose the resources use by the panel*/
    public void dispose(){
        clear();
    }

    @Override
    public void buttonPressed(int type) {

    }
}
