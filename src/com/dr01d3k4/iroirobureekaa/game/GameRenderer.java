package com.dr01d3k4.iroirobureekaa.game;



import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;



import com.dr01d3k4.iroirobureekaa.Assets;
import com.dr01d3k4.iroirobureekaa.GameScreen;
import com.dr01d3k4.iroirobureekaa.R;
import com.dr01d3k4.iroirobureekaa.Text;
import com.dr01d3k4.iroirobureekaa.game.GameWorld.GameState;
import com.dr01d3k4.iroirobureekaa.render.Graphics;
import com.dr01d3k4.iroirobureekaa.render.Pixmap;



public class GameRenderer {
	private final GameScreen game;
	private final int gridWidth;
	private final int gridHeight;
	
	private final int headerTextInset;
	
	private final String scoreTextBase;
	private final String scoreChangeTextBase;
	private final Text scoreTextObject;
	
	private final Text nextTextObject;
	private final int nextCellX;
	
	private final Text pausedTextObject;
	
	private final Pixmap blocks;
	
	
	
	public GameRenderer(final GameScreen game, final int gridWidth, final int gridHeight) {
		this.game = game;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		headerTextInset = 10;
		
		scoreTextBase = game.getString(R.string.score);
		scoreChangeTextBase = game.getString(R.string.score_change);
		
		scoreTextObject = new Text(scoreTextBase, headerTextInset, (int) (game.HEADER_HEIGHT * 0.1),
			game.pauseButton.x - headerTextInset, (int) (game.HEADER_HEIGHT * 0.4), Align.LEFT);
		
		nextTextObject = new Text(game.getString(R.string.next), headerTextInset,
			(int) (game.HEADER_HEIGHT * 0.6), game.pauseButton.x - headerTextInset,
			(int) (game.HEADER_HEIGHT * 0.4), Align.LEFT);
		nextCellX = nextTextObject.getRight() + headerTextInset;
		
		pausedTextObject = new Text(game.getString(R.string.paused), 0, game.PAUSED_TEXT_Y, game.CANVAS_WIDTH,
			game.PAUSED_TEXT_HEIGHT);
		
		blocks = Assets.blocks;
	}
	
	
	
	private int[] colourToImageLocation(final int colour) {
		int sourceX = 0;
		int sourceY = 0;
		
		final int properColour = GameColour.getDarkerColour(colour);
		
		if (properColour == GameColour.WILD) {
			sourceX = -1;
			sourceY = -1;
		} else if (properColour == GameColour.RED) {
			if (GameColour.isLightColour(colour)) {
				sourceX = game.IMAGE_CELL_SIZE;
			} else {
				sourceX = 0;
			}
			sourceY = 0;
		} else if (properColour == GameColour.GREEN) {
			if (GameColour.isLightColour(colour)) {
				sourceX = 3 * game.IMAGE_CELL_SIZE;
			} else {
				sourceX = 2 * game.IMAGE_CELL_SIZE;
			}
			sourceY = 0;
		} else if (properColour == GameColour.BLUE) {
			if (GameColour.isLightColour(colour)) {
				sourceX = game.IMAGE_CELL_SIZE;
			} else {
				sourceX = 0;
			}
			sourceY = game.IMAGE_CELL_SIZE;
		} else if (properColour == GameColour.YELLOW) {
			if (GameColour.isLightColour(colour)) {
				sourceX = 3 * game.IMAGE_CELL_SIZE;
			} else {
				sourceX = 2 * game.IMAGE_CELL_SIZE;
			}
			sourceY = game.IMAGE_CELL_SIZE;
		}
		
		return new int[] {sourceX, sourceY};
	}
	
	
	
	private void renderCell(final Graphics graphics, final float drawX, final float drawY, final int width,
		final int height, final int colour) {
		final int x = (int) drawX;
		final int y = (int) drawY;
		
		final int[] source = colourToImageLocation(colour);
		
		final int sourceWidth = game.IMAGE_CELL_SIZE;
		final int sourceHeight = game.IMAGE_CELL_SIZE;
		final int destinationWidth = width;
		final int destinationHeight = height;
		
		if ((source[0] < 0) || (source[1] < 0)) {
			graphics.drawRectangle(x, y, destinationWidth, destinationHeight, colour);
		} else {
			graphics.drawPixmap(blocks, x, y, source[0], source[1], sourceWidth, sourceHeight, destinationWidth, destinationHeight);
		}
	}
	
	
	
	private void renderCell(final Graphics graphics, final float cellX, final float cellY, final int colour) {
		final int x = (int) Math.floor(cellX * game.CELL_SIZE);
		final int y = (int) Math.floor(cellY * game.CELL_SIZE);
		
		renderCell(graphics, x, y, game.CELL_SIZE, game.CELL_SIZE, colour);
	}
	
	
	
	private void drawGameGrid(final Graphics graphics, final Paint paint) {
		graphics.drawRectangle(0, 0, game.GRID_PIXEL_WIDTH, game.GRID_PIXEL_HEIGHT, Color.WHITE);
		
		graphics.drawRectangle(game.world.playerHorizontalTarget * game.CELL_SIZE, 0, game.CELL_SIZE, game.GRID_PIXEL_HEIGHT, GameColour.FALLING_COLUMN_HINT);
		
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (!game.world.grid.isEmptyAt(x, y)) {
					renderCell(graphics, x, y - game.world.newRowSlideIn, game.world.grid
						.getColourAt(x, y));
				}
			}
		}
		
		int length = game.world.fallingPieces.size();
		for (int i = 0; i < length; i++) {
			final FallingPiece fallingPiece = game.world.fallingPieces.get(i);
			renderCell(graphics, fallingPiece.x, fallingPiece.y, fallingPiece.colour);
		}
		
		if ((game.world.getState() == GameState.ADDING_NEW_ROW) && (game.world.newRow != null)) {
			length = game.world.newRow.length;
			for (int i = 0; i < length; i++) {
				renderCell(graphics, i, gridHeight - game.world.newRowSlideIn, game.world.newRow[i]);
			}
		}
		
		final int bottomRowY = game.GRID_PIXEL_HEIGHT - game.BOTTOM_ROW_HEIGHT;
		graphics.drawRectangle(0, bottomRowY, game.GRID_PIXEL_WIDTH, game.BOTTOM_ROW_HEIGHT, GameColour.UI_LIGHT);
		
		if (game.world.nextRow != null) {
			length = game.world.nextRow.length;
			
			for (int i = 0; i < length; i++) {
				renderCell(graphics, (i * game.BOTTOM_ROW_CELL_FULL_WIDTH)
					+ game.BOTTOM_ROW_CELL_X_OFFSET, game.BOTTOM_ROW_CELL_Y, game.BOTTOM_ROW_CELL_WIDTH, game.BOTTOM_ROW_CELL_WIDTH, game.world.nextRow[i]);
			}
			
			graphics.drawRectangle(0, game.GRID_PIXEL_HEIGHT, game.GRID_PIXEL_WIDTH, game.BOTTOM_ROW_CELL_WIDTH, GameColour.UI_DARK);
		}
		
		final int multiplier = game.world.getMultiplier();
		if (multiplier > 0) {
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(60 + (multiplier * 10));
			paint.setTextScaleX(1.0f);
			graphics.drawText("x" + multiplier, game.GRID_PIXEL_WIDTH / 2, (int) (game.GRID_PIXEL_HEIGHT * 0.2), GameColour.TEXT);
		}
	}
	
	
	
	private void renderHeader(final Graphics graphics, final Paint paint) {
		graphics.drawRectangle(0, 0, game.CANVAS_WIDTH, game.HEADER_HEIGHT, GameColour.UI_LIGHT);
		String scoreText = "";
		
		final int scoreChange = game.world.getScoreChange();
		if (scoreChange == 0) {
			scoreText = String.format(scoreTextBase, game.world.getScore());
		} else {
			scoreText = String.format(scoreChangeTextBase, game.world.getScore(), game.world
				.getScoreChange());
		}
		
		scoreTextObject.setText(scoreText);
		scoreTextObject.render(graphics, paint);
		nextTextObject.render(graphics, paint);
		
		final int nextPieceSize = (int) (game.HEADER_HEIGHT * 0.4);
		renderCell(graphics, nextCellX, (int) (game.HEADER_HEIGHT * 0.58), nextPieceSize, nextPieceSize, game.world.nextFallingPieceColour);
		
		game.pauseButton.render(graphics, paint);
	}
	
	
	
	private void renderOnScreenControls(final Graphics graphics, final Paint paint) {
		if (game.hasOnScreenControls()) {
			graphics.drawRectangle(0, game.CANVAS_HEIGHT - game.FOOTER_HEIGHT, game.CANVAS_WIDTH, game.FOOTER_HEIGHT, GameColour.UI_LIGHT);
			game.leftButton.render(graphics, paint);
			game.downButton.render(graphics, paint);
			game.rightButton.render(graphics, paint);
		}
	}
	
	
	
	private void renderPaused(final Graphics graphics, final Paint paint) {
		graphics.clear(GameColour.UI_LIGHT);
		pausedTextObject.render(graphics, paint);
		game.resumeButton.render(graphics, paint);
		game.pauseQuitButton.render(graphics, paint);
	}
	
	
	
	public void render(final float deltaTime) {
		final Graphics graphics = game.getGraphics();
		final Paint paint = graphics.getPaint();
		
		if (!game.isPaused()) {
			graphics.clear(Color.DKGRAY);
			
			graphics.getCanvas().translate(game.TRANSLATE_CENTER_X, game.TRANSLATE_CENTER_Y);
			{
				drawGameGrid(graphics, paint);
				
			}
			graphics.getCanvas().translate(-game.TRANSLATE_CENTER_X, -game.TRANSLATE_CENTER_Y);
			
			graphics.drawRectangle(0, 0, game.CANVAS_WIDTH, game.TRANSLATE_CENTER_Y, GameColour.UI_DARK);
			
			renderHeader(graphics, paint);
			renderOnScreenControls(graphics, paint);
			
		} else if (game.isPaused()) {
			renderPaused(graphics, paint);
		}
	}
}