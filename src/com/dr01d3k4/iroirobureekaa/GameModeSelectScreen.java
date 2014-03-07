package com.dr01d3k4.iroirobureekaa;



import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;



import com.dr01d3k4.iroirobureekaa.button.Button;
import com.dr01d3k4.iroirobureekaa.button.OnButtonClickListener;
import com.dr01d3k4.iroirobureekaa.button.TextButton;
import com.dr01d3k4.iroirobureekaa.game.gamemode.GameMode;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class GameModeSelectScreen extends Screen {
	public Button[] buttons;
	public Text titleText;
	
	
	
	public GameModeSelectScreen(final IroiroBureekaa mainActivity) {
		super(mainActivity);
		buttons = new Button[GameMode.MODES.length];
	}
	
	
	
	@Override
	public void calculateSize() {
		super.calculateSize();
		
		final int titleTextY = getDimensionPixel(R.dimen.title_text_y);
		final int titleTextHeight = getDimensionPixel(R.dimen.title_text_height);
		
		titleText = new Text(getString(R.string.select_mode), 0, titleTextY, CANVAS_WIDTH, titleTextHeight);
		
		final int buttonWidth = getDimensionPixel(R.dimen.button_width);
		final int buttonX = (CANVAS_WIDTH - buttonWidth) / 2;
		final int buttonHeight = getDimensionPixel(R.dimen.button_height);
		final int buttonMargin = getDimensionPixel(R.dimen.button_margin);
		final int buttonStep = buttonHeight + buttonMargin;
		
		final int buttonY = titleTextY + titleTextHeight + buttonMargin;
		
		for (int i = 0, length = GameMode.MODES.length; i < length; i += 1) {
			final int id = i;
			buttons[i] = buttonManager.addButton(new TextButton(getString(GameMode.getTitle(i)), buttonX,
				buttonY + (i * buttonStep), buttonWidth, buttonHeight, new OnButtonClickListener() {
					@Override
					public void onClick() {
						mainActivity.playSound(Assets.buttonSelect);
						input.clearTouches();
						mainActivity.changeScreen(new GameScreen(mainActivity, id));
					}
				}));
		}
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
		titleText.render(graphics, paint);
		paint.setTypeface(Typeface.DEFAULT);
		
		buttonManager.render(graphics, paint);
	}
	
	
	
	@Override
	public void onBackPressed() {
		mainActivity.changeScreen(new MainMenuScreen(mainActivity));
	}
}