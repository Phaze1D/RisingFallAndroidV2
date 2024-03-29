package com.Phaze1D.RisingFallAndroidV2.Controllers;

import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Controllers.GameController.AdDelegate;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Hashtable;

public class ApplicationController extends ApplicationAdapter {
    private SpriteBatch batch;
    public GameController gameController;
    

    public ApplicationController(){

    }

    public ApplicationController(Hashtable<String, String> localeStrings, CorePaymentDelegate paymentDelegate, SoundControllerDelegate soundControllerDelegate){
        LocaleStrings.getInstance(localeStrings);
        
        gameController = new GameController(batch, paymentDelegate, soundControllerDelegate);
    }

    public void setAdDelegate(AdDelegate adDelegate){
    	if(gameController != null){
    		gameController.adDelegate = adDelegate;
    	}
    }

    @Override
    public void create () {
    	batch = new SpriteBatch(200);

        gameController.create();

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, .443f, .737f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameController.render();

    }

    @Override
    public void pause() {
        gameController.pause();
    }

    @Override
    public void resume() {
        gameController.resume();
    }

    @Override
    public void dispose() {
        gameController.dispose();
        batch.dispose();
    }
}
