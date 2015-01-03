package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.CustomLabel;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;


/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class PowerSidePanel extends Panel {

    public Player player;

    public TextureAtlas powerBallAtlas;

    public Ball[] powerBalls;



    public PowerSidePanel(Sprite panelSprite) {
        super(panelSprite);
    }

    public void createPanel(final Sprite notificationSprite){

        Player player = Player.shareInstance();

        Array<Sprite> powerSprites = powerBallAtlas.createSprites();
        powerBalls = new Ball[powerSprites.size];
    
        
        float xOffset = (getWidth() - powerSprites.get(0).getWidth() * 5)/6;
        float yOffset = (getHeight() - powerSprites.get(0).getHeight())/2;
             
        for (int i = 0; i < powerSprites.size; i++){
            Ball ball = new Ball(powerSprites.get(i));
            ball.setPosition((int)(xOffset + (powerSprites.get(0).getWidth() + xOffset)*i  ),  (int)yOffset);
            ball.powerType = i+1;
            ball.isPowerBall = true;
            addActor(ball);
            powerBalls[i] = ball;
        }


       // float xposition2 = yOffset - [[SizeManager sharedSizeManager] getPowerPanelBallSize].width/4 + [[SizeManager sharedSizeManager] getPowerPanelBallSize].height/2;
        float xOffset2 = yOffset - powerSprites.get(0).getWidth()/4 + powerSprites.get(0).getHeight()/2 ;

        for(int i = 0; i < powerSprites.size; i++){
            final int amount = player.getPowerAmount(i+1);
            if (amount > 0){
                final Group notiGroup = new Group();
                final Image noti = new Image(notificationSprite);
                noti.setPosition((int)(powerBalls[i].getX() + powerBalls[i].getWidth() - noti.getHeight()/2),(int)(xOffset2 + notificationSprite.getWidth()/2));
                notiGroup.setSize(noti.getWidth(), noti.getHeight());
                notiGroup.addActor(noti);


                ScaleToAction start = Actions.scaleTo(0,0);
                notiGroup.addAction(start);

                ScaleToAction up = Actions.scaleTo(1,1,.6f);
                Action complete = new Action() {
                    @Override
                    public boolean act(float delta) {

                        CustomLabel label = new CustomLabel(amount+"", new Label.LabelStyle(BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontPowerNoti(), null), Color.BLACK));
                        label.setAlignment(Align.center);
                        label.setPosition((int)(noti.getX() + notificationSprite.getWidth()/2 - label.getWidth()/2), (int)(noti.getY() + notificationSprite.getHeight()/2 - label.getHeight()/2));
                        notiGroup.addActor(label);

                        return true;
                    }
                };

                notiGroup.addAction(Actions.sequence(up, complete));
                powerBalls[i].notiNode = notiGroup;
                addActor(notiGroup);
            }else {
                powerBalls[i].setAlpha(.3f);
                powerBalls[i].setTouchable(Touchable.disabled);
            }
        }
    }

}
