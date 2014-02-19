package com.dr01d3k4.iroirobureekaa;



import java.util.List;



import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.input.TouchEvent;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public class GameOverScreen extends Screen {
	public final int score;
	public final int gridWidth;
	public final int gridHeight;
	
	private Text gameOverTextObject;
	private Text gameOverScoreTextObject;
	
	public Button gameOverPlayAgainButton;
	public Button gameOverQuitButton;
	
	
	
	public GameOverScreen(final IroiroBureekaa mainActivity, final int score, final int gridWidth,
		final int gridHeight) {
		super(mainActivity);
		this.score = score;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		
		// mainActivity.gameData.onGameOver(world.getScore());
		// mainActivity.saveGame();
	}
	
	
	
	@Override
	public void calculateSize() {
		super.calculateSize();
		
		final int pausedButtonsX = (int) (CANVAS_WIDTH * 0.1);
		final int pausedButtonsWidth = (int) (CANVAS_WIDTH * 0.8);
		final int pausedButtonsY = (int) (CANVAS_HEIGHT * 0.7);
		final int pausedButtonsHeight = (int) (CANVAS_HEIGHT * 0.08);
		final int pausedTextY = (int) (CANVAS_HEIGHT * 0.1);
		final int pausedTextHeight = (int) (CANVAS_HEIGHT * 0.15);
		final int gameOverScoreY = (int) (CANVAS_HEIGHT * 0.3);
		
		gameOverPlayAgainButton = new Button(getString(R.string.game_over_play_again), pausedButtonsX,
			pausedButtonsY, pausedButtonsWidth, pausedButtonsHeight);
		gameOverQuitButton = new Button(getString(R.string.game_over_quit_game), pausedButtonsX, pausedButtonsY
			+ (2 * pausedButtonsHeight), pausedButtonsWidth, pausedButtonsHeight);
		
		gameOverTextObject = new Text(getString(R.string.game_over), 0, pausedTextY, CANVAS_WIDTH,
			(int) (pausedTextHeight * 0.7));
		
		gameOverScoreTextObject = new Text(String.format(getString(R.string.game_over_score), score), 0,
			gameOverScoreY, CANVAS_WIDTH, (int) (CANVAS_HEIGHT * 0.06));
		
	}
	
	
	
	@Override
	public void update(final float deltaTime) {
		final List<TouchEvent> touchEvents = input.getTouchEvents();
		
		gameOverPlayAgainButton.hovered = false;
		gameOverQuitButton.hovered = false;
		int x;
		int y;
		boolean down;
		
		for (int pointer = 0; pointer < 10; pointer++) {
			x = input.getTouchX(pointer);
			y = input.getTouchY(pointer);
			down = input.isTouchDown(pointer);
			
			if (down && gameOverPlayAgainButton.visible && gameOverPlayAgainButton.isOver(x, y)) {
				gameOverPlayAgainButton.hovered = true;
				
			} else if (down && gameOverQuitButton.visible && gameOverQuitButton.isOver(x, y)) {
				gameOverQuitButton.hovered = true;
			}
		}
		
		TouchEvent touchEvent;
		for (int i = 0; i < touchEvents.size(); i++) {
			touchEvent = touchEvents.get(i);
			x = touchEvent.x;
			y = touchEvent.y;
			
			if (touchEvent.type == TouchEvent.TOUCH_UP) {
				if (gameOverPlayAgainButton.visible && gameOverPlayAgainButton.isOver(x, y)) {
					mainActivity.changeScreen(new GameScreen(mainActivity, gridWidth, gridHeight));
					return;
				} else if (gameOverQuitButton.visible && gameOverQuitButton.isOver(x, y)) {
					mainActivity.changeScreen(new MainMenuScreen(mainActivity));
					return;
				}
			}
		}
	}
	
	
	
	@Override
	public void render(final float deltaTime) {
		final Graphics graphics = mainActivity.getGraphics();
		final Paint paint = graphics.getPaint();
		graphics.clear(GameColour.UI_LIGHT);
		gameOverTextObject.render(graphics, paint);
		
		gameOverScoreTextObject.render(graphics, paint);
		
		gameOverPlayAgainButton.render(graphics, paint);
		gameOverQuitButton.render(graphics, paint);
	}
	
	
	
	@Override
	public void onBackPressed() {
		mainActivity.changeScreen(new MainMenuScreen(mainActivity));
	}
}