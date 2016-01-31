package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class PlayerManager {

    public Array<Player> players;

    public PlayerManager(MainScreen mainScreen) {
        players = new Array<>();
        int index = 0;
        for (Controller controller : Controllers.getControllers()) {
            players.add(new Player(index++, controller, mainScreen));
        }
    }

    public void renderPlayers(float delta, SpriteBatch spriteBatch) {
        for (Player player : players) {
            player.render(delta, spriteBatch);
        }
    }

    public void renderShapes(float delta, ShapeRenderer shapeRenderer) {
        for (Player player : players) {
            player.renderShapes(delta, shapeRenderer);
        }
    }

    public void dispose() {
        for (Player player : players) {
            player.dispose();
        }
    }

}
