package com.Phaze1D.RisingFallAndroidV2.Controllers;


import com.Phaze1D.RisingFallAndroidV2.Scenes.StartScene;
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
public class GameController extends Game implements StartScene.StartScreenDelegate{

    private SpriteBatch batch;

    private ScalingViewport viewport;

    private boolean isCreated;

    private TextureLoader textureLoader;

    private Player player;

    public GameController(SpriteBatch batch){
        this.batch = batch;
    }


    @Override
    public void create() {
        if (!isCreated) {
            player = Player.shareInstance();
            viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
            textureLoader = TextureLoader.shareTextureLoader();
            loadStartScreen();
            isCreated = true;
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
        player = null;
        textureLoader.dispose();
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


    }

    private void loadGameplayScreen(int levelID){


    }

    @Override
    public void playButtonPressed() {

    }

    @Override
    public void storeButtonPressed() {

    }
}
