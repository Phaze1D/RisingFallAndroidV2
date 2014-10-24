package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.LevelButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.LifePanel;
import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class LevelsScene extends Stage implements Screen, SimpleButton.SimpleButtonDelegate, LevelButton.LevelButtonDelegate {

    public LevelSceneDelegate delegate;

    public TextureAtlas sceneAtlas;
    public TextureAtlas buttonAtlas;

    private Player playerInfo;

    private boolean isCreated;
    private boolean isSubCreated;
    private boolean hasCreatedScene;

    private SimpleButton navigationB;

    private Vector2 naviPosition;

    private Vector2[] levelBPositions;
    private Vector2[] subLevelBPosition;

    private LevelButton[] parentLevelButtons;
    private LevelButton[] childLevelButtons;

    private LifePanel lifePanel;

    public CorePaymentDelegate corePaymentDelegate;


    @Override
    public void render(float delta) {
        act(delta);
        draw();
        if (hasCreatedScene && playerInfo.livesLeft == 0){
            lifePanel.updateTime();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        if (!isCreated){
            Gdx.input.setInputProcessor(this);
            createScene();
            isCreated = true;
        }

    }

    @Override
    public void hide() {
    
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
    
    

    @Override
	public void dispose() {
		corePaymentDelegate = null;
		lifePanel.corePaymentDelegate = null;
		lifePanel.clear();
		super.dispose();
	}

	private void createScene(){
        playerInfo = Player.shareInstance();
        createPosition();
        createNavigationButton();
        createLevelButton();
        createLifePanel();
        hasCreatedScene = true;

    }

    private void createPosition() {

       

        levelBPositions = new Vector2[10];
        subLevelBPosition = new Vector2[10];

        float height = buttonAtlas.createSprite("levelbutton").getHeight();
        float width = buttonAtlas.createSprite("levelbutton").getWidth();
        float yOffset = (getHeight() - height * 5)/6;
        float xOffset = (getWidth() - 4*width)/5;

        naviPosition = new Vector2(getWidth()/2, yOffset );
        
        int count = 0;
        //Parent level position
        for (int i = 0 ; i < 2; i++) {
            for (int j = 0; j < 5; j++) {

                float y = getHeight() - yOffset - (height + yOffset)*j - height - height/2;
                float x = xOffset + (3*height + 3*xOffset)*i;
                Vector2 point = new Vector2(x, y);

                levelBPositions[count] = point;
                count++;

            }
        }
        count = 0;
        //Sub level position
        for (int i = 0 ; i < 2; i++) {
            for (int j = 0; j < 5; j++) {

                float y = getHeight() - yOffset - (height + yOffset)*j - height - height/2;
                float x = 2*xOffset + height + (height + xOffset) *i;
                Vector2 point = new Vector2(x, y);

                subLevelBPosition[count] = point;
                count++;
            }
        }
    }

    private void createNavigationButton() {
    	    	
		Image backbo = new Image(sceneAtlas.createSprite("background"));
		backbo.setCenterPosition((int) (this.getWidth() / 2),
				(int) (this.getHeight() / 2));
		addActor(backbo);

		Image backgroundUI = new Image(sceneAtlas.createSprite("backgroundui"));
		backgroundUI.setPosition(0, 0);
		backgroundUI.setSize((int) this.getWidth(), (int) this.getHeight());
		addActor(backgroundUI);
    	

        SpriteDrawable up  = new SpriteDrawable(sceneAtlas.createSprite("naviB"));
        SpriteDrawable down = new SpriteDrawable(sceneAtlas.createSprite("naviBPressed"));
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up,down,null,BitmapFontSizer.getFontWithSize(14));

        navigationB = new SimpleButton("",style);
        navigationB.setCenterPosition((int)naviPosition.x, (int)naviPosition.y);
        navigationB.delegate = this;
        navigationB.type = SimpleButton.LEVEL_BACK_BUTTON;
        addActor(navigationB);
    }

    private void createLevelButton() {
    	
    	// Add Level at Action 

        parentLevelButtons = new LevelButton[10];

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.font = BitmapFontSizer.getFontWithSize(14);
        style.fontColor = Color.BLACK;
        SpriteDrawable up = new SpriteDrawable(buttonAtlas.createSprite("levelbutton"));
        style.up = up;
        int max = -1;
        
        for (int i = 0; i < 10; i++){

            LevelButton levelB = new LevelButton("" + i*10, style);
            levelB.setPosition((int)levelBPositions[i].x, (int)levelBPositions[i].y);
            levelB.parentNumber = i;
            if (levelB.parentNumber * 10 > playerInfo.levelAt) {
                levelB.setTouchable(Touchable.disabled);
                levelB.setAlpha(.4f);
            }else{
                levelB.setTouchable(Touchable.enabled);
                levelB.delegate = this;
                max = i;
            }

            parentLevelButtons[i] = levelB;
            addActor(levelB);
        }

        parentLevelButtons[max].currentLevelAnimation();
    }


    private void createLifePanel(){

        lifePanel = new LifePanel(sceneAtlas.createSprite("lifePanel"), corePaymentDelegate);
        lifePanel.setPosition((int) (getWidth() / 2 - lifePanel.getWidth() / 2), (int) (getHeight() / 2 - lifePanel.getHeight() / 2));
        if(playerInfo.livesLeft > 0){
            lifePanel.createLifePanel();
        }else{
            lifePanel.createTimePanel(buttonAtlas.createSprite("buttonS1B"));
        }

        addActor(lifePanel);
    }

    private void removeChildLevels(final int parentNumber){

        float duration = .2f;
        int count = 0;

        parentLevelButtons[parentNumber].setTouchable(Touchable.disabled);

        for (LevelButton childB: childLevelButtons){

            MoveToAction moveToAction = Actions.moveTo(levelBPositions[parentNumber].x, levelBPositions[parentNumber].y , duration);
            AlphaAction alphaAction = Actions.alpha(0, duration);
            ParallelAction parallelAction = Actions.parallel(moveToAction, alphaAction);

            if (count == 9){
                Action complete = new Action() {
                    @Override
                    public boolean act(float delta) {

                        for (LevelButton childB: childLevelButtons){
                            childB.remove();
                            childB.clear();
                            childB.clearActions();
                            childB.clearListeners();

                        }

                        for (LevelButton b: parentLevelButtons){
                            if (b.parentNumber * 10 <= playerInfo.levelAt){
                                b.setTouchable(Touchable.enabled);
                                b.setAlpha(1);
                            }
                        }

                        navigationB.setVisible(true);
                        lifePanel.setVisible(true);
                        childLevelButtons = null;

                        return true;
                    }
                };

                childB.addAction(Actions.sequence(parallelAction, complete));

            }else {
                childB.addAction(parallelAction);
            }

            count++;

        }
    }

    private void createChildLevels(final int parentNumber){
    	
    	//Add Level at Action

        navigationB.setVisible(false);

        for (LevelButton button: parentLevelButtons){
            button.setTouchable(Touchable.disabled);
            button.setAlpha(.4f);
        }

        childLevelButtons = new LevelButton[10];

        float duration = .2f;
        int childNumber = parentNumber*10;



        for (int i = 0; i < 10; i++){
            ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
            style.font = BitmapFontSizer.getFontWithSize(14);
            style.fontColor = Color.BLACK;
            SpriteDrawable up = new SpriteDrawable(buttonAtlas.createSprite("levelbutton"));
            style.up = up;

            LevelButton childB = new LevelButton(childNumber + "", style);
            childB.levelNumber = childNumber++;
            childB.setPosition(levelBPositions[parentNumber].x, levelBPositions[parentNumber].y);
            childB.setAlpha(0);
            childB.delegate = this;
            childB.parentNumber = parentNumber*-1;
            childB.isChild = true;
            childB.setTouchable(Touchable.disabled);

            MoveToAction moveToAction = Actions.moveTo(subLevelBPosition[i].x, subLevelBPosition[i].y, duration);
            ParallelAction parallelAction;

            if (childB.levelNumber > playerInfo.levelAt){
                AlphaAction alphaAction = Actions.alpha(.5f, duration);
                parallelAction = Actions.parallel(moveToAction, alphaAction);
            }else {
                AlphaAction alphaAction = Actions.alpha(1f, duration);
                parallelAction = Actions.parallel(moveToAction, alphaAction);
            }
            
            if (childB.levelNumber == playerInfo.levelAt) {
                childB.currentLevelAnimation();
            }


            childLevelButtons[i] = childB;

            if (i == 9){
                Action complete = new Action() {
                    @Override
                    public boolean act(float delta) {
                        LevelButton parent = parentLevelButtons[parentNumber];
                        parent.setAlpha(1);
                        parent.setTouchable(Touchable.enabled);

                        for (LevelButton b: childLevelButtons){
                            if (b.levelNumber > playerInfo.levelAt){
                                b.setTouchable(Touchable.disabled);
                            }else {
                                b.setTouchable(Touchable.enabled);
                            }
                        }

                        return true;
                    }
                };

                childB.addAction(Actions.sequence(parallelAction, complete));

            }else {
                childB.addAction(parallelAction);
            }

            addActor(childB);

        }
    }

    @Override
    public void buttonPressed(int type) {
        delegate.navigationPressed();
    }

    @Override
    public void parentPressed(int parentNumber) {

        if (isSubCreated){
            removeChildLevels(parentNumber);
            isSubCreated = false;
        }else{
            if (playerInfo.livesLeft > 0){
                lifePanel.setVisible(false);
                createChildLevels(parentNumber);
                isSubCreated = true;
            }else {
                lifePanel.runActionWarning();
            }
        }
    }

    @Override
    public void childPressed(int levelNumber) {
        delegate.beginGamePlay(levelNumber);
    }

    public interface LevelSceneDelegate{
        public void navigationPressed();
        public void beginGamePlay(int levelID);

    }

}
