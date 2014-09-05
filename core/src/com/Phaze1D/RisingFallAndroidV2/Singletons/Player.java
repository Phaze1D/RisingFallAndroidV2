package com.Phaze1D.RisingFallAndroidV2.Singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;

/**
 * Created by davidvillarreal on 8/19/14.
 * Rising Fall Android Version
 */

public class Player implements Serializable{
    private static Player ourInstance;

    private int power1;
    private int power2;
    private int power3;
    private int power4;
    private int power5;
    public int levelAt;
    public int livesLeft;
    private int[] scores;

    public double timeLeftOnLifes;
    public double timeLeftOnSocialMedia;

    private Player() {
        setInitialConditions();
    }

    public synchronized static Player shareInstance() {

        if (ourInstance == null){
           if (Gdx.files.local("assets/SupportFiles/player.dat").exists()){
               ourInstance = readPlayer();
               if (ourInstance == null){
                   ourInstance = new Player();
               }
           }else {
               ourInstance = new Player();
           }
        }

        return ourInstance;
    }

    private void setInitialConditions(){
        power1 = 5;
        power2 = 5;
        power3 = 5;
        power4 = 5;
        power5 = 5;
        levelAt = 99;
        livesLeft = 5;
        scores = new int[100];
    }

    /** @param powerType The power that will be increase by 1*/
    public void increasePower(int powerType){
        switch (powerType){
            case 1:
                power1++;
                break;
            case 2:
               power2++;
                break;
            case 3:
                power3++;
                break;
            case 4:
                power4++;
                break;
            case 5:
                power5++;
                break;
            default:
                break;
        }
    }

    /** @param powerType The power that will be decreased by 1*/
    public void decreasePower(int powerType){
        switch (powerType) {
            case 1:
                if (power1 > 0) {
                    power1--;
                }
                break;
            case 2:
                if (power2 > 0) {
                    power2--;
                }
                break;

            case 3:
                if (power3 > 0) {
                    power3--;
                }
                break;

            case 4:
                if (power4 > 0) {
                    power4--;
                }
                break;

            case 5:
                if (power5 > 0) {
                    power5--;
                }
                break;
            default:
                break;
        }

    }

    /** Returns a specific power amount*/
    public int getPowerAmount(int powerType){
        switch (powerType) {
            case 1:
                return power1;
            case 2:
                return power2;
            case 3:
                return power3;
            case 4:
                return power4;
            case 5:
                return power5;
            default:
                return -1;
        }

    }

    /** Calculates the next available time to share in social media*/
    public void calculateNextShareTime(){
        timeLeftOnLifes = System.currentTimeMillis()/1000 + 172800;
    }

    /** Calculates the next time for 5 new lives */
    public void calculateNextLifeTime(){

        timeLeftOnLifes = System.currentTimeMillis()/1000 + 300;

    }

   public void increaseLevelAt(){
       levelAt++;
   }

    public void decreaseLivesLeft(){
        if(livesLeft > 0){
            livesLeft--;
        }
    }

    public int getPassedScore(int levelID){
        return scores[levelID];
    }

    public void setScore(int leveID, int score){
        scores[leveID] = score;
    }

    public double getTimeLeftOnLifes() {
        return timeLeftOnLifes;
    }

    public double getTimeLeftOnSocialMedia() {
        return timeLeftOnSocialMedia;
    }

    /** Saves the player information into a file*/
    public static void savePlayer(){
        FileHandle file = Gdx.files.local("assets/SupportFiles/player.dat");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(ourInstance);
           file.writeBytes(byteArrayOutputStream.toByteArray(), false);
        }catch (Exception ex){
           ex.printStackTrace();
        }finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /** Reads and creates a new instances of Player*/
    public static Player readPlayer(){
        Player player = null;
        FileHandle file = Gdx.files.local("assets/SupportFiles/player.dat");
        ByteArrayInputStream byteArrayInputStream  = null;
        ObjectInputStream objectInputStream = null;

        try{
            byteArrayInputStream = new ByteArrayInputStream(file.readBytes());
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            player = (Player)objectInputStream.readObject();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {

            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return player;
    }

}
