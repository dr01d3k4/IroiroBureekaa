package com.dr01d3k4.iroirobureekaa;



import java.util.List;



import android.graphics.Color;
import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.game.gamemode.GameMode;
import com.dr01d3k4.iroirobureekaa.input.Input;
import com.dr01d3k4.iroirobureekaa.input.TouchEvent;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class GameModeSelectScreen extends Screen {
	public TextButton[] buttons;
	public Text titleText;
	
	
	
	public GameModeSelectScreen(final IroiroBureekaa mainActivity) {
		super(mainActivity);
		
		buttons = new TextButton[GameMode.MODES.length];
		
	}
	
	
	
	@Override
	public void calculateSize() {
		super.calculateSize();
		final int pausedTextY = getDimensionPixel(R.dimen.title_text_y); // (int) (CANVAS_HEIGHT * 0.1);
		final int pausedTextHeight = getDimensionPixel(R.dimen.title_text_height); // (int) (CANVAS_HEIGHT * 0.15);
		
		titleText = new Text(getString(R.string.select_mode), 0, pausedTextY, CANVAS_WIDTH, pausedTextHeight);
		// titleText = new Text(getString(R.string.select_mode), (int) (CANVAS_WIDTH * 0.05), (int) (CANVAS_HEIGHT * 0.1),
		// (int) (CANVAS_WIDTH * 0.9), (int) (CANVAS_HEIGHT * 0.12));
		
		final int buttonWidth = getDimensionPixel(R.dimen.button_width); // (int) (CANVAS_WIDTH * 0.8);
		final int buttonX = (CANVAS_WIDTH - buttonWidth) / 2;
		final int buttonHeight = getDimensionPixel(R.dimen.button_height); // (int) (CANVAS_HEIGHT * 0.1);
		final int buttonMargin = getDimensionPixel(R.dimen.button_margin); // (int) (CANVAS_HEIGHT * 0.1);
		
		final int buttonY = pausedTextY + pausedTextHeight + buttonMargin; // (int) (CANVAS_HEIGHT * 0.3);
		
		for (int i = 0, length = GameMode.MODES.length; i < length; i += 1) {
			buttons[i] = new TextButton(getString(GameMode.getTitle(i)), buttonX, buttonY
				+ (i * (buttonHeight + buttonMargin)), buttonWidth, buttonHeight);
		}
	}
	
	
	
	@Override
	public void update(final float deltaTime) {
		final List<TouchEvent> touchEvents = input.getTouchEvents();
		
		for (int i = 0, length = buttons.length; i < length; i += 1) {
			buttons[i].hovered = false;
		}
		
		int x;
		int y;
		boolean down;
		for (int pointer = 0, length = Input.MAX_TOUCHPOINTS; pointer < length; pointer += 1) {
			x = input.getTouchX(pointer);
			y = input.getTouchY(pointer);
			down = input.isTouchDown(pointer);
			
			if (down) {
				for (int i = 0, buttonsLength = buttons.length; i < buttonsLength; i += 1) {
					if (buttons[i].visible && buttons[i].isOver(x, y)) {
						buttons[i].hovered = true;
					}
				}
			}
		}
		
		TouchEvent touchEvent;
		for (int i = 0, length = touchEvents.size(); i < length; i += 1) {
			touchEvent = touchEvents.get(i);
			x = touchEvent.x;
			y = touchEvent.y;
			
			if (touchEvent.type == TouchEvent.TOUCH_UP) {
				for (int b = 0, buttonsLength = buttons.length; b < buttonsLength; b += 1) {
					if (buttons[b].visible && buttons[b].isOver(x, y)) {
						mainActivity.playSound(Assets.buttonSelect);
						input.clearTouches();
						mainActivity.changeScreen(new GameScreen(mainActivity, b));
						return;
					}
				}
			}
		}
	}
	
	
	
	@Override
	public void render(final float deltaTime) {
		final Graphics graphics = getGraphics();
		final Paint paint = graphics.getPaint();
		
		graphics.clear(Color.WHITE);
		
		titleText.render(graphics, paint);
		
		for (final TextButton button : buttons) {
			button.render(graphics, paint);
		}
	}
	
	
	
	@Override
	public void onBackPressed() {
		mainActivity.changeScreen(new MainMenuScreen(mainActivity));
	}
}