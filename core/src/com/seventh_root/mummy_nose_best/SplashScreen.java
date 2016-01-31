package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class SplashScreen extends ScreenAdapter {

    private MummyNoseBest game;
    private SpriteBatch spriteBatch;
    private Texture splash;

    public SplashScreen(MummyNoseBest game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        splash = new Texture(Gdx.files.internal("splash.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(splash, 0, 0);
        spriteBatch.end();
        for (Controller controller : Controllers.getControllers()) {
            if ((controller.getButton(7) && (SharedLibraryLoader.isWindows || SharedLibraryLoader.isLinux)) || (controller.getButton(4) && SharedLibraryLoader.isMac)) {
                if (game.mainScreen != null) {
                    game.mainScreen.dispose();
                }
                game.mainScreen = new MainScreen();
                game.setScreen(game.mainScreen);
            }
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        splash.dispose();
    }
}
