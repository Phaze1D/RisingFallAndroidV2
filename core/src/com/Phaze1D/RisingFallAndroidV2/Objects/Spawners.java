package com.Phaze1D.RisingFallAndroidV2.Objects;

import com.Phaze1D.RisingFallAndroid.SpriteNodes.Ball;
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

    public Spawners(Vector2 spawnVelocity, Vector2 position, int column){
        this.velocity = spawnVelocity;
        this.position = position;
        this.column = column;
    }

    public Ball spawnBall(){


        RandomXS128 randGen = new RandomXS128();

        int powerTest = randGen.nextInt(100) + 1;

        if (powerTest <= powerUpProb && !stopSpawningPower){
            int powerType = randGen.nextInt(5) + 1;
            Ball ball = new Ball(powerAtlas.createSprite("powerBall"+powerType));
            ball.setPosition(position.x,position.y);
            ball.column = column;
            ball.isPowerBall = true;
            ball.powerType = powerType +1;
            ball.isPhysicsActive = true;
            ball.ballColor = -1;
            ball.setInitPosition(position);
            return ball;

        }else {
            int randIndex = randGen.nextInt(6);
            int dp = randGen.nextInt(100) + 1;
            int un = randGen.nextInt(100) + 1;

            if (dp < doubleBallProb && levelAt >= 18){
                Ball ball = new Ball(badBallAtlas.createSprite("badBall"+randIndex));
                ball.doubleSprite = badBallAtlas.createSprite("ball"+randIndex);
                ball.setPosition(position.x, position.y);
                ball.column = column;
                ball.ballColor = randIndex;
                ball.isDoubleBall = true;
                ball.isPhysicsActive = true;
                ball.setInitPosition(position);
                return ball;
            }

            if (un < unMovableProb && levelAt >= 50){
                Ball ball = new Ball(unBallAtlas.createSprite("unBall" + randIndex));
                ball.setPosition(position.x, position.y);
                ball.column = column;
                ball.ballColor = randIndex;
                ball.isUnMovable = true;
                ball.isPhysicsActive = true;
                ball.setInitPosition(position);
                return ball;
            }

            Ball ball = new Ball(ballAtlas.createSprite("ball"+randIndex));
            ball.setPosition(position.x, position.y);
            ball.column = column;
            ball.ballColor = randIndex;
            ball.isPhysicsActive = true;
            ball.setInitPosition(position);
            return ball;
        }
    }

    /** Spawns a specific ball color used in power 2*/
    public Ball spawnSpecificBall(int balltype){
        Ball ball = new Ball(ballAtlas.createSprite("ball"+balltype));
        ball.setPosition(position.x,position.y);
        ball.column = column;
        ball.ballColor = balltype;
        return ball;
    }
}
