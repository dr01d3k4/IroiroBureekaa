package com.dr01d3k4.iroirobureekaa.game.gamemode;



import java.util.Locale;



import android.graphics.Paint;
import android.graphics.Paint.Align;



import com.dr01d3k4.iroirobureekaa.GameScreen;
import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public class TimedMode extends GameMode {
	private float time;
	private static final float MAX_TIME = 2 * 60;
	
	
	
	public TimedMode(final GameScreen gameScreen) {
		super(gameScreen);
	}
	
	
	
	@Override
	public void postUpdate(final float deltaTime) {
		time += deltaTime;
		if (time >= MAX_TIME) {
			gameScreen.gameOver();
		}
	}
	
	
	
	private String timeRemainingToString() {
		float timeRemaining = MAX_TIME - time;
		
		int minutes = 0;
		int seconds = 0;
		
		while (timeRemaining >= 60) {
			minutes += 1;
			timeRemaining -= 60;
		}
		
		seconds = (int) Math.floor(timeRemaining);
		
		final String timeString = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
		
		return timeString;
		
	}
	
	
	
	@Override
	public void postRender(final float deltaTime) {
		final Graphics graphics = gameScreen.getGraphics();
		final Paint paint = graphics.getPaint();
		
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(50);
		graphics.drawText(timeRemainingToString(), gameScreen.CANVAS_WIDTH / 2, gameScreen.HEADER_HEIGHT + 60, GameColour.TEXT);
	}
}