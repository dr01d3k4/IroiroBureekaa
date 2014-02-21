package com.dr01d3k4.iroirobureekaa;



import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;



import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public class Text {
	public String text;
	public int x;
	public int y;
	public int maxWidth;
	public int maxHeight;
	public float textSize;
	public float textBaseline;
	public float textScaleX;
	public Align align;
	
	
	
	public Text(final String text, final int x, final int y, final int maxWidth, final int maxHeight,
		final Align align) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		
		this.align = align;
		calculateTextData(new Paint());
	}
	
	
	
	public Text(final String text, final int x, final int y, final int maxWidth, final int maxHeight) {
		this(text, x, y, maxWidth, maxHeight, Align.CENTER);
	}
	
	
	
	public void setText(final String text) {
		this.text = text;
		calculateTextData(new Paint());
	}
	
	
	
	public void render(final Graphics graphics, final Paint paint) {
		paint.setTextSize(textSize);
		paint.setTextScaleX(textScaleX);
		paint.setTextAlign(align);
		final int renderX = (align == Align.LEFT) ? x : ((align == Align.CENTER) ? x + (maxWidth / 2) : x
			+ maxWidth);
		graphics.drawText(text, renderX, (int) ((y + maxHeight) - textBaseline), GameColour.TEXT);
	}
	
	
	
	public int getRight() {
		final Paint paint = new Paint();
		paint.setTextSize(textSize);
		paint.setTextScaleX(textScaleX);
		paint.setTextAlign(align);
		
		return (int) (x + paint.measureText(text));
	}
	
	
	
	private void calculateTextData(final Paint paint) {
		paint.setTextSize(100);
		paint.setTextScaleX(1.0f);
		
		final Rect bounds = new Rect();
		paint.getTextBounds(text + "|", 0, text.length() + 1, bounds);
		
		int textHeight = bounds.bottom - bounds.top;
		
		final float target = maxHeight * 0.7f;
		textSize = (target / textHeight) * 100f;
		
		paint.setTextSize(textSize);
		paint.setTextScaleX(1.0f);
		
		paint.getTextBounds(text + "|", 0, text.length() + 1, bounds);
		
		final int textWidth = bounds.right - bounds.left;
		textHeight = bounds.bottom - bounds.top;
		
		textBaseline = bounds.bottom + ((maxHeight - textHeight) / 2);
		textScaleX = (float) maxWidth / textWidth;
		textScaleX = 1.0f;
	}
}