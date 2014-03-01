package com.dr01d3k4.iroirobureekaa;



import java.util.List;
import java.util.Random;



import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;



import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.input.Input;
import com.dr01d3k4.iroirobureekaa.input.TouchEvent;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class MainMenuScreen extends Screen {
	private TextButton playButton;
	private TextButton highscoresButton;
	private TextButton helpButton;
	private TextButton settingsButton;
	private TextButton aboutButton;
	
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
		int buttonY = buttonMargin;
		final int buttonWidth = (int) (CANVAS_WIDTH * 0.55);
		final int buttonHeight = getDimensionPixel(R.dimen.button_height);
		
		playButton = new TextButton(getString(R.string.main_menu_play), buttonX, buttonY, buttonWidth,
			buttonHeight);
		playButton.backgroundColour = Color.WHITE;
		playButton.hoveredColour = GameColour.UI_LIGHT;
		
		buttonY += buttonHeight + buttonMargin;
		
		highscoresButton = new TextButton(getString(R.string.main_menu_highscores), buttonX, buttonY,
			buttonWidth, buttonHeight);
		highscoresButton.backgroundColour = Color.WHITE;
		highscoresButton.hoveredColour = GameColour.UI_LIGHT;
		
		buttonY += buttonHeight + buttonMargin;
		
		helpButton = new TextButton(getString(R.string.main_menu_help), buttonX, buttonY, buttonWidth,
			buttonHeight);
		helpButton.backgroundColour = Color.WHITE;
		helpButton.hoveredColour = GameColour.UI_LIGHT;
		
		buttonY += buttonHeight + buttonMargin;
		
		settingsButton = new TextButton(getString(R.string.main_menu_settings), buttonX, buttonY, buttonWidth,
			buttonHeight);
		settingsButton.backgroundColour = Color.WHITE;
		settingsButton.hoveredColour = GameColour.UI_LIGHT;
		
		buttonY += buttonHeight + buttonMargin;
		
		aboutButton = new TextButton(getString(R.string.main_menu_about), buttonX, buttonY, buttonWidth,
			buttonHeight);
		aboutButton.backgroundColour = Color.WHITE;
		aboutButton.hoveredColour = GameColour.UI_LIGHT;
	}
	
	
	
	public void startGame() {
		mainActivity.changeScreen(new GameModeSelectScreen(mainActivity));
		
	}
	
	
	
	@Override
	public void update(final float deltaTime) {
		final List<TouchEvent> touchEvents = input.getTouchEvents();
		playButton.hovered = false;
		highscoresButton.hovered = false;
		helpButton.hovered = false;
		settingsButton.hovered = false;
		aboutButton.hovered = false;
		
		int x;
		int y;
		boolean down;
		for (int pointer = 0, length = Input.MAX_TOUCHPOINTS; pointer < length; pointer += 1) {
			x = input.getTouchX(pointer);
			y = input.getTouchY(pointer);
			down = input.isTouchDown(pointer);
			
			if (down) {
				if (playButton.visible && playButton.isOver(x, y)) {
					playButton.hovered = true;
				} else if (highscoresButton.visible && highscoresButton.isOver(x, y)) {
					highscoresButton.hovered = true;
				} else if (helpButton.visible && helpButton.isOver(x, y)) {
					helpButton.hovered = true;
				} else if (settingsButton.visible && settingsButton.isOver(x, y)) {
					settingsButton.hovered = true;
				} else if (aboutButton.visible && aboutButton.isOver(x, y)) {
					aboutButton.hovered = true;
				}
			}
		}
		
		TouchEvent touchEvent;
		for (int i = 0, length = touchEvents.size(); i < length; i += 1) {
			touchEvent = touchEvents.get(i);
			x = touchEvent.x;
			y = touchEvent.y;
			
			if (touchEvent.type == TouchEvent.TOUCH_UP) {
				if (playButton.visible && playButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
					input.clearTouches();
					startGame();
					return;
				} else if (highscoresButton.visible && highscoresButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
				} else if (helpButton.visible && helpButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
				} else if (settingsButton.visible && settingsButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
				} else if (aboutButton.visible && aboutButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
				}
			}
		}
	}
	
	
	
	private final Random random = new Random();
	private float time = 0;
	private int word1Colour = GameColour.randomColour(random);
	private int word2Colour = GameColour.randomColour(random);
	
	
	
	private void pickColours() {
		int newWord1Colour;
		do {
			newWord1Colour = GameColour.randomColour(random);
		} while (newWord1Colour == word1Colour);
		word1Colour = newWord1Colour;
		
		int newWord2Colour;
		do {
			newWord2Colour = GameColour.randomColour(random);
		} while ((newWord2Colour == word2Colour) || (newWord2Colour == word1Colour));
		word2Colour = newWord2Colour;
	}
	
	
	
	@Override
	public void render(final float deltaTime) {
		final Graphics graphics = getGraphics();
		final Paint paint = graphics.getPaint();
		graphics.clear(Color.WHITE);
		
		time += deltaTime;
		final float newColourTime = 0.5f;
		while (time >= newColourTime) {
			pickColours();
			time -= newColourTime;
		}
		
		paint.setTextSize(titleFontSize);
		paint.setTextAlign(Align.CENTER);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		
		final int xStep = (int) (CANVAS_WIDTH * 0.18);
		int x = (int) (CANVAS_WIDTH * 0.1);
		final int yStep = (int) (titleCharacterHeight * 0.6);
		int column = 0;
		int y = 0;
		
		int colour = word1Colour;
		
		final int length = texts.length;
		String text;
		for (int i = 0; i < length; i += 1) {
			text = texts[i];
			y += titleCharacterHeight;
			
			if (text.equals(" ")) {
				column += 1;
				x += xStep;
				y = column * yStep;
				colour = ((column & 1) == 0) ? word1Colour : word2Colour;
				
			} else {
				colour = GameColour.TEXT;
				graphics.drawText(text, x, y, colour);
			}
		}
		
		paint.setTypeface(Typeface.DEFAULT);
		
		playButton.render(graphics, paint);
		highscoresButton.render(graphics, paint);
		helpButton.render(graphics, paint);
		settingsButton.render(graphics, paint);
		aboutButton.render(graphics, paint);
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