package com.dr01d3k4.iroirobureekaa.input;



import java.util.ArrayList;
import java.util.List;



import android.view.MotionEvent;
import android.view.View;



import com.dr01d3k4.iroirobureekaa.Pool;
import com.dr01d3k4.iroirobureekaa.Pool.PoolObjectFactory;



public class SingleTouchHandler implements TouchHandler {
	private boolean isTouched;
	private int touchX;
	private int touchY;
	private Pool<TouchEvent> touchEventPool;
	private final List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	private final List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	private float scaleX;
	private float scaleY;
	
	
	
	public SingleTouchHandler(final View view, final float scaleX, final float scaleY) {
		final PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	
	
	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		synchronized (this) {
			final TouchEvent touchEvent = touchEventPool.newObject();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					touchEvent.type = TouchEvent.TOUCH_DOWN;
					isTouched = true;
					break;
				
				case (MotionEvent.ACTION_MOVE):
					touchEvent.type = TouchEvent.TOUCH_DRAGGED;
					isTouched = true;
					break;
				
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					touchEvent.type = TouchEvent.TOUCH_UP;
					isTouched = false;
					break;
			}
			
			touchEvent.x = touchX = (int) (event.getX() * scaleX);
			touchEvent.y = touchY = (int) (event.getY() * scaleY);
			touchEventsBuffer.add(touchEvent);
			
			return true;
		}
	}
	
	
	
	@Override
	public boolean isTouchDown(final int pointer) {
		synchronized (this) {
			if (pointer == 0) {
				return isTouched;
			} else {
				return false;
			}
		}
	}
	
	
	
	@Override
	public int getTouchX(final int pointer) {
		synchronized (this) {
			return touchX;
		}
	}
	
	
	
	@Override
	public int getTouchY(final int pointer) {
		synchronized (this) {
			return touchY;
		}
	}
	
	
	
	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			final int length = touchEvents.size();
			for (int i = 0; i < length; i += 1) {
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
	
	
	
	@Override
	public void clearTouches() {
		touchEvents.clear();
		touchEventsBuffer.clear();
		isTouched = false;
		touchX = 0;
		touchY = 0;
	}
}
