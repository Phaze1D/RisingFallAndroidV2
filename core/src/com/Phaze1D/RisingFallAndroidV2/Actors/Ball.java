package com.Phaze1D.RisingFallAndroidV2.Actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Group;
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
    public int moveDirection;

    public Vector2 startPoint;
    public Vector2 endPoint;
    public Vector2 velocity;

    public Sprite doubleSprite;

    public Group notiNode;

    public boolean isPowerBall;
    public boolean isInMovingList;
    public boolean isDoubleBall;
    public boolean isUnMovable;
    public boolean didMove;
    public boolean hasBeenChecked;

    public Body body;


    public Ball(Sprite mainSprite) {
        super(mainSprite);

    }


    public void setPhysicsBody(Body body){
        body.setUserData(this);
        body.setLinearDamping(0);
        body.setLinearVelocity(velocity);
        this.body = body;
    }


    public BodyDef ballDef(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(new Vector2(getX(), getY()));

        return bodyDef;

    }


    public interface BallDelegate{
        public void ballTaped(Ball ball);
        public void ballMoved(Ball ball, int direction);
        public void powerBallTouch(Ball ball);
    }
}
