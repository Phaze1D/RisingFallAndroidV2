package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.CustomLabel;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class ScorePanel extends Panel {

    public int currentScore;
    public int targetScore;

    public boolean reachYet;

    public CustomLabel scoreLabel;
    public CustomLabel titleLabel;


    public ScorePanel(Sprite panelSprite) {
        super(panelSprite);
    }

    public void createScorePanel(int targetScore){
        currentScore = 0;
        this.targetScore = targetScore;

        titleLabel = new CustomLabel("scorek", new Label.LabelStyle(BitmapFontSizer.getFontWithSize(0), Color.BLACK));
        titleLabel.setPosition((int)(getWidth()/2 - titleLabel.getWidth()/2), (int)(getHeight()*4/7) );
        addActor(titleLabel);

        scoreLabel = new CustomLabel(currentScore + "/" + targetScore, new Label.LabelStyle(BitmapFontSizer.getFontWithSize(0), Color.BLACK));
        scoreLabel.setPosition((int)(getWidth()/2 - scoreLabel.getWidth()/2),(int)(getHeight()/7));
        scoreLabel.setAlignment(Align.center);
        addActor(scoreLabel);

    }

    public void updateScore(int addScore){
        currentScore += addScore;
        scoreLabel.setText(currentScore + "/" + targetScore);

        if (currentScore >= targetScore && !reachYet){
            titleLabel.setVisible(false);
            scoreLabel.setVisible(false);

            final CustomLabel reachL = new CustomLabel(targetScore + "", new Label.LabelStyle(BitmapFontSizer.getFontWithSize(0), Color.BLACK));
            reachL.setPosition((int)(getWidth()/2 - reachL.getWidth()/2), (int)(getHeight()/2 - reachL.getHeight()/2) );
            reachL.setAlignment(Align.center);
            AlphaAction alphaAction = Actions.alpha(0, 1.5f);
            ScaleToAction scaleToAction = Actions.scaleTo(1.5f,1.5f,1.5f);
            ParallelAction group = Actions.parallel(alphaAction, scaleToAction);
            Action complete = new Action() {
                @Override
                public boolean act(float delta) {
                    titleLabel.setVisible(true);
                    scoreLabel.setVisible(true);
                    reachL.remove();
                    reachL.clear();
                    return true;
                }
            };
            reachL.addAction(Actions.sequence(group, complete));
            addActor(reachL);
            reachYet = true;
        }

    }

    public boolean didReachScore(){
        if (currentScore >= targetScore){
            return true;
        }else {
            return false;
        }
    }

    public void didNotReachAnimation(){

        ScaleToAction up = Actions.scaleTo(1.5f, 1.5f, 1f);
        ScaleToAction down = Actions.scaleTo(1,1,1f);
        SequenceAction seq = Actions.sequence(up, down);
        scoreLabel.addAction(Actions.forever(seq));

    }


}
