package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SocialMediaButton;
import com.Phaze1D.RisingFallAndroidV2.Objects.Spawners;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class StartScene extends Stage implements Screen {

    public StartScreenDelegate delegate;


    public TextureAtlas startScreenAtlas;
    public TextureAtlas socialMediaAtlas;
    public TextureAtlas buttonAtlas;
    public TextureAtlas ballsAtlas;

    private boolean isCreated;
    private boolean isSocialSubCreated;
    private boolean hasFinishCreated;

    private Vector2 titlePosition;
    private Vector2 playButtonPosition;
    private Vector2 socialButtonPosition;
    private Vector2 storeButtonPosition;

    private Vector2[] socialSubPositions;
    private SocialMediaButton[] socialSubNodes;
    private Spawners[] spawners;
    private LinkedList<Ball> ballQuene;

    private SimpleButton playButton;
    private SimpleButton storeButton;

    private SocialMediaButton socialParent;

    private float socialSubAnimationDuration;
    private float spawnRate;

    private double deltaTime;
    private double passTime;

    private Vector2 velocity;


    public StartScene(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

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




    /** Start Screen Delegate*/
    public interface StartScreenDelegate{
        public void playButtonPressed();
        public void storeButtonPressed();
    }
}
