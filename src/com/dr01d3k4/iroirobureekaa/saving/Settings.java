package com.dr01d3k4.iroirobureekaa.saving;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class Settings {
	private static final String saveFilename = ".iroirobureekaa";
	
	
	
	public static void save(FileIO files, GameData gameData) {
		ObjectOutputStream obj = null;
		
		try {
			obj = new ObjectOutputStream(files.writeFile(saveFilename));
			obj.writeObject(gameData);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (obj != null) {
					obj.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static GameData load(FileIO files) {
		ObjectInputStream obj = null;
		GameData gameData = new GameData();
		
		try {
			obj = new ObjectInputStream(files.readFile(saveFilename));
			gameData = (GameData) obj.readObject();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (obj != null) {
					obj.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		
		return gameData;
	}
}