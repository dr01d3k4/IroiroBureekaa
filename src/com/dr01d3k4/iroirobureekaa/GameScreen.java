package com.dr01d3k4.iroirobureekaa;



import java.util.List;



import android.app.AlertDialog;
import android.content.DialogInterface;



import com.dr01d3k4.iroirobureekaa.game.GameRenderer;
import com.dr01d3k4.iroirobureekaa.game.GameWorld;
import com.dr01d3k4.iroirobureekaa.game.GameWorld.GameState;
import com.dr01d3k4.iroirobureekaa.game.Grid;
import com.dr01d3k4.iroirobureekaa.game.WorldListener;
import com.dr01d3k4.iroirobureekaa.game.gamemode.GameMode;
import com.dr01d3k4.iroirobureekaa.input.Input;
import com.dr01d3k4.iroirobureekaa.input.TouchEvent;
import com.dr01d3k4.iroirobureekaa.render.Graphics;
import com.dr01d3k4.iroirobureekaa.render.Pixmap;



public final class GameScreen extends Screen {
	public final GameWorld world;
	private GameRenderer renderer;
	private final WorldListener worldListener;
	private final GameMode gameMode;
	private final int gameModeId;
	
	private final int gridWidth;
	private final int gridHeight;
	
	public int HEADER_HEIGHT;
	public int FOOTER_HEIGHT;
	
	public int GRID_PIXEL_WIDTH;
	public int GRID_PIXEL_HEIGHT;
	
	public int CELL_SIZE;
	public final int IMAGE_CELL_SIZE = 48;
	public final int MOVEMENT_BUTTON_WIDTH = 110;
	public final int MOVEMENT_BUTTON_HEIGHT = 33;
	
	public int TRANSLATE_CENTER_X;
	public int TRANSLATE_CENTER_Y;
	
	public int BOTTOM_ROW_HEIGHT;
	public int BOTTOM_ROW_Y;
	
	public int BOTTOM_ROW_CELL_FULL_WIDTH;
	public int BOTTOM_ROW_CELL_WIDTH;
	public int BOTTOM_ROW_CELL_X_OFFSET;
	public int BOTTOM_ROW_CELL_HEIGHT;
	public int BOTTOM_ROW_CELL_Y;
	
	public int PAUSED_TEXT_Y;
	public int PAUSED_TEXT_HEIGHT;
	
	private boolean isPaused = false;
	private boolean askingQuit = false;
	private final boolean onScreenControls = true;
	
	public TextButton pauseButton;
	public TextButton resumeButton;
	public TextButton pauseQuitButton;
	
	public ImageButton leftButton;
	public ImageButton downButton;
	public ImageButton rightButton;
	
	
	
	public GameScreen(final IroiroBureekaa mainActivity, final int gameModeId) {
		super(mainActivity);
		gridWidth = Grid.WIDTH;
		gridHeight = Grid.HEIGHT;
		
		worldListener = new WorldListener(mainActivity);
		
		world = new GameWorld(gridWidth, gridHeight, worldListener);
		world.init();
		
		this.gameModeId = gameModeId;
		gameMode = GameMode.idToMode(gameModeId, this);
	}
	
	
	
	@Override
	public void calculateSize() {
		super.calculateSize();
		
		HEADER_HEIGHT = getDimensionPixel(R.dimen.game_header_height); // (int) (CANVAS_HEIGHT * 0.1);
		
		if (onScreenControls) {
			FOOTER_HEIGHT = getDimensionPixel(R.dimen.game_on_screen_control_height); // (int) (CANVAS_HEIGHT * 0.1);
		} else {
			FOOTER_HEIGHT = 0;
		}
		
		final int maxHeight = CANVAS_HEIGHT - (HEADER_HEIGHT + FOOTER_HEIGHT);
		
		BOTTOM_ROW_HEIGHT = getDimensionPixel(R.dimen.game_bottom_row_height); // (int) (CANVAS_HEIGHT * 0.02);
		
		int tempCellSize = CANVAS_WIDTH / gridWidth;
		
		if ((tempCellSize * gridHeight) > maxHeight) {
			tempCellSize = (maxHeight - BOTTOM_ROW_HEIGHT) / gridHeight;
		}
		
		final int tempGridPixelWidth = tempCellSize * gridWidth;
		final int tempGridPixelHeight = (tempCellSize * gridHeight) + BOTTOM_ROW_HEIGHT;
		
		CELL_SIZE = tempCellSize;
		GRID_PIXEL_WIDTH = tempGridPixelWidth;
		GRID_PIXEL_HEIGHT = tempGridPixelHeight;
		
		BOTTOM_ROW_Y = GRID_PIXEL_HEIGHT - BOTTOM_ROW_HEIGHT;
		BOTTOM_ROW_CELL_FULL_WIDTH = GRID_PIXEL_WIDTH / gridWidth;
		BOTTOM_ROW_CELL_WIDTH = (int) (BOTTOM_ROW_CELL_FULL_WIDTH * 0.8);
		BOTTOM_ROW_CELL_X_OFFSET = (BOTTOM_ROW_CELL_FULL_WIDTH - BOTTOM_ROW_CELL_WIDTH) / 2;
		BOTTOM_ROW_CELL_HEIGHT = (int) (BOTTOM_ROW_HEIGHT * 0.9);
		BOTTOM_ROW_CELL_Y = BOTTOM_ROW_Y + (BOTTOM_ROW_HEIGHT - BOTTOM_ROW_CELL_HEIGHT);
		
		TRANSLATE_CENTER_X = (CANVAS_WIDTH - GRID_PIXEL_WIDTH) / 2;
		TRANSLATE_CENTER_Y = HEADER_HEIGHT + ((maxHeight - GRID_PIXEL_HEIGHT) / 2);
		
		final int pauseButtonInset = getDimensionPixel(R.dimen.pause_button_inset); // (HEADER_HEIGHT - pauseButtonSize) / 2;
		final int pauseButtonSize = HEADER_HEIGHT - (2 * pauseButtonInset); // (int) (HEADER_HEIGHT * 0.8);
		pauseButton = new TextButton("||", CANVAS_WIDTH - pauseButtonSize - pauseButtonInset, pauseButtonInset,
			pauseButtonSize, pauseButtonSize);
		
		PAUSED_TEXT_Y = getDimensionPixel(R.dimen.title_text_y); // (int) (CANVAS_HEIGHT * 0.1);
		PAUSED_TEXT_HEIGHT = getDimensionPixel(R.dimen.title_text_height); // (int) (CANVAS_HEIGHT * 0.15);
		
		final int pausedButtonsWidth = getDimensionPixel(R.dimen.button_width); // (int) (CANVAS_WIDTH * 0.8);
		final int pausedButtonsX = (CANVAS_WIDTH - pausedButtonsWidth) / 2;
		final int pausedButtonsHeight = getDimensionPixel(R.dimen.button_height); // (int) (CANVAS_HEIGHT * 0.08);
		final int pausedButtonsMargin = getDimensionPixel(R.dimen.button_margin);
		final int pausedButtonsY = CANVAS_HEIGHT - (2 * pausedButtonsMargin) - (2 * pausedButtonsHeight); //  (int) (CANVAS_HEIGHT * 0.7);
		
		resumeButton = new TextButton(getString(R.string.resume), pausedButtonsX, pausedButtonsY,
			pausedButtonsWidth, pausedButtonsHeight);
		resumeButton.visible = false;
		
		pauseQuitButton = new TextButton(getString(R.string.pause_quit_game), pausedButtonsX, pausedButtonsY
			+ pausedButtonsHeight + pausedButtonsMargin, pausedButtonsWidth, pausedButtonsHeight);
		pauseQuitButton.visible = false;
		
		final int buttonMarginX = getDimensionPixel(R.dimen.game_on_screen_control_margin); // (int) (CANVAS_WIDTH * 0.025);
		final int buttonHeight = getDimensionPixel(R.dimen.game_on_screen_control_button_height); // (int) (FOOTER_HEIGHT * 0.8);
		final int buttonWidth = (CANVAS_WIDTH - (6 * buttonMarginX)) / 3;
		final int buttonY = (CANVAS_HEIGHT - FOOTER_HEIGHT) + ((FOOTER_HEIGHT - buttonHeight) / 2);
		
		final Graphics graphics = getGraphics();
		
		final int buttonImageY = 5 * IMAGE_CELL_SIZE;
		final Pixmap downImage = graphics
			.clipAndScalePixmap(Assets.textures, 0, buttonImageY, MOVEMENT_BUTTON_WIDTH, MOVEMENT_BUTTON_HEIGHT, buttonWidth, buttonHeight);
		final Pixmap leftImage = graphics
			.clipAndScalePixmap(Assets.textures, 0, buttonImageY + MOVEMENT_BUTTON_HEIGHT, MOVEMENT_BUTTON_WIDTH, MOVEMENT_BUTTON_HEIGHT, buttonWidth, buttonHeight);
		
		final Pixmap rightImage = graphics
			.clipAndScalePixmap(Assets.textures, 0, buttonImageY + (2 * MOVEMENT_BUTTON_HEIGHT), MOVEMENT_BUTTON_WIDTH, MOVEMENT_BUTTON_HEIGHT, buttonWidth, buttonHeight);
		
		leftButton = new ImageButton(leftImage, leftImage, buttonMarginX, buttonY, buttonWidth, buttonHeight); // new TextButton("<", buttonMarginX, buttonY, buttonWidth, buttonHeight);
		downButton = new ImageButton(downImage, downImage, (3 * buttonMarginX) + buttonWidth, buttonY,
			buttonWidth, buttonHeight); // new TextButton("v", (3 * buttonMarginX) + buttonWidth, buttonY, buttonWidth, buttonHeight);
		rightButton = new ImageButton(rightImage, rightImage, (5 * buttonMarginX) + (2 * buttonWidth), buttonY,
			buttonWidth, buttonHeight); //  new TextButton(">", (5 * buttonMarginX) + (2 * buttonWidth), buttonY, buttonWidth, buttonHeight);
		
		if (!onScreenControls) {
			leftButton.visible = false;
			downButton.visible = false;
			rightButton.visible = false;
		}
		
		renderer = new GameRenderer(this, gridWidth, gridHeight);
		
		// TODO: Either call setPaused(isPaused) or resumeButton.visible = isPaused etc
		setPaused(isPaused);
	}
	
	
	
	@Override
	public void update(final float deltaTime) {
		final List<TouchEvent> touchEvents = input.getTouchEvents();
		
		pauseButton.hovered = false;
		resumeButton.hovered = false;
		pauseQuitButton.hovered = false;
		leftButton.hovered = false;
		rightButton.hovered = false;
		downButton.hovered = false;
		
		int moveHorizontalButtons = 0;
		int moveToX = -1;
		boolean downPressed = false;
		
		int x;
		int y;
		boolean down;
		
		for (int pointer = 0, length = Input.MAX_TOUCHPOINTS; pointer < length; pointer += 1) {
			x = input.getTouchX(pointer);
			y = input.getTouchY(pointer);
			down = input.isTouchDown(pointer);
			
			if (down) {
				if (pauseButton.visible && pauseButton.isOver(x, y)) {
					pauseButton.hovered = true;
					
				} else if (resumeButton.visible && resumeButton.isOver(x, y)) {
					resumeButton.hovered = true;
					
				} else if (pauseQuitButton.visible && pauseQuitButton.isOver(x, y)) {
					pauseQuitButton.hovered = true;
					
				} else if (leftButton.visible && leftButton.isOver(x, y)) {
					leftButton.hovered = true;
					moveHorizontalButtons -= 1;
					
				} else if (rightButton.visible && rightButton.isOver(x, y)) {
					rightButton.hovered = true;
					moveHorizontalButtons += 1;
					
				} else if (downButton.visible && downButton.isOver(x, y)) {
					downButton.hovered = true;
					downPressed = true;
					
				} else {
					if ((x > TRANSLATE_CENTER_X) && (y > TRANSLATE_CENTER_Y)
						&& (x < (TRANSLATE_CENTER_X + GRID_PIXEL_WIDTH))
						&& (y < (TRANSLATE_CENTER_Y + GRID_PIXEL_HEIGHT))) {
						final int offsetX = x - TRANSLATE_CENTER_X;
						final int cellX = offsetX / CELL_SIZE;
						moveToX = cellX;
					}
					
				}
			}
		}
		
		TouchEvent touchEvent;
		for (int i = 0, length = touchEvents.size(); i < length; i += 1) {
			touchEvent = touchEvents.get(i);
			x = touchEvent.x;
			y = touchEvent.y;
			
			if (touchEvent.type == TouchEvent.TOUCH_UP) {
				if (pauseButton.visible && pauseButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
					input.clearTouches();
					setPaused(true);
					return;
				} else if (resumeButton.visible && resumeButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
					input.clearTouches();
					setPaused(false);
					return;
				} else if (pauseQuitButton.visible && pauseQuitButton.isOver(x, y)) {
					mainActivity.playSound(Assets.buttonSelect);
					askQuitGame();
					return;
					
				}
			}
		}
		
		if (!isPaused) {
			if (moveToX != -1) {
				world.playerHorizontalTarget = moveToX;
			} else if (moveHorizontalButtons < 0) {
				world.moveLeft(deltaTime);
			} else if (moveHorizontalButtons > 0) {
				world.moveRight(deltaTime);
			}
			
			world.setPlayerFastFall(downPressed);
			
			world.update(deltaTime);
			
			if (world.getState() == GameState.GAME_OVER) {
				gameOver();
				return;
			}
		}
		
		gameMode.postUpdate(deltaTime);
	}
	
	
	
	@Override
	public void render(final float deltaTime) {
		renderer.render(deltaTime);
		gameMode.postRender(deltaTime);
	}
	
	
	
	private void setPaused(final boolean paused) {
		isPaused = paused;
		pauseButton.visible = !isPaused;
		resumeButton.visible = isPaused;
		pauseQuitButton.visible = isPaused;
		leftButton.visible = !isPaused;
		downButton.visible = !isPaused;
		rightButton.visible = !isPaused;
	}
	
	
	
	public boolean isPaused() {
		return isPaused;
	}
	
	
	
	@Override
	public void pause() {
		setPaused(true);
	}
	
	
	
	public boolean hasOnScreenControls() {
		return onScreenControls;
	}
	
	
	
	private void quitGame() {
		mainActivity.okToUpdate = false;
		mainActivity.changeScreen(new MainMenuScreen(mainActivity));
		mainActivity.okToUpdate = true;
	}
	
	
	
	private void askQuitGame() {
		if (askingQuit) {
			return;
		}
		askingQuit = true;
		setPaused(true);
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new AlertDialog.Builder(mainActivity)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(getString(R.string.leaving))
					.setMessage(getString(R.string.sure_leave))
					.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialogue, final int which) {
							askingQuit = false;
							quitGame();
						}
					})
					.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialogue, final int which) {
							askingQuit = false;
						}
					}).setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(final DialogInterface dialog) {
							askingQuit = false;
						}
					}).show();
			}
		});
		
	}
	
	
	
	public void gameOver() {
		mainActivity.changeScreen(new GameOverScreen(mainActivity, world.getScore(), gameModeId));
	}
	
	
	
	@Override
	public void onBackPressed() {
		askQuitGame();
	}
	
	
	
	@Override
	public void dispose() {
		if (renderer != null) {
			renderer.dispose();
		}
	}
	
}
