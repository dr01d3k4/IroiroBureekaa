package com.dr01d3k4.iroirobureekaa.saving;



import java.io.Serializable;



import com.dr01d3k4.iroirobureekaa.game.gamemode.GameMode;



public final class GameData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public boolean soundEnabled = false;
	public final int MAX_HIGHSCORES = 10;
	public int[] infiniteHighscores = new int[] { };
	public int[] timedHighscores = new int[] { };
	public int totalScore = 0;
	public int totalInfiniteScore = 0;
	public int totalTimedScore = 0;
	public int totalGames = 0;
	public int totalInfiniteGames = 0;
	public int totalTimedGames = 0;
	
	
	
	public GameData() {
		clearHighscores();
	}
	
	
	
	public void clearHighscores() {
		infiniteHighscores = new int[] { };
		timedHighscores = new int[] { };
	}
	
	
	
	public String highscoresToString() {
		String highscoreString = "{";
		int length = infiniteHighscores.length;
		for (int i = 0; i < length; i += 1) {
			highscoreString += Integer.toString(infiniteHighscores[i])
				+ ((i < infiniteHighscores.length - 1) ? ", " : "");
		}
		highscoreString += "}";
		return highscoreString;
	}
	
	
	
	public void addScore(int score, int[] highscores) {
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
	
	
	
	public void onGameOver(int score, int gameModeId) {
		if (gameModeId == GameMode.INFINITE) {
			totalInfiniteScore += score;
			totalInfiniteGames += 1;
			addScore(score, infiniteHighscores);
			
		} else if (gameModeId == GameMode.TIMED) {
			totalTimedScore += score;
			totalTimedGames += 1;
			addScore(score, timedHighscores);
			
		} else {
			throw new IllegalArgumentException("Game mode not known");
		}
		
		totalGames += 1;
		totalScore += score;
	}
}