package com.dr01d3k4.iroirobureekaa.game;



public class FallingPiece {
	public float x;
	public float y;
	public int colour;
	public boolean canStartClear;
	
	
	
	public FallingPiece() {}
	
	
	
	public FallingPiece(final float x, final float y, final int colour, final boolean canStartClear) {
		this.x = x;
		this.y = y;
		this.colour = colour;
		this.canStartClear = canStartClear;
	}
	
	
	
//	public void setX(final float x) {
//		this.x = x;
//	}
//	
//	
//	
//	public float getX() {
//		return x;
//	}
//	
//	
//	
//	public void setY(final float y) {
//		this.y = y;
//	}
//	
//	
//	
//	public float getY() {
//		return y;
//	}
//	
//	
	
	public int getRoundedX() {
		return Math.round(x);
	}
	
	
	
	public int getRoundedY() {
		return Math.round(y);
	}
	
	
	
//	public int getColour() {
//		return colour;
//	}
//	
//	
//	
//	public boolean canStartClear() {
//		return startClear;
//	}
}