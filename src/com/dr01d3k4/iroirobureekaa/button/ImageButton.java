package com.dr01d3k4.iroirobureekaa.button;



import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.render.Graphics;
import com.dr01d3k4.iroirobureekaa.render.Pixmap;



public final class ImageButton extends Button {
	public Pixmap backgroundImage;
	public Pixmap hoverImage;
	
	
	
	public ImageButton(final Pixmap backgroundImage, final Pixmap hoverImage, final int x, final int y,
		final int width, final int height) {
		this(backgroundImage, hoverImage, x, y, width, height, null);
	}
	
	
	
	public ImageButton(final Pixmap backgroundImage, final Pixmap hoverImage, final int x, final int y,
		final int width, final int height, OnButtonClickListener clickListener) {
		super(x, y, width, height, clickListener);
		this.backgroundImage = backgroundImage;
		this.hoverImage = hoverImage;
	}
	
	
	
	@Override
	public void render(final Graphics graphics, final Paint paint) {
		if (!visible) {
			return;
		}
		
		graphics.drawPixmap(hovered ? hoverImage : backgroundImage, x, y);
	}
}
