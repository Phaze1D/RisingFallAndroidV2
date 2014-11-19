package com.Phaze1D.RisingFallAndroidV2.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.RandomXS128;

/**
 * Created by davidvillarreal on 8/21/14.
 * Rising Fall Android Version
 */
public class LevelFactory {

    public float velocity;
    public float initFill;
    public float powerBallDrop;
    public float doubleBallProb;
    public float unMovableProb;
    public float dropRate;
    public float finalSpeed;
    public float finalDRate;
    public float changeRate;
    public float incrementS;
    public float incrementD;

    public double gameTime;
    public double changeColorTime;
    public double changeSpeedTime;

    public int changeSpeedBNum;
    public int ceilingHeight;
    public int numOfColumns;
    public int gameType;
    public int numberOfBalls;
    public int targetScore;
    public int gameObjective;
    
    private boolean velocityLess;
    private boolean velocityGreater;
    private boolean dropLess;
    private boolean dropGreater;



    public LevelFactory(int levelID){
        initVariables(levelID);
    }

    private void initVariables(int levelID){

        FileHandle file = Gdx.files.internal("SupportFiles/testing.txt");
        String fileString = file.readString();
        String[] lines = fileString.split("\\r?\\n");
        String[] levelLine = lines[levelID].split("\\s+");

        velocity = Float.valueOf(levelLine[2]);
        initFill = Float.valueOf(levelLine[9]);
        doubleBallProb = Float.valueOf(levelLine[10]);
        unMovableProb = Float.valueOf(levelLine[12]);
        dropRate = Float.valueOf(levelLine[3]);
        finalSpeed = Float.valueOf(levelLine[13]);
        finalDRate = Float.valueOf(levelLine[14]);
        changeRate = Float.valueOf(levelLine[15]);

        gameTime = Double.valueOf(levelLine[5]);
        changeColorTime = Double.valueOf(levelLine[11]);

        ceilingHeight = Integer.valueOf(levelLine[4]);
        numOfColumns = Integer.valueOf(levelLine[6]);
        gameType = Integer.valueOf(levelLine[1]);
        numberOfBalls = Integer.valueOf(levelLine[7]);
        targetScore = Integer.valueOf(levelLine[8]);
        gameObjective = Integer.valueOf(levelLine[0]);

        RandomXS128 randomGen = new RandomXS128();
        powerBallDrop = randomGen.nextInt(4);

        if(velocity > finalSpeed){
        	velocityGreater = true;
        	velocityLess = false;
        }else{
        	velocityGreater = false;
        	velocityLess = true;
        }
        
        if(dropRate > finalDRate){
        	dropGreater = true;
        	dropLess = false;
        }else{
        	dropGreater = false;
        	dropLess = true;
        }
        
        

        calculateChangeSpeedTime();
    }

    /** Calculate when the next change in speed time will happen */
    private void calculateChangeSpeedTime(){
        if (gameType == 1) {
            changeSpeedTime = gameTime/changeRate;
        }else if (gameType == 2){
            changeSpeedBNum = (int)(numberOfBalls / changeRate);
        }
        incrementS = (finalSpeed - velocity)/(changeRate - 1);
        incrementD = (finalDRate - dropRate)/(changeRate - 1);


    }

    /** Changes the speed and drop rate when called*/
    public void changeSpeedAndDrop(){
    	
    	if(velocityGreater){
    		System.out.println("GREATER THE VELOCITY ---------");
    		 if (velocity + incrementS > 0  && velocity >= finalSpeed) {
    	            velocity = velocity + incrementS;
    	            System.out.println("CHANGED THE VELOCITY ---------");
    	        }
    	}
    	
    	if(velocityLess){
    		System.out.println("LESS THE VELOCITY ---------");
    		 if (velocity + incrementS > 0  && velocity <= finalSpeed) {
 	            velocity = velocity + incrementS;
 	            System.out.println("CHANGED THE VELOCITY ---------");
 	        }
    	}
    	
    	if(dropGreater){
    		System.out.println("GREATER THE DROP ---------");
    		if (dropRate + incrementD > 0 && dropRate >= finalDRate) {
                dropRate = dropRate + incrementD;
                System.out.println("CHANGED THE DROP RATE -----------");
            }
    	}
    	
    	if(dropLess){
    		System.out.println("LESS THE DROP ---------");
    		if (dropRate + incrementD > 0 && dropRate <= finalDRate) {
                dropRate = dropRate + incrementD;
                System.out.println("CHANGED THE DROP RATE -----------");
            }
    	}


   

    }
}
