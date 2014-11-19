package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SocialMediaButton;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SoundControllerDelegate;
import com.Phaze1D.RisingFallAndroidV2.Objects.Spawner;
import com.Phaze1D.RisingFallAndroidV2.Physics.PhysicsWorld;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class StartScene extends Stage implements Screen, SimpleButton.SimpleButtonDelegate{

    public StartScreenDelegate delegate;


    public TextureAtlas startScreenAtlas;
    public TextureAtlas buttonAtlas;
    public TextureAtlas ballsAtlas;

    private Sprite playSprite;
    private Sprite storeSprite;

    private boolean isCreated;
    private boolean hasFinishCreated;
    private boolean paused;

    private Vector2 titlePosition;
    private Vector2 playButtonPosition;
    private Vector2 storeButtonPosition;

    private SocialMediaButton[] socialSubNodes;
    private Spawner[] spawners;
    private LinkedList<Ball> ballQuene;

    private SimpleButton playButton;
    private SimpleButton storeButton;

    private float spawnRate;
    private float nextSpawn;

    private RandomXS128 randomGen = new RandomXS128();

    private Vector2 velocity;

    private Group ballGroup;

    private PhysicsWorld physicsWorld;

    private LocaleStrings strings;

    public SoundControllerDelegate soundDelegate;
    
    public StartScene(Viewport viewport, Batch batch) {
        super(viewport, batch);
        strings = LocaleStrings.getOurInstance();
    }

    @Override
    public void render(float delta) {
        act(delta);
        draw();

        if (hasFinishCreated && !paused) {

            nextSpawn += delta;

//            ((SpriteDrawable) socialParent.getStyle().imageUp).setSprite((Sprite) socialMediaAnimation.getKeyFrame(stateTime, true));
//            stateTime += delta;

            if (nextSpawn >= spawnRate) {
                nextSpawn = nextSpawn - spawnRate;
                spawnBall();
            }
            physicsWorld.evaluatePhysics(delta);
        }

        
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        if (!isCreated) {
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
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void dispose() {
        super.dispose();
       ballQuene.clear();
        if (socialSubNodes != null){
            socialSubNodes = null;
        }
        physicsWorld.dispose();
    }

    private void spawnBall() {


        if (spawners != null) {

            Ball ball = spawners[randomGen.nextInt(10)].spawnBall();
            ball.velocity.set(velocity);
            ball.isPhysicsActive = true;
            ball.setTouchable(Touchable.disabled);
            physicsWorld.addBody(ball);
            ballGroup.addActor(ball);
            ballQuene.addLast(ball);
        }


        if (ballQuene.size() > 0) {
            Ball ball = ballQuene.getFirst();
            if (ball.getY() < 0) {
                ballQuene.removeFirst();
                ball.remove();
                ball.isPhysicsActive = false;
                ball.body = null;
                ball.clear();
                ball.clearActions();
                ball.clearListeners();
                ball = null;
            }
        }



    }

    private void createScene() {

        initVariables();
        stillObjectPositions();
        createBackground();
        createTitle();
        createPlayButton();
        createStoreButton();

        hasFinishCreated = true;

    }

    private void initVariables() {

        playSprite = buttonAtlas.createSprite("buttonL1");
        storeSprite = buttonAtlas.createSprite("buttonL1");
        ballGroup = new Group();
        physicsWorld = new PhysicsWorld();
        physicsWorld.constantStep = 60;
        spawnRate = 1 / 1.0f;
        velocity = new Vector2(0, -250f);
        ballQuene = new LinkedList<Ball>();

//        _deltaTime = _spawnRate;

    }

    private void stillObjectPositions() {
    	

        titlePosition = new Vector2(getWidth() / 2, getHeight() - getHeight() / 5);
        playButtonPosition = new Vector2(getWidth() / 2, getHeight() / 2.25f);
        storeButtonPosition = new Vector2(getWidth() / 2, playButtonPosition.y - playSprite.getHeight() * 2);
        
    }

    private void createTitle() {
        Image title = new Image(startScreenAtlas.createSprite("Title"));
        title.setPosition((int)(titlePosition.x - title.getWidth()/2), (int)(titlePosition.y - title.getHeight()/2));
        addActor(title);
    }

    private void createPlayButton() {

        SpriteDrawable up = new SpriteDrawable(playSprite);
        SpriteDrawable down = new SpriteDrawable(buttonAtlas.createSprite("buttonL2"));
        BitmapFont font = BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontButtonL());
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, down, null, font);
        style.fontColor = Color.BLUE;

        playButton = new SimpleButton(strings.getValue("Play"), style);
        playButton.setPosition((int)(playButtonPosition.x - playButton.getWidth()/2), (int)(playButtonPosition.y - playButton.getHeight()/2));
        playButton.delegate = this;
        playButton.type = SimpleButton.PLAY_BUTTON;
        playButton.soundDelegate = soundDelegate;
        addActor(playButton);

    }

    private void createStoreButton() {

        SpriteDrawable up = new SpriteDrawable(storeSprite);
        SpriteDrawable down = new SpriteDrawable(buttonAtlas.createSprite("buttonL2"));
        BitmapFont font = BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontButtonL());
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, down, null, font);
        style.fontColor = Color.BLUE;

        storeButton = new SimpleButton(strings.getValue("Store"), style);
        storeButton.setPosition((int)(storeButtonPosition.x - storeButton.getWidth()/2), (int)(storeButtonPosition.y - storeButton.getHeight()/2));
        storeButton.delegate = this;
        storeButton.type = SimpleButton.STORE_BUTTON;
        storeButton.soundDelegate = soundDelegate;
        addActor(storeButton);

    }

    private void createBackground() {
        createSpawners();
        
        Image backbo = new Image(startScreenAtlas.createSprite("background0"));
        backbo.setCenterPosition((int)(this.getWidth()/2), (int)(this.getHeight()/2));
        addActor(backbo);
        
        
        addActor(ballGroup);
        
        Image backgroundUI = new Image(startScreenAtlas.createSprite("background"));
        backgroundUI.setPosition(0, 0);
        backgroundUI.setSize((int)this.getWidth(), (int)this.getHeight());
        addActor(backgroundUI);
        
        
        

        
    }

    private void createSpawners() {
        float width = ballsAtlas.createSprite("ball0").getWidth();

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




    @Override
    public void buttonPressed(int type) {
        if (type == SimpleButton.PLAY_BUTTON) {
            delegate.playButtonPressed();
        } else if (type == SimpleButton.STORE_BUTTON) {
            delegate.storeButtonPressed();
        }
    }

    /**
     * Start Screen Delegate
     */
    public interface StartScreenDelegate {
        public void playButtonPressed();

        public void storeButtonPressed();
    }
}
