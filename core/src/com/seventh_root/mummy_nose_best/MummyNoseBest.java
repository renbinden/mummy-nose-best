package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Game;

public class MummyNoseBest extends Game {

    private MainScreen mainScreen;
	
	@Override
	public void create () {
		mainScreen = new MainScreen();
        setScreen(mainScreen);
	}

    @Override
    public void dispose() {
        super.dispose();
        mainScreen.dispose();
    }
}
