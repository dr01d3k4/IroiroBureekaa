package com.dr01d3k4.iroirobureekaa.game;



import java.util.ArrayList;
import java.util.List;



import com.dr01d3k4.iroirobureekaa.CellPool;
import com.dr01d3k4.iroirobureekaa.FallingPiecePool;



public class Grid {
	public static final int WIDTH = 15; // 14;
	public static final int HEIGHT = 22; // 22;
	public static final int START_ROWS = 6; // (int) Math.ceil(HEIGHT / 4.0f);
	
	public final int width;
	public final int height;
	
	private final int[][] grid;
	private final boolean[][] markedForClear;
	private final boolean[][] lookedAt;
	private final int[][] cellWorth;
	
	
	
	public Grid(final int width, final int height) {
		this.width = width;
		this.height = height;
		
		grid = new int[width][height];
		markedForClear = new boolean[width][height];
		lookedAt = new boolean[width][height];
		cellWorth = new int[width][height];
	}
	
	
	
	public void init() {
		for (int x = 0; x < width; x += 1) {
			for (int y = 0; y < height; y += 1) {
				grid[x][y] = GameColour.NONE;
				markedForClear[x][y] = false;
				lookedAt[x][y] = false;
				cellWorth[x][y] = 0;
			}
		}
	}
	
	
	
	public boolean inBounds(final int x, final int y) {
		return ((x >= 0) && (y >= 0) && (x < width) && (y < height));
	}
	
	
	
	public int getColourAt(final int x, final int y) {
		int colour = GameColour.OUT_OF_BOUNDS;
		
		if (inBounds(x, y)) {
			colour = grid[x][y];
		}
		
		return colour;
	}
	
	
	
	public void setColourAt(final int x, final int y, final int colour) {
		if (inBounds(x, y)) {
			grid[x][y] = colour;
		}
	}
	
	
	
	public int getCellWorth(final int x, final int y) {
		int worth = 0;
		
		if (inBounds(x, y)) {
			worth = cellWorth[x][y];
		}
		
		return worth;
	}
	
	
	
	public void setCellWorth(final int x, final int y, final int worth) {
		if (inBounds(x, y)) {
			cellWorth[x][y] = worth;
		}
	}
	
	
	
	public boolean isExactlyColourAt(final int x, final int y, final int colour) {
		return (getColourAt(x, y) == colour);
	}
	
	
	
	public boolean isColourAt(final int x, final int y, final int colour) {
		final int colourOnGrid = getColourAt(x, y);
		boolean isColour = false;
		
		if (colourOnGrid == GameColour.OUT_OF_BOUNDS) {
			isColour = false;
		} else if ((colourOnGrid == colour)
			|| ((colour == GameColour.WILD) && (colourOnGrid != GameColour.NONE))
			|| (colourOnGrid == GameColour.WILD)) {
			isColour = true;
		}
		
		return isColour;
	}
	
	
	
	public boolean isEmptyAt(final int x, final int y) {
		return isExactlyColourAt(x, y, GameColour.NONE);
	}
	
	
	
	public boolean isSolidAt(final int x, final int y) {
		final int colour = getColourAt(x, y);
		return ((colour != GameColour.NONE) && (colour != GameColour.OUT_OF_BOUNDS));
	}
	
	
	
	public void setEmptyAt(final int x, final int y) {
		setColourAt(x, y, GameColour.NONE);
	}
	
	
	
	public boolean isMarkedForClearAt(final int x, final int y) {
		boolean marked = false;
		
		if (inBounds(x, y)) {
			marked = markedForClear[x][y];
		}
		
		return marked;
	}
	
	
	
	public void setMarkedForClearAt(final int x, final int y, final boolean marked) {
		if (inBounds(x, y)) {
			markedForClear[x][y] = marked;
		}
	}
	
	
	
	public void clearMarkedForClear() {
		for (int x = 0; x < width; x += 1) {
			for (int y = 0; y < height; y += 1) {
				markedForClear[x][y] = false;
			}
		}
	}
	
	
	
	public boolean hasBeenLookedAt(final int x, final int y) {
		boolean looked = false;
		
		if (inBounds(x, y)) {
			looked = lookedAt[x][y];
		}
		
		return looked;
	}
	
	
	
	public void setLookedAt(final int x, final int y, final boolean looked) {
		if (inBounds(x, y)) {
			lookedAt[x][y] = looked;
		}
	}
	
	
	
	public void clearLookedAt() {
		for (int x = 0; x < width; x += 1) {
			for (int y = 0; y < height; y += 1) {
				lookedAt[x][y] = false;
			}
		}
	}
	
	
	
	private void findFloodClearCellsRecursive(final int x, final int y, final int colour, final List<Cell> cells,
		CellPool cellPool) {
		if (isColourAt(x, y, colour) && !hasBeenLookedAt(x, y)) {
			cells.add(cellPool.newObject(x, y, colour));
			setLookedAt(x, y, true);
			findFloodClearCellsRecursive(x, y - 1, colour, cells, cellPool);
			findFloodClearCellsRecursive(x + 1, y, colour, cells, cellPool);
			findFloodClearCellsRecursive(x, y + 1, colour, cells, cellPool);
			findFloodClearCellsRecursive(x - 1, y, colour, cells, cellPool);
		}
	}
	
	
	
	public List<Cell> findFloodClearCells(final int x, final int y, final int colour, CellPool cellPool) {
		final List<Cell> cells = new ArrayList<Cell>();
		clearLookedAt();
		findFloodClearCellsRecursive(x, y, colour, cells, cellPool);
		clearLookedAt();
		return cells;
	}
	
	
	
	public List<FallingPiece> findCellsToFall(FallingPiecePool fallingPiecePool) {
		final List<FallingPiece> fallingPieces = new ArrayList<FallingPiece>();
		
		boolean shouldFall = false;
		for (int x = 0; x < width; x += 1) {
			shouldFall = isEmptyAt(x, height - 1);
			
			for (int y = height - 2; y >= 0; y -= 1) {
				if (isEmptyAt(x, y)) {
					shouldFall = true;
				} else {
					if (shouldFall) {
						// TODO: Maybe use previousWasEmpty to optimise this?
						fallingPieces.add(fallingPiecePool
							.newObject(x, y, getColourAt(x, y), isEmptyAt(x, y + 1)));
					}
				}
			}
		}
		
		return fallingPieces;
	}
	
	
	
	public void setFallingPiecesEmpty(final List<FallingPiece> fallingPieces) {
		int length = fallingPieces.size();
		FallingPiece fallingPiece;
		for (int i = 0; i < length; i += 1) {
			fallingPiece = fallingPieces.get(i);
			setEmptyAt(fallingPiece.getRoundedX(), fallingPiece.getRoundedY());
		}
	}
}