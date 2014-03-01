package com.dr01d3k4.iroirobureekaa.game.gamemode;



import com.dr01d3k4.iroirobureekaa.GameScreen;
import com.dr01d3k4.iroirobureekaa.R;



public abstract class GameMode {
	public static final int TIMED = 0;
	public static final int INFINITE = 1;
	public static final int[] MODES = {TIMED, INFINITE};
	
	protected final GameScreen gameScreen;
	
	
	
	public GameMode(final GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	};
	
	
	
	public abstract void postUpdate(float deltaTime);
	
	
	
	public abstract void postRender(float deltaTime);
	
	
	
	public static final GameMode idToMode(final int id, final GameScreen gameScreen) {
		GameMode gameMode;
		
		switch (id) {
			case INFINITE: {
				gameMode = new InfiniteMode(gameScreen);
				break;
			}
			
			case TIMED: {
				gameMode = new TimedMode(gameScreen);
				break;
			}
			
			default: {
				throw new IllegalArgumentException("Id not recognised");
			}
		}
		
		return gameMode;
	}
	
	
	
	public static final int getTitle(final int id) {
		int title;
		
		switch (id) {
			case INFINITE: {
				title = R.string.infinite_mode;
				break;
			}
			
			case TIMED: {
				title = R.string.timed_mode;
				break;
			}
			
			default: {
				throw new IllegalArgumentException("Id not recognised");
			}
		}
		
		return title;
	}
}