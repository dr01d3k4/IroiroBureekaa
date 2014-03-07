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
	public static Sound blockLand;
	public static Sound blockHighlight;
	public static Sound wildBlockHighlight;
	public static Sound blockDestroy;
	public static Sound newRowAdd;
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
				if (newBitmap.getPixel(x, y) == 0xffff00ff) {
					newBitmap.setPixel(x, y, 0x00ffffff);
				}
			}
		}
		
		textures.bitmap.recycle();
		textures.bitmap = newBitmap;
		
		final Audio audio = game.getAudio();
		buttonSelect = audio.newSound("button_select.wav");
		blockLand = audio.newSound("block_land.wav");
		blockHighlight = audio.newSound("block_highlight.wav");
		wildBlockHighlight = audio.newSound("wild_block_highlight.wav");
		blockDestroy = audio.newSound("block_destroy.wav");
		newRowAdd = audio.newSound("new_row_add.wav");
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
		
		blockLand.dispose();
		blockLand = null;
		
		blockHighlight.dispose();
		blockHighlight = null;
		
		wildBlockHighlight.dispose();
		wildBlockHighlight = null;
		
		blockDestroy.dispose();
		blockDestroy = null;
		
		newRowAdd.dispose();
		newRowAdd = null;
		
		loaded = false;
	}
}