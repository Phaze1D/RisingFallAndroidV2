package com.Phaze1D.RisingFallAndroidV2.Actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class Ball extends Image {

    public BallDelegate delegate;

    public int row;
    public int column;
    public int powerType;
    public int ballColor;

    public float finalYPosition;
    public float time;
    public int moveDirection;

    public Vector2 startPoint;
    public Vector2 endPoint;
    public Vector2 velocity;
    public Vector2 initPosition;

    public Sprite doubleSprite;

    public Group notiNode;

    public boolean isPowerBall;
    public boolean isInMovingList;
    public boolean isDoubleBall;
    public boolean isUnMovable;
    public boolean didMove;
    public boolean hasBeenChecked;
    public boolean isPhysicsActive;


    public Body body;


    public Ball(Sprite mainSprite) {
        super(mainSprite);

    }


    /** Calculates the next physical position on the screen depending on the current velocity*/
    public void calculateNextPhysicalPosition(float delta){
        time += delta;
        if (isPhysicsActive){
            int newX = (int)(initPosition.x + velocity.x * (time));
            int newY = (int)(initPosition.y+ velocity.y * (time));
            this.setPosition(newX,newY);
        }
    }

    public void setAlpha(float alpha){
        AlphaAction alphaAction = Actions.alpha(alpha);
        addAction(alphaAction);
    }

    public interface BallDelegate{
        public void ballTaped(Ball ball);
        public void ballMoved(Ball ball, int direction);
        public void powerBallTouch(Ball ball);
    }
}
