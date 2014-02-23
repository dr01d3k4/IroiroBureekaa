package com.dr01d3k4.iroirobureekaa;



import android.graphics.Color;



import com.dr01d3k4.iroirobureekaa.render.Graphics;
import com.dr01d3k4.iroirobureekaa.render.Pixmap.PixmapFormat;
import com.dr01d3k4.iroirobureekaa.saving.Settings;



public class LoadingScreen extends Screen {
	private Text loadingText;
	private boolean loaded = false;
	private float time = 0;
	private final float MIN_TIME = 1;
	
	
	
	public LoadingScreen(final IroiroBureekaa mainActivity) {
		super(mainActivity);
	}
	
	
	
	@Override
	public void calculateSize() {
		super.calculateSize();
		
		final int loadingTextHeight = (int) (CANVAS_HEIGHT * 0.15);
		loadingText = new Text(getString(R.string.loading), 0, (CANVAS_HEIGHT / 2) - (loadingTextHeight / 2),
			CANVAS_WIDTH, loadingTextHeight);
	}
	
	
	
	@Override
	public void update(final float deltaTime) {
		if (!loaded) {
			final Graphics graphics = mainActivity.getGraphics();
			Assets.blocks = graphics.newPixmap("blocks.png", PixmapFormat.RGB565);
			
			mainActivity.gameData = Settings.load(mainActivity.getFileIO());
			// android.util.Log.d("Loaded sound enabled", Boolean.toString(mainActivity.gameData.soundEnabled));
			// android.util.Log.d("Loaded highscores", mainActivity.gameData.highscoresToString());
			
			loaded = true;
		}
		
		time += deltaTime;
		if (time >= MIN_TIME) {
			mainActivity.changeScreen(new MainMenuScreen(mainActivity));
		}
		
	}
	
	
	
	@Override
	public void render(final float deltaTime) {
		final Graphics graphics = getGraphics();
		graphics.clear(Color.WHITE);
		loadingText.render(graphics, graphics.getPaint());
	}
}