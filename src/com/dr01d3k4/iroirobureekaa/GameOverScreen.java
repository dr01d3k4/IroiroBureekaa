package com.dr01d3k4.iroirobureekaa;



import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;



import com.dr01d3k4.iroirobureekaa.button.OnButtonClickListener;
import com.dr01d3k4.iroirobureekaa.button.TextButton;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class GameOverScreen extends Screen {
	private final int score;
	private final int gameModeId;
	
	private Text gameOverTextObject;
	private Text gameOverScoreTextObject;
	
	
	
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
		
		final int buttonWidth = getDimensionPixel(R.dimen.button_width);
		final int buttonX = (CANVAS_WIDTH - buttonWidth) / 2;
		final int buttonHeight = getDimensionPixel(R.dimen.button_height);
		final int buttonMargin = getDimensionPixel(R.dimen.button_margin);
		final int buttonStep = buttonMargin + buttonHeight;
		final int buttonCount = 3;
		final int buttonY = CANVAS_HEIGHT - (buttonCount * buttonStep);
		
		final int titleTextY = getDimensionPixel(R.dimen.title_text_y);
		final int titleTextHeight = getDimensionPixel(R.dimen.title_text_height);
		final int gameOverScoreY = getDimensionPixel(R.dimen.game_over_score_text_y);
		final int gameOverScoreHeight = getDimensionPixel(R.dimen.game_over_score_text_height);
		
		buttonManager.addButton(new TextButton(getString(R.string.game_over_play_again), buttonX, buttonY,
			buttonWidth, buttonHeight, new OnButtonClickListener() {
				@Override
				public void onClick() {
					mainActivity.playSound(Assets.buttonSelect);
					mainActivity.changeScreen(new GameScreen(mainActivity, gameModeId));
				}
			}));
		
		buttonManager.addButton(new TextButton(getString(R.string.share), buttonX, buttonY + buttonStep,
			buttonWidth, buttonHeight, new OnButtonClickListener() {
				@Override
				public void onClick() {
					mainActivity.playSound(Assets.buttonSelect);
					mainActivity.shareScore(score);
				}
			}));
		
		buttonManager.addButton(new TextButton(getString(R.string.game_over_quit_game), buttonX, buttonY
			+ (2 * buttonStep), buttonWidth, buttonHeight, new OnButtonClickListener() {
			@Override
			public void onClick() {
				mainActivity.playSound(Assets.buttonSelect);
				mainActivity.changeScreen(new MainMenuScreen(mainActivity));
				
			}
		}));
		
		gameOverTextObject = new Text(getString(R.string.game_over), 0, titleTextY, CANVAS_WIDTH,
			titleTextHeight);
		
		gameOverScoreTextObject = new Text(String.format(getString(R.string.game_over_score), score), 0,
			gameOverScoreY, CANVAS_WIDTH, gameOverScoreHeight);
	}
	
	
	
	@Override
	public void update(final float deltaTime) {
		buttonManager.update(input);
	}
	
	
	
	@Override
	public void render(final float deltaTime) {
		final Graphics graphics = getGraphics();
		final Paint paint = graphics.getPaint();
		
		graphics.clear(Color.WHITE);
		
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		gameOverTextObject.render(graphics, paint);
		paint.setTypeface(Typeface.DEFAULT);
		
		gameOverScoreTextObject.render(graphics, paint);
		buttonManager.render(graphics, paint);
	}
	
	
	
	@Override
	public void onBackPressed() {
		mainActivity.changeScreen(new MainMenuScreen(mainActivity));
	}
}