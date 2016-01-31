package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import static java.lang.Math.abs;

public class Player {

    private Board board;
    public final int index;
    public final Controller controller;
    public final Cursor cursor;
    private boolean actionProcessed; // Whether the swap has been processed.
    private boolean rotateProcessed; // Same as above, but for rotate

    public Player(int index, Controller controller) {
        this.board = new Board.Builder()
                .x(index * 768)
                .y(0)
                .width(6)
                .height(10)
                .build();
        this.index = index;
        this.controller = controller;
        this.cursor = new Cursor(this);
        cursor.xOffset = index * 768;
    }

    public void render(float delta, SpriteBatch spriteBatch) {
        board.render(delta, spriteBatch);
        cursor.moveTo(cursor.x, ((board.getHighestBottomTileYAt(((int) cursor.x + 32) / 64) - 1) * 64) + board.offset);
        if (SharedLibraryLoader.isLinux) {
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
        } else if (SharedLibraryLoader.isWindows) {
            if (abs(controller.getAxis(1)) >= 0.2F) {
                cursor.move(controller.getAxis(1) * delta * 128, 0);
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
        } else if (SharedLibraryLoader.isMac) {
            if (abs(controller.getAxis(2)) >= 0.2F) {
                cursor.move(controller.getAxis(2) * delta * 128, 0);
            }
            if (controller.getButton(11)) {
                if (!actionProcessed) { // Only process the swap if the action button wasn't held down last tick
                    board.swapTopRow(((int) cursor.x + 32) / 64);
                    actionProcessed = true;
                }
            } else {
                actionProcessed = false;
            }
            if (controller.getButton(13)) {
                if (!rotateProcessed) {
                    board.rotateBlocks(((int) cursor.x + 32) / 64, true);
                    rotateProcessed = true;
                }
            } else {
                rotateProcessed = false;
            }
        }
        cursor.render(delta, spriteBatch);
    }

    public void renderShapes(float delta, ShapeRenderer shapeRenderer) {
        switch (index) {
            case 0:
                shapeRenderer.setColor(Color.RED);
                break;
            case 1:
                shapeRenderer.setColor(Color.BLUE);
                break;
            case 2:
                shapeRenderer.setColor(Color.GREEN);
                break;
            case 3:
                shapeRenderer.setColor(Color.YELLOW);
                break;
            default:
                shapeRenderer.setColor(Color.BLACK);
                break;
        }
        int highestBottomTileY = board.getHighestBottomTileYAt(((int) cursor.x + 32) / 64);
        int lowestTopTileY = board.getLowestTopTileYAt(((int) cursor.x + 32) / 64);
        shapeRenderer.box(((((int) cursor.x + 32) / 64) * 64) + cursor.xOffset, (highestBottomTileY - 1) * 64 + board.offset, 0, 64, 64, 0);
        shapeRenderer.box(((((int) cursor.x + 32) / 64) * 64) + cursor.xOffset, (lowestTopTileY + 1) * 64 - board.offset, 0, 64, 64, 0);
    }

    public void dispose() {
        cursor.dispose();
    }

}
