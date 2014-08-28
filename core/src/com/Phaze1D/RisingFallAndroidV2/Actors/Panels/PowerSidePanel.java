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

    public void createPanel(Sprite notificationSprite){

        Player player = Player.shareInstance();

        Array<Sprite> powerSprites = powerBallAtlas.createSprites();

        float yOffset = (getHeight() - powerSprites.get(0).getHeight()*5)/6f;
        float xOffset = getWidth()*.3f;

        for (int i = 0; i < powerSprites.size; i++){
            Ball ball = new Ball(powerSprites.get(i));
            ball.setPosition((int)xOffset,  (int)(yOffset + (ball.getHeight() + yOffset)*i));
            ball.powerType = i+1;
            ball.isPowerBall = true;
            addActor(ball);
            powerBalls[i] = ball;
        }


        float xOffset2 = xOffset - powerSprites.get(0).getHeight()/2;

        for(int i = 0; i < powerSprites.size; i++){
            final int amount = player.getPowerAmount(i+1);
            if (amount > 0){
                final Group notiGroup = new Group();
                final Image noti = new Image(notificationSprite);
                noti.setPosition((int)(xOffset2 + notificationSprite.getWidth()/2), (int)(powerBalls[i].getY() + powerBalls[i].getHeight() - noti.getHeight()/2));
                notiGroup.addActor(noti);

                ScaleToAction start = Actions.scaleTo(0,0);
                notiGroup.addAction(start);

                ScaleToAction up = Actions.scaleTo(1,1,.6f);
                Action complete = new Action() {
                    @Override
                    public boolean act(float delta) {
                        ScaleToAction small = Actions.scaleTo(.6f,.6f, .8f);
                        ScaleToAction big = Actions.scaleTo(1f,1f,.8f);
                        SequenceAction seq = Actions.sequence(small,big);

                        CustomLabel label = new CustomLabel(amount+"", new Label.LabelStyle(BitmapFontSizer.getFontWithSize(0), Color.BLACK));
                        label.setAlignment(Align.center);
                        label.setPosition((int)(noti.getWidth()/2 - label.getWidth()/2), (int)(noti.getHeight()/2 - label.getHeight()/2));
                        notiGroup.addActor(label);
                        notiGroup.addAction(Actions.forever(seq));

                        return true;
                    }
                };

                powerBalls[i].notiNode = notiGroup;
                addActor(notiGroup);
            }else {
                powerBalls[i].setAlpha(.3f);
                powerBalls[i].setTouchable(Touchable.disabled);
            }
        }
    }

}
