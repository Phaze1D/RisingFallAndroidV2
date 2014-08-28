package com.Phaze1D.RisingFallAndroidV2.Singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by david on 8/20/2014.
 * Rising Fall Android Version
 */
public class BitmapFontSizer {

    private static BitmapFont shareBitmapFont;

    private static BitmapFontSizer ourInstance;

    private BitmapFontSizer(){

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SupportFiles/CooperBlack.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        shareBitmapFont = generator.generateFont(parameter);
        generator.dispose();

    }



    /** Returns a BitmapFont scaled to a size*/
    public static synchronized BitmapFont getFontWithSize(int size){

        if (ourInstance == null){
            ourInstance = new BitmapFontSizer();
        }

        shareBitmapFont.setScale(1f);

        return shareBitmapFont;
    }


}
