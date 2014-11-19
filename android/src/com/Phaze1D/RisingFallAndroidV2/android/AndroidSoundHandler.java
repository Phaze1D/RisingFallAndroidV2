package com.Phaze1D.RisingFallAndroidV2.android;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.Phaze1D.RisingFallAndroidV2.Controllers.SoundControllerDelegate;

public class AndroidSoundHandler implements SoundControllerDelegate {

	public Context mContext;
	
	private SoundPool soundPool;
	private boolean loaded;
	
	private int buttonID;
	private int winID;
	private int loseID;
	private int swipeID;
	private int tapID;
	private int powerID;
	private int reachID;
	private int testID;
	
	private float actualVolume;
	private float maxVolume;
	private float volume;
	
	
	
	public void loadSounds(Context mContext){
		this.mContext = mContext;
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				
				loaded = true;
			}
		});
		
		buttonID = soundPool.load(mContext, R.raw.button_sound, 1);
		reachID = soundPool.load(mContext, R.raw.high_reach, 1);
		loseID = soundPool.load(mContext, R.raw.lost_sound, 1);
		tapID = soundPool.load(mContext, R.raw.normal_ball, 1);
		powerID = soundPool.load(mContext, R.raw.power_ball, 1);
		swipeID = soundPool.load(mContext, R.raw.swipe_sound, 1);
		winID = soundPool.load(mContext, R.raw.win_sound, 1);
		
		
		AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;
		

	}
	
	
	@Override
	public void playWinSound() {
		if (loaded) {
            soundPool.play(winID, volume, volume, 1, 0, 1f);
            
        }
		
	}

	@Override
	public void playLoseSound() {
		if (loaded) {
            soundPool.play(loseID, volume, volume, 1, 0, 1f);
            
        }
		
	}

	@Override
	public void playSwipeSound() {
		if (loaded) {
            soundPool.play(swipeID, volume, volume, 1, 0, 1f);
            
        }
		
	}

	@Override
	public void playButtonSound() {
		if (loaded) {
            soundPool.play(buttonID, volume, volume, 1, 0, 1f);
            
        }
		
	}

	@Override
	public void playPowerSound() {
		if (loaded) {
            soundPool.play(powerID, volume, volume, 1, 0, 1f);
            
        }
		
	}

	@Override
	public void playNormalSound() {
		if (loaded) {
            soundPool.play(tapID, volume, volume, 1, 0, 1f);
            
        }
		
	}

	@Override
	public void playHighReachSound() {
		if (loaded) {
            soundPool.play(reachID, volume, volume, 1, 0, 1f);
            
        }
		
	}

	
	public void disposeSounds(){
		soundPool.unload(buttonID);
		soundPool.unload(powerID);
		soundPool.unload(winID);
		soundPool.unload(loseID);
		soundPool.unload(tapID);
		soundPool.unload(swipeID);
		soundPool.unload(reachID);
		
	}
	
}
