package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static java.lang.Math.abs;

public class Player {

    private Board board;
    public final int index;
    public final Controller controller;
    public final Cursor cursor;
    private boolean actionProcessed; // Whether the swap has been processed.
    private boolean rotateProcessed; // Same as above, but for rotate

    public Player(Board board, int index, Controller controller) {
        this.board = board;
        this.index = index;
        this.controller = controller;
        this.cursor = new Cursor(this);
    }

    public void render(float delta, SpriteBatch spriteBatch) {
        cursor.moveTo(cursor.x, ((board.getHighestBottomTileYAt(((int) cursor.x + 32) / 64) - 1) * 64) + board.offset);
        if (abs(controller.getAxis(0)) >= 0.2F) {
            cursor.move(controller.getAxis(0) * delta * 128, 0);
        }
        if (controller.getButton(0)) {
            if (!actionProcessed) { // Only process the swap if the action button wasn't held down last tick
                board.swapTopRow(((int) cursor.x + 32) / 64);
                actionProcessed = true;
            }
        } else {
            actionProcessed = false;
        }
        if (controller.getButton(2)) {
            if (!rotateProcessed) {
                board.rotateBlocks(((int) cursor.x + 32) / 64, true);
                rotateProcessed = true;
            }
        } else {
            rotateProcessed = false;
        }
        cursor.render(delta, spriteBatch);
    }

    public void renderShapes(float delta, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        int highestBottomTileY = board.getHighestBottomTileYAt(((int) cursor.x + 32) / 64);
        int lowestTopTileY = board.getLowestTopTileYAt(((int) cursor.x + 32) / 64);
        shapeRenderer.box((((int) cursor.x + 32) / 64) * 64, (highestBottomTileY - 1) * 64 + board.offset, 0, 64, 64, 0);
        shapeRenderer.box((((int) cursor.x + 32) / 64) * 64, (lowestTopTileY + 1) * 64 - board.offset, 0, 64, 64, 0);
    }

    public void dispose() {
        cursor.dispose();
    }

}
