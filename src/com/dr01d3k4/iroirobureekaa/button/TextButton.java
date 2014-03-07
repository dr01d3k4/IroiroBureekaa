package com.dr01d3k4.iroirobureekaa.button;



import android.graphics.Color;
import android.graphics.Paint;



import com.dr01d3k4.iroirobureekaa.Text;
import com.dr01d3k4.iroirobureekaa.game.GameColour;
import com.dr01d3k4.iroirobureekaa.render.Graphics;



public final class TextButton extends Button {
	public Text text;
	public int backgroundColour = Color.WHITE; // GameColour.UI_LIGHT;
	public int hoveredColour = GameColour.UI_LIGHT; // GameColour.UI_DARK;
	
	
	public TextButton(final String rawText, final int x, final int y, final int width, final int height) {
		this(rawText, x, y, width, height, null);
	}
	
	
	public TextButton(final String rawText, final int x, final int y, final int width, final int height,
		OnButtonClickListener clickListener) {
		super(x, y, width, height, clickListener);
		text = new Text(rawText, x, y, width, height);
	}
	
	
	
	@Override
	public void render(final Graphics graphics, final Paint paint) {
		if (!visible) {
			return;
		}
		
		graphics.drawRectangle(x, y, width, height, hovered ? hoveredColour : backgroundColour);
		paint.setStrokeWidth(2);
		graphics.outlineRectangle(x, y, width, height, GameColour.OUTLINE);
		text.render(graphics, paint);
		paint.setStrokeWidth(1);
	}
}
