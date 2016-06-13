package com.zhipu.chinavideo.manager;

import java.io.IOException;
import java.util.HashMap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.zhipu.chinavideo.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore.Audio.Media;

public class SoundManager {

	public static boolean isSOundOn = true ;
	public static SoundManager soundManager ;
	
	private SoundPool soundPool ;
	
	private MediaPlayer music_drop ;
	
	
	HashMap<Integer, Integer> musicId = new HashMap<Integer, Integer>();
	public static final int SFX_ID_SHAKE =0 ;
	
	public static final int[] ALLSOUND = {
		R.raw.click,
	} ;
	public static SoundManager getIntance(){
		
		if(soundManager == null){
			soundManager = new SoundManager() ;
		}
		return soundManager ;
	}
	
	public  void initAllSound(Context context){
		
//		AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//		int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
		if(soundPool == null){
			soundPool = new SoundPool( 5,  AudioManager.STREAM_MUSIC, 1 );
			musicId.put(SFX_ID_SHAKE, soundPool.load(context, ALLSOUND[SFX_ID_SHAKE], 1));
		}
		
		
	}
	
	private void playMusic(){
		
	}
	public void playSound(int id){
		if(isSOundOn && soundPool!=null){
			if(musicId.containsKey(id)){
				soundPool.play(musicId.get(id),1.0f,1.0f,0,0,1) ;
			}
		}
	}
	
	
	public void playDropMusic(Context context){
		
		if(music_drop!=null && music_drop.isPlaying()){
			return ;
		}
		
		if(music_drop == null){
			music_drop = MediaPlayer.create(context, R.raw.mommondrop);
			music_drop.setLooping(true);
		}
		try {
			music_drop.prepare();
			music_drop.seekTo(0); 
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			music_drop.start();
	
	}
	public void pauseDropMusic(){
		if(music_drop!=null){
			if(music_drop.isPlaying()){
				music_drop.pause();
			}
		}
	
	}
	public void resumeDropMusic(){
		if(music_drop!=null){
			if(!music_drop.isPlaying()){
				music_drop.start();
			}
		}
	}
	
	
	public  void stopDropMusic(){
		if(music_drop!=null){
			music_drop.stop();
			music_drop.release();
			music_drop = null ;
		}
	}
}
