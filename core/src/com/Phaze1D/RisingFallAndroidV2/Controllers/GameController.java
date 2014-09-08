package com.Phaze1D.RisingFallAndroidV2.Controllers;


import com.Phaze1D.RisingFallAndroidV2.Scenes.GameplayScene;
import com.Phaze1D.RisingFallAndroidV2.Scenes.LevelsScene;
import com.Phaze1D.RisingFallAndroidV2.Scenes.StartScene;
import com.Phaze1D.RisingFallAndroidV2.Scenes.StoreScene;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.Phaze1D.RisingFallAndroidV2.Singletons.TextureLoader;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

/**
 * Created by davidvillarreal on 8/13/14.
 * Rising Fall Android Version
 */
public class GameController extends Game implements StartScene.StartScreenDelegate, LevelsScene.LevelSceneDelegate, GameplayScene.GameSceneDelegate, StoreScene.StoreDelegate{

    private SpriteBatch batch;

    private ScalingViewport viewport;

    private boolean isCreated;

    private TextureLoader textureLoader;

    public GameController(SpriteBatch batch){
        this.batch = batch;
    }


    @Override
    public void create() {
        if (!isCreated) {

            Player.shareInstance();
            viewport = new ScalingViewport(Scaling.fill, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
            textureLoader = TextureLoader.shareTextureLoader();
            loadStartScreen();
            isCreated = true;
            BitmapFontSizer.sharedInstance();

        }
    }

    @Override
    public void pause() {
        super.pause();
        Player.savePlayer();
    }

    @Override
    public void resume() {
        super.resume();

    }

    @Override
    public void dispose() {
        super.dispose();
        Player.savePlayer();
        textureLoader.dispose();
        BitmapFontSizer.clear();
    }

    /** Loads the StartScreen data*/
    private void loadStartScreen(){

        setScreen(null);
        textureLoader.dispose();
        textureLoader.loadStartScreenAtlases();
        StartScene startScreen = new StartScene (viewport, batch);
        startScreen.startScreenAtlas =  textureLoader.getStartScreenAtlas();
        startScreen.buttonAtlas = textureLoader.getButtonAtlas();
        startScreen.ballsAtlas = textureLoader.getBallsAtlas();
        startScreen.socialMediaAtlas = textureLoader.getSocialMediaAtlas();
        startScreen.delegate = this;
        setScreen(startScreen);

    }

    /** Loads the LevelScreen*/
    private void loadLevelScreen(){
        setScreen(null);
        textureLoader.dispose();
        textureLoader.loadLevelsScreenAtlases();
        LevelsScene levelsScene = new LevelsScene();
        levelsScene.buttonAtlas = textureLoader.getButtonAtlas();
        levelsScene.sceneAtlas = textureLoader.getLevelsScreenAtlas();
        levelsScene.delegate = this;
        setScreen(levelsScene);


    }

    private void loadGameplayScreen(int levelID){

        setScreen(null);
        textureLoader.dispose();
        textureLoader.loadGamePlayScreenAtlases(levelID);
        GameplayScene gameplayScreen = new GameplayScene(levelID);
        gameplayScreen.badBallAtlas = textureLoader.getBadBallAtlas();
        gameplayScreen.ballAtlas = textureLoader.getBallsAtlas();
        gameplayScreen.gameSceneAtlas = textureLoader.getGameplayAtlas();
        gameplayScreen.buttonAtlas = textureLoader.getButtonAtlas();
        gameplayScreen.unMovableAtlas = textureLoader.getUnmovableBallAtlas();
        gameplayScreen.powerBallAtlas = textureLoader.getPowerBallAtlas();
        gameplayScreen.socialMediaAtlas = textureLoader.getSocialMediaAtlas();
        gameplayScreen.infoAtlas = textureLoader.getInfoAtlas();
        gameplayScreen.delegate = this;
        setScreen(gameplayScreen);


    }

    private void loadStoreScreen(){
        setScreen(null);
        textureLoader.dispose();
        textureLoader.loadStoreScreenAtlases();
        StoreScene storeScene = new StoreScene();
        storeScene.sceneAtlas = textureLoader.getStoreAtlas();
        storeScene.buttonAtlas = textureLoader.getButtonAtlas();
        storeScene.ballsAtlas = textureLoader.getBallsAtlas();
        storeScene.delegate = this;
        setScreen(storeScene);


    }

    @Override
    public void playButtonPressed() {
        loadLevelScreen();
    }

    @Override
    public void storeButtonPressed() {
        loadStoreScreen();
    }

    @Override
    public void navigationPressed() {
        loadStartScreen();
    }

    @Override
    public void beginGamePlay(int levelID) {
        loadGameplayScreen(levelID);
    }

    @Override
    public void quitGamePlay() {
        loadStartScreen();
    }

    @Override
    public void beginNextLevel(int level) {
        loadGameplayScreen(level);
    }

    @Override
    public void storeBackPressed() {
        loadStartScreen();
    }
}
