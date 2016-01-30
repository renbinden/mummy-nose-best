package com.seventh_root.mummy_nose_best;

import java.security.SecureRandom;

public class Board {
    private int height;
    private int width;
    private int[][] tiles;
    public int highestBottomRow;
    public int lowestTopRow;
    private SecureRandom random;

    public Board( int width, int height) {
        this.tiles = new int[width][height];
        this.width = width;
        this.height = height;
        random = new SecureRandom();
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
        highestBottomRow = 0;
        lowestTopRow = tiles[0].length;
        for(int x=0;x<this.width; x++)
        {
            for(int y=0;y<this.height; y++)
            {
                this.setTile(x, y, 0);
            }
        }

        this.addRows();
    }

    public void addRows() {
        for(int i=0;i<this.width; i++)
        {
            this.setTile(i, 0, this.getRandomInt(1, 5));
        }
        highestBottomRow++;
        for(int j=0;j<this.width; j++)
        {
            this.setTile(j, this.height-1, this.getRandomInt(1, 5));
        }
        lowestTopRow--;
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
        this.tiles[x][y] = value;
    }

    public int getTile(int x, int y) {
        return this.tiles[x][y];
    }

// Check the board for three matches, make them disappear
    public void checkBoard() {
        for(int y=0;y<this.height; y++)
        {
            for(int x=1;x<this.width - 1; x++)
            {
                int currentTile = this.getTile(x,y);

                if(currentTile!=0 && this.getTile(x - 1,y) == currentTile && this.getTile(x + 1,y) == currentTile) {
                    this.setTile(x-1,y,0);
                    this.setTile(x,y,0);
                    this.setTile(x+1,y,0);
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

    public void rotateBlocks(int x, boolean topBlocks) {
        int[] piecesList = new int[this.height/2];

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
    }
}
