package com.dr01d3k4.iroirobureekaa.game;



public class Cell {
	private int x;
	private int y;
	private int colour;
	
	
	
	public Cell(final int x, final int y, final int colour) {
		set(x, y, colour);
	}
	
	
	
	public void set(final int x, final int y, final int colour) {
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