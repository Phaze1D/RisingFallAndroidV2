package com.Phaze1D.RisingFallAndroidV2.Objects;


import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Scenes.GameplayScene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;


/**
 * Created by david on 8/24/2014.
 * Rising Fall Android Version
 */
public class LinkedList {

    private NodeNew head;
    private NodeNew tail;

    public int count;

    private GameplayScene gameScene;

    public LinkedList (GameplayScene gameScene){
        this.gameScene = gameScene;

    }

    public synchronized void addToEnd(Ball ball){

        NodeNew node = new NodeNew(ball);
        node.element.isInMovingList = true;
        node.element.setTouchable(Touchable.disabled);

        if (count == 0){
            head = node;
        }else if (count == 1){
            tail = node;
            head.next = tail;
        }else {
            tail.next = node;
            tail = node;
        }

        count++;


    }

    public synchronized void addToFront(Ball ball){

        NodeNew node = new NodeNew(ball);
        node.element.isInMovingList = true;
        node.element.setTouchable(Touchable.disabled);

        if (count == 0){
            head = node;
        }else if (count == 1){
            tail = head;
            head = node;
            head.next = tail;
        }else {
            node.next = head;
            head = node;
        }

        count++;

    }

    public synchronized void checkIfReached(){

        NodeNew current = head;
        NodeNew previous = null;

        while (current != null){

            if (current.element.isAtFinalPosition()){

                if (current.element.row == gameScene.numRows - 1){
                    gameScene.disableBalls();
                    gameScene.ceilingHit = true;
                    gameScene.stageAt = 3;
                    gameScene.hitBall = current.element;
                }

                if (current == head) {
                    current.element.setTouchable(Touchable.enabled);
                    current.element.isInMovingList = false;
                    head = current.next;
                    current.next = null;
                    //current.element = null;
                    current = null;
                    current = head;
                    count--;
                }else if (current == tail){
                    current.element.setTouchable(Touchable.enabled);
                    current.element.isInMovingList = false;
                    previous.next = null;
                    //current.element = null;
                    current = null;
                    tail = previous;
                    count--;
                }else {
                    current.element.setTouchable(Touchable.enabled);
                    current.element.isInMovingList = false;
                    previous.next = current.next;
                    current.next = null;
                    //current.element = null;
                    current = null;
                    current = previous.next;
                    count--;
                }

            }else {
                previous = current;
                current = current.next;
            }
        }
    }

    public synchronized void removeAll(){

    }

    public synchronized void gamePaused(){
        NodeNew  current = head;

        while (current != null) {
            current.element.isPhysicsActive = false;
            current.element.time = 0;
            current = current.next;
        }

    }

    public synchronized void gameResumed(){
        NodeNew  current = head;

        while (current != null) {
            current.element.isPhysicsActive = true;
            gameScene.world.addBody(current.element);
            current = current.next;
        }

    }

    public synchronized void findNodeRemove(Ball ball){

        NodeNew current = head;
        NodeNew previous = null;

        while (current.element != ball && current != null){
            previous = current;
            current = current.next;
        }

        if (current != null){
            if (current == head){
                current.element.setTouchable(Touchable.enabled);
                current.element.isInMovingList = false;
                head = current.next;
                current.next = null;
                current.element = null;
                current = null;
                count--;
            }else if(current == tail){
                current.element.setTouchable( Touchable.enabled);
                current.element.isInMovingList = false;
                previous.next = null;
                current.element = null;
                current = null;
                tail = previous;
                count--;
            }else {
                current.element.setTouchable(Touchable.enabled);
                current.element.isInMovingList = false;
                previous.next = current.next;
                current.next = null;
                current.element = null;
                current = null;
                count--;
            }
        }
    }

    public synchronized void printList(){

        NodeNew current = head;

        while (current != null){
            Gdx.app.log("Tag", current + " ----> " + current.next);
            current = current.next;
        }

        Gdx.app.log("Tag", "------- " + count);
    }



    private class NodeNew{

        public NodeNew next;
        public Ball element;

        public NodeNew(Ball ball){
            this.element = ball;
        }

    }


}
