package com.Phaze1D.RisingFallAndroidV2.Physics;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by davidvillarreal on 8/27/14.
 * Rising Fall Android Version
 */
public class PhyiscsWorld {

    private LinkedList<Ball> bodies = new LinkedList<Ball>();

    public int constantStep;

    /** Adds a new body to the physics world*/
    public synchronized boolean addBody(Ball body){
        if (body.isPhysicsActive){
            bodies.add(body);
            return true;
        }else{
            return false;
        }
    }

    /** Removes a body from the physic world*/
    public synchronized boolean removeBody(Actor body){
        return bodies.remove(body);
    }

    /** Evaluates the physics of the world
     * @param deltaT is the time since last render
     * */
    public void evaluatePhysics(float deltaT){


        Iterator<Ball> iterator = bodies.iterator();

            while (iterator.hasNext()) {
                Ball next = iterator.next();
                if (!next.isPhysicsActive) {
                    next.remove();
                    iterator.remove();
                } else {
                    next.calculateNextPhysicalPosition(deltaT);
                }
            }
    }

    /** Dispose all the resources use by the world*/
    public void dispose(){
        for (Ball body: bodies){
            body.clear();
        }

        bodies.clear();
    }

}
