package com.Phaze1D.RisingFallAndroidV2.Objects;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;

/**
 * Created by davidvillarreal on 9/2/14.
 * Rising Fall Android Version
 */
public class PhysicsList {

    private NodeNew head;
    private NodeNew tail;

    public int count;

    public synchronized void addToEnd(Ball ball){

        NodeNew node = new NodeNew(ball);

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

    public synchronized void evaluatePhysics(float delta){
        NodeNew current = head;
        NodeNew previous = null;

        while (current != null){
            Ball ball = current.element;
            if (!ball.isPhysicsActive) {
                if (ball.getY() < 0 ){
                    ball.remove();
                }

                if (current == head) {
                    head = current.next;
                    current.next = null;
                    current = null;
                    current = head;
                    count--;
                }else if (current == tail){
                    previous.next = null;
                    current = null;
                    tail = previous;
                    count--;
                }else {

                    previous.next = current.next;
                    current.next = null;
                    current = null;
                    current = previous.next;
                    count--;
                }

            } else {
                ball.calculateNextPhysicalPosition(delta);
                previous = current;
                current = current.next;
            }


        }
        
    }

    private class NodeNew{

        public NodeNew next;
        public Ball element;

        public NodeNew(Ball ball){
            this.element = ball;
        }

    }
}
