package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.*;
import com.Phaze1D.RisingFallAndroidV2.Objects.LevelFactory;
import com.Phaze1D.RisingFallAndroidV2.Objects.LinkedList;
import com.Phaze1D.RisingFallAndroidV2.Objects.Spawner;
import com.Phaze1D.RisingFallAndroidV2.Physics.PhyiscsWorld;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class GameplayScene extends Stage implements Screen, Ball.BallDelegate, SimpleButton.SimpleButtonDelegate {

    public GameSceneDelegate delegate;

    public Player playerInfo;

    public int levelID;
    public int stageAt;
    public int numRows;
    public int powerTypeAt;
    public int power2BallNum;
    public int powerMaxAmount;
    public int nextBallChange;

    public float maxColumns;
    public float firstX;
    public float firstY;
    public float yOffsetPA;
    public float xOffsetPA;

    public double deltaTime;
    public double nextTime;
    public double currentTime;
    public double nextColorTime;
    public double nextSpeedTime;

    public Ball[] ballsArray;
    public Spawner[] spawners;

    public LinkedList movingBallList;

    public boolean isCreated;
    public boolean isSettingCreated;
    public boolean hasFinishCreated;
    public boolean clickedBegin;
    public boolean pausedGame;
    public boolean objectiveReached;
    public boolean didReachScore;
    public boolean didWin;
    public boolean ceilingHit;
    public boolean didCreateTextView;

    public TextureAtlas gameSceneAtlas;
    public TextureAtlas buttonAtlas;
    public TextureAtlas ballAtlas;
    public TextureAtlas socialMediaAtlas;
    public TextureAtlas powerBallAtlas;
    public TextureAtlas unMovableAtlas;
    public TextureAtlas badBallAtlas;

    public Sprite playAreaSprite;
    public Sprite ceilingAreaSprite;

    public Image ceiling;
    public Image playArea;

    public ObjectivePanel objectivePanel;
    public SettingPanel settingPanel;
    public ScorePanel scorePanel;
    public PowerSidePanel powerSidePanel;
    public PowerTimePanel powerTimePanel;
    public InfoPanel infoPanel;
    public SimpleButton optionPanel;

    public LevelFactory levelFactory;

    public Ball hitBall;

    public Vector2 playAreaPosition;
    public Vector2 powerAreaPosition;
    public Vector2 optionAreaPosition;
    public Vector2 objectivePosition;
    public Vector2 scorePosition;
    public Vector2 powerTimePosition;
    public Vector2 settingPosition;
    public Vector2 ceilingPosition;

    private Group ballGroup;
    public PhyiscsWorld world;


    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        if (!isCreated){
            createScene();
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

    public void createScene(){
        initVariables();
        createPositions();
        createBackground();
        createPlayArea();
        createSideView();
        pauseGame();
        createInitialFill();
        hasFinishCreated = true;

    }

    private void initVariables() {

        ballGroup = new Group();
        world = new PhyiscsWorld();
        powerTypeAt = -1;
        powerMaxAmount = 0;
        playerInfo = Player.shareInstance();
        levelFactory = new LevelFactory(levelID);
        stageAt = 1;
        ceilingAreaSprite = new Sprite(new Texture(Gdx.files.internal("PlayAreaCeilingArea/ceiling" + levelFactory.ceilingHeight + ".png")));
        playAreaSprite = new Sprite(new Texture(Gdx.files.internal("PlayAreaCeilingArea/playArea" + levelFactory.ceilingHeight + ".png")));
        maxColumns = 8;

        float ballWidth = ballAtlas.createSprite("ball0").getWidth();
        xOffsetPA = (playAreaSprite.getWidth() - ballWidth*maxColumns)/(maxColumns + 1);

        float numTest = (playAreaSprite.getWidth() - xOffsetPA)/(xOffsetPA + ballWidth);
        numRows =(int) Math.ceil(numTest);
        yOffsetPA = (playAreaSprite.getHeight() - ballWidth * numRows)/(numRows + 1);

        ballsArray = new Ball[numRows * levelFactory.numOfColumns];
        spawners = new Spawner[levelFactory.numOfColumns];
        movingBallList = new LinkedList(this);

        if (levelFactory.gameType == 2){
            nextBallChange = levelFactory.numberOfBalls - levelFactory.changeSpeedBNum;
        }
    }

    private void createPositions() {

        Sprite powerAreaTest = gameSceneAtlas.createSprite("powerArea");
        Sprite scoreTest = gameSceneAtlas.createSprite("scoreArea");
        Sprite optionTest = gameSceneAtlas.createSprite("optionArea");
        Sprite levelIDtest = gameSceneAtlas.createSprite("LevelIDArea");
        Sprite gameOverTest = gameSceneAtlas.createSprite("gameOverArea");
        Sprite ballTest = ballAtlas.createSprite("ball0");

        float playAreaWidth = playAreaSprite.getWidth();
        float playCielingHeight = playAreaSprite.getHeight() + ceilingAreaSprite.getHeight();

        float sideViewWidth = gameSceneAtlas.createSprite("powerArea").getWidth();
        float xOffset1 = (getWidth() - playAreaWidth - sideViewWidth)/2f;
        float yOffset1 = (getHeight() - playCielingHeight - scoreTest.getHeight() )/2f;

        float yOffset2 = ( getHeight() - yOffset1 - optionTest.getHeight() - powerAreaTest.getHeight() - levelIDtest.getHeight())/2f;

        playAreaPosition = new Vector2(xOffset1, yOffset1);
        ceilingPosition = new Vector2(xOffset1, yOffset1 + playAreaSprite.getHeight() - 2);
        powerAreaPosition = new Vector2(xOffset1 * 2 + playAreaWidth, yOffset2 + powerAreaTest.getHeight());
        optionAreaPosition = new Vector2(xOffset1*2 + playAreaWidth, yOffset1);

        float xOffset2 = (playAreaWidth - scoreTest.getWidth()*3)/2f;

        objectivePosition = new Vector2(xOffset1, yOffset1 * 2 + playCielingHeight);
        scorePosition = new Vector2(objectivePosition.x + xOffset2 + scoreTest.getWidth(), yOffset1*2 + playCielingHeight);
        powerTimePosition = new Vector2(scorePosition.x + xOffset2 + scoreTest.getWidth(), yOffset1*2 + playCielingHeight);

        settingPosition = new Vector2(playAreaPosition.x + playAreaWidth/2 - gameOverTest.getWidth()/2, getHeight()/2 - gameOverTest.getHeight()/2);

        if (maxColumns == levelFactory.numOfColumns) {
            firstX = playAreaPosition.x + xOffsetPA;
            firstY = playAreaPosition.y + yOffsetPA;
        }else{
            int round = Math.round(maxColumns/levelFactory.numOfColumns);
            firstX = playAreaPosition.x + xOffsetPA + (xOffsetPA + ballTest.getWidth())* round;
            firstY = playAreaPosition.y + yOffsetPA;
        }

    }

    private void createBackground() {

    }

    private void createPlayArea() {

        playArea = new Image(playAreaSprite);
        playArea.setPosition((int)playAreaPosition.x, (int)playAreaPosition.y);
        addActor(playArea);
        addActor(ballGroup);

        ceiling = new Image(ceilingAreaSprite);
        ceiling.setPosition((int)ceilingPosition.x, (int)ceilingPosition.y);
        addActor(ceiling);

        createSpawners();
    }

    private void createSpawners(){

        for (int i = 0; i < levelFactory.numOfColumns; i++){
            Spawner spawner = new Spawner();
            spawner.column = i;
            spawner.position = new Vector2(firstX + (xOffsetPA + ballAtlas.createSprite("ball0").getWidth())*i, ceilingPosition.y);
            spawner.powerUpProb = levelFactory.powerBallDrop;
            spawner.doubleBallProb = levelFactory.doubleBallProb;
            spawner.unMovableProb = levelFactory.unMovableProb;
            spawner.levelAt = levelID;
            spawners[i] = spawner;
        }

    }

    private void createSideView() {
        optionPanel = new SimpleButton("", new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(gameSceneAtlas.createSprite("optionArea")),null,null, BitmapFontSizer.getFontWithSize(0)));
        optionPanel.setPosition(optionAreaPosition.x, optionAreaPosition.y);
        optionPanel.delegate = this;
        addActor(optionPanel);

        powerSidePanel = new PowerSidePanel(gameSceneAtlas.createSprite("powerArea"));
        powerSidePanel.powerBallAtlas = powerBallAtlas;
        powerSidePanel.setPosition(powerAreaPosition.x,powerAreaPosition.y);
        powerSidePanel.createPanel(gameSceneAtlas.createSprite("notificationCircle"));
        addActor(powerSidePanel);
        for (Ball pball: powerSidePanel.powerBalls) {
            pball.delegate = this;
        }


        objectivePanel = new ObjectivePanel(gameSceneAtlas.createSprite("objectiveArea"));
        objectivePanel.setPosition(objectivePosition.x, objectivePosition.y);
        objectivePanel.gameType = levelFactory.gameType;


        if (levelFactory.gameType == 1) {
            objectivePanel.time = (float)levelFactory.gameTime;
            objectivePanel.ballsLeft = -1;
        }else{
            objectivePanel.ballsLeft = levelFactory.numberOfBalls;
            objectivePanel.time = -1;
        }
        objectivePanel.createPanel();
        addActor(objectivePanel);

        scorePanel = new ScorePanel(gameSceneAtlas.createSprite("scoreArea"));
        scorePanel.setPosition(scorePosition.x, scorePosition.y);
        scorePanel.targetScore = levelFactory.targetScore;
        scorePanel.createScorePanel(levelFactory.targetScore);
        addActor(scorePanel);

        infoPanel = new InfoPanel(gameSceneAtlas.createSprite("LevelIDArea"));
        infoPanel.setPosition(getWidth() - infoPanel.getWidth(), getHeight() - infoPanel.getHeight());
        infoPanel.createPanel(levelID, playerInfo.getPassedScore(levelID));
        addActor(infoPanel);

    }

    private void createInitialFill() {

    }

    private void pauseGame() {

        pausedGame = true;
        movingBallList.gamePaused();
        removeGameplay();
        optionPanel.setTouchable(Touchable.disabled);
        creatingSettingPanel();

    }

    private void creatingSettingPanel(){

    }

    private void removeGameplay(){

        for (Ball ball : ballsArray) {
            if (ball != null) {
                ball.setAlpha(.5f);
                ball.setTouchable(Touchable.disabled);
            }
        }
    }

    private void removeSettingPanel(){

        settingPanel.clear();
        settingPanel.remove();
        settingPanel = null;
        optionPanel.setTouchable(Touchable.enabled);
        isSettingCreated = false;
        resumeGameplay();
    }

    private void resumeGameplay(){

    }

    public void disableBalls(){

    }


    @Override
    public void ballTaped(Ball ball) {

    }

    @Override
    public void ballMoved(Ball ball, int direction) {

    }

    @Override
    public void powerBallTouch(Ball ball) {

    }

    @Override
    public void buttonPressed(int type) {

    }

    public interface GameSceneDelegate{
        public void quitGamePlay();
        public void beginNextLevel(int level);
    }

}
