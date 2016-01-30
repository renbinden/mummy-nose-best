package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class MainScreen extends ScreenAdapter {

    SpriteBatch spriteBatch;

    public MainScreen() {
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }
}
