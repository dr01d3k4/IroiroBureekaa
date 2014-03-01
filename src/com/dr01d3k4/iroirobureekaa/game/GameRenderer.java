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



public final class GameRenderer {
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
	
	private Pixmap redBlock;
	private Pixmap redLightBlock;
	private Pixmap blueBlock;
	private Pixmap blueLightBlock;
	private Pixmap greenBlock;
	private Pixmap greenLightBlock;
	private Pixmap yellowBlock;
	private Pixmap yellowLightBlock;
	private Pixmap wildBlock;
	private Pixmap wildLightBlock;
	
	
	
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
		
		Graphics graphics = game.getGraphics();
		
		int i = game.IMAGE_CELL_SIZE;
		int c = game.CELL_SIZE;
		Pixmap textures = Assets.textures;
		redBlock = graphics.clipAndScalePixmap(textures, 0, 0, i, i, c, c);
		redLightBlock = graphics.clipAndScalePixmap(textures, i, 0, i, i, c, c);
		blueBlock = graphics.clipAndScalePixmap(textures, 0, i, i, i, c, c);
		blueLightBlock = graphics.clipAndScalePixmap(textures, i, i, i, i, c, c);
		greenBlock = graphics.clipAndScalePixmap(textures, 0, 2 * i, i, i, c, c);
		greenLightBlock = graphics.clipAndScalePixmap(textures, i, 2 * i, i, i, c, c);
		yellowBlock = graphics.clipAndScalePixmap(textures, 0, 3 * i, i, i, c, c);
		yellowLightBlock = graphics.clipAndScalePixmap(textures, i, 3 * i, i, i, c, c);
		wildBlock = graphics.clipAndScalePixmap(textures, 0, 4 * i, i, i, c, c);
		wildLightBlock = graphics.clipAndScalePixmap(textures, i, 4 * i, i, i, c, c);
	}
	
	
	
	@SuppressWarnings ("unused")
	private int[] colourToImageLocation(final int colour) {
		int sourceX = 0;
		int sourceY = 0;
		
		final int properColour = GameColour.getDarkerColour(colour);
		
		if (properColour == GameColour.RED) {
			sourceY = 0;
		} else if (properColour == GameColour.BLUE) {
			sourceY = game.IMAGE_CELL_SIZE;
		} else if (properColour == GameColour.GREEN) {
			sourceY = 2 * game.IMAGE_CELL_SIZE;
		} else if (properColour == GameColour.YELLOW) {
			sourceY = 3 * game.IMAGE_CELL_SIZE;
		} else if (properColour == GameColour.WILD) {
			sourceY = 4 * game.IMAGE_CELL_SIZE;
		}
		
		if (GameColour.isLightColour(colour)) {
			sourceX = game.IMAGE_CELL_SIZE;
		} else {
			sourceX = 0;
		}
		
		return new int[] {sourceX, sourceY};
	}
	
	
	
	private Pixmap colourToPixmap(int colour) {
		switch (colour) {
			case GameColour.RED:
				return redBlock;
			case GameColour.LIGHT_RED:
				return redLightBlock;
				
			case GameColour.BLUE:
				return blueBlock;
			case GameColour.LIGHT_BLUE:
				return blueLightBlock;
				
			case GameColour.GREEN:
				return greenBlock;
			case GameColour.LIGHT_GREEN:
				return greenLightBlock;
				
			case GameColour.YELLOW:
				return yellowBlock;
			case GameColour.LIGHT_YELLOW:
				return yellowLightBlock;
				
			case GameColour.WILD:
				return wildBlock;
			case GameColour.LIGHT_WILD:
				return wildLightBlock;
				
			default:
				throw new IllegalArgumentException("Colour not known");
				
		}
	}
	
	
	
	private void renderCell(final Graphics graphics, final float cellX, final float cellY, final int colour) {
		final int x = (int) Math.floor(cellX * game.CELL_SIZE);
		final int y = (int) Math.floor(cellY * game.CELL_SIZE);
		
		graphics.drawPixmap(colourToPixmap(colour), x, y);
	}
	
	
	
	private void renderCell(final Graphics graphics, final float drawX, final float drawY, int width, int height,
		final int colour) {
		final int x = (int) drawX;
		final int y = (int) drawY;
		
		graphics.drawPixmap(colourToPixmap(colour), x, y, width, height);
	}
	
	
	
	private void drawGameGrid(final Graphics graphics, final Paint paint) {
		graphics.drawRectangle(0, 0, game.GRID_PIXEL_WIDTH, game.GRID_PIXEL_HEIGHT, Color.WHITE);
		
		graphics.drawRectangle(game.world.playerHorizontalTarget * game.CELL_SIZE, 0, game.CELL_SIZE, game.GRID_PIXEL_HEIGHT, GameColour.FALLING_COLUMN_HINT);
		
		for (int x = 0; x < gridWidth; x += 1) {
			for (int y = 0; y < gridHeight; y += 1) {
				if (!game.world.grid.isEmptyAt(x, y)) {
					renderCell(graphics, x, y - game.world.newRowSlideIn, game.world.grid
						.getColourAt(x, y));
				}
			}
		}
		
		int length = game.world.fallingPieces.size();
		for (int i = 0; i < length; i += 1) {
			final FallingPiece fallingPiece = game.world.fallingPieces.get(i);
			renderCell(graphics, fallingPiece.x, fallingPiece.y, fallingPiece.colour);
		}
		
		if ((game.world.getState() == GameState.ADDING_NEW_ROW) && (game.world.newRow != null)) {
			length = game.world.newRow.length;
			for (int i = 0; i < length; i += 1) {
				renderCell(graphics, i, gridHeight - game.world.newRowSlideIn, game.world.newRow[i]);
			}
		}
		
		final int bottomRowY = game.GRID_PIXEL_HEIGHT - game.BOTTOM_ROW_HEIGHT;
		graphics.drawRectangle(0, bottomRowY, game.GRID_PIXEL_WIDTH, game.BOTTOM_ROW_HEIGHT, GameColour.UI_LIGHT);
		
		if (game.world.nextRow != null) {
			length = game.world.nextRow.length;
			
			for (int i = 0; i < length; i += 1) {
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
		
		final int nextPieceSize = (int) (nextTextObject.maxHeight); //  - nextTextObject.textBaseline); // (int) (game.HEADER_HEIGHT * 0.4);
		renderCell(graphics, nextCellX, nextTextObject.y,
		// (int) (game.HEADER_HEIGHT * 0.54),
		nextPieceSize, nextPieceSize, game.world.nextFallingPieceColour);
		
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
	
	
	
	public void dispose() {
		redBlock.dispose();
		redLightBlock.dispose();
		blueBlock.dispose();
		blueLightBlock.dispose();
		greenBlock.dispose();
		greenLightBlock.dispose();
		yellowBlock.dispose();
		yellowLightBlock.dispose();
		wildBlock.dispose();
		wildLightBlock.dispose();
	}
}