package com.Phaze1D.RisingFallAndroidV2.Controllers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ApplicationController extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameController gameController;


    @Override
    public void create () {
        batch = new SpriteBatch(200);
        gameController = new GameController(batch);

        gameController.create();

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 1, 1);
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
