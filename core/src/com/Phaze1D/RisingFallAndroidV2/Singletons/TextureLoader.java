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



    private TextureLoader(){
        assetManager = new AssetManager();
    }

    public static TextureLoader shareTextureLoader(){
        if (shareInstance == null){
            shareInstance = new TextureLoader();
        }

        return shareInstance;
    }

    /** Loads the start screen atlases into memory*/
    public void loadStartScreenAtlases(){
        assetManager.load("ButtonsAtlas/Buttons.atlas", TextureAtlas.class);
        assetManager.load("SocialMediaAtlas/SocialMediaArt.atlas", TextureAtlas.class);
        assetManager.load("StartScreenAtlas/StartScreen.atlas", TextureAtlas.class);
        assetManager.load("BallAtlas/Ball.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        startScreenAtlas = assetManager.get("StartScreenAtlas/StartScreen.atlas", TextureAtlas.class);
        socialMediaAtlas = assetManager.get("SocialMediaAtlas/SocialMediaArt.atlas", TextureAtlas.class);
        buttonAtlas = assetManager.get("ButtonsAtlas/Buttons.atlas", TextureAtlas.class);
        ballsAtlas = assetManager.get("BallAtlas/Ball.atlas", TextureAtlas.class);
        isStartScreenLoaded = true;
    }

    /** Loads the level screen atlases into memory*/
    public void loadLevelsScreenAtlases(){
        assetManager.load("LevelScreenAtlas/LevelScreen.atlas", TextureAtlas.class);
        assetManager.load("ButtonsAtlas/Buttons.atlas", TextureAtlas.class);

        while (!assetManager.update()){

        }

        buttonAtlas = assetManager.get("ButtonsAtlas/Buttons.atlas", TextureAtlas.class);
        levelsScreenAtlas = assetManager.get("LevelScreenAtlas/LevelScreen.atlas", TextureAtlas.class);
        isLevelScreenLoaded = true;
    }

    /** Loads the game play screen atlases into memory*/
    public void loadGamePlayScreenAtlases(int levelID){


        assetManager.load("ButtonsAtlas/Buttons.atlas", TextureAtlas.class);
        assetManager.load("GameplayAtlas/Gameplay.atlas", TextureAtlas.class);
        assetManager.load("BallAtlas/PowerBallArt.atlas", TextureAtlas.class);
        assetManager.load("BallAtlas/Ball.atlas", TextureAtlas.class);
        assetManager.load("BallAtlas/UnmovableBallArt.atlas",TextureAtlas.class);
        assetManager.load("BallAtlas/BadBallArt.atlas", TextureAtlas.class);
        assetManager.load("SocialMediaAtlas/SocialMediaArt.atlas", TextureAtlas.class);
        assetManager.load("InfoAtlas/Info"+levelID, TextureAtlas.class);
        assetManager.finishLoading();

        buttonAtlas = assetManager.get("ButtonsAtlas/Buttons.atlas", TextureAtlas.class);
        gameplayAtlas = assetManager.get("GameplayAtlas/Gameplay.atlas", TextureAtlas.class);
        powerBallAtlas = assetManager.get("BallAtlas/PowerBallArt.atlas", TextureAtlas.class);
        ballsAtlas = assetManager.get("BallAtlas/Ball.atlas", TextureAtlas.class);
        unmovableBallAtlas = assetManager.get("BallAtlas/UnmovableBallArt.atlas",TextureAtlas.class);
        badBallAtlas = assetManager.get("BallAtlas/BadBallArt.atlas", TextureAtlas.class);
        socialMediaAtlas = assetManager.get("SocialMediaAtlas/SocialMediaArt.atlas", TextureAtlas.class);
        infoAtlas = assetManager.get("InfoAtlas/Info"+levelID, TextureAtlas.class);
        isGameplayScreenLoaded = true;

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
        }



    }

    private void disposeStartMenuAssets(){
        startScreenAtlas.dispose();
        socialMediaAtlas.dispose();
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
