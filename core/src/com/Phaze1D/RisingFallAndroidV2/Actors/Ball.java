package com.Phaze1D.RisingFallAndroidV2.Actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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

    public Sprite doubleSprite;

    public Group notiNode;

    public boolean isPowerBall;
    public boolean isInMovingList;
    public boolean isDoubleBall;
    public boolean isUnMovable;
    public boolean didMove;
    public boolean hasBeenChecked;


    public Ball(Sprite mainSprite) {
        super(mainSprite);

    }





    public interface BallDelegate{
        public void ballTaped(Ball ball);
        public void ballMoved(Ball ball, int direction);
        public void powerBallTouch(Ball ball);
    }
}
