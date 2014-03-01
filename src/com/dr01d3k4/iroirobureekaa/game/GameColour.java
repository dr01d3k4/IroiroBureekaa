package com.dr01d3k4.iroirobureekaa.game;



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
	
	private static final int[] RANDOM_COLOURS = {RED, GREEN, BLUE, YELLOW, WILD};
	private static final int[] RANDOM_COLOUR_PROBABILITY = {324, 324, 249, 99, 4};
	private static final int[] COLOUR_WORTH = {15, 15, 20, 45, 60};
	
	public static final int UI_LIGHT = Color.LTGRAY;
	public static final int UI_DARK = Color.DKGRAY;
	public static final int FALLING_COLUMN_HINT = 0xffefefef; // e0e0e0;
	public static final int TEXT = Color.BLACK;
	public static final int OUTLINE = Color.BLACK;
	
	
	
	public static int randomColour(final Random random) {
		final int randomNumber = random.nextInt(1000);
		int cumulative = 0;
		int colour = RANDOM_COLOURS[0];
		int length = RANDOM_COLOUR_PROBABILITY.length;
		for (int i = 0; i < length; i += 1) {
			cumulative += RANDOM_COLOUR_PROBABILITY[i];
			if (randomNumber <= cumulative) {
				colour = RANDOM_COLOURS[i];
				break;
			}
		}
		return colour;
	}
	
	
	
	public static int getColourWorth(int colour) {
		colour = getDarkerColour(colour);
		int worth = 0;
		int length = RANDOM_COLOURS.length;
		for (int i = 0; i < length; i += 1) {
			if (RANDOM_COLOURS[i] == colour) {
				worth = COLOUR_WORTH[i];
				break;
			}
		}
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
}