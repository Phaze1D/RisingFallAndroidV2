package com.Phaze1D.RisingFallAndroidV2.Physics;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by davidvillarreal on 8/27/14.
 * Rising Fall Android Version
 */
public class PhyiscsWorld {

    public LinkedList<Ball> bodies = new LinkedList<Ball>();

    public int constantStep;

    /** Adds a new body to the physics world*/
    public synchronized boolean addBody(Ball body){
        if (body.isPhysicsActive){
            body.initPosition.set(body.getX(), body.getY());
            body.time = 0;
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

    float t = 0;
    float dt = .001f;
    double accumulater = 0;

    public void evaluatePhysics(float frameTime){
        if(frameTime > .25){
            frameTime = .25f;
        }

        accumulater += frameTime;
        while (accumulater >= dt){
            integrate(t,dt);
            t+= dt;
            accumulater -= dt;
        }


    }

    private void integrate(float t, float dt){
        Iterator<Ball> iterator = bodies.iterator();

        while (iterator.hasNext()) {
            Ball next = iterator.next();
            if (!next.isPhysicsActive) {
                if (next.getY() < 0 ){
                    next.remove();
                }
                iterator.remove();
            } else {
                next.calculateNextPhysicalPosition(dt);
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
