package com.Phaze1D.RisingFallAndroidV2.Objects;


import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


/**
 * Created by davidvillarreal on 8/15/14.
 * Rising Fall Android Version
 */
public class Spawner {

    public Vector2 position;

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

    public RandomXS128 randGen;

    public Ball spawnBall(){


        int powerTest = randGen.nextInt(100) + 1;


        if (powerTest <= powerUpProb && !stopSpawningPower){
            Array<Sprite> powerSprites = powerAtlas.createSprites();
            int powerType = randGen.nextInt(5);
            Ball ball = new Ball(powerSprites.get(powerType));
            ball.setPosition(position.x,position.y);
            ball.column = column;
            ball.isPowerBall = true;
            ball.powerType = powerType +1;
            ball.ballColor = -1;

            return ball;

        }else {
            int randIndex = randGen.nextInt(6);
            int dp = randGen.nextInt(100) + 1;
            int un = randGen.nextInt(100) + 1;

            if (dp < doubleBallProb && levelAt >= 18){
                Ball ball = new Ball(badBallAtlas.createSprite("badBall"+randIndex));
                ball.doubleSprite = ballAtlas.createSprite("ball"+randIndex);
                ball.setPosition(position.x, position.y);
                ball.column = column;
                ball.ballColor = randIndex;
                ball.isDoubleBall = true;
                return ball;
            }

            if (un < unMovableProb && levelAt >= 50){
                Ball ball = new Ball(unBallAtlas.createSprite("unBall" + randIndex));
                ball.setPosition(position.x, position.y);
                ball.column = column;
                ball.ballColor = randIndex;
                ball.isUnMovable = true;
                return ball;
            }

            Ball ball = new Ball(ballAtlas.createSprite("ball"+randIndex));
            ball.setPosition(position.x, position.y);
            ball.column = column;
            ball.ballColor = randIndex;
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
