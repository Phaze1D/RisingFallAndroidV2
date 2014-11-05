package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SocialMediaButton;
import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Controllers.PaymentFlowCompletionListener;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;


/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class SettingPanel extends Panel implements SimpleButton.SimpleButtonDelegate, SocialMediaButton.SocialMediaButtonDelegate, PaymentFlowCompletionListener {

    public SettingPanelDelegate delegate;

    public int targetScore;
    public int objectiveLeft;
    public int gameType;

    public boolean socialCreated;

    public TextureAtlas socialMediaAtlas;
    public TextureAtlas infoAtlas;
    public TextureAtlas buttonAtlas;
    public TextureAtlas gameAtlas;

    public SocialMediaButton socialB;

    public TextArea textView;

    public SocialMediaButton[] socialChildren;

    private SocialMediaButton backB;
    private LocaleStrings strings;
    public CorePaymentDelegate corePaymentDelegate;


    public SettingPanel(Sprite panelSprite) {
        super(panelSprite);
        strings = LocaleStrings.getOurInstance();
    }

    public void createIntroPanel(int levelAt){

        String key;
        String info;

        if (levelAt <= 2 || levelAt == 18 || levelAt == 28 || levelAt == 50 || levelAt == 70) {
            key = "LevelInfo"+levelAt;
            info = strings.getValue(key);
        }else{
            if (gameType == 1) {
                info = strings.getValue("LevelInfo"+101);
            }else{
                info = strings.getValue("LevelInfo"+102);
            }
        }

        float xOffset = (getWidth() - infoAtlas.createSprite("correct").getWidth()*2)/3;
        
        TextField.TextFieldStyle style = new TextField.TextFieldStyle(BitmapFontSizer.getFontWithSize(15), Color.BLACK,null,null,null);
        textView = new TextArea(info, style);
        textView.setDisabled(true);
        textView.setBounds(xOffset,0, xOffset + infoAtlas.createSprite("correct").getWidth()*2, getHeight()/2 - 5);
        
        textView.setTouchable(Touchable.disabled);
        textView.setMessageText("INFO");

        addActor(textView);

    createInfoTexture(levelAt);

    }

    private void createInfoTexture(int levelAt){


//        if (levelAt <= 2 || levelAt == 18 || levelAt == 28 || levelAt == 50 || levelAt == 70) {
//
//        }else{
//            levelAt = 101;
//        }


        float xOffset = (getWidth() - infoAtlas.createSprite("correct").getWidth()*2)/3;

        Image correctI = new Image(infoAtlas.createSprite("correct"));
        correctI.setPosition((int)(xOffset), (int)(getHeight() - 5 - correctI.getHeight()));
        addActor(correctI);

        Image incorrectI = new Image(infoAtlas.createSprite("incorrect"));
        incorrectI.setPosition((int)(xOffset*2 + incorrectI.getWidth()), (int)(getHeight() - 5 - incorrectI.getHeight()));
        addActor(incorrectI);

    }


    public void createPausePanel(){

        Sprite button1 = buttonAtlas.createSprite("buttonL1");
        Sprite button2 = buttonAtlas.createSprite("buttonL2");

        float yOffset = (getHeight() - button1.getHeight() - button2.getHeight())/3;
        Vector2 quitPosition = new Vector2(getWidth()/2 - button1.getWidth()/2, yOffset);
        Vector2 resumePosition = new Vector2(getWidth()/2 - button1.getWidth()/2, yOffset*2 + button1.getHeight());

        SpriteDrawable up = new SpriteDrawable(button1);
        SpriteDrawable down = new SpriteDrawable(button2);
        BitmapFont font = BitmapFontSizer.getFontWithSize(11);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, down, null, font);
        style.fontColor = Color.BLACK;

        SimpleButton quitButton = new SimpleButton(strings.getValue("QuitK"), style);
        quitButton.type = SimpleButton.QUIT_BUTTON;
        quitButton.setPosition((int)quitPosition.x, (int)quitPosition.y);
        quitButton.delegate = this;
        addActor(quitButton);

        SimpleButton resumeButton = new SimpleButton(strings.getValue("ResumeK"), style);
        resumeButton.setPosition((int)resumePosition.x, (int)resumePosition.y);
        resumeButton.type = SimpleButton.RESUME_BUTTON;
        resumeButton.delegate = this;
        addActor(resumeButton);


    }

    public void createGameOverPanel(boolean didWin, CorePaymentDelegate cPD){
        if (didWin){
            createGameWon();
        }else{
        	corePaymentDelegate = cPD;
            createGameLost();
        }
    }

    public void createGameWon(){
    	

        Sprite button1 = buttonAtlas.createSprite("buttonL1");
        Sprite button2 = buttonAtlas.createSprite("buttonL2");

        float yOffset = (getHeight() - button1.getHeight()*3)/4;

        BitmapFont font = BitmapFontSizer.getFontWithSize(11);
        Label title = new Label(strings.getValue("GameWonK"), new Label.LabelStyle(BitmapFontSizer.getFontWithSize(20), Color.BLACK));
        title.setPosition((int)(getWidth()/2 - title.getWidth()/2), (int)(getHeight() - yOffset - title.getHeight()));
        title.setAlignment(Align.center);
        addActor(title);

        SpriteDrawable up = new SpriteDrawable(button1);
        SpriteDrawable down = new SpriteDrawable(button2);

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, down, null, font);
        style.fontColor = Color.BLACK;

        SimpleButton nextLevelB = new SimpleButton(strings.getValue("NextLevelK"), style);
        nextLevelB.setPosition((int)(getWidth()/2 - nextLevelB.getWidth()/2), (int)(yOffset*2 + button1.getHeight()));
        nextLevelB.delegate = this;
        nextLevelB.type = SimpleButton.NEXT_LEVEL_BUTTON;
        addActor(nextLevelB);

        SimpleButton mainB = new SimpleButton(strings.getValue("MainMenuK"), style);
        mainB.delegate = this;
        mainB.setPosition((int)(getWidth()/2 - button1.getWidth()/2), (int)(yOffset));
        mainB.type = SimpleButton.QUIT_BUTTON;
        addActor(mainB);
    }

    public void createGameLost(){
    	
        BitmapFont font = BitmapFontSizer.getFontWithSize(21);
        Sprite button1b = buttonAtlas.createSprite("buttonS1B");
        Sprite button2b = buttonAtlas.createSprite("buttonS2B");
        Sprite button1g = buttonAtlas.createSprite("buttonS1G");
        Sprite button2g = buttonAtlas.createSprite("buttonS2G");
        
        float yOffset = (getHeight() - button1b.getHeight()*5)/6;
        float xOffset = (getWidth() - button1b.getWidth() * 2)/3;

        Vector2 payBPosition = new Vector2(getWidth()/2 - button1b.getWidth()/2, getHeight() - yOffset*2 - button1b.getHeight()*2);
        Vector2 socialPosition = new Vector2( getWidth()/2, getHeight() - yOffset*3 - button1b.getHeight()*3);
        Vector2 resetPosition = new Vector2(xOffset, getHeight() - yOffset*5 - button1b.getHeight()*5);
        Vector2 quitPosition = new Vector2(xOffset*2 + button1b.getWidth(),getHeight() - yOffset*5 - button1b.getHeight()*5 );


        Label keepPlayLabel = new Label(strings.getValue("KeepPlayingK"), new Label.LabelStyle(BitmapFontSizer.getFontWithSize(20), Color.BLACK));
        keepPlayLabel.setPosition((int)(getWidth()/2 - keepPlayLabel.getWidth()/2), (int)(getHeight() -  yOffset - button1b.getHeight()));
        addActor(keepPlayLabel);

        SpriteDrawable upB = new SpriteDrawable(button1b);
        SpriteDrawable downB = new SpriteDrawable(button2b);
        
        SpriteDrawable upG = new SpriteDrawable(button1g);
        SpriteDrawable downG = new SpriteDrawable(button2g);
        
        
        ImageTextButton.ImageTextButtonStyle styleB = new ImageTextButton.ImageTextButtonStyle(upB, downB, null, font);
        styleB.fontColor = Color.BLACK;
        
        ImageTextButton.ImageTextButtonStyle styleG = new ImageTextButton.ImageTextButtonStyle(upG, downG, null, font);
   

        SimpleButton payButton = new SimpleButton(strings.getValue(".99K"), styleB);
        payButton.delegate = this;
        payButton.type = SimpleButton.PAY_BUTTON;
        payButton.setPosition((int)payBPosition.x, (int)payBPosition.y);
        addActor(payButton);

        SpriteDrawable shareUp = new SpriteDrawable(gameAtlas.createSprite("shareB1"));
        SpriteDrawable shareDown = new SpriteDrawable(gameAtlas.createSprite("shareB2"));
        
        
        socialB = new SocialMediaButton(shareUp, shareDown);
        socialB.setCenterPosition((int)socialPosition.x, (int)socialPosition.y);
        socialB.delegate = this;
        socialB.type = SocialMediaButton.SOCIAL_BUTTON;
        addActor(socialB);

        Label endLabel = new Label(strings.getValue("EndK"), new Label.LabelStyle(BitmapFontSizer.getFontWithSize(20), Color.BLACK));
        endLabel.setPosition((int)(getWidth()/2 - endLabel.getWidth()/2), (int)(getHeight() -  yOffset*4 - button1b.getHeight()*4));
        addActor(endLabel);

        SimpleButton resetButton = new SimpleButton(strings.getValue("RestartK"), styleG);
        resetButton.setPosition((int)resetPosition.x, (int)resetPosition.y);
        resetButton.type = SimpleButton.RESTART_BUTTON;
        resetButton.delegate = this;
        addActor(resetButton);

        SimpleButton quitButton = new SimpleButton(strings.getValue("QuitK"), styleG);
        quitButton.setPosition((int)quitPosition.x, (int)quitPosition.y);
        quitButton.type = SimpleButton.QUIT_BUTTON;
        quitButton.delegate = this;
        addActor(quitButton);

    }

    private void createSocialChildren(){

        float duration = .3f;

        Sprite button1 = buttonAtlas.createSprite("backButton");
        socialChildren = new SocialMediaButton[6];

        float xOffset = (getWidth() - socialMediaAtlas.createSprite("facebook").getWidth()*3)/4;
        float yOffset = (getHeight() - socialMediaAtlas.createSprite("facebook").getHeight() * 2 - button1.getHeight())/4;

        Array<Sprite> childrenSprites = socialMediaAtlas.createSprites();
        Array<TextureAtlas.AtlasRegion> regions = socialMediaAtlas.getRegions();

        int count = 0;

        for (int row = 0; row < 2; row++){
            for (int column = 0; column < 3; column++){
                Vector2 point = new Vector2(xOffset + (xOffset + childrenSprites.get(0).getWidth())*column, getHeight() - (yOffset  + (yOffset + childrenSprites.get(0).getHeight()) * row + childrenSprites.get(0).getHeight()));

                final SocialMediaButton subSocial = new SocialMediaButton(new SpriteDrawable(childrenSprites.get(count)));
                subSocial.setPosition((int)(getWidth()/2 - subSocial.getWidth()/2),(int)(getHeight()/2 - subSocial.getHeight()/2) );
                subSocial.setAlpha(0);
                subSocial.delegate = this;
                subSocial.subType = regions.get(count).index;
                subSocial.indexInSubArray = count;
                subSocial.setTouchable(Touchable.disabled);
                socialChildren[count] = subSocial;

                AlphaAction fadeIN = Actions.fadeIn(duration);
                MoveToAction move = Actions.moveTo((int)point.x, (int)point.y, duration);
                ParallelAction group = Actions.parallel(fadeIN, move);
                Action complete = new Action() {
                    @Override
                    public boolean act(float delta) {
                        subSocial.setTouchable(Touchable.enabled);
                        return true;
                    }
                };

                subSocial.addAction(Actions.sequence(group, complete));
                addActor(subSocial);
                count++;

            }
        }

        backB = new SocialMediaButton(new SpriteDrawable(button1), new SpriteDrawable(buttonAtlas.createSprite("backButton2")));
        
        backB.setPosition((int)(getWidth()/2 - backB.getWidth()/2), (int)(yOffset - backB.getHeight()/2));
        backB.delegate = this;
        backB.type = SocialMediaButton.SOCIAL_BUTTON;
        addActor(backB);
    }

    private void removeSocialChildren(){

        Vector2 point = new Vector2((int)(getWidth()/2 - socialChildren[0].getWidth()/2),(int)(getHeight()/2 - socialChildren[0].getHeight()/2));

        for (final SocialMediaButton button: socialChildren){

            AlphaAction fadeIN = Actions.fadeOut(.3f);
            MoveToAction move = Actions.moveTo((int)point.x, (int)point.y, .3f);
            ParallelAction group = Actions.parallel(fadeIN, move);
            Action complete = new Action() {
                @Override
                public boolean act(float delta) {
                    button.remove();
                    button.clear();

                    return true;
                }
            };

            button.addAction(Actions.sequence(group, complete));
        }

        socialChildren = null;
        backB.remove();
        backB.clear();
        backB = null;

    }

    @Override
    public void buttonPressed(int type) {

        if (type == SimpleButton.QUIT_BUTTON){
            delegate.quitButtonPressed();
        }else if (type == SimpleButton.RESUME_BUTTON){
            delegate.resumeButtonPressed();
        }else if(type == SimpleButton.NEXT_LEVEL_BUTTON){
            delegate.startNextLevel();
        }else if (type == SimpleButton.RESTART_BUTTON){
            delegate.restartButtonPressed();
        }else if (type == SimpleButton.PAY_BUTTON){
        	this.setTouchable(Touchable.disabled);
        	corePaymentDelegate.setPaymentFlowCompletionListener(this);
            corePaymentDelegate.buyItem(CorePaymentDelegate.KEEP_PLAYING_ID);
        }
    }


    @Override
    public void socialButtonPressed() {

        if (socialCreated){
            removeSocialChildren();
            createGameLost();
            socialCreated = false;
            socialB.isOpen = false;

        }else{
            clear();
            addActor(panelActor);
            createSocialChildren();
            socialCreated = true;
            socialB.isOpen = true;
        }
    }

    @Override
    public void subSocialButtonPressed(boolean didShare) {
        if (didShare) {
            delegate.continuePlaying();
        }else{
            //Display error message
        }

    }

    @Override
    public void disableChild() {

    }

    @Override
    public void enableChild() {

    }
    
    

    @Override
	public void paymentComplete(boolean didPay) {
    	this.setTouchable(Touchable.enabled);
		if(didPay){
			delegate.continuePlaying();
		}
		
	}

	public interface SettingPanelDelegate{

        public void quitButtonPressed();
        public void resumeButtonPressed();
        public void startNextLevel();
        public void restartButtonPressed();
        public void continuePlaying();

    }
}
