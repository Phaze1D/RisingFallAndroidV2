package com.Phaze1D.RisingFallAndroidV2.Objects;


import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by davidvillarreal on 8/15/14.
 * Rising Fall Android Version
 */
public class Spawners {


    private Vector2 velocity;
    private Vector2 position;

    public int column;
    public int levelAt;

    public float powerUpProb;
    public float doubleBallProb;
    public float unMovableProb;

    public boolean stopSpawningPower;

    public TextureAtlas ballAtlas;
    public TextureAtlas powerAtlas;
    public TextureAtlas badBallAtlas;
    public TextureAtlas unBallAtlas;


}
