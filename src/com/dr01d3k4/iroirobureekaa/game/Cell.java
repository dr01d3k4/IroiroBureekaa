package com.dr01d3k4.iroirobureekaa.game;



public class Cell {
	private final int x;
	private final int y;
	private final int colour;
	
	
	
	public Cell(final int x, final int y, final int colour) {
		this.x = x;
		this.y = y;
		this.colour = colour;
	}
	
	
	
	public int getX() {
		return x;
	}
	
	
	
	public int getY() {
		return y;
	}
	
	
	
	public int getColour() {
		return colour;
	}
}