package com.dr01d3k4.iroirobureekaa.game.gamemode;



import java.util.Locale;



import android.graphics.Paint;
import android.graphics.Paint.Align;



import com.dr01d3k4.iroirobureekaa.GameScreen;
import com.dr01d3k4.iroirobureekaa.R;
import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.game.GameWorld.GameState;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class TimedMode extends GameMode {
	private float time;
	private final float MAX_TIME;
	private GameState previousGameState = GameState.NONE;
	private final String lastMove;
	
	
	
	public TimedMode(final GameScreen gameScreen) {
		super(gameScreen);
		
		time = 0;
		MAX_TIME = gameScreen.mainActivity.getResources().getInteger(R.integer.timed_game_length);
		lastMove = gameScreen.getString(R.string.last_move);
	}
	
	
	
	@Override
	public void postUpdate(final float deltaTime) {
		if (gameScreen.isPaused()) {
			return;
		}
		
		time += deltaTime;
		final GameState currentGameState = gameScreen.world.getState();
		if (time >= MAX_TIME) {
			time = MAX_TIME;
			
			if ((currentGameState == GameState.PLAYER_FALLING)
				&& (previousGameState != GameState.PLAYER_FALLING)) {
				gameScreen.gameOver();
				return;
			}
		}
		
		previousGameState = currentGameState;
	}
	
	
	
	private String timeRemainingToString() {
		float timeRemaining = MAX_TIME - time;
		String timeString;
		
		int minutes = 0;
		int seconds = 0;
		
		while (timeRemaining >= 60) {
			minutes += 1;
			timeRemaining -= 60;
		}
		
		seconds = (int) Math.floor(timeRemaining);
		
		if ((minutes == 0) && (seconds == 0)) {
			timeString = lastMove;
		} else {
			timeString = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
		}
		
		return timeString;
		
	}
	
	
	
	@Override
	public void postRender(final float deltaTime) {
		if (gameScreen.isPaused()) {
			return;
		}
		
		final Graphics graphics = gameScreen.getGraphics();
		final Paint paint = graphics.getPaint();
		
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(50);
		graphics.drawText(timeRemainingToString(), gameScreen.CANVAS_WIDTH / 2, gameScreen.HEADER_HEIGHT + 60, GameColour.TEXT);
	}
}