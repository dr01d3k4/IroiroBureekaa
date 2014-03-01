package com.dr01d3k4.iroirobureekaa.audio;



import android.media.SoundPool;



public class Sound {
	int soundId;
	SoundPool soundPool;
	
	
	
	public Sound(final SoundPool soundPool, final int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
	}
	
	
	
	public void play(final float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}
	
	
	
	public void dispose() {
		soundPool.unload(soundId);
	}
}
