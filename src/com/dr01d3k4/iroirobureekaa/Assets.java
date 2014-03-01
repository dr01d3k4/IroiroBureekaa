package com.dr01d3k4.iroirobureekaa;



import android.graphics.Bitmap;



import com.dr01d3k4.iroirobureekaa.audio.Audio;
import com.dr01d3k4.iroirobureekaa.audio.Sound;
import com.dr01d3k4.iroirobureekaa.render.Pixmap;
import com.dr01d3k4.iroirobureekaa.render.Pixmap.PixmapFormat;



public final class Assets {
	private static boolean loaded = false;
	public static Pixmap textures;
	public static Sound buttonSelect;
	public static Sound blockDestroy;
	public static float SOUND_VOLUME = 1f;
	
	
	
	public static void loadAssets(final IroiroBureekaa game) {
		if (loaded) {
			return;
		}
		
		loaded = true;
		
		android.util.Log.d("Assets", "Loading assets");
		textures = game.getGraphics().newPixmap("textures.png", PixmapFormat.ARGB8888);
		
		final int width = textures.bitmap.getWidth();
		final int height = textures.bitmap.getHeight();
		final Bitmap newBitmap = textures.bitmap.copy(textures.bitmap.getConfig(), true);
		
		for (int x = 0; x < width; x += 1) {
			for (int y = 0; y < height; y += 1) {
				if (newBitmap.getPixel(x, y) == 0xffffffff) {
					newBitmap.setPixel(x, y, 0x00000000);
				}
			}
		}
		
		textures.bitmap.recycle();
		textures.bitmap = newBitmap;
		
		final Audio audio = game.getAudio();
		buttonSelect = audio.newSound("button_select.wav");
		blockDestroy = audio.newSound("block_destroy.wav");
	}
	
	
	
	public static void dispose() {
		if (!loaded) {
			return;
		}
		
		android.util.Log.d("Assets", "Disposing assets");
		textures.dispose();
		textures = null;
		
		buttonSelect.dispose();
		buttonSelect = null;
		
		blockDestroy.dispose();
		blockDestroy = null;
		
		loaded = false;
	}
}