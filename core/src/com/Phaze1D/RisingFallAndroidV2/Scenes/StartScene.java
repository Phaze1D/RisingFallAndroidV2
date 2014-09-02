package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SocialMediaButton;
import com.Phaze1D.RisingFallAndroidV2.Objects.Spawner;
import com.Phaze1D.RisingFallAndroidV2.Physics.PhysicsWorld;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
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
public class StartScene extends Stage implements Screen, SimpleButton.SimpleButtonDelegate, SocialMediaButton.SocialMediaButtonDelegate {

    public StartScreenDelegate delegate;


    public TextureAtlas startScreenAtlas;
    public TextureAtlas socialMediaAtlas;
    public TextureAtlas buttonAtlas;
    public TextureAtlas ballsAtlas;

    private Sprite playSprite;
    private Sprite storeSprite;

    private boolean isCreated;
    private boolean isSocialSubCreated;
    private boolean hasFinishCreated;
    private boolean paused;

    private Vector2 titlePosition;
    private Vector2 playButtonPosition;
    private Vector2 socialButtonPosition;
    private Vector2 storeButtonPosition;

    private Vector2[] socialSubPositions;
    private SocialMediaButton[] socialSubNodes;
    private Spawner[] spawners;
    private LinkedList<Ball> ballQuene;

    private SimpleButton playButton;
    private SimpleButton storeButton;

    private SocialMediaButton socialParent;

    private Animation socialMediaAnimation;

    private float socialSubAnimationDuration;
    private float spawnRate;
    private float stateTime;
    private float accumulator = 0;
    private float nextSpawn;

    private RandomXS128 randomGen = new RandomXS128();

    private double deltaTime;
    private double passTime;

    private Vector2 velocity;

    private Group ballGroup;

    private PhysicsWorld physicsWorld;


    public StartScene(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }

    @Override
    public void render(float delta) {

        act(delta);
        draw();

        if (hasFinishCreated && !paused) {

            nextSpawn += delta;

            ((SpriteDrawable) socialParent.getStyle().imageUp).setSprite((Sprite) socialMediaAnimation.getKeyFrame(stateTime, true));
            stateTime += delta;

            if (nextSpawn >= spawnRate) {
                nextSpawn = nextSpawn - spawnRate;
                spawnBall();
            }
        }

        physicsWorld.evaluatePhysics(delta);
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
        createTitle();
        createPlayButton();
        createStoreButton();
        createSocialMediaButton();
        createBackground();

        hasFinishCreated = true;

    }

    private void initVariables() {

        playSprite = buttonAtlas.createSprite("buttonL1");
        storeSprite = buttonAtlas.createSprite("buttonL1");
        ballGroup = new Group();
        physicsWorld = new PhysicsWorld();
        physicsWorld.constantStep = 60;
        spawnRate = 1 / 1.0f;
        socialSubAnimationDuration = .3f;
        velocity = new Vector2(0, -250f);
        ballQuene = new LinkedList<Ball>();

//        _deltaTime = _spawnRate;

    }

    private void stillObjectPositions() {

        Sprite test = new Sprite(socialMediaAtlas.createSprite("facebook"));

        titlePosition = new Vector2(getWidth() / 2, getHeight() - getHeight() / 5);
        playButtonPosition = new Vector2(getWidth() / 2, getHeight() / 2.25f);
        socialButtonPosition = new Vector2(getWidth() * .9f, getHeight() * .1f);
        storeButtonPosition = new Vector2(getWidth() / 2, playButtonPosition.y - playSprite.getHeight() * 2);

        socialSubPositions = new Vector2[6];
        float xOffset = test.getWidth() + test.getWidth() * .3f;
        float yOffset = test.getHeight() + test.getHeight() * .3f;


        int count = 0;

        for (int row = -1; row < 2; row++) {
            for (int column = 0; column < 2; column++) {
                Vector2 vector2 = new Vector2(playButtonPosition.x + xOffset * row, playButtonPosition.y + yOffset * column);
                socialSubPositions[count] = vector2;
                count++;
            }
        }
    }

    private void createTitle() {
        addActor(ballGroup);
        Image title = new Image(startScreenAtlas.createSprite("Title"));
        title.setPosition(titlePosition.x - title.getWidth()/2, titlePosition.y - title.getHeight()/2);
        addActor(title);
    }

    private void createPlayButton() {

        SpriteDrawable up = new SpriteDrawable(playSprite);
        SpriteDrawable down = new SpriteDrawable(buttonAtlas.createSprite("buttonL2"));
        BitmapFont font = BitmapFontSizer.getFontWithSize(0);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, down, null, font);
        style.fontColor = Color.BLACK;

        playButton = new SimpleButton("PlayK", style);
        playButton.setPosition(playButtonPosition.x - playButton.getWidth()/2, playButtonPosition.y - playButton.getHeight()/2);
        playButton.delegate = this;
        playButton.type = SimpleButton.PLAY_BUTTON;
        addActor(playButton);

    }

    private void createStoreButton() {

        SpriteDrawable up = new SpriteDrawable(storeSprite);
        SpriteDrawable down = new SpriteDrawable(buttonAtlas.createSprite("buttonL2"));
        BitmapFont font = BitmapFontSizer.getFontWithSize(0);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, down, null, font);
        style.fontColor = Color.BLACK;

        storeButton = new SimpleButton("StoreK", style);
        storeButton.setPosition(storeButtonPosition.x - storeButton.getWidth()/2, storeButtonPosition.y - storeButton.getHeight()/2);
        storeButton.delegate = this;
        storeButton.type = SimpleButton.STORE_BUTTON;
        addActor(storeButton);

    }

    private void createSocialMediaButton() {

        SpriteDrawable up = new SpriteDrawable(socialMediaAtlas.createSprite("facebook"));

        socialParent = new SocialMediaButton(up);
        socialParent.setPosition(socialButtonPosition.x - socialParent.getWidth()/2, socialButtonPosition.y - socialParent.getHeight()/2);
        socialParent.delegate = this;
        socialParent.type = SocialMediaButton.SOCIAL_BUTTON;
        addActor(socialParent);
        socialMediaAnimation = new Animation(2f, socialMediaAtlas.createSprites());

    }

    private void createBackground() {
        createSpawners();
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


    private void createSocialChildren() {

        playButton.setVisible(false);
        storeButton.setVisible(false);
        socialParent.setTouchable(Touchable.disabled);

        Array<Sprite> sprites = socialMediaAtlas.createSprites();
        socialSubNodes = new SocialMediaButton[sprites.size];


        for (int i = 0; i < sprites.size; i++) {
            SpriteDrawable up = new SpriteDrawable(sprites.get(i));
            final SocialMediaButton child = new SocialMediaButton(up);
            child.setAlpha(0);
            child.indexInSubArray = i;
            child.delegate = this;
            child.setPosition(socialButtonPosition.x - child.getWidth()/2, socialButtonPosition.y - child.getHeight()/2);
            child.setTouchable(Touchable.disabled);
            socialSubNodes[i] = child;
            AlphaAction fadeIn = Actions.fadeIn(socialSubAnimationDuration);
            MoveToAction moveToAction = Actions.moveTo(socialSubPositions[i].x - child.getWidth() / 2, socialSubPositions[i].y, socialSubAnimationDuration);

            Action complete = new Action() {
                @Override
                public boolean act(float delta) {
                    child.setTouchable(Touchable.enabled);
                    socialParent.setTouchable(Touchable.enabled);
                    return true;
                }
            };

            SequenceAction seq = Actions.sequence(Actions.parallel(moveToAction, fadeIn), complete);
            child.addAction(seq);
            addActor(child);
        }
    }

    private void removeSocialChildren() {

        socialParent.setTouchable(Touchable.disabled);

        int count = 0;

        for (final SocialMediaButton node : socialSubNodes) {
            count++;
            node.setTouchable(Touchable.disabled);
            AlphaAction fadeOut = Actions.fadeOut(socialSubAnimationDuration);
            MoveToAction moveToAction = Actions.moveTo(socialParent.getCenterX(), socialParent.getY(), socialSubAnimationDuration);


            if (count == socialSubNodes.length) {

                Action complete = new Action() {
                    @Override
                    public boolean act(float delta) {
                        playButton.setVisible(true);
                        storeButton.setVisible(true);
                        node.clear();
                        node.clearActions();
                        node.remove();
                        socialSubNodes = null;
                        socialParent.setTouchable(Touchable.enabled);
                        return true;
                    }
                };

                ParallelAction group = Actions.parallel(fadeOut, moveToAction);
                node.addAction(Actions.sequence(group, complete));
            } else {
                node.addAction(Actions.parallel(fadeOut, moveToAction));
            }
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

    @Override
    public void socialButtonPressed() {

        if (isSocialSubCreated) {
            removeSocialChildren();
            isSocialSubCreated = false;
            socialParent.isOpen = false;
        } else {
            createSocialChildren();
            isSocialSubCreated = true;
            socialParent.isOpen = true;
        }

    }

    @Override
    public void subSocialButtonPressed(boolean didShare) {

    }

    @Override
    public void disableChild() {

    }

    @Override
    public void enableChild() {

    }

    /**
     * Start Screen Delegate
     */
    public interface StartScreenDelegate {
        public void playButtonPressed();

        public void storeButtonPressed();
    }
}
