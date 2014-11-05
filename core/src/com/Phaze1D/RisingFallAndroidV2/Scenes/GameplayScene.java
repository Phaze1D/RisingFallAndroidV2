package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.CustomLabel;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.*;
import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Objects.LevelFactory;
import com.Phaze1D.RisingFallAndroidV2.Objects.LinkedList;
import com.Phaze1D.RisingFallAndroidV2.Objects.Spawner;
import com.Phaze1D.RisingFallAndroidV2.Physics.PhysicsWorld;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.Phaze1D.RisingFallAndroidV2.Singletons.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

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
    public int passEndTexture;

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

    public Image playArea;
    public Image endAnimationNode;

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
    public Vector2 infoAreaPosition;
    
    private RandomXS128 randGen = new RandomXS128();

    private Group ballGroup;
    private Group endAnimationGroup;
    
    public PhysicsWorld world;
    public CorePaymentDelegate corePaymentDelegate;

    public GameplayScene(int levelID){
        this.levelID = levelID;
        levelFactory = new LevelFactory(levelID);
    }

    @Override
    public void render(float delta) {
        act(delta);
        draw();
        


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
        	currentTime += delta;

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
                    nextTime = currentTime + 1/.5;
                }else{
                    nextTime = currentTime + 1.0/levelFactory.dropRate;
                }
            }

            if (powerTimePanel != null && powerTypeAt != 2 && powerTimePanel.updatetimer()){
                powerTypeAt = -1;
                if (powerTimePanel != null){
                    removePowerTimePanel();
                }
            }

            objectiveReached = objectivePanel.updateObjective(currentTime);

            if(levelFactory.gameType == 1 && objectivePanel.time <= 6 && !objectiveReached){
            	startAlmostFinishAnimation((int)objectivePanel.time);
            }
            
            if(levelFactory.gameType == 2 && objectivePanel.ballsLeft <= 5 && !objectiveReached){
            	startAlmostFinishAnimation(objectivePanel.ballsLeft);
            }
            
            if (objectiveReached && movingBallList.count == 0){
                stageAt = 3;
                didReachScore = scorePanel.didReachScore();
                pauseGame();
            }
            world.evaluatePhysics(delta);
        }



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
        deltaTime = nextTime - currentTime;
        pauseGame();
    }

    @Override
    public void resume() {

    }
    
    

    @Override
	public void dispose() {
		corePaymentDelegate = null;
		super.dispose();
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
        endAnimationGroup = new Group();
        ballGroup.setSize(getWidth(), getHeight());
        world = new PhysicsWorld();
        powerTypeAt = -1;
        powerMaxAmount = 0;
        playerInfo = Player.shareInstance();
        stageAt = 1;
        
        playAreaSprite = new Sprite(new Texture(Gdx.files.internal("PlayAreaCeilingArea/playArea" + levelFactory.ceilingHeight + TextureLoader.shareTextureLoader().screenSizeAlt + ".png")));
        maxColumns = 8;

        float ballWidth = ballAtlas.createSprite("ball0").getWidth();
        xOffsetPA = (playAreaSprite.getWidth() - ballWidth*maxColumns)/(maxColumns + 1);

        //float numTest = (playAreaSprite.getHeight() - xOffsetPA)/(xOffsetPA + ballWidth);
        switch (levelFactory.ceilingHeight){
            case 1:
                numRows = 13;
                break;
            case 2:
            case 3:
                numRows = 12;
                break;
            case 4:
            case 5:
                numRows = 11;
                break;
            case 6:
                numRows = 10;

        }
        //numRows =(int) Math.ceil(numTest);
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
        Sprite powerTimeTest = gameSceneAtlas.createSprite("playerTimeArea");
        Sprite optionTest = gameSceneAtlas.createSprite("optionArea");
        Sprite levelIDtest = gameSceneAtlas.createSprite("LevelIDArea");
        Sprite gameOverTest = gameSceneAtlas.createSprite("gameOverArea");
        Sprite ballTest = ballAtlas.createSprite("ball0");
        

        float playAreaWidth = playAreaSprite.getWidth();
        
        float xOffset = (this.getWidth() - playAreaWidth - optionTest.getWidth())/3;
        float yOffset = (this.getHeight() - playAreaSprite.getHeight() - powerAreaTest.getHeight())/2;
        
        playAreaPosition = new Vector2(xOffset, yOffset);
        optionAreaPosition = new Vector2(xOffset * 2 + playAreaWidth, (getHeight() - playAreaSprite.getHeight() - powerAreaTest.getHeight())/2);
        
        float topXOffset = (getWidth() - powerAreaTest.getWidth() - powerTimeTest.getWidth() - levelIDtest.getWidth() )/3;
        
        powerAreaPosition = new Vector2(topXOffset, yOffset* 2 + playAreaSprite.getHeight());
        powerTimePosition = new Vector2(topXOffset * 2 + powerAreaTest.getWidth(), yOffset* 2 + playAreaSprite.getHeight() );
        infoAreaPosition = new Vector2(topXOffset*3 + powerAreaTest.getWidth() + powerTimeTest.getWidth(), yOffset* 2 + playAreaSprite.getHeight());
        
        settingPosition = new Vector2(playAreaPosition.x + playAreaWidth/2 - gameOverTest.getWidth()/2, playAreaPosition.y  + playAreaSprite.getHeight()/2 - gameOverTest.getHeight()/2);
        objectivePosition = new Vector2(playAreaPosition.x, playAreaPosition.y + playAreaSprite.getHeight());
        scorePosition = new Vector2(playAreaPosition.x + playAreaWidth, playAreaPosition.y + playAreaSprite.getHeight());
        
        
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

    	
    	Image backboxes = new Image(gameSceneAtlas.createSprite("backBoxes"));
    	backboxes.setCenterPosition((int)getWidth()/2, (int)getHeight()/2);
    	addActor(backboxes);
    	
    }

    private void createPlayArea() {

        playArea = new Image(playAreaSprite);
        playArea.setPosition((int)playAreaPosition.x, (int)playAreaPosition.y);
        addActor(endAnimationGroup);
        addActor(playArea);
        addActor(ballGroup);


        createSpawners();
    }

    private void createSpawners(){
    	

        for (int i = 0; i < levelFactory.numOfColumns; i++){
            Spawner spawner = new Spawner();
            spawner.column = i;
            spawner.position = new Vector2(firstX + (xOffsetPA + ballAtlas.createSprite("ball0").getWidth())*i, playAreaPosition.y + playAreaSprite.getHeight() - ballAtlas.createSprite("ball0").getHeight()/1.5f);
            spawner.powerUpProb = levelFactory.powerBallDrop;
            spawner.doubleBallProb = levelFactory.doubleBallProb;
            spawner.unMovableProb = levelFactory.unMovableProb;
            spawner.ballAtlas = ballAtlas;
            spawner.powerAtlas = powerBallAtlas;
            spawner.badBallAtlas = badBallAtlas;
            spawner.unBallAtlas = unMovableAtlas;
            spawner.randGen = randGen;
            spawner.levelAt = levelID;
            spawners[i] = spawner;
        }

    }

    private void createSideView() {
        optionPanel = new SimpleButton("", new ImageTextButton.ImageTextButtonStyle(new SpriteDrawable(gameSceneAtlas.createSprite("optionArea")),null,null, BitmapFontSizer.getFontWithSize(11)));
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
        objectivePanel.setPosition((int)objectivePosition.x, (int)objectivePosition.y - objectivePanel.getHeight());
        objectivePanel.gameType = levelFactory.gameType;


        if (levelFactory.gameType == 1) {
            objectivePanel.time = levelFactory.gameTime;
            objectivePanel.ballsLeft = -1;
        }else{
            objectivePanel.ballsLeft = levelFactory.numberOfBalls;
            objectivePanel.time = -1;
        }
        objectivePanel.createPanel();
        addActor(objectivePanel);

        scorePanel = new ScorePanel(gameSceneAtlas.createSprite("scoreArea"));
        scorePanel.setPosition((int)scorePosition.x - scorePanel.getWidth(), (int)scorePosition.y - scorePanel.getHeight());
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
            ball.velocity.set(0, -levelFactory.velocity);
        }

        ball.isPhysicsActive = true;
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


        if ( !isSettingCreated){

            for (Ball ball: powerSidePanel.powerBalls){
                ball.setTouchable(Touchable.disabled);
            }

            settingPanel = new SettingPanel(gameSceneAtlas.createSprite("gameOverArea"));
            settingPanel.socialMediaAtlas = socialMediaAtlas;
            settingPanel.buttonAtlas = buttonAtlas;
            settingPanel.gameAtlas = gameSceneAtlas;
            settingPanel.infoAtlas = infoAtlas;
            settingPanel.setPosition((int)settingPosition.x, (int)settingPosition.y);
            settingPanel.gameType = levelFactory.gameType;


            if (levelFactory.gameType == 1) {
                settingPanel.objectiveLeft = (int)levelFactory.gameTime;
            }else{
                settingPanel.objectiveLeft = levelFactory.numberOfBalls;
            }
            settingPanel.targetScore = levelFactory.targetScore;
            settingPanel.delegate = this;

            if (stageAt == 1){
                settingPanel.createIntroPanel(levelID);
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
                        SequenceAction seq = Actions.sequence(out,in);
                        RepeatAction repeat = Actions.repeat(3,seq);
                        ballsArray[i].addAction(repeat);
                    }

                    AlphaAction in = Actions.alpha(.5f, .8f);
                    AlphaAction out = Actions.alpha(1f,.8f);
                    SequenceAction seq = Actions.sequence(out,in);
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

                            settingPanel.createGameOverPanel(didWin, corePaymentDelegate);
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

                    settingPanel.createGameOverPanel(didWin, corePaymentDelegate);
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
        settingPanel.corePaymentDelegate = null;
        settingPanel = null;
        optionPanel.setTouchable(Touchable.enabled);
        isSettingCreated = false;
        resumeGameplay();
    }

    private void resumeGameplay(){

        AlphaAction alphaAction = Actions.alpha(1f);
        playArea.addAction(alphaAction);


        if (levelFactory.gameType == 1){
            objectivePanel.futureTime = currentTime + objectivePanel.time;
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
            }
        }
    }

    private  void changeRandomBallColor(){

        RandomXS128 randGen = new RandomXS128();
        int randIndex = randGen.nextInt(ballsArray.length);

        for (int i = 0; i < 30; i++){
            if(ballsArray[randIndex] != null && !ballsArray[randIndex].isInMovingList && !ballsArray[randIndex].isDoubleBall && !ballsArray[randIndex].isPowerBall){
                int toColor = randGen.nextInt(6);
                ballsArray[randIndex].changeColor(toColor, ballAtlas);
                break;
            }else{
                randIndex = randGen.nextInt(ballsArray.length);
            }
        }
    }

    private void createPowerTimePanel(){

        powerTimePanel = new PowerTimePanel(gameSceneAtlas.createSprite("playerTimeArea"));
        powerTimePanel.setPosition((int)powerTimePosition.x, (int)getHeight());
        powerTimePanel.currentTime = (float)currentTime;
        powerTimePanel.powerType = powerTypeAt;
        powerTimePanel.powerBallAtlas = powerBallAtlas;
        MoveToAction move = Actions.moveTo((int)powerTimePosition.x, (int)powerTimePosition.y, .3f);
        addActor(powerTimePanel);
        powerTimePanel.addAction(move);

        if (powerTypeAt == 2){
            powerTimePanel.createPanelWithBalls();
        }else if (powerTypeAt > 0){
            powerTimePanel.createPanelWithTimer();
        }
        addActor(powerTimePanel);
    }

    private void removePowerTimePanel(){

        MoveToAction move = Actions.moveTo((int)powerTimePosition.x, (int)getHeight(), .3f);
        Action complete = new Action() {
            @Override
            public boolean act(float delta) {
                powerTimePanel.clear();
                powerTimePanel.remove();
                powerTimePanel = null;
                return true;
            }
        };

        powerTimePanel.addAction(Actions.sequence(move, complete));

    }

    public void disableBalls(){

//        for (int i = 0; i <_ballsArray.count; i++) {
//            if ([[_ballsArray objectAtIndex:i] isKindOfClass:[Ball class]]) {
//                ((Ball *)[_ballsArray objectAtIndex:i]).userInteractionEnabled = NO;
//            }
//        }
    	
    	

    }


    @Override
    public void ballTaped(Ball ball) {

        if (!ceilingHit){
            Array<Ball> connectedBalls = new Array<Ball>();
            connectionAlgo(ball, connectedBalls);

            for (Ball ball1: connectedBalls){
                ball1.hasBeenChecked = false;
            }

            if (connectedBalls.size >= 3){
                int score = (connectedBalls.size -2)*3;
                if (powerTypeAt == 5){
                    score *= 2;
                }

                runBallRemoveEffect(score, ball);

                scorePanel.updateScore(score);

                for (Ball cball: connectedBalls){
                    if (cball.isDoubleBall){
                        cball.doubleClicked();
                    }else {
                        cball.clear();
                        cball.remove();
                        int ballIndex = cball.column + levelFactory.numOfColumns*cball.row;
                        ballsArray[ballIndex] = null;
                    }
                }
                updateBallPosition();
            }
            connectedBalls.clear();
            connectedBalls = null;
        }

    }

    private void connectionAlgo(Ball ball, Array<Ball> connectedBalls){

        ball.hasBeenChecked = true;
        connectedBalls.add(ball);

        int ballIndex = ball.column + levelFactory.numOfColumns*ball.row;
        int rightIndex = ballIndex + 1;
        int leftIndex = ballIndex - 1;
        int upIndex = ballIndex + levelFactory.numOfColumns;
        int downIndex = ballIndex - levelFactory.numOfColumns;

        boolean rightBool = rightIndex % levelFactory.numOfColumns != 0 &&
                ballsArray[rightIndex] != null &&
                !ballsArray[rightIndex].isInMovingList &&
                ballsArray[rightIndex].ballColor == ball.ballColor &&
                !ballsArray[rightIndex].hasBeenChecked;

        if (rightBool){
            connectionAlgo(ballsArray[rightIndex], connectedBalls);
        }

        boolean leftBool = ballIndex % levelFactory.numOfColumns != 0 &&
                ballsArray[leftIndex] != null &&
                !ballsArray[leftIndex].isInMovingList &&
                ballsArray[leftIndex].ballColor == ball.ballColor &&
                !ballsArray[leftIndex].hasBeenChecked;

        if (leftBool){
            connectionAlgo(ballsArray[leftIndex], connectedBalls);
        }

        boolean upBool = upIndex < ballsArray.length &&
                ballsArray[upIndex] != null &&
                !ballsArray[upIndex].isInMovingList &&
                !ballsArray[upIndex].hasBeenChecked &&
                ballsArray[upIndex].ballColor == ball.ballColor;


        if (upBool){
            connectionAlgo(ballsArray[upIndex], connectedBalls);
        }

        boolean downBool = downIndex >= 0 &&
                !ballsArray[downIndex].hasBeenChecked &&
                ballsArray[downIndex].ballColor == ball.ballColor;

        if (downBool){
            connectionAlgo(ballsArray[downIndex], connectedBalls);
        }

    }

    private void runBallRemoveEffect(int score, Ball ball){

        final CustomLabel scoreLab = new CustomLabel("" + score, new Label.LabelStyle(BitmapFontSizer.getFontWithSize(15), Color.BLACK));
        scoreLab.setPosition(ball.getX() + ball.getWidth()/2, ball.getY() + ball.getHeight()/2);
        scoreLab.setScale(0);

        AlphaAction alphaAction = Actions.alpha(.4f, .5f);
        ScaleToAction scale = Actions.scaleTo(2,2,.5f);
        Action complete = new Action() {
            @Override
            public boolean act(float delta) {
                scoreLab.remove();
                return true;
            }
        };
        ParallelAction group = Actions.parallel(alphaAction, scale);
        scoreLab.addAction(Actions.sequence(group, complete));
        addActor(scoreLab);

    }

    @Override
    public void ballMoved(Ball ball, int direction) {

        int ballIndex = ball.column + levelFactory.numOfColumns*ball.row;

        if (direction == 1){
            int upBallIndex = ballIndex + levelFactory.numOfColumns;
            if (upBallIndex < ballsArray.length && ballsArray[upBallIndex] != null && !ballsArray[upBallIndex].isInMovingList && !ballsArray[upBallIndex].isUnMovable){
                switchBalls(ball, ballIndex, upBallIndex);
            }
        }else if (direction == -1){
            int downIndex = ballIndex - levelFactory.numOfColumns;
            if (downIndex >= 0 && !ballsArray[downIndex].isUnMovable){
                switchBalls(ball, ballIndex, downIndex);
            }
        }else if (direction == 2){
            int rightIndex = ballIndex + 1;
            if (rightIndex % levelFactory.numOfColumns != 0 && ballsArray[rightIndex] != null && !ballsArray[rightIndex].isInMovingList && !ballsArray[rightIndex].isUnMovable){
                switchBalls(ball,ballIndex,rightIndex);
            }
        }else if (direction == -2){
            int leftIndex = ballIndex - 1;
            if (ballIndex % levelFactory.numOfColumns != 0 && ballsArray[leftIndex] != null && !ballsArray[leftIndex].isInMovingList && !ballsArray[leftIndex].isUnMovable){
                switchBalls(ball,ballIndex,leftIndex);
            }
        }
    }

    private void switchBalls(Ball ball, int ballIndex, int tempIndex){

        Ball temp = ballsArray[tempIndex];
        int rowTemp = ball.row;
        int columnTemp = ball.column;
        ball.row = temp.row;
        ball.column = temp.column;
        temp.row = rowTemp;
        temp.column = columnTemp;
        ballsArray[ballIndex] = null;
        ballsArray[tempIndex] = ball;
        ballsArray[ballIndex] = temp;
        temp.clearActions();
        ball.clearActions();

        Vector2 tempPosition = new Vector2(ball.getX(), ball.getY());
        ball.addAction(Actions.moveTo((int)temp.getX(), (int)temp.getY(), .1f));
        temp.addAction(Actions.moveTo((int)tempPosition.x, (int)tempPosition.y, .1f));
    }


    @Override
    public void powerBallTouch(Ball ball) {

        int powerBallType = ball.powerType;

        switch (powerBallType){
            case 1:
                powerBallType1();
                break;
            case 2:
                powerBallType2();
                break;
            case 3:
                powerBallType3();
                break;
            case 4:
                powerBallType4();
                if (ball.getParent() == powerSidePanel){
                    updateBallPosition();
                }
                break;
            case 5:
                powerBallType5();
                break;
            default:
                break;
        }

        if (ball.getParent() == ballGroup){
            int ballIndex = ball.column + levelFactory.numOfColumns* ball.row;
            ball.clear();
            ball.remove();
            ballsArray[ballIndex] = null;
            updateBallPosition();
        }
    }

    private void powerBallType1(){

        powerTypeAt = 1;
        if (powerTimePanel != null){
            powerTimePanel.powerType = powerTypeAt;
            powerTimePanel.resetTimer();
        }else{
            createPowerTimePanel();
        }
    }

    private void powerBallType2(){

        powerTypeAt = 2;
        if (powerTimePanel != null){
            powerTimePanel.powerType = powerTypeAt;
            powerTimePanel.resetBalls();
        }else{
            createPowerTimePanel();
        }

        RandomXS128 randGen = new RandomXS128();
        power2BallNum = randGen.nextInt(6);

    }

    private void powerBallType3(){

        int ballFromColor = 0;
        int ballToColor = 0;

        for (Ball aBallsArray : ballsArray) {
            if (aBallsArray != null && !aBallsArray.isPowerBall) {
                ballFromColor = aBallsArray.ballColor;
                break;
            }
        }

        for (Ball aBallsArray : ballsArray) {
            if (aBallsArray != null && aBallsArray.ballColor != ballFromColor && !aBallsArray.isPowerBall) {
                ballToColor = aBallsArray.ballColor;
                break;
            }
        }

        for (Ball aBallsArray : ballsArray) {
            if (aBallsArray != null && !aBallsArray.isDoubleBall && aBallsArray.ballColor == ballFromColor && !aBallsArray.isPowerBall) {
                aBallsArray.changeColor(ballToColor, ballAtlas);
            }
        }

    }

    private void powerBallType4(){

        for (int i = 0; i < levelFactory.numOfColumns*2; i++){
            if (ballsArray[i] != null && !ballsArray[i].isInMovingList && !ballsArray[i].isPowerBall){
                Ball cball = ballsArray[i];
                cball.clear();
                cball.remove();

                ballsArray[i] = null;
            }
        }

        scorePanel.updateScore(levelFactory.numOfColumns *6);
    }

    private void powerBallType5(){

        powerTypeAt = 5;
        if (powerTimePanel != null){
            powerTimePanel.powerType = powerTypeAt;
            powerTimePanel.resetTimer();
        }else{
            createPowerTimePanel();
        }
    }

    @Override
    public void buttonPressed(int type) {
        pauseGame();
    }

    @Override
    public void quitButtonPressed() {

        if (didWin){
            if (playerInfo.levelAt < 100 && playerInfo.levelAt == levelID){
                playerInfo.levelAt++;
            }
        }else {
            playerInfo.livesLeft--;
            if (playerInfo.livesLeft == 0){
                playerInfo.calculateNextLifeTime();
            }
        }
        if (playerInfo.getPassedScore(levelID) < scorePanel.currentScore){
            playerInfo.setScore(levelID, scorePanel.currentScore);
        }

        delegate.quitGamePlay();


    }

    @Override
    public void resumeButtonPressed() {
        removeSettingPanel();
    }

    @Override
    public void startNextLevel() {

        if (playerInfo.getPassedScore(levelID) < scorePanel.currentScore){
            playerInfo.setScore(levelID, scorePanel.currentScore);
        }

        if (playerInfo.levelAt < 100 && playerInfo.levelAt == levelID){
            playerInfo.levelAt++;
        }

        if (levelID < 100){
            delegate.beginNextLevel(++levelID);
        }

    }

    @Override
    public void restartButtonPressed() {

        if (playerInfo.getPassedScore(levelID) < scorePanel.currentScore){
            playerInfo.setScore(levelID, scorePanel.currentScore);
        }

        playerInfo.livesLeft--;
        if (playerInfo.livesLeft == 0){
            playerInfo.calculateNextLifeTime();
            delegate.quitGamePlay();
        }else{
            delegate.beginNextLevel(levelID);
        }
    }

    @Override
    public void continuePlaying() {

        int midIndex = (int)ballsArray.length/2;

        for (int i = midIndex - 1; i < ballsArray.length; i++){
            if (ballsArray[i] != null){
                movingBallList.findNodeRemove(ballsArray[i]);
                ballsArray[i].clear();
                ballsArray[i].remove();
                ballsArray[i] = null;
            }
        }

        if (objectiveReached){
            if (levelFactory.gameType == 1){
                objectivePanel.time = 20;
            }else {
                objectivePanel.ballsLeft = 20;
            }
            objectiveReached = false;
        }

        if (ceilingHit && scorePanel.currentScore < scorePanel.targetScore){
            if (levelFactory.gameType == 1 && objectivePanel.time <= 10){
                objectivePanel.time = objectivePanel.time + 10;
            }else if(levelFactory.gameType == 2 && objectivePanel.ballsLeft <= 10){
                objectivePanel.ballsLeft = objectivePanel.ballsLeft + 10;
            }
        }

        if (ceilingHit){
            ceilingHit = false;
            hitBall = null;

        }

        stageAt = 2;

        scorePanel.titleLabel.clearActions();
        scorePanel.titleLabel.addAction(Actions.scaleTo(1,1));
        removeSettingPanel();
        pauseGame();
    }
    
    
    
    private void startAlmostFinishAnimation(int amount){
    	if(amount != passEndTexture){
    		if(amount == 5){
    			endAnimationNode = new Image(new SpriteDrawable(gameSceneAtlas.createSprite("end"+amount)));
    			endAnimationNode.setCenterPosition((int)(playAreaPosition.x + playArea.getWidth()/2), (int)(playAreaPosition.y + playArea.getHeight()/2));
    			endAnimationGroup.addActor(endAnimationNode);
    			
    		}else{
    			endAnimationNode.setDrawable(new SpriteDrawable(gameSceneAtlas.createSprite("end"+amount)));
    			
    		}
    		
    		passEndTexture = amount;
    	}
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
                    objectivePanel.futureTime = (currentTime + levelFactory.gameTime);
                }

                clickedBegin = true;
                stageAt = 2;
                removeSettingPanel();

                nextTime = currentTime;
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
