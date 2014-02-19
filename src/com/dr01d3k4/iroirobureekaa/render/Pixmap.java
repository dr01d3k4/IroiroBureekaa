package com.dr01d3k4.iroirobureekaa.render;



import android.graphics.Bitmap;



public class Pixmap {
	public static enum PixmapFormat {
		ARGB8888, ARGB4444, RGB565
	}
	
	
	
	public Bitmap bitmap;
	private final PixmapFormat format;
	
	
	
	public Pixmap(final Bitmap bitmap, final PixmapFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}
	
	
	
	public int getWidth() {
		return bitmap.getWidth();
	}
	
	
	
	public int getHeight() {
		return bitmap.getHeight();
	}
	
	
	
	public PixmapFormat getFormat() {
		return format;
	}
	
	
	
	public void dispose() {
		bitmap.recycle();
	}
}