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
   
    private boolean XXS = false;
    private boolean XS = false;
    private boolean S = false;
    private boolean M = false;
    private boolean H = false;
    private boolean XH = false;
    
    private BitmapFontSizer(){
    	String size = TextureLoader.shareTextureLoader().screenSizeAlt;
    	
    	if(size.equalsIgnoreCase("XXS")){
    		XXS = true;
    	}else if(size.equalsIgnoreCase("XS")){
    		XS = true;
    	}else if(size.equalsIgnoreCase("S")){
    		S = true;
    	}else if(size.equalsIgnoreCase("M")){
    		M = true;
    	}else if(size.equalsIgnoreCase("H")){
    		H = true;
    	}else if(size.equalsIgnoreCase("XH")){
    		XH = true;
    	}
    	
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
    
    public float fontPowerTime(){
    	if (XXS) {
            return 4*1.5f;
        }else if(XS){
            return 8*1.5f;
        }else if(S){
            return 11*1.5f;
        }else if(M){
            return 15*1.5f;
        }else if(H){
            return 19*1.5f;
        }else if(XH){
            
        } 
        return 11;
    }
    
   
    public float fontPopEffect(){
    	 if (XXS) {
             return 8*1.5f;
         }else if(XS){
             return 11*1.5f;
         }else if(S){
             return 15*1.5f;
         }else if(M){
             return 19*1.5f;
         }else if(H){
             return 25*1.5f;
         }else if(XH){
             
         } 
         return 14;
    }
    
    public float fontButtonL(){
        
        if (XXS) {
            return 7*1.5f;
        }else if(XS){
            return 10*1.5f;
        }else if(S){
            return 14*1.5f;
        }else if(M){
            return 18*1.5f;
        }else if(H){
            return 24*1.5f;
        }else if(XH){
            
        } 
        return 14;
    }

    public float fontButtonS(){
        
        if (XXS) {
            return 7*1.5f;
        }else if(XS){
            return 10*1.5f;
        }else if(S){
            return 14*1.5f;
        }else if(M){
            return 18*1.5f;
        }else if(H){
            return 24*1.5f;
        }else if(XH){
            
        }

       return 14;
    }

    public float fontGameOver(){
        if (XXS) {
            return 10*1.5f;
        }else if(XS){
            return 15*1.5f;
        }else if(S){
            return 20*1.5f;
        }else if(M){
            return 25*1.5f;
        }else if(H){
            return 33*1.5f;
        }else if(XH){
            
        }

        return 0;
    }

   public float fontGameplayLevelID(){
        if (XXS) {
            return 9*1.5f;
        }else if(XS){
            return 14*1.5f;
        }else if(S){
            return 18*1.5f;
        }else if(M){
            return 24*1.5f;
        }else if(H){
            return 30*1.5f;
        }else if(XH){
            
        }

        return 18;
    }

    public float fontGameWon(){
        if (XXS) {
            return 10*1.5f;
        }else if(XS){
            return 15*1.5f;
        }else if(S){
            return 20*1.5f;
        }else if(M){
            return 25*1.5f;
        }else if(H){
            return 33*1.5f;
        }else if(XH){
            
        }

        return 20;
    }

    public float fontIntroText(){
        if (XXS) {
            return 6.5f*1.5f;
        }else if(XS){
            return 9.5f*1.5f;
        }else if(S){
            return 13*1.5f;
        }else if(M){
            return 18*1.5f;
        }else if(H){
            return 24*1.5f;
        }else if(XH){
            
        }

        return 13;
    }

    public float fontKeepPlaying(){
        if (XXS) {
            return 10*1.5f;
        }else if(XS){
            return 15*1.5f;
        }else if(S){
            return 20*1.5f;
        }else if(M){
            return 25*1.5f;
        }else if(H){
            return 33*1.5f;
        }else if(XH){
            
        }

        return 20;
    }

    public float fontLifePanelLifes(){
        if (XXS) {
            return 9.5f*1.5f;
        }else if(XS){
            return 15*1.5f;
        }else if(S){
            return 19*1.5f;
        }else if(M){
            return 24*1.5f;
        }else if(H){
            return 30*1.5f;
        }else if(XH){
            
        }

        return 19;
    }

    public float fontLifePanelTime(){
        if (XXS) {
            return 9.5f*1.5f;
        }else if(XS){
            return 15*1.5f;
        }else if(S){
            return 19*1.5f;
        }else if(M){
            return 24*1.5f;
        }else if(H){
            return 30*1.5f;
        }else if(XH){
            
        }

        return 19;
    }

    public float fontLifePanelTitle(){
        if (XXS) {
            return 9.5f*1.5f;
        }else if(XS){
            return 15*1.5f;
        }else if(S){
            return 19*1.5f;
        }else if(M){
            return 24*1.5f;
        }else if(H){
            return 30*1.5f;
        }else if(XH){
            
        }

        return 19;
    }

    public float fontPowerNoti(){
        if (XXS) {
            return 5.5f*1.5f;
        }else if(XS){
            return 8*1.5f;
        }else if(S){
            return 11*1.5f;
        }else if(M){
            return 13.5f*1.5f;
        }else if(H){
            return 20*1.5f;
        }else if(XH){
            
        }

        return 11;
    }

    public float fontScorePanel(){
        if (XXS) {
            return 8*1.5f;
        }else if(XS){
            return 12*1.5f;
        }else if(S){
            return 16*1.5f;
        }else if(M){
            return 22*1.5f;
        }else if(H){
            return 26*1.5f;
        }else if(XH){
            
        }

            return 16;
    }

    public float fontStoreInfo(){
        if (XXS) {
            return 8.5f*1.5f -3;
        }else if(XS){
            return 13*1.5f -3;
        }else if(S){
            return 17*1.5f-3;
        }else if(M){
            return 23*1.5f-3;
        }else if(H){
            return 29*1.5f-3;
        }else if(XH){
            
        }

        return 17;
    }

    public float fontStoreTitle(){
        if (XXS) {
            return 7.5f*1.5f;
        }else if(XS){
            return 11*1.5f;
        }else if(S){
            return 15*1.5f;
        }else if(M){
            return 21*1.5f;
        }else if(H){
            return 27*1.5f;
        }else if(XH){
            
        }

        return 15;
    }

    public float fontLevelButton(){
        if (XXS) {
            return 7*1.5f;
        }else if(XS){
            return 10*1.5f;
        }else if(S){
            return 14*1.5f;
        }else if(M){
            return 18*1.5f;
        }else if(H){
            return 24*1.5f;
        }else if(XH){
            
        }

        return 14;
    }

    public float fontObjectivePanel(){
        if (XXS) {
            return 8*1.5f;
        }else if(XS){
            return 12*1.5f;
        }else if(S){
            return 16*1.5f;
        }else if(M){
            return 22*1.5f;
        }else if(H){
            return 26*1.5f;
        }else if(XH){
            
        }

        return 16;
    }


}
