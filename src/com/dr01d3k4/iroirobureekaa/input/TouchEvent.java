package com.dr01d3k4.iroirobureekaa.input;



public class TouchEvent {
	public static final int TOUCH_DOWN = 0;
	public static final int TOUCH_UP = 1;
	public static final int TOUCH_DRAGGED = 2;
	
	public int type;
	public int x, y;
	public int pointer;
	
	
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Touch: ");
		
		if (type == TOUCH_DOWN) {
			builder.append("down");
		} else if (type == TOUCH_DRAGGED) {
			builder.append("dragged");
		} else {
			builder.append("up");
		}
		
		builder.append("; Pointer: ");
		builder.append(pointer);
		builder.append("; Coordinates: (");
		builder.append(x);
		builder.append(", ");
		builder.append(y);
		builder.append(")");
		
		return builder.toString();
	}
}