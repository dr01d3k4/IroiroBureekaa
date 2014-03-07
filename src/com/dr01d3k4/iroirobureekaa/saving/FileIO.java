package com.dr01d3k4.iroirobureekaa.saving;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;



public final class FileIO {
	private final Context context;
	private final AssetManager assets;
	
	
	
	public FileIO(final Context context) {
		this.context = context;
		assets = context.getAssets();
	}
	
	
	
	public InputStream readAsset(String filename) throws IOException {
		return assets.open(filename);
	}
	
	
	
	public InputStream readFile(String filename) throws IOException {
		return context.openFileInput(filename);
	}
	
	
	
	public FileOutputStream writeFile(final String filename) throws IOException {
		return context.openFileOutput(filename, Context.MODE_PRIVATE);
	}
	
	
	
	public SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}