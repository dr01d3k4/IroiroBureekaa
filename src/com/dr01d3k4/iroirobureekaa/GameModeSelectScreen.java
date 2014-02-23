package com.dr01d3k4.iroirobureekaa;



import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.game.gamemode.GameMode;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public class GameModeSelectScreen extends Screen {
	public Button[] buttons;
	
	
	
	public GameModeSelectScreen(IroiroBureekaa mainActivity) {
		super(mainActivity);
		
		buttons = new Button[0];
	}
	
	
	
	public void calculateSize() {
		super.calculateSize();
	}
	
	
	
	@Override
	public void update(float deltaTime) {
		mainActivity.changeScreen(new GameScreen(mainActivity, GameMode.TIMED));
	}
	
	
	
	@Override
	public void render(float deltaTime) {
		Graphics graphics = getGraphics();
		Paint paint = graphics.getPaint();
		
		int length = buttons.length;
		for (int i = 0; i < length; i += 1) {
			buttons[i].render(graphics, paint);
		}
	}
}