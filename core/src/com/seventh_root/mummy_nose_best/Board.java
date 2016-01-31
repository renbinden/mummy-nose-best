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
        this.clear();

        this.addRows();
    }

    public void clear() {
        for(int x=0;x<this.width; x++)
        {
            for(int y=0;y<this.height; y++)
            {
                this.setTile(x, y, 0);
            }
        }
    }

    public void addRows() {
        addRow(0);
        highestBottomRow++;
        addRow(this.height-1);
        lowestTopRow--;
    }

    public void addRow(int y) {
        int lastTile = -1;
        for (int i = 0; i < this.width; i++) {
            int nextTile;
            do {
                nextTile = this.getRandomInt(1, 4);
            } while (nextTile == lastTile);
            this.setTile(i, y, nextTile);
            lastTile = nextTile;
        }
    }

    // Returns true if the tile is attached to the top of the board.
    public boolean isTileTop(int posX,int posY) {
        if(posY == 0) {
            return true;
        }

        for(int y=posY;y>=0; y--)
        {
            if(this.getTile(posX,y) == 0) {
                return false;
            }
        }

        return true;
    }

    public void moveRows() {
        int x,y;

        // Move any bottom tiles up
        for(x=0;x<this.width; x++)
        {
            for(y=this.height-1;y>=0; y--)
            {
                int currentTile = this.getTile(x, y);
                if(currentTile>0 && this.isTileTop(x,y)) {
                    this.setTile(x, y + 1, currentTile);
                }
            }
        }

        // Move any top tiles down
        for(x=0;x<this.width; x++)
        {
            for(y=0;y<this.height; y++)
            {
                int currentTile = this.getTile(x, y);
                if(currentTile>0 && !this.isTileTop(x,y)) {
                    this.setTile(x, y - 1, currentTile);
                }
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

    public boolean checkBoardAndCompress() {
        boolean compressed = false;

        for(int y=0;y<this.height; y++)
        {
            for(int x=1;x<this.width - 1; x++)
            {
                int currentTile = this.getTile(x,y);

                if(currentTile!=0 && this.getTile(x - 1,y) == currentTile && this.getTile(x + 1,y) == currentTile) {
                    this.explodeTile(x-1,y);
                    this.explodeTile(x,y);
                    this.explodeTile(x+1,y);

                    compressed = true;
                }
            }
        }

        return compressed;
    }

    public void explodeTile(int posX,int posY) {
        boolean isTileTop = this.isTileTop(posX,posY);
        this.setTile(posX,posY,0);
        if(isTileTop) {
            for (int y = posY; y < this.height; y++) {
                int nextValue = this.getTile(posX, y+1);
                if(nextValue != 0) {
                    this.setTile(posX, y, nextValue);
                    this.setTile(posX, y+1, 0);
                } else {
                    break;
                }
            }
        } else {
            for (int y = posY; y >= 0; y--) {
                int nextValue = this.getTile(posX, y-1);
                if(nextValue != 0) {
                    this.setTile(posX, y, nextValue);
                    this.setTile(posX, y-1, 0);
                } else {
                    break;
                }
            }
        }
    }

    public void swapBlocks(int x, int y, int py) {
        int sourceTileValue = this.getTile(x,y);
        int targetTileValue = this.getTile(x,py);
        this.setTile(x,y, targetTileValue);
        this.setTile(x,py, sourceTileValue);
    }

    public void rotateBlocks(int x, boolean topBlocks) {
        int[] piecesList = new int[this.height];

        if(topBlocks) {
            for (int y = 0; y < this.height; y++) {
                int thisTileValue = this.getTile(x, y);
                int nextTileValue = this.getTile(x, y + 1);
                if (thisTileValue != 0) {
                    if (nextTileValue == 0) {
                        piecesList[0] = thisTileValue;
                        break;
                    } else {
                        piecesList[y + 1] = thisTileValue;
                    }
                }
            }
            // put the tiles in the correct location
            for (int y = 0; y < this.height; y++) {
                int newTile = piecesList[y];
                this.setTile(x, y, newTile);
                if(newTile == 0) {
                    break;
                }
            }
        } else {
            for (int y = this.height-1; y > 0; y--) {
                int thisTileValue = this.getTile(x, y);
                int nextTileValue = this.getTile(x, y - 1);
                if (thisTileValue != 0) {
                    if (nextTileValue == 0) {
                        piecesList[piecesList.length-1] = thisTileValue;
                        break;
                    } else {
                        piecesList[y - 1] = thisTileValue;
                    }
                }
            }
            // put the tiles in the correct location
            for (int y = this.height-1; y > 0; y--) {
                int newTile = piecesList[y];
                this.setTile(x, y, newTile);
                if(newTile == 0) {
                    break;
                }
            }
        }
    }
}
