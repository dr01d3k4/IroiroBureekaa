package com.dr01d3k4.iroirobureekaa.game;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;



import android.graphics.Color;



public final class GameColour {
	public static final int NONE = Color.WHITE;
	public static final int OUT_OF_BOUNDS = Color.BLACK;
	
	public static final int RED = 0xffc00000;
	public static final int GREEN = 0xff00c000;
	public static final int BLUE = 0xff0000c0;
	public static final int YELLOW = 0xffdfdf00;
	public static final int WILD = 0xffc000c0;
	
	public static final int LIGHT_RED = 0xffff1414;
	public static final int LIGHT_GREEN = 0xff14ff14;
	public static final int LIGHT_BLUE = 0xff1414ff;
	public static final int LIGHT_YELLOW = 0xfff2f214;
	public static final int LIGHT_WILD = 0xffff00ff;
	
	private static final List<Integer> colourBag = new ArrayList<Integer>();
	
	private static final int[] RANDOM_COLOURS = {RED, GREEN, BLUE, YELLOW}; // , WILD};
	private static final int[] RANDOM_COLOUR_PROBABILITY = {3, 3, 2, 1}; // {32, 32, 24, 10}; // , 2};
	private static final int[] COLOUR_WORTH = {100, 100, 200, 450, 600};
	private static final int COLOUR_WORTH_SCALE = 1;
	
	public static final int UI_LIGHT = Color.LTGRAY;
	public static final int UI_DARK = Color.DKGRAY;
	public static final int FALLING_COLUMN_HINT = 0xefefefef;
	public static final int TEXT = Color.BLACK;
	public static final int OUTLINE = Color.BLACK;
	
	
	
	private static void fillColourBag() {
		colourBag.clear();
		
		for (int i = 0, length = RANDOM_COLOURS.length; i < length; i += 1) {
			for (int j = 0, count = RANDOM_COLOUR_PROBABILITY[i]; j < count; j += 1) {
				colourBag.add(RANDOM_COLOURS[i]);
			}
		}
	}
	
	
	
	public static int randomColourFromBag(final Random random) {
		if (colourBag.size() == 0) {
			fillColourBag();
		}
		
		final int index = random.nextInt(colourBag.size());
		final int colour = colourBag.get(index);
		colourBag.remove(index);
		
		return colour;
	}
	
	
	
	public static int getColourWorth(int colour) {
		colour = getDarkerColour(colour);
		int worth = 0;
		try {
			int index = getColourToIndex(colour);
			worth = COLOUR_WORTH[index];
		} catch (IllegalArgumentException e) {}
		worth *= COLOUR_WORTH_SCALE;
		return worth;
	}
	
	
	
	public static int getLighterColour(final int colour) {
		switch (colour) {
			case RED:
				return LIGHT_RED;
			case GREEN:
				return LIGHT_GREEN;
			case BLUE:
				return LIGHT_BLUE;
			case YELLOW:
				return LIGHT_YELLOW;
			case WILD:
				return LIGHT_WILD;
			default:
				return colour;
		}
	}
	
	
	
	public static int getDarkerColour(final int colour) {
		switch (colour) {
			case LIGHT_RED:
				return RED;
			case LIGHT_GREEN:
				return GREEN;
			case LIGHT_BLUE:
				return BLUE;
			case LIGHT_YELLOW:
				return YELLOW;
			case LIGHT_WILD:
				return WILD;
			default:
				return colour;
		}
	}
	
	
	
	public static boolean isLightColour(final int colour) {
		return ((colour == LIGHT_RED) || (colour == LIGHT_GREEN) || (colour == LIGHT_BLUE)
			|| (colour == LIGHT_YELLOW) || (colour == LIGHT_WILD));
	}
	
	
	
	public static int getColourToIndex(final int colour) {
		switch (colour) {
			case RED:
				return 0;
			case GREEN:
				return 1;
			case BLUE:
				return 2;
			case YELLOW:
				return 3;
			case WILD:
				return 4;
				
			default:
				throw new IllegalArgumentException("Colour not known");
		}
	}
}