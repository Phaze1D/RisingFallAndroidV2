package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by davidvillarreal on 9/3/14.
 * Rising Fall Android Version
 */
public class StoreBuyPanel extends Panel implements SimpleButton.SimpleButtonDelegate{

    public StoreBuyPanelDelegate delegate;

    public SimpleButton[] powerItem;

    private Label title;

    public TextureAtlas itemsAtlas;

    public StoreBuyPanel(Sprite panelSprite) {
        super(panelSprite);
    }


    public void createPanel(){
        powerItem = new SimpleButton[5];
        createTitle();
        createItems();
        createPosition();

    }

    public void createPosition(){
        float yOffset = (getHeight() - powerItem[0].getHeight()*5 - title.getHeight())/7;
        title.setPosition((int)(getWidth()/2 - title.getWidth()/2), (int)(getHeight() - yOffset - title.getHeight()));
        addActor(title);

        for (int i = 0 ; i < 5; i++){
            float yPosition = yOffset + ( yOffset + powerItem[0].getHeight())*i;
            powerItem[i].setPosition((int)(getWidth()/2 - powerItem[i].getWidth()/2), (int)(yPosition));
            addActor(powerItem[i]);
        }
    }


    private void createTitle(){

        title = new Label("ItemsK", new Label.LabelStyle(BitmapFontSizer.getFontWithSize(15), Color.BLACK));
        title.setAlignment(Align.center);
    }

    private void createItems(){

       for (int i = 0; i < 5; i++){
           SpriteDrawable spriteDrawable = new SpriteDrawable(itemsAtlas.createSprite("sp" + i));
           SimpleButton pbutton = new SimpleButton("", new ImageTextButton.ImageTextButtonStyle(spriteDrawable,null,null, BitmapFontSizer.getFontWithSize(11)));
           pbutton.type = i+9;
           pbutton.delegate = this;
           addActor(pbutton);
           powerItem[i] = pbutton;
       }

    }


    public void disableButton(){
        for (SimpleButton button: powerItem ){
            button.setTouchable(Touchable.disabled);
        }
    }

    public void enableButton(){
        for (SimpleButton button: powerItem ){
            button.setTouchable(Touchable.enabled);
        }
    }


    @Override
    public void buttonPressed(int type) {
        delegate.pButtonPressed(type -8);
    }

    public interface StoreBuyPanelDelegate{
        public void pButtonPressed(int powerTyped);
    }
}
