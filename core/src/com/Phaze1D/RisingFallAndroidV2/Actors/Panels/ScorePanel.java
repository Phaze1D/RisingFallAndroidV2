package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.CustomLabel;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SoundControllerDelegate;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    
    public CustomLabel titleLabel;

    public SoundControllerDelegate soundDelegate;

    public ScorePanel(Sprite panelSprite) {
        super(panelSprite);
        
    }

    public void createScorePanel(int targetScore){
    	
//    	 int fontsize = 16;
//    	    _currentScore = 0;
//    	    _targetScore = targetScore;
//    	    
//    	    NSMutableString * stringL = [[NSMutableString alloc]init];
//    	    
//    	    [stringL appendString:NSLocalizedString(@"Score:", nil)];
//    	    [stringL appendFormat:@" %d/%d", _currentScore, _targetScore];
//    	    
//    	    _titleLabel = [SKLabelNode labelNodeWithFontNamed:@"CooperBlack"];
//    	    _titleLabel.fontColor = [UIColor whiteColor];
//    	    _titleLabel.fontSize = fontsize;
//    	    _titleLabel.verticalAlignmentMode = SKLabelVerticalAlignmentModeCenter;
//    	    _titleLabel.horizontalAlignmentMode = SKLabelHorizontalAlignmentModeCenter;
//    	    _titleLabel.zPosition = 2;
//    	    _titleLabel.text = stringL;
//    	    _titleLabel.position = CGPointMake(-_titleLabel.frame.size.width/1.5, -self.size.height/2);
//    	    [self addChild: _titleLabel];

    	
        currentScore = 0;
        this.targetScore = targetScore;
        String titleString = LocaleStrings.getOurInstance().getValue("Score") + " " + currentScore + "/" + targetScore;

        int fontSize = (int)BitmapFontSizer.sharedInstance().fontScorePanel();
        
        titleLabel = new CustomLabel(titleString, new Label.LabelStyle(BitmapFontSizer.getFontWithSize(fontSize), Color.YELLOW));
        titleLabel.setPosition((int)(titleLabel.getWidth()/5), (int) (getHeight() / 2 - titleLabel.getHeight() / 2) );
        addActor(titleLabel);


    }

    public void updateScore(int addScore){
        currentScore += addScore;
        titleLabel.setText(LocaleStrings.getOurInstance().getValue("Score") + " " + currentScore + "/" + targetScore);

        if (currentScore >= targetScore && !reachYet){
            titleLabel.setVisible(false);
           soundDelegate.playHighReachSound();
            final CustomLabel reachL = new CustomLabel(targetScore + "", new Label.LabelStyle(BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontScorePanel()), Color.YELLOW));
            reachL.setPosition((int)(getWidth()/2 - reachL.getWidth()/2), (int)(getHeight()/2 - reachL.getHeight()/2) );
            reachL.setAlignment(Align.center);
            AlphaAction alphaAction = Actions.alpha(0, 1.5f);
            ScaleToAction scaleToAction = Actions.scaleTo(1.5f,1.5f,1.5f);
            ParallelAction group = Actions.parallel(alphaAction, scaleToAction);
            Action complete = new Action() {
                @Override
                public boolean act(float delta) {
                    titleLabel.setVisible(true);
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
        titleLabel.addAction(Actions.forever(seq));

    }

    @Override
    public boolean remove(){
    	
    	return super.remove();
    }

}
