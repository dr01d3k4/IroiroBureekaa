package com.dr01d3k4.iroirobureekaa;



import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class TextButton {
	public Text text;
	public int x;
	public int y;
	public int width;
	public int height;
	//	public int textX;
	//	public int textY;
	public int backgroundColour = GameColour.UI_LIGHT;
	public int hoveredColour = GameColour.UI_DARK;
	public boolean hovered;
	public boolean visible = true;
	
	
	
	public TextButton(final String rawText, final int x, final int y, final int width, final int height) {
		text = new Text(rawText, x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		visible = true;
		//		
		//		textX = x + (width / 2);
		//		textY = y + (int) (height * 0.7);
	}
	
	
	
	public boolean isOver(final int testX, final int testY) {
		return ((testX >= x) && (testY >= y) && (testX <= (x + width)) && (testY <= (y + height)));
	}
	
	
	
	public void render(final Graphics graphics, final Paint paint) {
		if (!visible) {
			return;
		}
		
		graphics.drawRectangle(x, y, width, height, hovered ? hoveredColour : backgroundColour);
		paint.setStrokeWidth(2);
		graphics.outlineRectangle(x, y, width, height, GameColour.OUTLINE);
		text.render(graphics, paint);
		//		paint.setTextAlign(Align.CENTER);
		//		paint.setTextSize(64);
		// graphics.drawText(text, textX, textY, GameColour.TEXT);
		paint.setStrokeWidth(1);
	}
}
