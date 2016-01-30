package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.security.SecureRandom;

public class Board {

    private int height;
    private int width;
    private int[][] tiles;
    private SecureRandom random;
    public float offset;
    private Texture texture;
    private Array<TextureRegion> textureRegions;
    public boolean lost;
    private Texture lose;
    private Sound clearingBlocks;
    private Sound lostSound;
    private Sound movingUpSlow;

    public Board( int width, int height) {
        this.tiles = new int[width][height];
        this.width = width;
        this.height = height;
        random = new SecureRandom();
        texture = new Texture("tools.png");
        textureRegions = new Array<>();
        for (int x = 0; x < texture.getWidth(); x += 64) {
            textureRegions.add(new TextureRegion(texture, x, 0, 64, 64));
        }
        lost = false;
        lose = new Texture(Gdx.files.internal("lose.png"));
        clearingBlocks = Gdx.audio.newSound(Gdx.files.internal("AudioEffects/clearingblocks.wav"));
        lostSound = Gdx.audio.newSound(Gdx.files.internal("AudioEffects/Gameover.ogg"));
        movingUpSlow = Gdx.audio.newSound(Gdx.files.internal("AudioEffects/Movingupslow.ogg"));
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRandomInt(int min, int max) {
        return min + random.nextInt((max + 1) - min);
    }

    public void create() {
        for(int x=0;x<this.width; x++)
        {
            for(int y=0;y<this.height; y++)
            {
                this.setTile(x, y, 0);
            }
        }

        addRows();
        addAdditionalRows();
    }

    public void addRows() {
        movingUpSlow.play();
        for(int i=0;i<this.width; i++)
        {
            this.setTile(i, 0, this.getRandomInt(1, 5));
        }
        for(int j=0;j<this.width; j++)
        {
            this.setTile(j, this.height-1, this.getRandomInt(1, 5));
        }
    }

    public void moveRows() {
        int x,y;
        for(x=0;x<this.width; x++)
        {
            for(y=this.height/2;y>0; y--)
            {
                this.setTile(x, y, this.getTile(x,y-1));
            }
        }

        for(x=0;x<this.width; x++)
        {
            for(y=this.height/2;y<9; y++)
            {
                this.setTile(x, y, this.getTile(x,y+1));
            }
        }
    }

    public void addAdditionalRows() {
        moveRows();
        addRows();
    }

    public void setTile(int x, int y, int value) {
        if (x >= 0 && x < tiles.length && y < tiles[x].length) {
            tiles[x][y] = value;
        }
    }

    public int getTile(int x, int y) {
        return x >= 0 && x < tiles.length && y < tiles[x].length ? tiles[x][y] : 0;
    }

// Check the board for three matches, make them disappear
    public void checkBoard() {
        for(int y=0;y<this.height; y++)
        {
            for(int x=1;x<this.width - 1; x++)
            {
                int currentTile = this.getTile(x,y);

                if(currentTile!=0 && this.getTile(x - 1,y) == currentTile && this.getTile(x + 1,y) == currentTile) {
                    setTile(x-1,y,0);
                    setTile(x,y,0);
                    setTile(x+1,y,0);
                    clearingBlocks.play();
                }
            }
        }
    }

    public boolean compressBoard() {

        boolean anyTilesMoved = false;
        boolean compressed = false;

        do {
            anyTilesMoved = false;
            for (int y = this.height / 2; y > 0; y--) {
                for (int x =0; x < this.width; x++) {
                    int currentTile = this.getTile(x, y);

                    if (currentTile != 0) {
                        if(this.getTile(x, y-1)==0) {
                            this.setTile(x, y-1, currentTile);
                            this.setTile(x, y, 0);

                            anyTilesMoved = true;
                            compressed = true;
                        }
                    }
                }
            }

            for (int y = this.height / 2; y < 9; y++) {
                for (int x = 0; x < this.width; x++) {
                    int currentTile = this.getTile(x, y);

                    if (currentTile != 0) {
                        if(this.getTile(x, y+1)==0) {
                            this.setTile(x, y+1, currentTile);
                            this.setTile(x, y, 0);

                            anyTilesMoved = true;
                            compressed = true;
                        }
                    }
                }
            }
        } while (anyTilesMoved);

        return compressed;
    }

    public void swapBlocks(int x, int y, int py) {
        int sourceTileValue = this.getTile(x,y);
        int targetTileValue = this.getTile(x,py);
        this.setTile(x,y, targetTileValue);
        this.setTile(x,py, sourceTileValue);
    }

    public void swapTopRow(int x) {
        int highestBottomTileY = getHighestBottomTileYAt(x);
        int lowestTopTileY = getLowestTopTileYAt(x);
        int source = getTile(x, highestBottomTileY);
        int target = getTile(x, lowestTopTileY);
        setTile(x, lowestTopTileY, source);
        setTile(x, highestBottomTileY, target);
        checkBoard();
        compressBoard();
    }

    public void render(float delta, SpriteBatch spriteBatch) {
        if (!lost) {
            offset += delta * 4;
            for (int x = 0; x < getWidth(); x++) {
                if (getHighestBottomTileYAt(x) == 4 && getLowestTopTileYAt(x) == 5 && offset >= 64) {
                    lost = true;
                    lostSound.play();
                }
            }
            if (offset >= 64) {
                addAdditionalRows();
                do {
                    checkBoard();
                } while (compressBoard());
                offset = 0;
            }
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    int i = getTile(x, y);
                    if (i != 0) {
                        if (y < 5) {
                            spriteBatch.draw(textureRegions.get(i), 64 * x, (64 * (y - 1)) + offset);
                        } else {
                            spriteBatch.draw(textureRegions.get(i), 64 * x, (64 * (y + 1)) - offset);
                        }
                    }
                }
            }
        } else {
            spriteBatch.draw(lose, 0, 256);
        }
    }

    public int getHighestBottomTileYAt(int x) {
        int y = 4;
        while (getTile(x, y) == 0 && y > 0) {
            y--;
        }
        return y;
    }

    public int getLowestTopTileYAt(int x) {
        int y = 5;
        while (getTile(x, y) == 0 && y < 10) {
            y++;
        }
        return y;
    }


    public void rotateBlocks(int x, boolean topBlocks) {
        int[] piecesList = new int[(this.height / 2) + 1];

        if(topBlocks) {
            for (int y = 0; y < this.height / 2; y++) {
                int thisTileValue = this.getTile(x, y);
                int nextTileValue = this.getTile(x, y + 1);
                if (thisTileValue != 0) {
                    if (nextTileValue == 0) {
                        piecesList[0] = thisTileValue;
                    } else {
                        piecesList[y + 1] = thisTileValue;
                    }
                }
            }
            // put the tiles in the correct location
            for (int y = 0; y < this.height / 2; y++) {
                this.setTile(x, y, piecesList[y]);
            }
        } else {
            for (int y = this.height-1; y > this.height / 2; y--) {
                int thisTileValue = this.getTile(x, y);
                int nextTileValue = this.getTile(x, y - 1);
                if (thisTileValue != 0) {
                    if (nextTileValue == 0) {
                        piecesList[piecesList.length-1] = thisTileValue;
                    } else {
                        piecesList[y - 1 - (this.height / 2)] = thisTileValue;
                    }
                }
            }
            // put the tiles in the correct location
            for (int y = this.height-1; y > this.height / 2; y--) {
                this.setTile(x, y, piecesList[y - (this.height / 2)]);
            }
        }
        checkBoard();
        compressBoard();
    }
}
