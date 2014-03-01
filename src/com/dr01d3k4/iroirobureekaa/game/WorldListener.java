package com.dr01d3k4.iroirobureekaa.game;



import com.dr01d3k4.iroirobureekaa.Assets;
import com.dr01d3k4.iroirobureekaa.IroiroBureekaa;



public class WorldListener {
	public IroiroBureekaa game;
	
	
	
	public WorldListener(IroiroBureekaa game) {
		this.game = game;
	}
	
	
	
	public void onBlockDestroy() {
		game.playSound(Assets.blockDestroy);
	}
}
