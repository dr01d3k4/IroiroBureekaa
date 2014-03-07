package com.dr01d3k4.iroirobureekaa.input;



import java.util.List;



import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.View;



public final class Input {
	public static final int MAX_TOUCHPOINTS = 10;
	private TouchHandler touchHandler;
	
	
	
	public Input(final Context context, final View view, final float scaleX, final float scaleY) {
		if (VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR) {
			touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
		} else {
			touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
		}
	}
	
	
	
	public boolean isTouchDown(final int pointer) {
		return touchHandler.isTouchDown(pointer);
	}
	
	
	
	public int getTouchX(final int pointer) {
		return touchHandler.getTouchX(pointer);
	}
	
	
	
	public int getTouchY(final int pointer) {
		return touchHandler.getTouchY(pointer);
	}
	
	
	
	public List<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}
	
	
	
	public void clearTouches() {
		touchHandler.clearTouches();
	}
}