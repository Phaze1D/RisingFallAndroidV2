package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.SellItemPanel;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.StoreBuyPanel;
import com.Phaze1D.RisingFallAndroidV2.Objects.Spawner;
import com.Phaze1D.RisingFallAndroidV2.Physics.PhysicsWorld;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.LinkedList;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class StoreScene extends Stage implements Screen, StoreBuyPanel.StoreBuyPanelDelegate, SimpleButton.SimpleButtonDelegate{

    public StoreDelegate delegate;

    private boolean isCreated;
    private boolean hasFinished;
    private boolean isSellCreated;

    public TextureAtlas sceneAtlas;
    public TextureAtlas buttonAtlas;
    public TextureAtlas ballsAtlas;

    private Spawner[] spawners;
    private LinkedList<Ball> ballQuene;

//    @property NSArray * productIdentifiers;
//    @property NSArray * products;

    private float deltaTime;
    private float passTime;
    private float spawnRate;

    private Vector2 velocity;
    private Vector2 sidePosition;
    private Vector2 backButtonPostion;
    private Vector2 initPointSellPanel;

    private StoreBuyPanel buyPanel;
    private SellItemPanel sellItemPanel;

    private SimpleButton backB;

    private PhysicsWorld physicsWorld;
    private Group ballGroup;

    private Sprite itemsArea;
    private Sprite backButton;
    private Sprite sellItemArea;

//    @property SKProductsRequest * productsRequest;


    @Override
    public void render(float delta) {
        draw();
        act();
        if (hasFinished){

            physicsWorld.evaluatePhysics(deltaTime);
        }


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        if(!isCreated){
            Gdx.input.setInputProcessor(this);
            createScene();
            hasFinished = true;
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

        initVariables();
        createBackground();
        createPositions();
        createSideView();
        createBackButton();
        //validateProductsID();


    }

    private void initVariables() {

        spawnRate = 1;
        ballQuene = new LinkedList<Ball>();
        velocity = new Vector2(0, -200);
        initPointSellPanel = new Vector2();
        physicsWorld = new PhysicsWorld();
        ballGroup = new Group();
        addActor(ballGroup);
        itemsArea = sceneAtlas.createSprite("itemsArea");
        backButton = sceneAtlas.createSprite("backButton");
        sellItemArea = sceneAtlas.createSprite("sellItemArea");

    }

    private void createBackground() {
        createSpawners();
    }

    private void createSpawners(){

        float width = ballsAtlas.createSprite("ball0").getWidth();
        RandomXS128 randomGen = new RandomXS128();
        spawners = new Spawner[10];
        for (int i = 0; i < 10; i++) {
            Spawner spawners1 = new Spawner();
            float offsetX = (getWidth() - (width * 10)) / 11;
            float x = offsetX + (width + offsetX) * i;
            spawners1.position = new Vector2(x, getHeight());
            spawners1.powerUpProb = -1;
            spawners1.ballAtlas = ballsAtlas;
            spawners1.randGen = randomGen;
            spawners[i] = spawners1;

        }

    }

    private void createPositions() {

        float yOffset = (getHeight() - backButton.getHeight() - itemsArea.getHeight())/3f;

        backButtonPostion = new Vector2(0, yOffset);
        sidePosition = new Vector2(0, yOffset* 2 + backButton.getHeight());

    }

    private void createSideView() {

        buyPanel = new StoreBuyPanel(itemsArea);
        buyPanel.setPosition((int)sidePosition.x, (int)sidePosition.y);
        buyPanel.delegate = this;
        buyPanel.createPanel();
        addActor(buyPanel);

    }

    private void createBackButton() {

        backB = new SimpleButton("", new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(backButton), null,null, BitmapFontSizer.getFontWithSize(11)));
        backB.setPosition((int)backButtonPostion.x, (int)backButtonPostion.y);
        backB.delegate = this;
        addActor(backB);

    }

    private void spawnRandomBall(){

//        if (_spawners != nil) {
//            int randIndex = arc4random() % (_spawners.count);
//            Ball * ballSp = [[_spawners objectAtIndex:randIndex] spawnBall];
//            ballSp.velocity = _velocity;
//            [ballSp setPhysicsProperties];
//            [_ballQuene addObject:ballSp];
//            [self addChild: ballSp];
//
//        }
//
//        //Remove ball that is off the screen
//        if (_ballQuene.count > 0) {
//            Ball * ballF = [_ballQuene objectAtIndex:0];
//            if (ballF.position.y < 0) {
//                [_ballQuene removeObjectAtIndex:0];
//                [ballF removeFromParent];
//                ballF = nil;
//            }
//        }

    }

    private void createSellPanel(final int powerType){
        buyPanel.disableButton();
        float xOffset = (getWidth() - buyPanel.getWidth() - sellItemArea.getWidth())/2;
        initPointSellPanel.set(getWidth()/2, 0);

        sellItemPanel = new SellItemPanel(sellItemArea);
        sellItemPanel.setPosition((int)initPointSellPanel.x, (int)initPointSellPanel.y);
        sellItemPanel.createPanel(powerType, true);
        sellItemPanel.addAction(Actions.alpha(0));
        sellItemPanel.addAction(Actions.scaleTo(0,0));

        MoveToAction moveTo = Actions.moveTo(xOffset + buyPanel.getWidth(), getHeight()/2 - sellItemPanel.getHeight()/2,.25f);
        ScaleToAction scaleU = Actions.scaleTo(1,1,.25f);
        AlphaAction alphaAction = Actions.alpha(1, .25f);
        ParallelAction group = Actions.parallel(moveTo,scaleU,alphaAction);
        Action complete = new Action() {
            @Override
            public boolean act(float delta) {
                buyPanel.enableButton();
                sellItemPanel.createTextArea(powerType);
                return true;
            }
        };

        sellItemPanel.addAction(Actions.sequence(group, complete));

        addActor(sellItemPanel);
    }

    private void removeSellPanel(){
        buyPanel.disableButton();
        sellItemPanel.textView.remove();
        sellItemPanel.textView = null;

        MoveToAction moveTo = Actions.moveTo(initPointSellPanel.x, initPointSellPanel.y, .25f);
        ScaleToAction scaleU = Actions.scaleTo(0,0,.25f);
        AlphaAction alphaAction = Actions.alpha(0, .25f);
        ParallelAction group = Actions.parallel(moveTo,scaleU,alphaAction);
        Action complete = new Action() {
            @Override
            public boolean act(float delta) {
                sellItemPanel.remove();
                sellItemPanel.clear();
                sellItemPanel = null;
                buyPanel.enableButton();
                return true;
            }
        };

        sellItemPanel.addAction(Actions.sequence(group, complete));

    }

    private void replaceSellPanel(final int powerType){

        if (powerType != sellItemPanel.powerType){
            buyPanel.disableButton();
            sellItemPanel.textView.remove();

            MoveToAction moveTo = Actions.moveTo(initPointSellPanel.x, initPointSellPanel.y, .25f);
            ScaleToAction scaleU = Actions.scaleTo(0,0,.25f);
            AlphaAction alphaAction = Actions.alpha(0, .25f);
            ParallelAction group = Actions.parallel(moveTo,scaleU,alphaAction);
            Action complete = new Action() {
                @Override
                public boolean act(float delta) {
                    sellItemPanel.remove();
                    sellItemPanel.clear();
                    sellItemPanel = null;
                    createSellPanel(powerType);
                    return true;
                }
            };

            sellItemPanel.addAction(Actions.sequence(group, complete));
        }else{
            removeSellPanel();
            isSellCreated = false;
        }

    }


    @Override
    public void pButtonPressed(int powerTyped) {

        if (!isSellCreated){
            createSellPanel(powerTyped);
            isSellCreated = true;
        }else{
            replaceSellPanel(powerTyped);
        }

    }

    @Override
    public void buttonPressed(int type) {

        if (isSellCreated){
            removeSellPanel();
            isSellCreated = false;
        }else {
            delegate.storeBackPressed();
        }

    }

    public interface StoreDelegate{
        public void storeBackPressed();
    }
}
