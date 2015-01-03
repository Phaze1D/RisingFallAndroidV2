package com.Phaze1D.RisingFallAndroidV2.Singletons;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Created by david on 8/20/2014.
 * Rising Fall Android Version
 */
public class BitmapFontSizer {

    private static Hashtable<Integer, BitmapFont> fontTable = new Hashtable<Integer, BitmapFont>();
    private static Hashtable<String, BitmapFont>forenTable = new Hashtable<String, BitmapFont>();

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
    public static synchronized BitmapFont getFontWithSize(int size, String word){
    	
    	String laCode = Locale.getDefault().getLanguage();
    	
    	if(word != null){
    		word = getUniqueLetters(word);
    	}
    	
    	
    	
    	

		if (laCode.equalsIgnoreCase("ja") && word != null) {
			BitmapFont font = forenTable.get(word);

			if (font != null) {
				return font;
			} else {
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
						Gdx.files.internal("SupportFiles/fontTest.otf"));
				FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
				parameter.characters = word;
				parameter.size = size;

				font = generator.generateFont(parameter);
				forenTable.put(word, font);
				generator.dispose();
				return font;
			}

		} else if (laCode.equalsIgnoreCase("zh") && word != null) {
			BitmapFont font = forenTable.get(word);

			if (font != null) {
				return font;
			} else {
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
						Gdx.files.internal("SupportFiles/fontTest.otf"));
				FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
				parameter.characters = word;
				parameter.size = size;

				font = generator.generateFont(parameter);
				forenTable.put(word, font);
				generator.dispose();
				return font;
			}

		} else if (laCode.equalsIgnoreCase("ko") && word != null) {

			BitmapFont font = forenTable.get(word);

			if (font != null) {
				return font;
			} else {
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
						Gdx.files
								.internal("SupportFiles/AppleSDGothicNeo-Bold.otf"));
				FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
				parameter.characters = word;
				parameter.size = size;

				font = generator.generateFont(parameter);
				forenTable.put(word, font);
				generator.dispose();
				return font;
			}

		} else if (laCode.equalsIgnoreCase("ru") && word != null) {
			BitmapFont font = forenTable.get(word);

			if (font != null) {
				return font;
			} else {
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
						Gdx.files.internal("SupportFiles/GenevaCY.dfont"));
				FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
				parameter.characters = word;
				parameter.size = size;

				font = generator.generateFont(parameter);
				forenTable.put(word, font);
				generator.dispose();
				return font;
			}
    		
    	}else{
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
    	
    	
        
    }
    
    private static String getUniqueLetters(String word){
    	
    	char[] array = word.toCharArray();
    	ArrayList<Character> newArray = new ArrayList<Character>();
    	
    	
    	
    	for(int i = 0; i < word.length(); i++){
    		
    		char checking = array[i];
    		boolean isunque = true;
    		
    		for(int j = 0; j < newArray.size(); j++){
    			if(checking == newArray.get(j)){
    				isunque = false;
    			}
    		}
    		
    		if(isunque){
    			newArray.add(checking);
    		}
    	}
    	
    	char[] finalArray = new char[newArray.size()];
    	for(int i = 0; i < newArray.size(); i++){
    		finalArray[i] = newArray.get(i);
    	}
    	
    	
    	return new String(finalArray);
    }

    public static void clear(){
        fontTable.clear();
    }
    
    public float fontPowerTime(){
    	if (XXS) {
            return 4*2f;
        }else if(XS){
            return 8*2f;
        }else if(S){
            return 11*2f;
        }else if(M){
            return 15*2f;
        }else if(H){
            return 19*2f;
        }else if(XH){
            
        } 
        return 11;
    }
    
   
    public float fontPopEffect(){
    	 if (XXS) {
             return 8*2f;
         }else if(XS){
             return 11*2f;
         }else if(S){
             return 15*2f;
         }else if(M){
             return 19*2f;
         }else if(H){
             return 25*2f;
         }else if(XH){
             
         } 
         return 14;
    }
    
    public float fontButtonL(){
        
        if (XXS) {
            return 7*2f;
        }else if(XS){
            return 10*2f;
        }else if(S){
            return 14*2f;
        }else if(M){
            return 18*2f;
        }else if(H){
            return 24*2f;
        }else if(XH){
            
        } 
        return 14;
    }

    public float fontButtonS(){
        
        if (XXS) {
            return 7*2f;
        }else if(XS){
            return 10*2f;
        }else if(S){
            return 14*2f;
        }else if(M){
            return 18*2f;
        }else if(H){
            return 24*2f;
        }else if(XH){
            
        }

       return 14;
    }

    public float fontGameOver(){
        if (XXS) {
            return 10*2f;
        }else if(XS){
            return 15*2f;
        }else if(S){
            return 20*2f;
        }else if(M){
            return 25*2f;
        }else if(H){
            return 33*2f;
        }else if(XH){
            
        }

        return 0;
    }

   public float fontGameplayLevelID(){
        if (XXS) {
            return 9*2f;
        }else if(XS){
            return 14*2f;
        }else if(S){
            return 18*2f;
        }else if(M){
            return 24*2f;
        }else if(H){
            return 30*2f;
        }else if(XH){
            
        }

        return 18;
    }

    public float fontGameWon(){
        if (XXS) {
            return 10*2f;
        }else if(XS){
            return 15*2f;
        }else if(S){
            return 20*2f;
        }else if(M){
            return 25*2f;
        }else if(H){
            return 33*2f;
        }else if(XH){
            
        }

        return 20;
    }

    public float fontIntroText(){
        if (XXS) {
            return 6.5f*2f;
        }else if(XS){
            return 9.5f*2f;
        }else if(S){
            return 13*2f;
        }else if(M){
            return 18*2f;
        }else if(H){
            return 21*2f;
        }else if(XH){
            
        }

        return 13;
    }

    public float fontKeepPlaying(){
        if (XXS) {
            return 10*2f;
        }else if(XS){
            return 15*2f;
        }else if(S){
            return 20*2f;
        }else if(M){
            return 25*2f;
        }else if(H){
            return 33*2f;
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
            return 8*2f;
        }else if(XS){
            return 12*2f;
        }else if(S){
            return 16*2f;
        }else if(M){
            return 20*2f;
        }else if(H){
            return 24*2f;
        }else if(XH){
            
        }

            return 16;
    }

    public float fontStoreInfo(){
        if (XXS) {
            return 8.5f*1.5f;
        }else if(XS){
            return 13*1.5f;
        }else if(S){
            return 17*1.5f;
        }else if(M){
            return 23*1.5f;
        }else if(H){
            return 29*1.5f;
        }else if(XH){
            
        }

        return 17;
    }

    public float fontStoreTitle(){
        if (XXS) {
            return 7.5f*2f;
        }else if(XS){
            return 10*2f;
        }else if(S){
            return 13*2f;
        }else if(M){
            return 16*2f;
        }else if(H){
            return 19*2f;
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
            return 8*2f;
        }else if(XS){
            return 12*2f;
        }else if(S){
            return 16*2f;
        }else if(M){
            return 20*2f;
        }else if(H){
            return 24*2f;
        }else if(XH){
            
        }

        return 16;
    }


}
