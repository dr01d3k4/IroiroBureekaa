package com.dr01d3k4.iroirobureekaa.saving;



import java.io.Serializable;



public class GameData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public boolean soundEnabled = false;
	public final int MAX_HIGHSCORES = 10;
	public int[] highscores = new int[] { };
	public int totalScore = 0;
	public int totalGames = 0;
	
	
	
	public GameData() {
		clearHighscores();
	}
	
	
	
	public void clearHighscores() {
		highscores = new int[] { };
	}
	
	
	
	public String highscoresToString() {
		String highscoreString = "{";
		int length = highscores.length;
		for (int i = 0; i < length; i += 1) {
			highscoreString += Integer.toString(highscores[i]) + ((i < highscores.length - 1) ? ", " : "");
		}
		highscoreString += "}";
		return highscoreString;
	}
	
	
	
	public void onGameOver(int score) {
		totalGames += 1;
		totalScore += score;
		
		if ((highscores == null) || (highscores.length == 0)) {
			highscores = new int[] {score};
			
		} else {
			int addAtPosition = highscores.length;
			int length = highscores.length;
			for (int i = 0; i < length; i += 1) {
				if (score > highscores[i]) {
					addAtPosition = i;
					break;
				}
			}
			
			if (addAtPosition < MAX_HIGHSCORES) {
				int[] newHighscores = new int[highscores.length + 1];
				for (int i = 0; i < addAtPosition; i += 1) {
					newHighscores[i] = highscores[i];
				}
				newHighscores[addAtPosition] = score;
				length = highscores.length;
				for (int i = addAtPosition; i < length; i += 1) {
					newHighscores[i + 1] = highscores[i];
				}
				highscores = newHighscores;
			}
		}
	}
}