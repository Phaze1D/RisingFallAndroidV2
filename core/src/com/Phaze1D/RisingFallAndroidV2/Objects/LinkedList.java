package com.Phaze1D.RisingFallAndroidV2.Objects;


import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Scenes.GameplayScene;
import com.badlogic.gdx.Gdx;
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




    private class NodeNew{

        public NodeNew next;
        public Ball element;

        public NodeNew(Ball ball){
            this.element = ball;
        }

    }

}
