package com.Phaze1D.RisingFallAndroidV2.Controllers;

import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Hashtable;

public class ApplicationController extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameController gameController;
    

    public ApplicationController(){

    }

    public ApplicationController(Hashtable<String, String> localeStrings){
        LocaleStrings.getInstance(localeStrings);
    }


    @Override
    public void create () {
        batch = new SpriteBatch(200);
        gameController = new GameController(batch);

        gameController.create();

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
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
