package com.Phaze1D.RisingFallAndroidV2.Singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.Hashtable;

/**
 * Created by david on 8/20/2014.
 * Rising Fall Android Version
 */
public class BitmapFontSizer {

    private static Hashtable<Integer, BitmapFont> fontTable = new Hashtable<Integer, BitmapFont>();

    private static BitmapFontSizer ourInstance;

    private BitmapFontSizer(){

    }

    public synchronized static BitmapFontSizer sharedInstance(){
        if (ourInstance == null){
            ourInstance = new BitmapFontSizer();
        }
        return ourInstance;
    }



    /** Returns a BitmapFont scaled to a size*/
    public static synchronized BitmapFont getFontWithSize(int size){

        BitmapFont bitmapFont = fontTable.get(size);
        if (bitmapFont != null){
            return bitmapFont;
        }else {

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SupportFiles/CooperBlack.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            fontTable.put(size, generator.generateFont(parameter));
            generator.dispose();
        }

        return fontTable.get(size);
    }

    public static void clear(){
        fontTable.clear();
    }


}
