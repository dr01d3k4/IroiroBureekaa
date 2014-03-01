package com.dr01d3k4.iroirobureekaa.audio;



import java.io.IOException;



import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;



public class Music implements OnCompletionListener {
	private final MediaPlayer mediaPlayer;
	private boolean isPrepared = false;
	
	
	
	public Music(final AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer
				.setDataSource(assetDescriptor.getFileDescriptor(), assetDescriptor.getStartOffset(), assetDescriptor
					.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		} catch (final Exception e) {
			throw new RuntimeException("Couldn't load music");
		}
	}
	
	
	
	public void dispose() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.release();
	}
	
	
	
	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}
	
	
	
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}
	
	
	
	public boolean isStopped() {
		return !isPrepared;
	}
	
	
	
	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}
	
	
	
	public void play() {
		if (mediaPlayer.isPlaying()) {
			return;
		}
		
		try {
			synchronized (this) {
				if (!isPrepared) {
					mediaPlayer.prepare();
				}
				mediaPlayer.start();
			}
		} catch (final IllegalStateException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void setLooping(final boolean isLooping) {
		mediaPlayer.setLooping(isLooping);
	}
	
	
	
	public void setVolume(final float volume) {
		mediaPlayer.setVolume(volume, volume);
	}
	
	
	
	public void stop() {
		mediaPlayer.stop();
		synchronized (this) {
			isPrepared = false;
		}
	}
	
	
	
	@Override
	public void onCompletion(final MediaPlayer player) {
		synchronized (this) {
			isPrepared = false;
		}
	}
}
