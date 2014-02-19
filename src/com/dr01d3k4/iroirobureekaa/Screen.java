package com.dr01d3k4.iroirobureekaa;



import com.dr01d3k4.iroirobureekaa.input.Input;



public abstract class Screen {
	public final IroiroBureekaa mainActivity;
	public final Input input;
	public int CANVAS_WIDTH;
	public int CANVAS_HEIGHT;
	
	
	
	public Screen(final IroiroBureekaa mainActivity) {
		this.mainActivity = mainActivity;
		input = mainActivity.getInput();
	}
	
	
	
	public void calculateSize() {
		CANVAS_WIDTH = mainActivity.CANVAS_WIDTH;
		CANVAS_HEIGHT = mainActivity.CANVAS_HEIGHT;
	}
	
	
	
	public void resume() {}
	
	
	
	public abstract void update(float deltaTime);
	
	
	
	public abstract void render(float deltaTime);
	
	
	
	public void pause() {}
	
	
	
	public void dispose() {}
	
	
	
	public String getString(final int id) {
		return mainActivity.getResources().getString(id);
	}
	
	
	
	public boolean signInButtonsVisible() {
		return false;
	}
	
	
	
	public void onBackPressed() {}
}