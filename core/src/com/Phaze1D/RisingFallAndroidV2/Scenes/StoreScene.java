package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.SellItemPanel;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.StoreBuyPanel;
import com.Phaze1D.RisingFallAndroidV2.Objects.Spawner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class StoreScene extends Stage implements Screen{

    private boolean isCreated;
    private boolean hasFinished;
    private boolean isSellCreated;

    public TextureAtlas sceneAtlas;
    public TextureAtlas buttonAtlas;

    private Spawner[] spawners;
    private LinkedList<Ball> ballQuene;

//    @property NSArray * productIdentifiers;
//    @property NSArray * products;

    private float deltaTime;
    private float passTime;
    private float spawnRate;

    private Vector2 velocity;
    private Vector2 sidePosition;
    private Vector2 backButtonPostion;
    private Vector2 initPointSellPanel;

    private StoreBuyPanel buyPanel;
    private SellItemPanel sellItemPanel;

    private SimpleButton backB;

//    @property SKProductsRequest * productsRequest;


    @Override
    public void render(float delta) {
        draw();
        act();
        if (hasFinished){

        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        if(!isCreated){
            Gdx.input.setInputProcessor(this);
            createScene();
            hasFinished = true;
        }

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }


    private void createScene(){

        initVariables();
        createBackground();
        createPositions();
        createSideView();
        createBackButton();
        validateProductsID();


    }

    private void initVariables() {

    }

    private void createBackground() {

    }

    private void createPositions() {

    }

    private void createSideView() {

    }

    private void createBackButton() {

    }

    private void validateProductsID() {

    }

}
