package com.dr01d3k4.iroirobureekaa.audio;



import java.io.IOException;



import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;



public class Audio {
	private final AssetManager assets;
	private final SoundPool soundPool;
	
	
	
	public Audio(final Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		assets = activity.getAssets();
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}
	
	
	
	public Music newMusic(final String filename) {
		try {
			final AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			return new Music(assetDescriptor);
		} catch (final IOException e) {
			throw new RuntimeException("Couldn't load music \"" + filename + "\"");
		}
	}
	
	
	
	public Sound newSound(final String filename) {
		try {
			final AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			final int soundId = soundPool.load(assetDescriptor, 0);
			return new Sound(soundPool, soundId);
		} catch (final IOException e) {
			throw new RuntimeException("Couldn't load music \"" + filename + "\"");
		}
	}
}
