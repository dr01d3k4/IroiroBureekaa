package com.dr01d3k4.iroirobureekaa;



import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;



import com.dr01d3k4.iroirobureekaa.button.OnButtonClickListener;
import com.dr01d3k4.iroirobureekaa.button.TextButton;
import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class MainMenuScreen extends Screen {
	private String[] texts;
	private int titleCharacterHeight = 100;
	private float titleFontSize;
	
	
	
	public MainMenuScreen(final IroiroBureekaa mainActivity) {
		super(mainActivity);
	}
	
	
	
	@Override
	public void calculateSize() {
		super.calculateSize();
		
		final String title = getString(R.string.app_title);
		
		final String[] words = title.split(" ");
		int longestWord = words[0].length();
		
		int length = words.length;
		String word;
		
		for (int i = 0; i < length; i += 1) {
			word = words[i];
			if (word.length() > longestWord) {
				longestWord = word.length();
			}
		}
		
		texts = new String[title.length()];
		titleCharacterHeight = (int) ((CANVAS_HEIGHT / longestWord) * 0.8);
		length = title.length();
		for (int i = 0; i < length; i += 1) {
			texts[i] = title.substring(i, i + 1);
		}
		titleFontSize = (new Text("ロ", 0, 0, titleCharacterHeight, titleCharacterHeight)).textSize;
		
		final int buttonMargin = getDimensionPixel(R.dimen.button_margin);
		final int buttonX = (int) (CANVAS_WIDTH * 0.4);
		final int buttonWidth = (int) (CANVAS_WIDTH * 0.55);
		final int buttonHeight = getDimensionPixel(R.dimen.button_height);
		int buttonY = buttonMargin;
		
		buttonManager.addButton(new TextButton(getString(R.string.main_menu_play), buttonX, buttonY,
			buttonWidth, buttonHeight, new OnButtonClickListener() {
				@Override
				public void onClick() {
					mainActivity.playSound(Assets.buttonSelect);
					input.clearTouches();
					startGame();
				}
			}));
		
		buttonY += buttonHeight + buttonMargin;
		
		buttonManager.addButton(new TextButton(getString(R.string.main_menu_highscores), buttonX, buttonY,
			buttonWidth, buttonHeight, new OnButtonClickListener() {
				@Override
				public void onClick() {
					mainActivity.playSound(Assets.buttonSelect);
				}
			}));
		
		buttonY += buttonHeight + buttonMargin;
		
		buttonManager.addButton(new TextButton(getString(R.string.main_menu_help), buttonX, buttonY,
			buttonWidth, buttonHeight, new OnButtonClickListener() {
				@Override
				public void onClick() {
					mainActivity.playSound(Assets.buttonSelect);
				}
			}));
		
		buttonY += buttonHeight + buttonMargin;
		
		buttonManager.addButton(new TextButton(getString(R.string.main_menu_settings), buttonX, buttonY,
			buttonWidth, buttonHeight, new OnButtonClickListener() {
				@Override
				public void onClick() {
					mainActivity.playSound(Assets.buttonSelect);
				}
			}));
		
		buttonY += buttonHeight + buttonMargin;
		
		buttonManager.addButton(new TextButton(getString(R.string.main_menu_about), buttonX, buttonY,
			buttonWidth, buttonHeight, new OnButtonClickListener() {
				@Override
				public void onClick() {
					mainActivity.playSound(Assets.buttonSelect);
				}
			}));
	}
	
	
	
	public void startGame() {
		mainActivity.changeScreen(new GameModeSelectScreen(mainActivity));
		
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
		
		paint.setTextSize(titleFontSize);
		paint.setTextAlign(Align.CENTER);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		
		final int xStep = (int) (CANVAS_WIDTH * 0.18);
		int x = (int) (CANVAS_WIDTH * 0.1);
		final int yStep = (int) (titleCharacterHeight * 0.6);
		int column = 0;
		int y = 0;
		
		int colour = GameColour.TEXT;
		
		final int length = texts.length;
		String text;
		for (int i = 0; i < length; i += 1) {
			text = texts[i];
			y += titleCharacterHeight;
			
			if (text.equals(" ")) {
				column += 1;
				x += xStep;
				y = column * yStep;
				
			} else {
				colour = GameColour.TEXT;
				graphics.drawText(text, x, y, colour);
			}
		}
		
		paint.setTypeface(Typeface.DEFAULT);
		
		buttonManager.render(graphics, paint);
	}
	
	
	
	@Override
	public boolean signInButtonsVisible() {
		return true;
	}
	
	
	
	@Override
	public void onBackPressed() {
		mainActivity.finish();
	}
}