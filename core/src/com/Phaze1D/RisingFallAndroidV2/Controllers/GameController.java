package com.Phaze1D.RisingFallAndroidV2.Controllers;


import java.util.Locale;

import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Scenes.GameplayScene;
import com.Phaze1D.RisingFallAndroidV2.Scenes.LevelsScene;
import com.Phaze1D.RisingFallAndroidV2.Scenes.StartScene;
import com.Phaze1D.RisingFallAndroidV2.Scenes.StoreScene;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
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
    
    public AdDelegate adDelegate;
    
    private CorePaymentDelegate paymentDelegate;
    
    private SoundControllerDelegate soundDelegate;
    
    public int stageAt = 0;

    public GameController(SpriteBatch batch, CorePaymentDelegate paymentDelegate, SoundControllerDelegate soundDelegate){
        this.batch = batch;
        this.paymentDelegate = paymentDelegate;
        this.soundDelegate = soundDelegate;
        String laCode = Locale.getDefault().getLanguage();
       
        
    }
    
    public CorePaymentDelegate getPaymentDelegate(){
    	return paymentDelegate;
    }


    @Override
    public void create() {
        if (!isCreated) {

            paymentDelegate.setPlayer(Player.shareInstance());
            viewport = new ScalingViewport(Scaling.fill, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
            textureLoader = TextureLoader.shareTextureLoader();
            textureLoader.selectScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        paymentDelegate = null;
    }

    /** Loads the StartScreen data*/
    public void loadStartScreen(){
    	adDelegate.currentScene(1);
    	if(isCreated){
    		adDelegate.showAd();
    	}
    	if(getScreen() != null){
    		getScreen().dispose();
    	}
        setScreen(null);
        textureLoader.dispose();
        textureLoader.loadStartScreenAtlases();
        StartScene startScreen = new StartScene (viewport, batch);
        startScreen.startScreenAtlas =  textureLoader.getStartScreenAtlas();
        startScreen.buttonAtlas = textureLoader.getButtonAtlas();
        startScreen.ballsAtlas = textureLoader.getBallsAtlas();
        startScreen.delegate = this;
        startScreen.soundDelegate = soundDelegate;
        stageAt = 0;
        setScreen(startScreen);
        
        

    }

    /** Loads the LevelScreen*/
    public void loadLevelScreen(){
    	getScreen().dispose();
    	 adDelegate.currentScene(2);
    	adDelegate.showAd();
        setScreen(null);
        textureLoader.dispose();
        textureLoader.loadLevelsScreenAtlases();
        LevelsScene levelsScene = new LevelsScene();
        levelsScene.corePaymentDelegate = paymentDelegate;
        levelsScene.buttonAtlas = textureLoader.getButtonAtlas();
        levelsScene.sceneAtlas = textureLoader.getLevelsScreenAtlas();
        levelsScene.delegate = this;
        levelsScene.soundDelegate = soundDelegate;
        stageAt = 1;
        setScreen(levelsScene);
       

    }

    public void loadGameplayScreen(int levelID){
    	getScreen().dispose();
    	adDelegate.currentScene(3);
    	adDelegate.hideAd();
        setScreen(null);
        textureLoader.dispose();
        textureLoader.loadGamePlayScreenAtlases(levelID);
        GameplayScene gameplayScreen = new GameplayScene(levelID);
        gameplayScreen.soundDelegate = soundDelegate;
        gameplayScreen.badBallAtlas = textureLoader.getBadBallAtlas();
        gameplayScreen.ballAtlas = textureLoader.getBallsAtlas();
        gameplayScreen.gameSceneAtlas = textureLoader.getGameplayAtlas();
        gameplayScreen.buttonAtlas = textureLoader.getButtonAtlas();
        gameplayScreen.unMovableAtlas = textureLoader.getUnmovableBallAtlas();
        gameplayScreen.powerBallAtlas = textureLoader.getPowerBallAtlas();
        gameplayScreen.socialMediaAtlas = textureLoader.getSocialMediaAtlas();
        gameplayScreen.infoAtlas = textureLoader.getInfoAtlas();
        gameplayScreen.delegate = this;
        gameplayScreen.corePaymentDelegate = paymentDelegate;
        stageAt = 2;
        setScreen(gameplayScreen);
        

    }

    public void loadStoreScreen(){
    	getScreen().dispose();
    	adDelegate.currentScene(4);
    	adDelegate.hideAd();
        setScreen(null);
        textureLoader.dispose();
        textureLoader.loadStoreScreenAtlases();
        StoreScene storeScene = new StoreScene();
        storeScene.sceneAtlas = textureLoader.getStoreAtlas();
        storeScene.buttonAtlas = textureLoader.getButtonAtlas();
        storeScene.ballsAtlas = textureLoader.getBallsAtlas();
        storeScene.itemsAtlas = textureLoader.getItemsAtlas();
        storeScene.delegate = this;
        storeScene.corePaymentDelegate = paymentDelegate;
        storeScene.soundDelegate = soundDelegate;
        stageAt = -1;
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
    
    public interface AdDelegate{
    	public void hideAd();
    	public void showAd();
    	public void currentScene(int sceneID);
    }
}
