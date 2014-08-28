package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.LevelButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.LifePanel;
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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

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

    private void createScene(){
        playerInfo = Player.shareInstance();
        createPosition();
        createNavigationButton();
        createLevelButton();
        createLifePanel();
        hasCreatedScene = true;

    }

    private void createPosition() {

        naviPosition = new Vector2(getWidth()/2, sceneAtlas.createSprite("naviB").getHeight()/2 );

        levelBPositions = new Vector2[10];
        subLevelBPosition = new Vector2[10];

        float height = buttonAtlas.createSprite("levelButton").getHeight();
        float width = buttonAtlas.createSprite("levelButton").getWidth();
        float yOffset = (getHeight() - height * 5)/6;
        float xOffset = (getWidth() - 4*width)/5;


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

        SpriteDrawable up  = new SpriteDrawable(sceneAtlas.createSprite("naviB"));
        SpriteDrawable down = new SpriteDrawable(sceneAtlas.createSprite("naviBPressed"));
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up,down,null,BitmapFontSizer.getFontWithSize(0));

        navigationB = new SimpleButton("",style);
        navigationB.setCenterPosition(naviPosition.x, naviPosition.y);
        navigationB.delegate = this;
        navigationB.type = SimpleButton.LEVEL_BACK_BUTTON;
        addActor(navigationB);
    }

    private void createLevelButton() {

        parentLevelButtons = new LevelButton[10];
        for (int i = 0; i < 10; i++){
            ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
            style.font = BitmapFontSizer.getFontWithSize(0);
            style.fontColor = Color.BLACK;
            SpriteDrawable up = new SpriteDrawable(buttonAtlas.createSprite("levelButton"));
            style.up = up;


            LevelButton levelB = new LevelButton("" + i*10, style);
            levelB.setPosition(levelBPositions[i].x, levelBPositions[i].y);
            levelB.parentNumber = i;
            if (levelB.parentNumber * 10 > playerInfo.levelAt) {
                levelB.setTouchable(Touchable.disabled);
                levelB.setAlpha(.4f);
            }else{
                levelB.setTouchable(Touchable.enabled);
                levelB.delegate = this;
            }

            parentLevelButtons[i] = levelB;
            addActor(levelB);
        }

    }


    private void createLifePanel(){

        lifePanel = new LifePanel(sceneAtlas.createSprite("lifePanel"));
        lifePanel.setPosition((int)(getWidth()/2 - lifePanel.getWidth()/2), (int)(getHeight()/2 - lifePanel.getHeight()/2));
        if(playerInfo.livesLeft > 0){
            lifePanel.createLifePanel();
        }else{
            lifePanel.createTimePanel(buttonAtlas.createSprite("buttonXS1"));
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
            style.font = BitmapFontSizer.getFontWithSize(0);
            style.fontColor = Color.BLACK;
            SpriteDrawable up = new SpriteDrawable(buttonAtlas.createSprite("levelButton"));
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
