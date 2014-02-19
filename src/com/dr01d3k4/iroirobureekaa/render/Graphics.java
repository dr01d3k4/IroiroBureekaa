package com.dr01d3k4.iroirobureekaa.render;



import java.io.IOException;
import java.io.InputStream;



import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;



import com.dr01d3k4.iroirobureekaa.render.Pixmap.PixmapFormat;



public class Graphics {
	private final AssetManager assets;
	private final Bitmap frameBuffer;
	private final Canvas canvas;
	private final Paint paint;
	private final Rect sourceRectangle = new Rect();
	private final Rect destinationRectangle = new Rect();
	
	
	
	public Graphics(final AssetManager assets, final Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		canvas = new Canvas(frameBuffer);
		paint = new Paint();
	}
	
	
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	
	
	public Paint getPaint() {
		return paint;
	}
	
	
	
	public Pixmap newPixmap(final String filename, PixmapFormat format) {
		Config config = null;
		if (format == PixmapFormat.RGB565) {
			config = Config.RGB_565;
		} else if (format == PixmapFormat.ARGB4444) {
			config = Config.ARGB_4444;
		} else {
			config = Config.ARGB_8888;
		}
		
		final Options options = new Options();
		options.inPreferredConfig = config;
		
		InputStream inputStream = null;
		Bitmap bitmap = null;
		
		try {
			inputStream = assets.open(filename);
			bitmap = BitmapFactory.decodeStream(inputStream);
			if (bitmap == null) {
				throw new RuntimeException("Couldn't load bitmap from asset \"" + filename + "\"");
			}
		} catch (final IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset \"" + filename + "\"");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {}
			}
		}
		
		if (bitmap.getConfig() == Config.RGB_565) {
			format = PixmapFormat.RGB565;
		} else if (bitmap.getConfig() == Config.ARGB_4444) {
			format = PixmapFormat.ARGB4444;
		} else {
			format = PixmapFormat.ARGB8888;
		}
		
		return new Pixmap(bitmap, format);
	}
	
	
	
	public void clear(final int color) {
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
	}
	
	
	
	public void drawPixel(final int x, final int y, final int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}
	
	
	
	public void drawLine(final int x, final int y, final int x2, final int y2, final int color) {
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}
	
	
	
	public void outlineRectangle(final int x, final int y, final int width, final int height, final int color) {
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(x, y, (x + width), (y + height), paint);
	}
	
	
	
	public void drawRectangle(final int x, final int y, final int width, final int height, final int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, (x + width), (y + height), paint);
	}
	
	
	
	
	
	public void drawPixmap(final Pixmap pixmap, final int x, final int y, final int sourceX, final int sourceY,
		final int sourceWidth, final int sourceHeight, final int destinationWidth, final int destinationHeight) {
		sourceRectangle.left = sourceX;
		sourceRectangle.top = sourceY;
		sourceRectangle.right = (sourceX + sourceWidth) - 1;
		sourceRectangle.bottom = (sourceY + sourceHeight) - 1;
		
		destinationRectangle.left = x;
		destinationRectangle.top = y;
		destinationRectangle.right = (x + destinationWidth) - 1;
		destinationRectangle.bottom = (y + destinationHeight) - 1;
		
		canvas.drawBitmap(pixmap.bitmap, sourceRectangle, destinationRectangle, null);
	}
	
	
	
	public void drawPixmap(final Pixmap pixmap, final int x, final int y) {
		canvas.drawBitmap(pixmap.bitmap, x, y, null);
	}
	
	
	
	public void drawText(final String text, final int x, final int y, final int colour) {
		paint.setColor(colour);
		paint.setStyle(Style.FILL);
		canvas.drawText(text, x, y, paint);
	}
	
	
	
	public int getWidth() {
		return frameBuffer.getWidth();
	}
	
	
	
	public int getHeight() {
		return frameBuffer.getHeight();
	}
}
