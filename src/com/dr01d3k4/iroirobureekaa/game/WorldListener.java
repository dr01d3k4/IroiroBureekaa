package com.dr01d3k4.iroirobureekaa.game;



import com.dr01d3k4.iroirobureekaa.Assets;
import com.dr01d3k4.iroirobureekaa.IroiroBureekaa;



public class WorldListener {
	public IroiroBureekaa game;
	
	private boolean playedBlockLandThisFrame = false;
	
	private int blocksLanded = 0;
	private final int[] landedColours;
	
	private int blocksHighlighted = 0;
	private final int[] highlightedColours;
	
	private int blocksDestroyed = 0;
	private final int[] destroyedColours;
	
	
	
	public WorldListener(final IroiroBureekaa game) {
		this.game = game;
		
		highlightedColours = new int[] {0, 0, 0, 0, 0};
		landedColours = new int[] {0, 0, 0, 0, 0};
		destroyedColours = new int[] {0, 0, 0, 0, 0};
	}
	
	
	
	private int addArray(final int[] original, final int[] add) {
		if (original.length != add.length) {
			throw new IllegalArgumentException("The arrays have different sizes");
		}
		
		int total = 0;
		
		for (int i = 0, length = original.length; i < length; i += 1) {
			final int toAdd = add[i];
			original[i] += toAdd;
			total += toAdd;
		}
		
		return total;
	}
	
	
	
	public void update() {
		playedBlockLandThisFrame = false;
	}
	
	
	
	public void onNewRowAdd() {
		game.playSound(Assets.newRowAdd);
	}
	
	
	
	public void onBlockLand(int colourIndex) {
		blocksLanded += 1;
		landedColours[colourIndex] += 1;
		
		if (!playedBlockLandThisFrame) {
			game.playSound(Assets.blockLand);
			playedBlockLandThisFrame = true;
		}
	}
	
	
	
	public void onBlockHighlightStep(final int[] highlightedColours, final boolean hasWild) {
		blocksHighlighted += addArray(this.highlightedColours, highlightedColours);
		game.playSound(hasWild ? Assets.wildBlockHighlight : Assets.blockHighlight);
	}
	
	
	
	public void onBlockDestroy(final int[] destroyedColours) {
		blocksDestroyed += addArray(this.destroyedColours, destroyedColours);
		game.playSound(Assets.blockDestroy);
	}
	
	
	
	public int getBlocksHighlighted() {
		return blocksHighlighted;
	}
	
	
	
	public int getBlocksLanded() {
		return blocksLanded;
	}
	
	
	
	public int getBlocksDestroyed() {
		return blocksDestroyed;
	}	
}