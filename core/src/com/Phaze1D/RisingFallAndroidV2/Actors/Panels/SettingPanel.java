package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import java.io.StringWriter;
import java.util.ArrayList;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SocialMediaButton;
import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Controllers.PaymentFlowCompletionListener;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SoundControllerDelegate;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.Phaze1D.RisingFallAndroidV2.Singletons.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public SoundControllerDelegate soundDelegate;
    
    public Animation introAni;
    
    public Image introImage;


    public SettingPanel(Sprite panelSprite) {
        super(panelSprite);
        strings = LocaleStrings.getOurInstance();
        float fontSize = BitmapFontSizer.sharedInstance().fontButtonL();
        BitmapFontSizer.getFontWithSize((int)fontSize, strings.getValue("Quit") + strings.getValue("Resume") );
    }

    public void createIntroPanel(int levelAt){

        String key;
        String info;

        if (levelAt <= 2 || levelAt == 18 || levelAt == 28 || levelAt == 50 || levelAt == 70) {
            key = "InfoLevel"+levelAt;
            info = strings.getValue(key);
        }else{
           info = String.format(strings.getValue("InfoLevel"+101), targetScore);
        }

        int fontSize = (int) BitmapFontSizer.sharedInstance().fontIntroText();
        TextField.TextFieldStyle style = new TextField.TextFieldStyle(BitmapFontSizer.getFontWithSize(fontSize, info),Color.BLACK,null,null,null);
        textView = new TextArea(info, style);
        textView.setDisabled(true);
        textView.setSize((int)(getWidth()/1.25f), (int)(getHeight()/2 - getHeight()/8f));
        textView.setPosition( (int)(getWidth()/2 - textView.getWidth()/2), (int)(getHeight()/25f));
        textView.setTouchable(Touchable.disabled);
        textView.setMessageText("INFO");

        addActor(textView);

    createInfoTexture(levelAt);

    }

    private void createInfoTexture(int levelAt){
    	
    	if(levelAt <= 2 || levelAt == 18){
    		
    		Array<Sprite> frames = new Array<Sprite>();
    		
    		for(int i = 0; i < infoAtlas.createSprites().size; i++){
    			frames.add(infoAtlas.createSprite("Level"+levelAt+""+i));	
    		}
    		
    		introAni = new Animation(1f, frames);
    		
    		introImage = new Image(new SpriteDrawable(frames.first()));
    		introImage.setCenterPosition((int)(this.getWidth()/2), (int)(this.getHeight() - this.getHeight()/4 - 10));
    		addActor(introImage);
    	}

    	if(levelAt == 50){
    		Sprite image = new Sprite(new Texture(Gdx.files.internal("Intros/Intro50" + TextureLoader.shareTextureLoader().screenSizeAlt + ".png")));
    		introImage = new Image(new SpriteDrawable(image));
    		introImage.setCenterPosition((int)(this.getWidth()/2), (int)(this.getHeight() - this.getHeight()/4 - 10));
    		addActor(introImage);
    	}
    	

    }
    
    public void animationNumber(float number){
    	if(introImage != null && introAni != null){
    		introImage.setDrawable(new SpriteDrawable((Sprite) introAni.getKeyFrame(number, true)));
    	}
    }


    public void createPausePanel(){

        Sprite button1 = buttonAtlas.createSprite("buttonL1");
        Sprite button2 = buttonAtlas.createSprite("buttonL2");

        float yOffset = (getHeight() - button1.getHeight() - button2.getHeight())/3;
        Vector2 quitPosition = new Vector2(getWidth()/2 - button1.getWidth()/2, yOffset);
        Vector2 resumePosition = new Vector2(getWidth()/2 - button1.getWidth()/2, yOffset*2 + button1.getHeight());

        SpriteDrawable up = new SpriteDrawable(button1);
        SpriteDrawable down = new SpriteDrawable(button2);
        float fontSize = BitmapFontSizer.sharedInstance().fontButtonL();
        BitmapFont font = BitmapFontSizer.getFontWithSize((int)fontSize, strings.getValue("Quit") + strings.getValue("Resume") );
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, down, null, font);
        style.fontColor = Color.BLUE;

        SimpleButton quitButton = new SimpleButton(strings.getValue("Quit"), style);
        quitButton.type = SimpleButton.QUIT_BUTTON;
        quitButton.setPosition((int)quitPosition.x, (int)quitPosition.y);
        quitButton.delegate = this;
        quitButton.soundDelegate = soundDelegate;
        addActor(quitButton);

        SimpleButton resumeButton = new SimpleButton(strings.getValue("Resume"), style);
        resumeButton.setPosition((int)resumePosition.x, (int)resumePosition.y);
        resumeButton.type = SimpleButton.RESUME_BUTTON;
        resumeButton.delegate = this;
        resumeButton.soundDelegate = soundDelegate;
        addActor(resumeButton);
        
        BitmapFont font2 = BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontLifePanelLifes(), null);
        
        SpriteDrawable heartS = new SpriteDrawable(gameAtlas.createSprite("heart"));
        Image heart = new Image(heartS);
        heart.setPosition(0, (int)(getHeight() - heart.getHeight()));
        addActor(heart);

        Player playerInfo = Player.shareInstance();
        
        Label livesLeft = new Label("" + playerInfo.livesLeft, new Label.LabelStyle(font2, Color.WHITE));
        livesLeft.setPosition((int)(heart.getCenterX() - livesLeft.getWidth()/2), (int)(heart.getCenterY() - livesLeft.getHeight()/2));
        addActor(livesLeft);


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
        
        float fontSizeB = BitmapFontSizer.sharedInstance().fontButtonL();
        float fontSizeL = BitmapFontSizer.sharedInstance().fontGameWon();
        
        
        BitmapFont font = BitmapFontSizer.getFontWithSize((int)fontSizeB, strings.getValue("Next")+strings.getValue("MainMenu"));
        
        Label title = new Label(strings.getValue("YouWon"), new Label.LabelStyle(BitmapFontSizer.getFontWithSize((int)fontSizeL, strings.getValue("YouWon")), Color.BLACK));
        title.setPosition((int)(getWidth()/2 - title.getWidth()/2), (int)(getHeight() - yOffset - title.getHeight()));
        title.setAlignment(Align.center);
        addActor(title);

        SpriteDrawable up = new SpriteDrawable(button1);
        SpriteDrawable down = new SpriteDrawable(button2);

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, down, null, font);
        style.fontColor = Color.BLUE;

        SimpleButton nextLevelB = new SimpleButton(strings.getValue("Next"), style);
        nextLevelB.setPosition((int)(getWidth()/2 - nextLevelB.getWidth()/2), (int)(yOffset*2 + button1.getHeight()));
        nextLevelB.delegate = this;
        nextLevelB.type = SimpleButton.NEXT_LEVEL_BUTTON;
        nextLevelB.soundDelegate = soundDelegate;
        addActor(nextLevelB);

        SimpleButton mainB = new SimpleButton(strings.getValue("MainMenu"), style);
        mainB.delegate = this;
        mainB.setPosition((int)(getWidth()/2 - button1.getWidth()/2), (int)(yOffset));
        mainB.type = SimpleButton.QUIT_BUTTON;
        mainB.soundDelegate = soundDelegate;
        addActor(mainB);
    }

    public void createGameLost(){
    	
        
        Sprite button1b = buttonAtlas.createSprite("buttonS1B");
        Sprite button2b = buttonAtlas.createSprite("buttonS2B");
        Sprite button1g = buttonAtlas.createSprite("buttonS1G");
        Sprite button2g = buttonAtlas.createSprite("buttonS2G");
        
        float yOffset = (getHeight() - button1b.getHeight()*5)/6;
        float xOffset = (getWidth() - button1b.getWidth() * 2)/3;
        
        float fontSizeB = BitmapFontSizer.sharedInstance().fontButtonL();
        float fontSizeL = BitmapFontSizer.sharedInstance().fontGameWon();
        
        BitmapFont font = BitmapFontSizer.getFontWithSize((int)fontSizeB, strings.getValue("Restart") + strings.getValue("Quit") + strings.getValue("buy"));
        BitmapFont font2 = BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontLifePanelLifes(), null);

        Vector2 payBPosition = new Vector2(xOffset, getHeight() - yOffset*2 - button1b.getHeight()*2);
        Vector2 socialPosition = new Vector2( xOffset*2 + button1b.getWidth(), getHeight() - yOffset*2 - button1b.getHeight()*2);
        Vector2 resetPosition = new Vector2(xOffset, getHeight() - yOffset*5 - button1b.getHeight()*5);
        Vector2 quitPosition = new Vector2(xOffset*2 + button1b.getWidth(),getHeight() - yOffset*5 - button1b.getHeight()*5 );
        
        SpriteDrawable heartS = new SpriteDrawable(gameAtlas.createSprite("heart"));
        Image heart = new Image(heartS);
        heart.setPosition(0, (int)(getHeight() - heart.getHeight()));
        addActor(heart);

        Player playerInfo = Player.shareInstance();
        
        Label livesLeft = new Label("" + playerInfo.livesLeft, new Label.LabelStyle(font2, Color.WHITE));
        livesLeft.setPosition((int)(heart.getCenterX() - livesLeft.getWidth()/2), (int)(heart.getCenterY() - livesLeft.getHeight()/2));
        addActor(livesLeft);


        Label keepPlayLabel = new Label(strings.getValue("Playon"), new Label.LabelStyle(BitmapFontSizer.getFontWithSize((int)fontSizeL, strings.getValue("Playon")), Color.BLACK));
        keepPlayLabel.setPosition((int)(getWidth()/2 - keepPlayLabel.getWidth()/2), (int)(getHeight() -  yOffset - button1b.getHeight()));
        addActor(keepPlayLabel);

        SpriteDrawable upB = new SpriteDrawable(button1b);
        SpriteDrawable downB = new SpriteDrawable(button2b);
        
        SpriteDrawable upG = new SpriteDrawable(button1g);
        SpriteDrawable downG = new SpriteDrawable(button2g);
        
        
        ImageTextButton.ImageTextButtonStyle styleB = new ImageTextButton.ImageTextButtonStyle(upB, downB, null, font);
        styleB.fontColor = Color.BLUE;
        
        ImageTextButton.ImageTextButtonStyle styleG = new ImageTextButton.ImageTextButtonStyle(upG, downG, null, font);
   

        SimpleButton payButton = new SimpleButton(strings.getValue("buy"), styleB);
        payButton.delegate = this;
        payButton.type = SimpleButton.PAY_BUTTON;
        payButton.setPosition((int)payBPosition.x, (int)payBPosition.y);
        payButton.soundDelegate = soundDelegate;
        addActor(payButton);

        SpriteDrawable shareUp = new SpriteDrawable(gameAtlas.createSprite("shareB1"));
        SpriteDrawable shareDown = new SpriteDrawable(gameAtlas.createSprite("shareB2"));
        
        
        socialB = new SocialMediaButton(shareUp, shareDown);
        socialB.setCenterPosition((int)(socialPosition.x + socialB.getWidth()/1.5f), payButton.getCenterY());
        socialB.delegate = this;
        socialB.soundDelegate = soundDelegate;
        socialB.type = SocialMediaButton.SOCIAL_BUTTON;
        addActor(socialB);

        Label endLabel = new Label(strings.getValue("GameOver"), new Label.LabelStyle(BitmapFontSizer.getFontWithSize((int)fontSizeL, strings.getValue("GameOver")), Color.BLACK));
        endLabel.setPosition((int)(getWidth()/2 - endLabel.getWidth()/2), (int)(getHeight() -  yOffset*4 - button1b.getHeight()*4));
        addActor(endLabel);

        SimpleButton resetButton = new SimpleButton(strings.getValue("Restart"), styleG);
        resetButton.setPosition((int)resetPosition.x, (int)resetPosition.y);
        resetButton.type = SimpleButton.RESTART_BUTTON;
        resetButton.delegate = this;
        resetButton.soundDelegate = soundDelegate;
        addActor(resetButton);

        SimpleButton quitButton = new SimpleButton(strings.getValue("Quit"), styleG);
        quitButton.setPosition((int)quitPosition.x, (int)quitPosition.y);
        quitButton.type = SimpleButton.QUIT_BUTTON;
        quitButton.delegate = this;
        quitButton.soundDelegate = soundDelegate;
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
                subSocial.soundDelegate = soundDelegate;
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
        backB.soundDelegate = soundDelegate;
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
