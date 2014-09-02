package com.Phaze1D.RisingFallAndroidV2.Physics;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Objects.PhysicsList;


/**
 * Created by davidvillarreal on 8/27/14.
 * Rising Fall Android Version
 */
public class PhysicsWorld{

    private PhysicsList bodies = new PhysicsList();
    public int constantStep;

    /** Adds a new body to the physics world*/
    public synchronized boolean addBody(Ball body){
        if (body.isPhysicsActive){
            body.initPosition.set(body.getX(), body.getY());
            body.time = 0;
            bodies.addToFront(body);
            return true;
        }else{
            return false;
        }
    }

    float t = 0;
    float dt = .001f;
    double accumulater = 0;

    public void evaluatePhysics(float frameTime){
        if(frameTime > .25){
            frameTime = .25f;
        }

        accumulater += frameTime;
        while (accumulater >= dt){
            integrate(dt);
            t+= dt;
            accumulater -= dt;
        }


    }

    private void integrate( float dt){
        bodies.evaluatePhysics(dt);
    }

    /** Dispose all the resources use by the world*/
    public void dispose(){


    }

}
