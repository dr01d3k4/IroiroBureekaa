package com.dr01d3k4.iroirobureekaa.game;



import com.dr01d3k4.iroirobureekaa.game.GameWorld.GameState;



public final class FallingPiece {
	public float x;
	public float y;
	public int colour;
	public boolean canStartClear;
	public int horizontalTarget;
	
	
	
	public FallingPiece(final float x, final float y, final int colour, final boolean canStartClear) {
		set(x, y, colour, canStartClear);
	}
	
	
	
	public void set(final float x, final float y, final int colour, final boolean canStartClear) {
		this.x = x;
		this.y = y;
		this.colour = colour;
		this.canStartClear = canStartClear;
		
		horizontalTarget = getRoundedX();
	}
	
	
	
	public int getRoundedX() {
		return Math.round(x);
	}
	
	
	
	public int getRoundedY() {
		return Math.round(y);
	}
	
	
	
	public boolean attemptMove(final float dx, float dy, final Grid grid) {
		boolean collision = false;
		boolean collidedAtLeastOnce = false;
		int collisionX = -1;
		int collisionY = -1;
		
		final float newX = x + dx;
		final float newY = y + dy;
		
		if (newX <= 0) {
			x = 0;
		}
		
		if (newX >= (grid.width - 1)) {
			x = grid.width - 1;
		}
		
		if (newY >= (grid.height - 1)) {
			y = grid.height - 1;
			return false;
		}
		
		if (y > -1) {
			boolean checkForCollision = true;
			
			final int startX = (int) ((dx <= 0) ? Math.floor(x) : Math.ceil(x));
			final int endX = (int) ((dx <= 0) ? Math.ceil(x + dx) : Math.floor(x));
			final int incX = (dx <= 0) ? 1 : -1;
			
			while (checkForCollision) {
				collision = false;
				checkForCollision = false;
				
				if ((y < -1) && collidedAtLeastOnce) {
					collision = true;
					collisionX = getRoundedX();
					collisionY = getRoundedY();
					break;
				}
				
				for (int testX = startX; (dx <= 0) ? (testX <= endX) : (testX >= endX); testX += incX) {
					for (int testY = (int) Math.floor(y); testY < (Math.ceil(y + dy) + 1); testY += 1) {
						if (grid.isSolidAt(testX, testY)) {
							collision = true;
							collisionX = testX;
							collisionY = testY;
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
					y = (float) Math.floor(collisionY - 1);
					dy = 0;
				} else {
					checkForCollision = false;
				}
			}
		}
		
		if (collidedAtLeastOnce) {
			x = collisionX;
		} else {
			x += dx;
			y += dy;
		}
		
		return !collidedAtLeastOnce;
	}
	
	
	
	public boolean handleCollision(final GameWorld gameWorld) {
		final int x = getRoundedX();
		final int y = getRoundedY();
		
		if (y < 0) {
			gameWorld.changeState(GameState.LOST);
			return false;
		}
		
		gameWorld.grid.setColourAt(x, y, colour);
		gameWorld.grid.setCellWorth(x, y, GameColour.getColourWorth(colour));
		gameWorld.landedPieces.add(this);
		
		gameWorld.worldListener.onBlockLand(GameColour.getColourToIndex(colour));
		
		return true;
	}
	
	
	
	public void attemptMoveHorizontal(final Grid grid, final float deltaTime) {
		if (horizontalTarget < 0) {
			horizontalTarget = 0;
		}
		
		if (horizontalTarget >= grid.width) {
			horizontalTarget = grid.width - 1;
		}
		
		if (x != horizontalTarget) {
			final float moveAmount = deltaTime / GameWorld.PLAYER_HORIZONTAL_MOVE_TIME;
			final int moveDirection = (horizontalTarget > x) ? 1 : -1;
			
			if (Math.abs(x - horizontalTarget) < moveAmount) {
				x = Math.round(horizontalTarget);
			} else {
				x += moveAmount * moveDirection;
			}
			
			if (grid.isSolidAt((int) Math.floor(x), (int) Math.floor(y))
				|| grid.isSolidAt((int) Math.floor(x), (int) Math.ceil(y))) {
				x = getRoundedX();
				horizontalTarget = getRoundedX();
			}
			
			if (grid.isSolidAt((int) Math.ceil(x), (int) Math.floor(y))
				|| grid.isSolidAt((int) Math.ceil(x), (int) Math.ceil(y))) {
				x = getRoundedX();
				horizontalTarget = getRoundedX();
			}
		} else {
			x = horizontalTarget;
		}
	}
}