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
    public Board gameBoard;
    PlayerManager playerManager;

    public MainScreen() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        gameBoard = new Board(6, 10);
        gameBoard.create();
        compressBoard();
        playerManager = new PlayerManager(gameBoard);
    }

    public void compressBoard() {
        boolean madeCompression = false;
        do {
            gameBoard.checkBoard();
            madeCompression = gameBoard.compressBoard();
        }while(madeCompression);
    }

    public void tileClickListener(int x, int y) {
        // Reverse y as it is from the bottom.
        int reverseY = 9 - y;
        // Make sure we're on the board.
        if (x >= this.gameBoard.getWidth() || reverseY > this.gameBoard.getHeight()) {
            return;
        }

        int tileNumber = this.gameBoard.getTile(x, reverseY);

        if (tileNumber > 0) {
            int swapDirection = (reverseY > 5) ? -1 : 1;

            if (swapDirection == 1) {

                for (int py = reverseY + 1; py < 10; py++) {
                    int foundTileValue = this.gameBoard.getTile(x, py);

                    if (foundTileValue > 0) {
                        if (py > 5) {
                            this.gameBoard.swapBlocks(x, reverseY, py);
                        }
                        else {
                            // we aren't on the bottom so rotate the blocks around
                            this.gameBoard.rotateBlocks(x,true);
                        }

                        // Get out of the loop as we've either done a swap or rejected the swap
                        break;
                    }
                }
            } else {

                for (int py = reverseY - 1; py >= 0; py--) {
                    int foundTileValue = this.gameBoard.getTile(x, py);

                    if (foundTileValue > 0) {
                        if (py < 5) {
                            this.gameBoard.swapBlocks(x, reverseY, py);
                        }
                        else {
                            // we aren't on the bottom so rotate the blocks around
                            this.gameBoard.rotateBlocks(x,false);
                        }
                        // Get out of the loop as we've either done a swap or rejected the swap
                        break;
                    }
                }
            }

            compressBoard();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        gameBoard.render(delta, spriteBatch);
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
    }

}
