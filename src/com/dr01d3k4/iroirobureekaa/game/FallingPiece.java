package com.dr01d3k4.iroirobureekaa.game;



public class FallingPiece {
	private float x;
	private float y;
	private final int colour;
	private final boolean startClear;
	
	
	
	public FallingPiece(final float x, final float y, final int colour, final boolean startClear) {
		this.x = x;
		this.y = y;
		this.colour = colour;
		this.startClear = startClear;
	}
	
	
	
	public void setX(final float x) {
		this.x = x;
	}
	
	
	
	public float getX() {
		return x;
	}
	
	
	
	public void setY(final float y) {
		this.y = y;
	}
	
	
	
	public float getY() {
		return y;
	}
	
	
	
	public int getRoundedX() {
		return Math.round(x);
	}
	
	
	
	public int getRoundedY() {
		return Math.round(y);
	}
	
	
	
	public int getColour() {
		return colour;
	}
	
	
	
	public boolean canStartClear() {
		return startClear;
	}
}