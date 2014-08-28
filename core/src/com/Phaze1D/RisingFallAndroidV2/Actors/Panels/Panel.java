package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by davidvillarreal on 8/26/14.
 * Rising Fall Android Version
 */
public class Panel extends WidgetGroup{

    public Image panelActor;

    protected Panel(Sprite panelSprite){

        panelActor = new Image(panelSprite);
        setSize(panelActor.getWidth(), panelActor.getHeight());
        //addActor(panelActor);
    }



}
