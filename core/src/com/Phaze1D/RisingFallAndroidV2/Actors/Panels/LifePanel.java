package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.CustomLabel;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class LifePanel extends Panel  implements SimpleButton.SimpleButtonDelegate{


    public double timeLeft;

    private SimpleButton buyButton;

    private CustomLabel timeLabel;

    private BitmapFont font;

    public LifePanel(Sprite panelSprite){
        super(panelSprite);



    }

    /** Creates the life panel showing how many lives the player has left*/
    public void createLifePanel(){
        font = BitmapFontSizer.getFontWithSize(19);
        Player playerInfo = Player.shareInstance();

        Label titleLabel = new Label("LifesK", new Label.LabelStyle(font, Color.BLACK));
        titleLabel.setPosition((int) (getWidth() / 2 - titleLabel.getWidth() / 2), (int) (getHeight() * 2 / 3 - titleLabel.getHeight() / 2));
        addActor(titleLabel);

        Label livesLeft = new Label("" + playerInfo.livesLeft, new Label.LabelStyle(font, Color.BLACK));
        livesLeft.setPosition((int) (getWidth() / 2 - livesLeft.getWidth() / 2), (int) (getHeight() / 3 - livesLeft.getHeight() / 2));
        addActor(livesLeft);


    }

    /** Creates time panel showing how much time the player has for the next 5 lives*/
    public void createTimePanel(Sprite buttonSprite){
        font = BitmapFontSizer.getFontWithSize(12);
        Player playerInfo = Player.shareInstance();
        timeLeft = playerInfo.getTimeLeftOnLifes() - System.currentTimeMillis()/1000;

        Label titleLabel = new Label("LifesK", new Label.LabelStyle(font, Color.BLACK));
        titleLabel.setPosition((int) (getWidth() / 2 - titleLabel.getWidth() / 2), (int) (getHeight() - titleLabel.getHeight()));
        addActor(titleLabel);

        SpriteDrawable up = new SpriteDrawable(buttonSprite);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up,null,null,font);
        style.fontColor = Color.BLACK;

        buyButton = new SimpleButton(".99k", style);
        buyButton.setPosition((int) (getWidth() / 2 - buyButton.getWidth() / 2), (int) (buttonSprite.getHeight() / 2 - buyButton.getHeight()));


        buyButton.delegate = this;
        addActor(buyButton);

        int minutes = (int)timeLeft/60;
        int seconds = (int) (timeLeft - minutes*60);
        String formatted = String.format("%02d:%02d",minutes, seconds);
        timeLabel = new CustomLabel(formatted, new Label.LabelStyle(font, Color.BLACK));
        timeLabel.setPosition((int) (getWidth() / 2 - timeLabel.getWidth() / 2), (int) (getHeight() / 2 - timeLabel.getHeight() / 2));
        timeLabel.setAlignment(Align.center);
        addActor(timeLabel);

    }

    /** Runs an action warning the user that he/she has no lives left*/
    public void runActionWarning(){

        ScaleToAction big = Actions.scaleTo(1.5f,1.5f,.7f);
        ScaleToAction small = Actions.scaleTo(1,1, .7f);

        timeLabel.addAction(Actions.sequence(big,small));
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

        Player playerInfo = Player.shareInstance();
        playerInfo.livesLeft = 5;
        playerInfo.timeLeftOnLifes = 0;
        clear();
        createLifePanel();

//        PaymentClass * payment = [[PaymentClass alloc] init];
//        if ([payment payDollar]) {
//            GameData * info = [GameData sharedGameData];
//            info.player.lifesLeft = 5;
//            info.player.timeLeftOnLifes = 0;
//
//            [self clearAll];
//            [self createLifePanel];
//
//        }

    }
}
