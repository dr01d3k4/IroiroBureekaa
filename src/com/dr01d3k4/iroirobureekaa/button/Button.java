package com.dr01d3k4.iroirobureekaa.button;



import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.render.Graphics;



public abstract class Button {
	public int x;
	public int y;
	public int width;
	public int height;
	public boolean hovered = false;
	public boolean visible = true;
	public final OnButtonClickListener clickListener;
	
	
	
	public Button(final int x, final int y, final int width, final int height) {
		this(x, y, width, height, null);
	}
	
	
	
	public Button(final int x, final int y, final int width, final int height, OnButtonClickListener clickListener) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.clickListener = clickListener;
		hovered = false;
		visible = true;
	}
	
	
	
	public void onClick() {
		if (!visible) {
			return;
		}
		clickListener.onClick();
	}
	
	
	
	public boolean isOver(final int testX, final int testY) {
		return ((testX >= x) && (testY >= y) && (testX <= (x + width)) && (testY <= (y + height)));
	}
	
	
	
	public abstract void render(final Graphics graphics, final Paint paint);
}
