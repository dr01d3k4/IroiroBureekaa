package com.dr01d3k4.iroirobureekaa;



import java.util.List;



import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.input.Input;
import com.dr01d3k4.iroirobureekaa.input.TouchEvent;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class GameOverScreen extends Screen {
	private final int score;
	private final int gameModeId;
	
	private Text gameOverTextObject;
	private Text gameOverScoreTextObject;
	
	public TextButton gameOverPlayAgainButton;
	public TextButton shareButton;
	public TextButton gameOverQuitButton;
	
	
	
	public GameOverScreen(final IroiroBureekaa mainActivity, final int score, final int gameModeId) {
		super(mainActivity);
		this.score = score;
		this.gameModeId = gameModeId;
		
		mainActivity.gameData.onGameOver(score, gameModeId);
		mainActivity.saveGame();
	}
	
	
	
	@Override
	public void calculateSize() {
		super.calculateSize();
		
		final int pausedButtonsWidth = getDimensionPixel(R.dimen.button_width);
		final int pausedButtonsX = (CANVAS_WIDTH - pausedButtonsWidth) / 2;
		final int pausedButtonsHeight = getDimensionPixel(R.dimen.button_height);
		final int pausedButtonsMargin = getDimensionPixel(R.dimen.button_margin);
		final int pausedButtonsY = CANVAS_HEIGHT - (3 * pausedButtonsMargin) - (3 * pausedButtonsHeight);
		
		//		final int pausedButtonsX = (int) (CANVAS_WIDTH * 0.1);
		//		final int pausedButtonsWidth = (int) (CANVAS_WIDTH * 0.8);
		//		final int pausedButtonsY = (int) (CANVAS_HEIGHT * 0.5);
		//		final int pausedButtonsHeight = (int) (CANVAS_HEIGHT * 0.08);
		//		final int pausedButtonsMargin = (int) (CANVAS_HEIGHT * 0.05);
		final int pausedTextY = getDimensionPixel(R.dimen.title_text_y); // (int) (CANVAS_HEIGHT * 0.1);
		final int pausedTextHeight = getDimensionPixel(R.dimen.title_text_height); // (int) (CANVAS_HEIGHT * 0.15);
		final int gameOverScoreY = getDimensionPixel(R.dimen.game_over_score_text_y); // (int) (CANVAS_HEIGHT * 0.3);
		final int gameOverScoreHeight = getDimensionPixel(R.dimen.game_over_score_text_height);
		
		gameOverPlayAgainButton = new TextButton(getString(R.string.game_over_play_again), pausedButtonsX,
			pausedButtonsY, pausedButtonsWidth, pausedButtonsHeight);
		shareButton = new TextButton(getString(R.string.share), pausedButtonsX, pausedButtonsY
			+ pausedButtonsMargin + pausedButtonsHeight, pausedButtonsWidth, pausedButtonsHeight);
		gameOverQuitButton = new TextButton(getString(R.string.game_over_quit_game), pausedButtonsX,
			pausedButtonsY + (2 * pausedButtonsMargin) + (2 * pausedButtonsHeight), pausedButtonsWidth,
			pausedButtonsHeight);
		
		gameOverTextObject = new Text(getString(R.string.game_over), 0, pausedTextY, CANVAS_WIDTH,
			pausedTextHeight);
		
		gameOverScoreTextObject = new Text(String.format(getString(R.string.game_over_score), score), 0,
			gameOverScoreY, CANVAS_WIDTH, gameOverScoreHeight);
	}
	
	
	
	@Override
	public void update(final float deltaTime) {
		final List<TouchEvent> touchEvents = input.getTouchEvents();
		
		gameOverPlayAgainButton.hovered = false;
		shareButton.hovered = false;
		gameOverQuitButton.hovered = false;
		int x;
		int y;
		boolean down;
		
		for (int pointer = 0, length = Input.MAX_TOUCHPOINTS; pointer < length; pointer += 1) {
			x = input.getTouchX(pointer);
			y = input.getTouchY(pointer);
			down = input.isTouchDown(pointer);
			
			if (down) {
				if (gameOverPlayAgainButton.visible && gameOverPlayAgainButton.isOver(x, y)) {
					gameOverPlayAgainButton.hovered = true;
					
				} else if (shareButton.visible && shareButton.isOver(x, y)) {
					shareButton.hovered = true;
					
				} else if (gameOverQuitButton.visible && gameOverQuitButton.isOver(x, y)) {
					gameOverQuitButton.hovered = true;
				}
			}
		}
		
		TouchEvent touchEvent;
		for (int i = 0, length = touchEvents.size(); i < length; i += 1) {
			touchEvent = touchEvents.get(i);
			x = touchEvent.x;
			y = touchEvent.y;
			
			if (touchEvent.type == TouchEvent.TOUCH_UP) {
				if (gameOverPlayAgainButton.visible && gameOverPlayAgainButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
					mainActivity.changeScreen(new GameScreen(mainActivity, gameModeId));
					return;
					
				} else if (shareButton.visible && shareButton.isOver(x, y)) {
					mainActivity.shareScore(score);
					return;
					
				} else if (gameOverQuitButton.visible && gameOverQuitButton.isOver(x, y)) {
					
					mainActivity.playSound(Assets.buttonSelect);
					mainActivity.changeScreen(new MainMenuScreen(mainActivity));
					return;
				}
			}
		}
	}
	
	
	
	@Override
	public void render(final float deltaTime) {
		final Graphics graphics = getGraphics();
		final Paint paint = graphics.getPaint();
		graphics.clear(GameColour.UI_LIGHT);
		gameOverTextObject.render(graphics, paint);
		
		gameOverScoreTextObject.render(graphics, paint);
		
		gameOverPlayAgainButton.render(graphics, paint);
		shareButton.render(graphics, paint);
		gameOverQuitButton.render(graphics, paint);
	}
	
	
	
	@Override
	public void onBackPressed() {
		mainActivity.changeScreen(new MainMenuScreen(mainActivity));
	}
}