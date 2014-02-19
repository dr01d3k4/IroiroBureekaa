package com.dr01d3k4.iroirobureekaa.render;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



import com.dr01d3k4.iroirobureekaa.IroiroBureekaa;



public class RenderView extends SurfaceView implements Runnable {
	public IroiroBureekaa mainActivity;
	private Bitmap frameBuffer;
	private Thread renderThread = null;
	private SurfaceHolder holder;
	private volatile boolean running = false;
	
	
	
	public RenderView(Context context) {
		super(context);
	}
	
	public RenderView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}
	
	
	
	public void init(final IroiroBureekaa iroiroBureekaa, final Bitmap frameBuffer) {
		mainActivity = iroiroBureekaa;
		this.frameBuffer = frameBuffer;
		holder = getHolder();
	}
	
	
	
	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}
	
	
	
	@Override
	public void run() {
		final Rect destinationRectangle = new Rect();
		long startTime = System.nanoTime();
		Canvas canvas = null;
		float deltaTime = 0;
		
		while (running) {
			if (!holder.getSurface().isValid()) {
				continue;
			}
			
			deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			
			mainActivity.update(deltaTime);
			mainActivity.render(deltaTime);
			
			canvas = holder.lockCanvas();
			if (canvas != null) {
				canvas.getClipBounds(destinationRectangle);
				canvas.drawBitmap(frameBuffer, null, destinationRectangle, null);
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	
	
	public void pause() {
		running = false;
		while (true) {
			try {
				renderThread.join();
				return;
			} catch (final InterruptedException e) {}
		}
	}
}