package com.dr01d3k4.iroirobureekaa;



import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.render.Graphics;
import com.dr01d3k4.iroirobureekaa.render.Pixmap;



public final class ImageButton {
	public Pixmap backgroundImage;
	public Pixmap hoverImage;
	public int x;
	public int y;
	public int width;
	public int height;
	public boolean hovered;
	public boolean visible = true;
	
	
	
	public ImageButton(final Pixmap backgroundImage, final Pixmap hoverImage, final int x, final int y,
		final int width, final int height) {
		this.backgroundImage = backgroundImage;
		this.hoverImage = hoverImage;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	
	
	public boolean isOver(final int testX, final int testY) {
		return ((testX >= x) && (testY >= y) && (testX <= (x + width)) && (testY <= (y + height)));
	}
	
	
	
	public void render(final Graphics graphics, final Paint paint) {
		if (!visible) {
			return;
		}
		
		graphics.drawPixmap(hovered ? hoverImage : backgroundImage, x, y);
	}
}
