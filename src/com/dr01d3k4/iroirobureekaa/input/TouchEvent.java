package com.dr01d3k4.iroirobureekaa.input;



public final class TouchEvent {
	public static final int TOUCH_DOWN = 0;
	public static final int TOUCH_UP = 1;
	public static final int TOUCH_DRAGGED = 2;
	
	public int type;
	public int x, y;
	public int pointer;
}