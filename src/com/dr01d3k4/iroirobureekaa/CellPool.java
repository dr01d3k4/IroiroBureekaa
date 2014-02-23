package com.dr01d3k4.iroirobureekaa;



import java.util.ArrayList;
import java.util.List;



import com.dr01d3k4.iroirobureekaa.game.Cell;



public class CellPool {
	private final List<Cell> freeCells;
	private final int maxSize;
	
	
	
	public CellPool(int maxSize) {
		this.maxSize = maxSize;
		freeCells = new ArrayList<Cell>(maxSize);
	}
	
	
	
	public Cell newObject(final int x, final int y, final int colour) {
		Cell cell = null;
		
		if (freeCells.isEmpty()) {
			cell = new Cell(x, y, colour);
		} else {
			cell = freeCells.remove(freeCells.size() - 1);
			cell.set(x, y, colour);
		}
		
		return cell;
	}
	
	
	
	public void free(Cell cell) {
		if (freeCells.size() < maxSize) {
			freeCells.add(cell);
		}
	}
}
