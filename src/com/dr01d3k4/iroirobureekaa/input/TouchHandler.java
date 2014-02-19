package com.dr01d3k4.iroirobureekaa.input;



import java.util.List;



import android.view.View.OnTouchListener;



public interface TouchHandler extends OnTouchListener {
	public boolean isTouchDown(int pointer);
	
	
	
	public int getTouchX(int pointer);
	
	
	
	public int getTouchY(int pointer);
	
	
	
	public List<TouchEvent> getTouchEvents();
	
	
	
	public void clearTouches();
}
