package com.dr01d3k4.iroirobureekaa;



import java.util.ArrayList;
import java.util.List;



import com.dr01d3k4.iroirobureekaa.game.FallingPiece;



public class FallingPiecePool {
	private final List<FallingPiece> freePieces;
	private final int maxSize;
	
	
	
	public FallingPiecePool(int maxSize) {
		this.maxSize = maxSize;
		freePieces = new ArrayList<FallingPiece>(maxSize);
	}
	
	
	
	public FallingPiece newObject(final float x, final float y, final int colour, final boolean canStartClear) {
		FallingPiece piece = null;
		
		if (freePieces.isEmpty()) {
			piece = new FallingPiece(x, y, colour, canStartClear);
		} else {
			piece = freePieces.remove(freePieces.size() - 1);
			piece.set(x, y, colour, canStartClear);
		}
		
		return piece;
	}
	
	
	
	public void free(FallingPiece piece) {
		if (freePieces.size() < maxSize) {
			freePieces.add(piece);
		}
	}
}
