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
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class GameplayScene extends Stage implements Screen, Ball.BallDelegate, SimpleButton.SimpleButtonDelegate, SettingPanel.SettingPanelDelegate {

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
    public TextureAtlas infoAtlas;

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

    public GameplayScene(int levelID){
        this.levelID = levelID;
        levelFactory = new LevelFactory(levelID);
    }

    @Override
    public void render(float delta) {

        currentTime = System.currentTimeMillis()/1000;

        if (powerTimePanel != null){
            powerTimePanel.currentTime = (float)currentTime;
        }

        movingBallList.checkIfReached();
        if (ceilingHit && !pausedGame){
            didReachScore = scorePanel.didReachScore();
            didWin = false;
            pauseGame();
        }

        if (hasFinishCreated && clickedBegin && !pausedGame && !ceilingHit){

            if (currentTime >= nextColorTime){
                changeRandomBallColor();
                nextColorTime = currentTime + levelFactory.changeColorTime - delta;
            }

            if (levelID >= 70 && levelFactory.gameType == 1 && currentTime >= nextSpeedTime){
                levelFactory.changeSpeedAndDrop();
                nextSpeedTime = currentTime + levelFactory.changeSpeedTime - delta;
            }

            if (currentTime >= nextTime && !objectiveReached){
                spawnBall();

                if (powerTypeAt == 1){
                    nextTime = currentTime + 1/.5f - delta;
                }else{
                    nextTime = currentTime + 1/levelFactory.dropRate - delta;
                }
            }

            if (powerTimePanel != null && powerTimePanel.updatetimer() && powerTypeAt != 2){
                powerTypeAt = -1;
                if (powerTimePanel != null){
                    removePowerTimePanel();
                }
            }

            objectiveReached = objectivePanel.updateObjective();
            if (objectiveReached && movingBallList.count == 0){
                stageAt = 3;
                didReachScore = scorePanel.didReachScore();
                pauseGame();
            }

        }
        act(delta);
        draw();
        world.evaluatePhysics(delta);


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        if (!isCreated){
            Gdx.input.setInputProcessor(this);
            addListener(new ScreenListener());
            createScene();
        }

    }

    @Override
    public void hide() {
        clear();
    }

    @Override
    public void pause() {
        pauseGame();
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
        stageAt = 1;
        ceilingAreaSprite = new Sprite(new Texture(Gdx.files.internal("PlayAreaCeilingArea/ceiling" + levelFactory.ceilingHeight + ".png")));
        playAreaSprite = new Sprite(new Texture(Gdx.files.internal("PlayAreaCeilingArea/playArea" + levelFactory.ceilingHeight + ".png")));
        maxColumns = 8;

        float ballWidth = ballAtlas.createSprite("ball0").getWidth();
        xOffsetPA = (playAreaSprite.getWidth() - ballWidth*maxColumns)/(maxColumns + 1);

        float numTest = (playAreaSprite.getHeight() - xOffsetPA)/(xOffsetPA + ballWidth);
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
        powerAreaPosition = new Vector2(xOffset1 * 2 + playAreaWidth, yOffset2  + yOffset1 + optionTest.getHeight());

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
        optionPanel.setPosition((int)optionAreaPosition.x, (int)optionAreaPosition.y);
        optionPanel.delegate = this;
        addActor(optionPanel);

        powerSidePanel = new PowerSidePanel(gameSceneAtlas.createSprite("powerArea"));
        powerSidePanel.powerBallAtlas = powerBallAtlas;
        powerSidePanel.setPosition((int)powerAreaPosition.x,(int)powerAreaPosition.y);
        powerSidePanel.createPanel(gameSceneAtlas.createSprite("notificationCircle"));
        addActor(powerSidePanel);
        for (Ball pball: powerSidePanel.powerBalls) {
            pball.delegate = this;
        }


        objectivePanel = new ObjectivePanel(gameSceneAtlas.createSprite("objectiveArea"));
        objectivePanel.setPosition((int)objectivePosition.x, (int)objectivePosition.y);
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
        scorePanel.setPosition((int)scorePosition.x, (int)scorePosition.y);
        scorePanel.targetScore = levelFactory.targetScore;
        scorePanel.createScorePanel(levelFactory.targetScore);
        addActor(scorePanel);

        infoPanel = new InfoPanel(gameSceneAtlas.createSprite("LevelIDArea"));
        infoPanel.setPosition(getWidth() - infoPanel.getWidth(), getHeight() - infoPanel.getHeight());
        infoPanel.createPanel(levelID, playerInfo.getPassedScore(levelID));
        addActor(infoPanel);

    }

    private void createInitialFill() {
        int maxRow = numRows -2;
        RandomXS128 randGen = new RandomXS128();

        for (int i = 0; i < levelFactory.initFill * numRows * levelFactory.numOfColumns;){
            int randC = randGen.nextInt(levelFactory.numOfColumns);

            if (ballsArray[ ( randC + (maxRow -1)*levelFactory.numOfColumns)] == null){
                int row = 0;
                while (ballsArray[randC] != null){
                    randC = randC + levelFactory.numOfColumns;
                    row++;
                }

                int randIndex = randGen.nextInt(6);
                Ball ball = new Ball(ballAtlas.createSprite("ball" + randIndex));
                ball.column = randC % levelFactory.numOfColumns;
                ball.ballColor = randIndex;
                ball.row = row;
                ball.setTouchable(Touchable.enabled);
                ball.delegate = this;
                ball.setPosition(firstX + (xOffsetPA + ball.getWidth())*ball.column, firstY + (yOffsetPA + ball.getHeight())* ball.row);
                ballsArray[randC] = ball;
                i++;

            }
        }
    }

    private void spawnBall(){

        if (powerMaxAmount >= 2){
            for (Spawner sp: spawners){
                sp.stopSpawningPower = true;
            }
        }

        RandomXS128 randGen = new RandomXS128();

        int randC = randGen.nextInt(levelFactory.numOfColumns);
        int row = 0;
        int index = randC;

        while (ballsArray[index] != null && row < numRows){
            index = index + levelFactory.numOfColumns;
            row++;
            if (row == numRows){
                row = 0;
                randC = randGen.nextInt(levelFactory.numOfColumns);
                index = randC;
            }
        }

        Ball ball;
        if (powerTypeAt == 2){
            ball = spawners[randC].spawnSpecificBall(power2BallNum);
            if (powerTimePanel != null && powerTimePanel.updateBallsLeft()){
                powerTypeAt = -1;
                if (powerTimePanel != null){
                    removePowerTimePanel();
                }
            }
        }else {
            ball = spawners[randC].spawnBall();
        }

        ball.finalYPosition = firstY + (yOffsetPA + ball.getHeight())*row;
        ball.row = row;
        ball.delegate = this;
        if (powerTypeAt == 1){
            ball.velocity.set(0, levelFactory.velocity * (-.5f));
        }else {
            ball.velocity.set(0, levelFactory.velocity * (-1f));
        }

        ball.isPhysicsActive = true;
        world.addBody(ball);
        ballsArray[index] = ball;
        movingBallList.addToEnd(ball);

        if (ball.isPowerBall){
            powerMaxAmount++;
        }

        ballGroup.addActor(ball);

        if (levelFactory.gameType == 2){
            objectivePanel.ballsLeft--;
            if (objectivePanel.ballsLeft == nextBallChange){
                levelFactory.changeSpeedAndDrop();
                nextBallChange = objectivePanel.ballsLeft - levelFactory.changeSpeedBNum;
            }
        }
    }

    private void pauseGame() {

        pausedGame = true;
        movingBallList.gamePaused();
        removeGameplay();
        optionPanel.setTouchable(Touchable.disabled);
        creatingSettingPanel();

    }

    private void creatingSettingPanel(){

        AlphaAction alphaAction = Actions.alpha(.3f);
        playArea.addAction(alphaAction);
        AlphaAction alph = Actions.alpha(.3f);
        ceiling.addAction(alph);

        if ( !isSettingCreated){

            for (Ball ball: powerSidePanel.powerBalls){
                ball.setTouchable(Touchable.disabled);
            }

            settingPanel = new SettingPanel(gameSceneAtlas.createSprite("gameOverArea"));
            settingPanel.socialMediaAtlas = socialMediaAtlas;
            settingPanel.buttonAtlas = buttonAtlas;
            settingPanel.infoAtlas = infoAtlas;
            settingPanel.setPosition((int)settingPosition.x, (int)settingPosition.y);
            settingPanel.gameType = levelFactory.gameType;


            if (levelFactory.gameType == 1) {
                settingPanel.objectiveLeft = (int)levelFactory.gameTime;
            }else{
                settingPanel.objectiveLeft = levelFactory.numberOfBalls;
            }
//
            settingPanel.targetScore = levelFactory.targetScore;
            settingPanel.delegate = this;

            if (stageAt == 1){
                //settingPanel.createIntroPanel(levelID);
                addActor(settingPanel);
            }else if (stageAt == 2){

                settingPanel.createPausePanel();
                addActor(settingPanel);

            }else if(stageAt == 3){

                if (ceilingHit){



                    int hitIndex = hitBall.column - levelFactory.numOfColumns + (levelFactory.numOfColumns * hitBall.row);

                    for (int i = hitIndex; i>= 0; i = i -levelFactory.numOfColumns){
                        AlphaAction in = Actions.alpha(.5f, .8f);
                        AlphaAction out = Actions.alpha(1f,.8f);
                        SequenceAction seq = Actions.sequence(in,out);
                        RepeatAction repeat = Actions.repeat(3,seq);
                        ballsArray[i].addAction(repeat);
                    }

                    AlphaAction in = Actions.alpha(.5f, .8f);
                    AlphaAction out = Actions.alpha(1f,.8f);
                    SequenceAction seq = Actions.sequence(in,out);
                    RepeatAction repeat = Actions.repeat(3,seq);
                    Action complete = new Action() {
                        @Override
                        public boolean act(float delta) {
                            if (!didReachScore){
                                scorePanel.didNotReachAnimation();
                                didWin = false;
                            }

                            if (objectiveReached && didReachScore){
                                didWin = true;
                            }

                            settingPanel.createGameOverPanel(didWin);
                            addActor(settingPanel);


                            return true;
                        }
                    };

                    hitBall.addAction(Actions.sequence(repeat, complete));

                }else{

                    if (!didReachScore){
                        scorePanel.didNotReachAnimation();
                        didWin = false;
                    }

                    if (objectiveReached && didReachScore){
                        didWin = true;
                    }

                    settingPanel.createGameOverPanel(didWin);
                    addActor(settingPanel);
                }
            }
            isSettingCreated = true;
        }

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

        AlphaAction alphaAction = Actions.alpha(1f);
        playArea.addAction(alphaAction);
        AlphaAction alph = Actions.alpha(1f);
        ceiling.addAction(alph);

        if (levelFactory.gameType == 1){
            objectivePanel.futureTime = System.currentTimeMillis()/1000 + objectivePanel.time;
        }
        pausedGame = false;
        if (powerTimePanel != null) {
            powerTimePanel.targetTime = powerTimePanel.currentTime + powerTimePanel.time;
        }

        for (Ball ball: powerSidePanel.powerBalls){
            if (ball.notiNode != null){
                ball.setTouchable(Touchable.enabled);
            }
        }

        createAllBallsArray();
        movingBallList.gameResumed();
        nextTime = currentTime + deltaTime;

    }

    /** Creates and addes the balls in the ball array*/
    private void createAllBallsArray(){

        for (Ball aBallsArray : ballsArray) {
            if (aBallsArray != null) {

                if (!aBallsArray.hasParent()) {
                    ballGroup.addActor(aBallsArray);
                }

                aBallsArray.setAlpha(1);
                aBallsArray.setTouchable(Touchable.enabled);
            }
        }
    }

    /** Updates all the balls*/
    private void updateBallPosition(){

        for (Ball aBallsArray : ballsArray) {
            if (aBallsArray != null) {
                calculateNewPosition(aBallsArray);
            }
        }
    }

    /** Calculates the new ball positions*/
    private void calculateNewPosition(Ball ball){

        int rowBelow = ball.row - 1;
        int belowIndex = ball.column + levelFactory.numOfColumns*rowBelow;
        boolean didGo = false;

        while ( (belowIndex >= 0 && ballsArray[belowIndex] == null)){
            rowBelow--;
            belowIndex = ball.column + levelFactory.numOfColumns*rowBelow;
            didGo = true;
        }

        if (didGo){
            rowBelow++;
            belowIndex = ball.column + levelFactory.numOfColumns*rowBelow;
            int oldIndex = ball.column + levelFactory.numOfColumns*ball.row;
            ball.row = rowBelow;
            //ball.updateName();
            ball.finalYPosition = firstY + (yOffsetPA + ball.getHeight())*ball.row;
            ballsArray[oldIndex] = null;
            ballsArray[belowIndex] = ball;

            if (!ball.isInMovingList){
                movingBallList.addToFront(ball);
                ball.velocity.set(0, levelFactory.velocity * -1);
                ball.isPhysicsActive = true;
                world.addBody(ball);
            }
        }
    }

    private  void changeRandomBallColor(){

    }

    private void removePowerTimePanel(){

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

    @Override
    public void quitButtonPressed() {

    }

    @Override
    public void resumeButtonPressed() {

    }

    @Override
    public void startNextLevel() {

    }

    @Override
    public void restartButtonPressed() {

    }

    @Override
    public void continuePlaying() {

    }

    private class ScreenListener extends InputListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            if (!clickedBegin){

                if (levelFactory.gameType == 1){
                    objectivePanel.futureTime = (float)(System.currentTimeMillis()/1000 + levelFactory.gameTime);
                }

                clickedBegin = true;
                stageAt = 2;
                removeSettingPanel();

                nextTime = currentTime + 1.0/levelFactory.dropRate;
                nextColorTime = currentTime + levelFactory.changeColorTime;
                nextSpeedTime = currentTime + levelFactory.changeSpeedTime;

            }
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {


        }
    }

    public interface GameSceneDelegate{
        public void quitGamePlay();
        public void beginNextLevel(int level);
    }

}
