package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;

public class MainScreen extends ScreenAdapter {

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    PlayerManager playerManager;

    public MainScreen() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        playerManager = new PlayerManager();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        playerManager.renderPlayers(delta, spriteBatch);
        spriteBatch.end();
        shapeRenderer.begin(Line);
        playerManager.renderShapes(delta, shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        playerManager.dispose();
        shapeRenderer.dispose();
    }

}
