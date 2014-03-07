package com.dr01d3k4.iroirobureekaa.game;



import java.util.ArrayList;
import java.util.List;



public final class FallingPiecePool {
	private final List<FallingPiece> freePieces;
	private final int maxSize;
	
	
	
	public FallingPiecePool(final int maxSize) {
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
	
	
	
	public void free(final FallingPiece piece) {
		if (freePieces.size() < maxSize) {
			freePieces.add(piece);
		}
	}
}