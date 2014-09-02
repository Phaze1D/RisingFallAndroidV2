package com.Phaze1D.RisingFallAndroidV2.Actors;

import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.PowerSidePanel;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

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

    public Vector2 startPoint = new Vector2();
    public Vector2 endPoint = new Vector2();
    public Vector2 midPoint = new Vector2();
    public final Vector2 velocity = new Vector2();
    public final Vector2 initPosition = new Vector2();

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
        addListener(new BallListener(this));
    }



    public boolean isAtFinalPosition(){

        if (getY() < finalYPosition){
            isPhysicsActive = false;
            setPosition((int)getX(), (int)finalYPosition);
            return true;
        }

        return false;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition((int)x,(int)y);
    }

    public void changeColor(int ballColor, TextureAtlas ballAtlas){

        Sprite newColor = ballAtlas.createSprite("ball"+ballColor);
        this.ballColor = ballColor;
        isUnMovable = false;
       setDrawable(new SpriteDrawable(newColor));

    }

    public void doubleClicked(){
        isDoubleBall = true;
        setDrawable(new SpriteDrawable(doubleSprite));
    }

    public int calculateDirection(Vector2 point2){
        float distance = fcalculateDirection(point2);
        float y = point2.y - startPoint.y;
        float x = point2.x - startPoint.x;
        float angle = (float)Math.asin(y/distance);

        if (Math.abs(angle) > Math.PI/4){

            if (y > 0){
                return 1;
            }else {
                return -1;
            }

        }else {
            if (x > 0){
                return 2;
            }else {
                return -2;
            }
        }

    }

    public float fcalculateDirection(){
        float x = endPoint.x - startPoint.x;
        float y = endPoint.y - startPoint.y;
        float distance = (float)(Math.pow(x,2) + Math.pow(y,2));

        return (float) Math.sqrt(distance);

    }

    public float fcalculateDirection(Vector2 endPoint){
        float x = endPoint.x - startPoint.x;
        float y = endPoint.y - startPoint.y;
        float distance = (float)(Math.pow(x,2) + Math.pow(y,2));

        return (float) Math.sqrt(distance);
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


    private class BallListener extends InputListener {

        private Ball ball;

        public BallListener(Ball ball){
            this.ball = ball;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            startPoint.set(x, y);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            if (isPowerBall){
                if (ball.getParent() instanceof PowerSidePanel){
                    Player playerInfo = Player.shareInstance();
                    playerInfo.decreasePower(ball.powerType);
                    int amount = playerInfo.getPowerAmount(ball.powerType);
                    if (amount > 0 ){
                        if(ball.notiNode.getChildren().get(0).getClass().isInstance(Label.class)){
                            Label label = (Label) ball.notiNode.getChildren().get(0);
                            label.setText(amount+"");
                        }else{
                            Label label = (Label) ball.notiNode.getChildren().get(1);
                            label.setText(amount+"");
                        }
                    }else {
                        ball.notiNode.clear();
                        ball.notiNode.remove();
                        ball.notiNode = null;
                        ball.setAlpha(.3f);
                        ball.setTouchable(Touchable.disabled);
                    }
                }

                ball.delegate.powerBallTouch(ball);

            }else {
                if (!didMove){
                    ball.delegate.ballTaped(ball);
                }
            }

            didMove = false;

        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {

            if (!isPowerBall){
                midPoint.set(x, y);
                float distance = fcalculateDirection(midPoint);

                if (distance > getHeight() && !didMove){
                    didMove = true;
                    moveDirection = calculateDirection(midPoint);
                    if (!isUnMovable){
                        ball.delegate.ballMoved(ball, moveDirection);
                    }
                }
            }
        }
    }

    public interface BallDelegate{
        public void ballTaped(Ball ball);
        public void ballMoved(Ball ball, int direction);
        public void powerBallTouch(Ball ball);
    }
}
