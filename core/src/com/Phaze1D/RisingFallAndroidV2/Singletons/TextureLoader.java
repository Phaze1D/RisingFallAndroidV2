package com.Phaze1D.RisingFallAndroidV2.Singletons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;


/**
 * Created by davidvillarreal on 8/13/14.
 * Rising Fall Android Version
 */
public class TextureLoader implements Disposable {


    private static TextureLoader shareInstance;

    private AssetManager assetManager;

    private TextureAtlas startScreenAtlas;
    private TextureAtlas socialMediaAtlas;
    private TextureAtlas buttonAtlas;
    private TextureAtlas ballsAtlas;
    private TextureAtlas levelsScreenAtlas;
    private TextureAtlas gameplayAtlas;
    private TextureAtlas powerBallAtlas;
    private TextureAtlas unmovableBallAtlas;
    private TextureAtlas badBallAtlas;
    private TextureAtlas storeAtlas;
    private TextureAtlas itemsAtlas;
    private TextureAtlas infoAtlas;


    private boolean isStartScreenLoaded;
    private boolean isLevelScreenLoaded;
    private boolean isGameplayScreenLoaded;
    private boolean isStoreScreenLoaded;
    
    public String screenSizeAlt;



    private TextureLoader(){
        assetManager = new AssetManager();
        
    }

    public static TextureLoader shareTextureLoader(){
        if (shareInstance == null){
            shareInstance = new TextureLoader();
        }

        return shareInstance;
    }
    
    public void selectScreenSize(float screenWidth, float screenHeight){
    	
    	  if(screenWidth >= 320 && screenHeight >= 480){
    	        screenSizeAlt = "XXS";
    	    }
    	    
    	    if(screenWidth >= 480 && screenHeight >= 800){
    	        screenSizeAlt = "XS";
    	    }

    	    if(screenWidth >= 640 && screenHeight >= 960){
    	        screenSizeAlt = "S";
    	    }

    	    if(screenWidth >= 750 && screenHeight >= 1200){
    	        screenSizeAlt = "M";
    	    }

    	    if(screenWidth >= 1080 && screenHeight >= 1700){
    	        screenSizeAlt = "H";
    	    }

//    	    if(screenWidth >= 1440 && screenHeight >= 2200){
//    	        screenSizeAlt = "XH";
//    	    }
    	
    }

    /** Loads the start screen atlases into memory*/
    public void loadStartScreenAtlases(){
        assetManager.load("Buttons/Buttons" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("StartMenuArt/StartMenuArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("AllBallArt/BallsArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.finishLoading();

        startScreenAtlas = assetManager.get("StartMenuArt/StartMenuArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        buttonAtlas = assetManager.get("Buttons/Buttons" + screenSizeAlt + ".atlas", TextureAtlas.class);
        ballsAtlas = assetManager.get("AllBallArt/BallsArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        isStartScreenLoaded = true;
    }

    /** Loads the level screen atlases into memory*/
    public void loadLevelsScreenAtlases(){
        assetManager.load("LevelsArt/LevelSceneArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("Buttons/Buttons" + screenSizeAlt + ".atlas", TextureAtlas.class);

        while (!assetManager.update()){

        }

        buttonAtlas = assetManager.get("Buttons/Buttons" + screenSizeAlt + ".atlas", TextureAtlas.class);
        levelsScreenAtlas = assetManager.get("LevelsArt/LevelSceneArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        isLevelScreenLoaded = true;
    }

    /** Loads the game play screen atlases into memory*/
    public void loadGamePlayScreenAtlases(int levelAt){

        if (levelAt <= 2 || levelAt == 18 || levelAt == 28 || levelAt == 50 || levelAt == 70) {

        }else{
            levelAt = 101;
        }


        assetManager.load("Buttons/Buttons" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("GameplayArt/GameplayArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("AllBallArt/PowerBallArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("AllBallArt/BallsArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("AllBallArt/UnmovableBallArt" + screenSizeAlt + ".atlas",TextureAtlas.class);
        assetManager.load("BallAtlas/BadBallArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("SocialMediaArt/SocialMediaArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        //assetManager.load("InfoAtlas/Info"+levelAt +".atlas", TextureAtlas.class);
        assetManager.finishLoading();

        buttonAtlas = assetManager.get("Buttons/Buttons" + screenSizeAlt + ".atlas", TextureAtlas.class);
        gameplayAtlas = assetManager.get("GameplayArt/GameplayArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        powerBallAtlas = assetManager.get("AllBallArt/PowerBallArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        ballsAtlas = assetManager.get("AllBallArt/BallsArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        unmovableBallAtlas = assetManager.get("AllBallArt/UnmovableBallArt" + screenSizeAlt + ".atlas",TextureAtlas.class);
        badBallAtlas = assetManager.get("BallAtlas/BadBallArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        socialMediaAtlas = assetManager.get("SocialMediaArt/SocialMediaArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        //infoAtlas = assetManager.get("InfoAtlas/Info"+levelAt+".atlas", TextureAtlas.class);
        isGameplayScreenLoaded = true;

    }

    public void loadStoreScreenAtlases(){
        assetManager.load("StoreArt/StoreArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("Buttons/Buttons" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.load("AllBallArt/BallsArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        assetManager.finishLoading();

        storeAtlas = assetManager.get("StoreArt/StoreArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        buttonAtlas = assetManager.get("Buttons/Buttons" + screenSizeAlt + ".atlas", TextureAtlas.class);
        ballsAtlas = assetManager.get("AllBallArt/BallsArt" + screenSizeAlt + ".atlas", TextureAtlas.class);
        isStoreScreenLoaded = true;

    }

    public TextureAtlas getStartScreenAtlas() {
        return startScreenAtlas;
    }

    public TextureAtlas getSocialMediaAtlas() {
        return socialMediaAtlas;
    }

    public TextureAtlas getButtonAtlas() {
        return buttonAtlas;
    }

    public TextureAtlas getBallsAtlas() {
        return ballsAtlas;
    }

    public TextureAtlas getLevelsScreenAtlas(){
        return levelsScreenAtlas;
    }

    public TextureAtlas getInfoAtlas() {
        return infoAtlas;
    }

    public TextureAtlas getGameplayAtlas() {
        return gameplayAtlas;
    }

    public TextureAtlas getPowerBallAtlas() {
        return powerBallAtlas;
    }

    public TextureAtlas getUnmovableBallAtlas() {
        return unmovableBallAtlas;
    }

    public TextureAtlas getStoreAtlas() {
        return storeAtlas;
    }

    public TextureAtlas getItemsAtlas() {
        return itemsAtlas;
    }

    public TextureAtlas getBadBallAtlas() {
        return badBallAtlas;
    }

    @Override
    public void dispose() {
        if (isStartScreenLoaded){
            disposeStartMenuAssets();
        }else if (isLevelScreenLoaded){
            disposeLevelScreenAssets();
        }else if (isGameplayScreenLoaded){
            disposeGamePlayScreenAssets();
        }else if(isStoreScreenLoaded){
            disposeStoreAssets();
        }



    }

    private void disposeStoreAssets(){
        storeAtlas.dispose();
        buttonAtlas.dispose();
        assetManager.clear();
        isStoreScreenLoaded = false;
    }

    private void disposeStartMenuAssets(){
        startScreenAtlas.dispose();
        buttonAtlas.dispose();
        ballsAtlas.dispose();
        assetManager.clear();
        isStartScreenLoaded = false;
    }

    private void disposeLevelScreenAssets(){
        buttonAtlas.dispose();
        levelsScreenAtlas.dispose();
        assetManager.clear();
        isLevelScreenLoaded = false;
    }

    private void disposeGamePlayScreenAssets(){
        buttonAtlas.dispose();
        badBallAtlas.dispose();
        gameplayAtlas.dispose();
        ballsAtlas.dispose();
        powerBallAtlas.dispose();
        unmovableBallAtlas.dispose();
        socialMediaAtlas.dispose();
        assetManager.clear();

        isGameplayScreenLoaded = false;
    }
}
