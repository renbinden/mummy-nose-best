package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import static java.lang.Math.abs;

public class Player {

    public MummyNoseBest game;
    public MainScreen mainScreen;
    public Board board;
    public final int index;
    public final Controller controller;
    public final Cursor cursor;
    private boolean actionProcessed; // Whether the swap has been processed.
    private boolean rotateProcessed; // Same as above, but for rotate
    private boolean rotateDownProcessed;

    public Player(int index, Controller controller, MainScreen mainScreen, MummyNoseBest game) {
        this.game = game;
        this.mainScreen = mainScreen;
        this.board = new Board.Builder()
                .player(this)
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
        if  (!board.lost) {
            cursor.moveTo(cursor.x, ((board.getHighestBottomTileYAt(((int) cursor.x + 32) / 64) - 1) * 64) + board.offset);
            if (SharedLibraryLoader.isLinux) {
                control(controller.getAxis(0), delta, 2, 0, 3);
            } else if (SharedLibraryLoader.isWindows) {
                control(controller.getAxis(1), delta, 2, 0, 3);
            } else if (SharedLibraryLoader.isMac) {
                control(controller.getAxis(2), delta, 13, 11, 14);
            }
            cursor.render(delta, spriteBatch);
        }
    }

    public void control(float axis, float delta, int buttonA, int buttonX, int buttonY) {
        if (abs(axis) >= 0.2F) {
            cursor.move(axis * delta * 128, 0);
        }
        if (controller.getButton(buttonA)) {
            if (!actionProcessed) { // Only process the swap if the action button wasn't held down last tick
                board.swapTopRow(((int) cursor.x + 32) / 64);
                actionProcessed = true;
            }
        } else {
            actionProcessed = false;
        }
        if (controller.getButton(buttonX)) {
            if (!rotateProcessed) {
                board.rotateBlocks(((int) cursor.x + 32) / 64, true);
                rotateProcessed = true;
            }
        } else {
            rotateProcessed = false;
        }
        if (controller.getButton(buttonY)) {
            if (!rotateDownProcessed) {
                board.rotateBlocks(((int) cursor.x + 32) / 64, false);
                rotateDownProcessed = true;
            }
        } else {
            rotateDownProcessed = false;
        }
    }

    public void renderShapes(float delta, ShapeRenderer shapeRenderer) {
        if (!board.lost) {
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
            board.renderShapes(delta, shapeRenderer);
        }
    }

    public void dispose() {
        board.dispose();
        cursor.dispose();
    }

}
