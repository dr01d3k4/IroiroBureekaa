package com.dr01d3k4.iroirobureekaa.input;



import java.util.ArrayList;
import java.util.List;



import android.annotation.TargetApi;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;



import com.dr01d3k4.iroirobureekaa.Pool;
import com.dr01d3k4.iroirobureekaa.Pool.PoolObjectFactory;



@TargetApi (Build.VERSION_CODES.FROYO)
public class MultiTouchHandler implements TouchHandler {
	private final boolean[] isTouched = new boolean[Input.MAX_TOUCHPOINTS];
	private final int[] touchX = new int[Input.MAX_TOUCHPOINTS];
	private final int[] touchY = new int[Input.MAX_TOUCHPOINTS];
	private final int[] id = new int[Input.MAX_TOUCHPOINTS];
	private Pool<TouchEvent> touchEventPool;
	private final List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	private final List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	private float scaleX;
	private float scaleY;
	
	
	
	public MultiTouchHandler(final View view, final float scaleX, final float scaleY) {
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
			final int action = event.getAction() & MotionEvent.ACTION_MASK;
			final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int pointerCount = event.getPointerCount();
			TouchEvent touchEvent;
			
			for (int i = 0; i < Input.MAX_TOUCHPOINTS; i++) {
				if (i >= pointerCount) {
					isTouched[i] = false;
					id[i] = -1;
					continue;
				}
				
				final int pointerId = event.getPointerId(i);
				
				if ((event.getAction() != MotionEvent.ACTION_MOVE) && (i != pointerIndex)) {
					continue;
				}
				
				switch (action) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_POINTER_DOWN:
						touchEvent = touchEventPool.newObject();
						touchEvent.type = TouchEvent.TOUCH_DOWN;
						touchEvent.pointer = pointerId;
						touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
						touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
						isTouched[i] = true;
						id[i] = pointerId;
						touchEventsBuffer.add(touchEvent);
						break;
					
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_POINTER_UP:
					case MotionEvent.ACTION_CANCEL:
						touchEvent = touchEventPool.newObject();
						touchEvent.type = TouchEvent.TOUCH_UP;
						touchEvent.pointer = pointerId;
						touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
						touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
						isTouched[i] = false;
						id[i] = pointerId;
						touchEventsBuffer.add(touchEvent);
						break;
					
					case MotionEvent.ACTION_MOVE:
						touchEvent = touchEventPool.newObject();
						touchEvent.type = TouchEvent.TOUCH_DRAGGED;
						touchEvent.pointer = pointerId;
						touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
						touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
						isTouched[i] = true;
						id[i] = pointerId;
						touchEventsBuffer.add(touchEvent);
						break;
				
				}
			}
			
			return true;
		}
	}
	
	
	
	@Override
	public boolean isTouchDown(final int pointer) {
		synchronized (this) {
			final int index = getIndex(pointer);
			if ((index < 0) || (index >= Input.MAX_TOUCHPOINTS)) {
				return false;
			} else {
				return isTouched[index];
			}
		}
	}
	
	
	
	@Override
	public int getTouchX(final int pointer) {
		synchronized (this) {
			final int index = getIndex(pointer);
			if ((index < 0) || (index >= Input.MAX_TOUCHPOINTS)) {
				return 0;
			} else {
				return touchX[index];
			}
		}
	}
	
	
	
	@Override
	public int getTouchY(final int pointer) {
		synchronized (this) {
			final int index = getIndex(pointer);
			if ((index < 0) || (index >= Input.MAX_TOUCHPOINTS)) {
				return 0;
			} else {
				return touchY[index];
			}
		}
	}
	
	
	
	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			final int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
	
	
	
	private int getIndex(final int pointerId) {
		for (int i = 0; i < Input.MAX_TOUCHPOINTS; i++) {
			if (id[i] == pointerId) {
				return i;
			}
		}
		return -1;
	}
	
	
	
	@Override
	public void clearTouches() {
		touchEvents.clear();
		touchEventsBuffer.clear();
		for (int i = 0; i < Input.MAX_TOUCHPOINTS; i++) {
			isTouched[i] = false;
			touchX[i] = 0;
			touchX[i] = 0;
			id[i] = -1;
		}
		
	}
}