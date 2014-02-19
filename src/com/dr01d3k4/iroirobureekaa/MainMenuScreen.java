package com.dr01d3k4.iroirobureekaa;



import java.util.List;



import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;



import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.game.Grid;
import com.dr01d3k4.iroirobureekaa.input.TouchEvent;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public class MainMenuScreen extends Screen {
	private Button playButton;
	
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
		for (final String word : words) {
			if (word.length() > longestWord) {
				longestWord = word.length();
			}
		}
		
		texts = new String[title.length()];
		titleCharacterHeight = (int) ((CANVAS_HEIGHT / longestWord) * 0.8);
		for (int i = 0; i < title.length(); i++) {
			texts[i] = title.substring(i, i + 1);
		}
		titleFontSize = (new Text("ロ", 0, 0, titleCharacterHeight, titleCharacterHeight)).textSize;
		
		playButton = new Button(getString(R.string.play), (int) (CANVAS_WIDTH * 0.45),
			(int) (CANVAS_HEIGHT * 0.1), (int) (CANVAS_WIDTH * 0.5), (int) (CANVAS_HEIGHT * 0.1));
		playButton.backgroundColour = Color.WHITE;
	}
	
	
	
	public void startGame() {
		mainActivity.changeScreen(new GameScreen(mainActivity, Grid.WIDTH, Grid.HEIGHT));
	}
	
	
	
	@Override
	public void update(final float deltaTime) {
		final List<TouchEvent> touchEvents = input.getTouchEvents();
		playButton.hovered = false;
		
		int x;
		int y;
		boolean down;
		for (int pointer = 0; pointer < 10; pointer++) {
			x = input.getTouchX(pointer);
			y = input.getTouchY(pointer);
			down = input.isTouchDown(pointer);
			
			if (down && playButton.visible && playButton.isOver(x, y)) {
				playButton.hovered = true;
				
			}
		}
		
		TouchEvent touchEvent;
		for (int i = 0; i < touchEvents.size(); i++) {
			touchEvent = touchEvents.get(i);
			x = touchEvent.x;
			y = touchEvent.y;
			
			if (touchEvent.type == TouchEvent.TOUCH_UP) {
				if (playButton.visible && playButton.isOver(x, y)) {
					input.clearTouches();
					startGame();
					return;
				}
			}
		}
	}
	
	
	
	@Override
	public void render(final float deltaTime) {
		final Graphics graphics = mainActivity.getGraphics();
		final Paint paint = graphics.getPaint();
		graphics.clear(Color.WHITE);
		
		paint.setTextSize(titleFontSize);
		paint.setTextAlign(Align.CENTER);
		final int xStep = (int) (CANVAS_WIDTH * 0.2);
		int x = (int) (CANVAS_WIDTH * 0.1);
		int y = 0;
		
		for (final String text : texts) {
			y += titleCharacterHeight;
			
			if (text.equals(" ")) {
				x += xStep;
				y = 0;
				
			} else {
				graphics.drawText(text, x, y, GameColour.TEXT);
			}
		}
		
		playButton.render(graphics, paint);
	}
	
	
	
	@Override
	public boolean signInButtonsVisible() {
		return true;
	}
}