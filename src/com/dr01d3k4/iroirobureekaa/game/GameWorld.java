package com.dr01d3k4.iroirobureekaa.game;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GameWorld {
	public enum GameState {
		NONE, INITIALIZING, ADDING_NEW_ROW, PLAYER_FALLING, LANDING_CELLS, PRE_CLEARING, CLEARING, GAP_FILL_FALL, LOST, GAME_OVER
	}
	
	
	
	public final int width;
	public final int height;
	public final Grid grid;
	private GameState state = GameState.NONE;
	private GameState previousState = GameState.NONE;
	
	private final Random random;
	
	private static final float NEW_ROW_SLIDE_IN_TIME = 0.17f; // 0.14f;
	public float newRowSlideIn = 0;
	public int[] nextRow;
	public int[] newRow;
	private int addedRows = 0;
	private int startingRows = 0;
	
	private static final int NEW_ROW_BASE_TIME = 8;
	private static final int NEW_ROW_RANDOM_TIME = 8;
	private float newRowTime = 0;
	private float newRowCumulativeTime = 0;
	
	public List<FallingPiece> fallingPieces;
	public final List<FallingPiece> landedPieces;
	public List<Cell> floodClearLocations;
	public final List<Cell> preClearedList;
	
	private final float PLAYER_FALL_TIME = 0.166f; // 0.15f;
	private final float PLAYER_FAST_FALL_TIME = 0.048f; // 0.063f;
	private final float PLAYER_HORIZONTAL_MOVE_TIME = 0.081f; // 0.092f;
	private final float PLAYER_START_Y = (float) -0.8 / PLAYER_FALL_TIME;
	public int playerHorizontalTarget;
	private boolean playerFastFall = false;
	public int nextFallingPieceColour;
	
	private final float PRE_CLEAR_TIME = 0.06f;
	private float preClearCumulativeTime = 0;
	
	private final float FINAL_CLEAR_TIME = 0.35f;
	private float finalClearCumulativeTime = 0;
	
	private final float GAP_FILL_TIME = 0.11f;
	
	private static final int MIN_GROUP_SIZE = 3;
	private static final int WILD_COLOUR_MULTIPLIER_MINIMUM = 3;
	
	private int score = 0;
	private int scoreChange = 0;
	private int multiplier = 0;
	
	
	
	public GameWorld(final int width, final int height) {
		this.width = width;
		this.height = height;
		grid = new Grid(this.width, this.height);
		random = new Random();
		
		newRowTime = NEW_ROW_BASE_TIME + random.nextInt(NEW_ROW_RANDOM_TIME);
		startingRows = Grid.START_ROWS;
		
		fallingPieces = new ArrayList<FallingPiece>();
		landedPieces = new ArrayList<FallingPiece>();
		floodClearLocations = new ArrayList<Cell>();
		preClearedList = new ArrayList<Cell>();
		
		playerHorizontalTarget = (int) Math.floor(width / 2);
		nextFallingPieceColour = GameColour.randomColour(random);
		
		nextRow = generateNextRow();
		newRow = nextRow;
	}
	
	
	
	public void init() {
		grid.init();
		changeState(GameState.INITIALIZING);
	}
	
	
	
	public int getScore() {
		return score;
	}
	
	
	
	public int getMultiplier() {
		return multiplier;
	}
	
	
	
	public int getScoreChange() {
		return scoreChange;
	}
	
	
	
	private void changeState(final GameState newState) {
		changeState(newState, false, false);
	}
	
	
	
	private void changeState(final GameState newState, final boolean goingBack, final boolean forcePause) {
		if ((newState == GameState.PLAYER_FALLING) || (newState == GameState.GAP_FILL_FALL)) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					grid.setCellWorth(x, y, GameColour.getColourWorth(grid.getColourAt(x, y)));
				}
			}
		}
		grid.clearMarkedForClear();
		grid.clearLookedAt();
		
		if ((newState == GameState.LOST) || (newState == GameState.GAME_OVER)) {
			previousState = GameState.NONE;
			state = newState;
			
		} else {
			previousState = state;
			
			if (!goingBack && (newRowCumulativeTime >= newRowTime)
				&& (newState == GameState.PLAYER_FALLING)) {
				newRowCumulativeTime = 0;
				newRowTime = NEW_ROW_BASE_TIME + random.nextInt(NEW_ROW_RANDOM_TIME);
				changeState(GameState.ADDING_NEW_ROW);
			} else {
				state = newState;
			}
		}
	}
	
	
	
	private void backState() {
		changeState(previousState, true, false);
	}
	
	
	
	public GameState getState() {
		return state;
	}
	
	
	
	public void update(final float deltaTime) {
		switch (state) {
			case INITIALIZING: {
				if (addedRows < startingRows) {
					changeState(GameState.ADDING_NEW_ROW);
				} else {
					changeState(GameState.PLAYER_FALLING);
				}
				break;
			}
			
			case ADDING_NEW_ROW: {
				updateStateAddingNewRow(deltaTime);
				break;
			}
			
			case PLAYER_FALLING: {
				updateStatePlayerFalling(deltaTime);
				break;
			}
			
			case LANDING_CELLS: {
				updateLandingPieces();
				break;
			}
			
			case PRE_CLEARING: {
				updatePreClearing(deltaTime);
				break;
			}
			
			case CLEARING: {
				updateClearing(deltaTime);
				break;
			}
			
			case GAP_FILL_FALL: {
				updateGapFillFall(deltaTime);
				break;
			}
			
			case LOST: {
				multiplier = 0;
				score += scoreChange;
				scoreChange = 0;
				lose();
				break;
			}
			
			case GAME_OVER:
			case NONE:
			default: {
				break;
			}
		}
		
		if (state == GameState.PLAYER_FALLING) {
			newRowCumulativeTime += deltaTime;
			if (newRowCumulativeTime > newRowTime) {
				newRowCumulativeTime = newRowTime;
			}
		}
	}
	
	
	
	private void updateStateAddingNewRow(final float deltaTime) {
		if (nextRow == null) {
			nextRow = generateNextRow();
		}
		
		if (newRow == null) {
			newRow = nextRow;
		}
		
		newRowSlideIn += deltaTime / NEW_ROW_SLIDE_IN_TIME;
		
		if (newRowSlideIn >= 1) {
			for (int x = 0; x < width; x++) {
				if (grid.isSolidAt(x, 0)) {
					changeState(GameState.LOST);
					return;
				}
				
				for (int y = 0; y < (height - 1); y++) {
					grid.setColourAt(x, y, grid.getColourAt(x, y + 1));
				}
				
				grid.setColourAt(x, height - 1, newRow[x]);
			}
			
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					grid.setCellWorth(x, y, GameColour.getColourWorth(grid.getColourAt(x, y)));
				}
			}
			
			nextRow = generateNextRow();
			newRow = nextRow;
			newRowSlideIn = 0;
			addedRows++;
			backState();
		}
	}
	
	
	
	private int[] generateNextRow() {
		final int[] row = new int[width];
		for (int i = 0; i < row.length; i++) {
			row[i] = GameColour.randomColour(random);
		}
		return row;
	}
	
	
	
	private void updateStatePlayerFalling(final float deltaTime) {
		if (fallingPieces.size() == 0) {
			grid.clearMarkedForClear();
			
			int newColour = nextFallingPieceColour;
			if (multiplier >= WILD_COLOUR_MULTIPLIER_MINIMUM) {
				newColour = GameColour.WILD;
			} else {
				nextFallingPieceColour = GameColour.randomColour(random);
			}
			
			fallingPieces.add(new FallingPiece((float) Math.floor(width / 2), PLAYER_START_Y, newColour,
				true));
			playerHorizontalTarget = fallingPieces.get(0).getRoundedX();
		}
		
		multiplier = 0;
		
		final FallingPiece fallingPiece = fallingPieces.get(0);
		
		attemptMovePlayerHorizontal(fallingPiece, deltaTime);
		
		float fallSpeed = PLAYER_FALL_TIME;
		if (playerFastFall && (fallingPiece.getRoundedY() >= -1)) {
			fallSpeed = PLAYER_FAST_FALL_TIME;
		}
		
		final boolean moved = attemptMoveFallingPiece(fallingPiece, 0, deltaTime / fallSpeed);
		if (!moved) {
			if (!handleFallingPieceCollision(fallingPiece)) {
				return;
			}
			
			fallingPieces.clear();
		}
		
		if (fallingPieces.size() == 0) {
			grid.clearMarkedForClear();
			changeState(GameState.LANDING_CELLS);
		}
	}
	
	
	
	private void updateLandingPieces() {
		grid.clearLookedAt();
		
		if (landedPieces.size() > 0) {
			int x = 0;
			int y = 0;
			int colour = GameColour.NONE;
			boolean canStartClear = false;
			
			final int length = landedPieces.size();
			FallingPiece landedPiece;
			for (int i = 0; i < length; i++) {
				landedPiece = landedPieces.get(i);
				
				x = landedPiece.getRoundedX();
				y = landedPiece.getRoundedY();
				colour = landedPiece.getColour();
				canStartClear = landedPiece.canStartClear();
				
				if (canStartClear && grid.isColourAt(x, y + 1, colour)) {
					if (!grid.isMarkedForClearAt(x, y) && !grid.isMarkedForClearAt(x, y + 1)) {
						int newColour = grid.getColourAt(x, y + 1);
						
						if (newColour == GameColour.WILD) {
							if (colour == GameColour.WILD) {
								continue;
							}
							newColour = colour;
						}
						
						final List<Cell> cells = grid.findFloodClearCells(x, y, newColour);
						
						if (cells.size() >= MIN_GROUP_SIZE) {
							if (colour != GameColour.WILD) {
								grid.setColourAt(x, y, colour);
								grid.setCellWorth(x, y, GameColour
									.getColourWorth(newColour));
							} else {
								grid.setColourAt(x, y, GameColour.WILD);
								grid.setCellWorth(x, y, GameColour
									.getColourWorth(GameColour.WILD));
							}
							
							markCellsForClear(cells);
							multiplier++;
							
							startFloodClearAt(x, y, newColour, floodClearLocations);
							grid.setMarkedForClearAt(x, y, true);
							grid.setMarkedForClearAt(x, y + 1, true);
						}
					}
				}
			}
		}
		
		landedPieces.clear();
		grid.clearMarkedForClear();
		
		if (floodClearLocations.size() > 0) {
			changeState(GameState.PRE_CLEARING);
		} else {
			score += scoreChange;
			scoreChange = 0;
			changeState(GameState.PLAYER_FALLING);
		}
	}
	
	
	
	private void updatePreClearing(final float deltaTime) {
		preClearCumulativeTime += deltaTime;
		
		while (preClearCumulativeTime >= PRE_CLEAR_TIME) {
			final int length = floodClearLocations.size();
			Cell cell;
			for (int i = 0; i < length; i++) {
				cell = floodClearLocations.get(i);
				scoreChange += grid.getCellWorth(cell.getX(), cell.getY()) * multiplier;
			}
			
			floodClearLocations = stepFloodClear(floodClearLocations);
			preClearCumulativeTime -= PRE_CLEAR_TIME;
		}
		
		if (floodClearLocations.size() == 0) {
			changeState(GameState.CLEARING);
			preClearCumulativeTime = 0;
		}
	}
	
	
	
	private void updateClearing(final float deltaTime) {
		finalClearCumulativeTime += deltaTime;
		
		if (finalClearCumulativeTime > FINAL_CLEAR_TIME) {
			final int length = preClearedList.size();
			Cell cell;
			for (int i = 0; i < length; i++) {
				cell = preClearedList.get(i);
				grid.setEmptyAt(cell.getX(), cell.getY());
			}
			
			preClearedList.clear();
			changeState(GameState.GAP_FILL_FALL);
			finalClearCumulativeTime = 0;
		}
	}
	
	
	
	private void updateGapFillFall(final float deltaTime) {
		if (fallingPieces.size() == 0) {
			floodClearLocations.clear();
			fallingPieces = grid.findCellsToFall();
			grid.setFallingPiecesEmpty(fallingPieces);
			grid.clearMarkedForClear();
		}
		
		final List<FallingPiece> newFallingPieces = new ArrayList<FallingPiece>();
		boolean moved = false;
		
		final int length = fallingPieces.size();
		FallingPiece fallingPiece;
		for (int i = 0; i < length; i++) {
			fallingPiece = fallingPieces.get(i);
			moved = attemptMoveFallingPiece(fallingPiece, 0, deltaTime / GAP_FILL_TIME);
			
			if (!moved) {
				if (!handleFallingPieceCollision(fallingPiece)) {
					return;
				}
			} else {
				newFallingPieces.add(fallingPiece);
			}
		}
		
		fallingPieces = newFallingPieces;
		
		if (fallingPieces.size() == 0) {
			grid.clearMarkedForClear();
			changeState(GameState.LANDING_CELLS);
		}
	}
	
	
	
	// TODO: Move Game.handleFallingPieceCollision to FallingPiece class
	private boolean handleFallingPieceCollision(final FallingPiece fallingPiece) {
		final int x = fallingPiece.getRoundedX();
		final int y = fallingPiece.getRoundedY();
		
		if (y < 0) {
			changeState(GameState.LOST);
			return false;
		}
		
		final int colour = fallingPiece.getColour();
		
		grid.setColourAt(x, y, colour);
		grid.setCellWorth(x, y, GameColour.getColourWorth(colour));
		landedPieces.add(fallingPiece);
		
		return true;
	}
	
	
	
	// TODO: Move Game.attemptMovePlayerHorizontal to FallingPiece class
	private void attemptMovePlayerHorizontal(final FallingPiece fallingPiece, final float deltaTime) {
		if (playerHorizontalTarget < 0) {
			playerHorizontalTarget = 0;
		}
		
		if (playerHorizontalTarget >= width) {
			playerHorizontalTarget = width - 1;
		}
		
		// TODO: Possible problem here with floating point accuracy?
		if (fallingPiece.getX() != playerHorizontalTarget) {
			final float moveAmount = deltaTime / PLAYER_HORIZONTAL_MOVE_TIME;
			final int moveDirection = (playerHorizontalTarget > fallingPiece.getX()) ? 1 : -1;
			
			// if (Math.abs(fallingPiece.getX() - playerHorizontalTarget) < moveAmount) {
			
			if (Math.abs(fallingPiece.getX() - playerHorizontalTarget) < moveAmount) {
				fallingPiece.setX(Math.round(playerHorizontalTarget));
			} else {
				fallingPiece.setX(fallingPiece.getX() + (moveAmount * moveDirection));
			}
			
			final float x = fallingPiece.getX();
			final float y = fallingPiece.getY();
			
			if (grid.isSolidAt((int) Math.floor(x), (int) Math.floor(y))
				|| grid.isSolidAt((int) Math.floor(x), (int) Math.ceil(y))) {
				fallingPiece.setX((int) Math.ceil(x));
				playerHorizontalTarget = fallingPiece.getRoundedX();
			}
			
			if (grid.isSolidAt((int) Math.ceil(x), (int) Math.floor(y))
				|| grid.isSolidAt((int) Math.ceil(x), (int) Math.ceil(y))) {
				fallingPiece.setX((int) Math.floor(x));
				playerHorizontalTarget = fallingPiece.getRoundedX();
			}
		} else {
			fallingPiece.setX(playerHorizontalTarget);
		}
	}
	
	
	
	public void moveLeft(float deltaTime) {
		if (fallingPieces.size() != 1) {
			return;
		}
		if (Math.abs(fallingPieces.get(0).getX() - playerHorizontalTarget) < deltaTime
			/ PLAYER_HORIZONTAL_MOVE_TIME) {
			playerHorizontalTarget -= 1;
		}
	}
	
	
	
	public void moveRight(float deltaTime) {
		if (fallingPieces.size() != 1) {
			return;
		}
		if (Math.abs(fallingPieces.get(0).getX() - playerHorizontalTarget) < deltaTime
			/ PLAYER_HORIZONTAL_MOVE_TIME) {
			playerHorizontalTarget += 1;
		}
	}
	
	
	
	public void setPlayerFastFall(boolean downPressed) {
		playerFastFall = downPressed;
	}
	
	
	
	// TODO: Move Game.attemptMoveFallingPiece to FallingPiece class
	private boolean attemptMoveFallingPiece(final FallingPiece fallingPiece, final float dx, float dy) {
		boolean collision = false;
		boolean collidedAtLeastOnce = false;
		int collisionX = -1;
		int collisionY = -1;
		
		final float newX = fallingPiece.getX() + dx;
		final float newY = fallingPiece.getY() + dy;
		
		if (newX <= 0) {
			fallingPiece.setX(0);
		}
		
		if (newX >= (width - 1)) {
			fallingPiece.setX(width - 1);
		}
		
		if (newY >= (height - 1)) {
			fallingPiece.setY(height - 1);
			return false;
		}
		
		if (fallingPiece.getY() > -1) {
			boolean checkForCollision = true;
			
			final int startX = (int) ((dx <= 0) ? Math.floor(fallingPiece.getX()) : Math.ceil(fallingPiece
				.getX()));
			final int endX = (int) ((dx <= 0) ? Math.ceil(fallingPiece.getX() + dx) : Math
				.floor(fallingPiece.getX()));
			final int incX = (dx <= 0) ? 1 : -1;
			
			while (checkForCollision) {
				collision = false;
				checkForCollision = false;
				
				if ((fallingPiece.getY() < -1) && collidedAtLeastOnce) {
					collision = true;
					collisionX = fallingPiece.getRoundedX();
					collisionY = fallingPiece.getRoundedY();
					break;
				}
				
				for (int x = startX; (dx <= 0) ? (x <= endX) : (x >= endX); x += incX) {
					for (int y = (int) Math.floor(fallingPiece.getY()); y < (Math.ceil(fallingPiece
						.getY()
						+ dy) + 1); y++) {
						if (grid.isSolidAt(x, y)) {
							collision = true;
							collisionX = x;
							collisionY = y;
							break;
						}
					}
					
					if (collision) {
						break;
					}
				}
				
				if (collision) {
					checkForCollision = true;
					collidedAtLeastOnce = true;
					fallingPiece.setY((float) Math.floor(collisionY - 1));
					dy = 0;
				} else {
					checkForCollision = false;
				}
			}
		}
		
		if (collidedAtLeastOnce) {
			fallingPiece.setX(collisionX);
		} else {
			fallingPiece.setX(fallingPiece.getX() + dx);
			fallingPiece.setY(fallingPiece.getY() + dy);
		}
		
		return !collidedAtLeastOnce;
	}
	
	
	
	private void lose() {
		changeState(GameState.GAME_OVER);
	}
	
	
	
	private void startFloodClearAt(final int x, final int y, final int colour, final List<Cell> floodClearList) {
		if (grid.isColourAt(x, y, colour) && !grid.hasBeenLookedAt(x, y)) {
			floodClearList.add(new Cell(x, y, colour));
			grid.setLookedAt(x, y, true);
		}
	}
	
	
	
	private List<Cell> stepFloodClear(final List<Cell> floodClearList) {
		final List<Cell> newClearList = new ArrayList<Cell>();
		grid.clearLookedAt();
		
		int x = 0;
		int y = 0;
		int colour = GameColour.NONE;
		int lighter = GameColour.NONE;
		
		final int length = floodClearList.size();
		Cell cell;
		for (int i = 0; i < length; i++) {
			cell = floodClearList.get(i);
			
			x = cell.getX();
			y = cell.getY();
			colour = cell.getColour();
			lighter = GameColour.getLighterColour(colour);
			
			if (grid.isExactlyColourAt(x, y, GameColour.WILD)) {
				lighter = GameColour.getLighterColour(GameColour.WILD);
			}
			
			grid.setColourAt(x, y, lighter);
			grid.setCellWorth(x, y, GameColour.getColourWorth(lighter));
			
			preClearedList.add(cell);
			
			startFloodClearAt(x, y - 1, colour, newClearList);
			startFloodClearAt(x + 1, y, colour, newClearList);
			startFloodClearAt(x, y + 1, colour, newClearList);
			startFloodClearAt(x - 1, y, colour, newClearList);
		}
		
		return newClearList;
	}
	
	
	
	private void markCellsForClear(final List<Cell> cells) {
		final int length = cells.size();
		Cell cell;
		for (int i = 0; i < length; i++) {
			cell = cells.get(i);
			grid.setMarkedForClearAt(cell.getX(), cell.getY(), true);
		}
	}
	
	
	
	public int getHighestBlock() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (grid.isSolidAt(x, y)) {
					return y;
				}
			}
		}
		return height - 1;
	}
	
}