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
		for (int i = 0; i < highscores.length; i++) {
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
			for (int i = 0; i < highscores.length; i++) {
				if (score > highscores[i]) {
					addAtPosition = i;
					break;
				}
			}
			
			if (addAtPosition < MAX_HIGHSCORES) {
				int[] newHighscores = new int[highscores.length + 1];
				for (int i = 0; i < addAtPosition; i++) {
					newHighscores[i] = highscores[i];
				}
				newHighscores[addAtPosition] = score;
				for (int i = addAtPosition; i < highscores.length; i++) {
					newHighscores[i + 1] = highscores[i];
				}
				highscores = newHighscores;
			}
		}
	}
}