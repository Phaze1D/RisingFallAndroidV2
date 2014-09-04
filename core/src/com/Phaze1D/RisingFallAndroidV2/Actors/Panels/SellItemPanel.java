package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.Phaze1D.RisingFallAndroidV2.Singletons.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by davidvillarreal on 9/3/14.
 * Rising Fall Android Version
 */
public class SellItemPanel extends Panel implements SimpleButton.SimpleButtonDelegate {
    public TextArea textView;
    public int powerType;

    public TextureAtlas buttonAtlas;
    public TextureAtlas storeSceneAtlas;

    private LocaleStrings strings;

    public SellItemPanel(Sprite panelSprite) {
        super(panelSprite);
        strings = LocaleStrings.getOurInstance();
    }

    public void createPanel(int powerType, boolean isValidProduct){

        this.powerType = powerType;
        if (isValidProduct){
            createValidProductPanel();
        }else{
            createInvalidProductPanel();
        }
    }

    public void createTextArea(int powerType){

        int fontSize = 17;

//        NSString * key = [NSString stringWithFormat:@"PowerInfoK%d", powerType];
//        NSString * info = NSLocalizedString(key, nil);


        textView = new TextArea(strings.getValue("Infomation about the product that will be bought"), new TextField.TextFieldStyle(BitmapFontSizer.getFontWithSize(fontSize), Color.BLACK,null,null,null));
        textView.setSize((int)(getWidth()/1.5f), (int)(getHeight()/2));
        textView.setPosition( getWidth()/2 - textView.getWidth()/2, getHeight()/2 - textView.getHeight()/2);
        textView.setDisabled(true);
        textView.setTouchable(Touchable.disabled);
        addActor(textView);
    }


    private void createValidProductPanel(){

//        NSString * textureName = [NSString stringWithFormat:@"st%d",_powerType];

        Sprite powerItemSp = storeSceneAtlas.createSprite("st"+powerType);
        Sprite buyButtonSp = buttonAtlas.createSprite("buttonS1");
        float yOffset = (getHeight() - powerItemSp.getHeight() - buyButtonSp.getHeight())/4;

        Image titleNode = new Image(new SpriteDrawable(powerItemSp));
        titleNode.setPosition((int)(getWidth()/2 - titleNode.getWidth()/2), (int)(getHeight() - yOffset));
        addActor(titleNode);

        SpriteDrawable up = new SpriteDrawable(buyButtonSp);
        BitmapFont font = BitmapFontSizer.getFontWithSize(11);
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle(up, null, null, font);
        style.fontColor = Color.BLACK;

        SimpleButton buyButton = new SimpleButton(strings.getValue(".99K"),style );
        buyButton.setPosition((int)(getWidth()/2 - buyButton.getWidth()/2), (int)(yOffset - buyButton.getHeight()) );
        buyButton.delegate = this;
        addActor(buyButton);

    }

    private void createInvalidProductPanel(){

    }

    @Override
    public void buttonPressed(int type) {

        if (payButtonPressed()){
            Player player = Player.shareInstance();
            player.increasePower(powerType);
        }else {
            //Failed to pay
        }
    }


    private boolean payButtonPressed(){
        return true;
    }
}
