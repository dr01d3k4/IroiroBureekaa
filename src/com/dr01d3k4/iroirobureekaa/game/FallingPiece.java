package com.dr01d3k4.iroirobureekaa.game;



public final class FallingPiece {
	public float x;
	public float y;
	public int colour;
	public boolean canStartClear;
	
	
	
	public FallingPiece(final float x, final float y, final int colour, final boolean canStartClear) {
		set(x, y, colour, canStartClear);
	}
	
	
	
	public void set(float x, float y, int colour, boolean canStartClear) {
		this.x = x;
		this.y = y;
		this.colour = colour;
		this.canStartClear = canStartClear;
	}
	
	
	
	public int getRoundedX() {
		return Math.round(x);
	}
	
	
	
	public int getRoundedY() {
		return Math.round(y);
	}
}