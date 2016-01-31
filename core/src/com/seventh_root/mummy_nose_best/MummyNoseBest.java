package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Game;

public class MummyNoseBest extends Game {

    public SplashScreen splashScreen;
    public MainScreen mainScreen;
	
	@Override
	public void create () {
        splashScreen = new SplashScreen(this);
		mainScreen = new MainScreen(this);
        setScreen(splashScreen);
	}

    @Override
    public void dispose() {
        super.dispose();
        splashScreen.dispose();
        mainScreen.dispose();
    }
}
