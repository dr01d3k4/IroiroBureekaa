package com.dr01d3k4.iroirobureekaa.button;



import java.util.ArrayList;
import java.util.List;



import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.input.Input;
import com.dr01d3k4.iroirobureekaa.input.TouchEvent;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public class ButtonManager {
	public List<Button> buttons;
	
	
	
	public ButtonManager() {
		buttons = new ArrayList<Button>();
	}
	
	
	
	public Button addButton(Button button) {
		buttons.add(button);
		return button;
	}
	
	
	
	public void update(Input input) {
		final List<TouchEvent> touchEvents = input.getTouchEvents();
		
		for (int i = 0, length = buttons.size(); i < length; i += 1) {
			buttons.get(i).hovered = false;
		}
		
		int x;
		int y;
		boolean down;
		Button button;
		
		for (int pointer = 0, length = Input.MAX_TOUCHPOINTS; pointer < length; pointer += 1) {
			x = input.getTouchX(pointer);
			y = input.getTouchY(pointer);
			down = input.isTouchDown(pointer);
			
			if (!down) {
				continue;
			}
			
			for (int i = 0, buttonCount = buttons.size(); i < buttonCount; i += 1) {
				button = buttons.get(i);
				if (button.visible && button.isOver(x, y)) {
					button.hovered = true;
				}
			}
		}
		
		TouchEvent touchEvent;
		for (int i = 0, length = touchEvents.size(); i < length; i += 1) {
			touchEvent = touchEvents.get(i);
			x = touchEvent.x;
			y = touchEvent.y;
			
			if (touchEvent.type != TouchEvent.TOUCH_UP) {
				continue;
			}
			
			for (int b = 0, buttonCount = buttons.size(); b < buttonCount; b += 1) {
				button = buttons.get(b);
				if (button.visible && button.isOver(x, y)) {
					button.onClick();
					return;
				}
			}
		}
	}
	
	
	
	public void render(Graphics graphics, Paint paint) {
		for (int i = 0, length = buttons.size(); i < length; i += 1) {
			buttons.get(i).render(graphics, paint);
		}
	}
	
	
	
	public void clearButtons() {
		buttons.clear();
	}
}
