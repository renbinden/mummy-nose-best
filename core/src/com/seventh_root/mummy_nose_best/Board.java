package com.seventh_root.mummy_nose_best;

public class Board {
    private int height;
    private int width;
    private int[] tiles;

    public Board( int width, int height) {
        this.tiles = new int[width*height];
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRandomInt(int min, int max) {
        return (int) (Math.floor(Math.random() * (max - min)) + min);
    }

    public void create() {
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

        for(int j=0;j<this.width; j++)
        {
            this.setTile(j, this.height-1, this.getRandomInt(1, 5));
        }
    }

    public void moveRows() {
        int x,y;
        for(x=0;x<this.width; x++)
        {
            for(y=this.height/2;y<0; y--)
            {
                this.setTile(x, y, this.getTile(x,y-1));
            }
        }

        for(x=0;x<this.width; x++)
        {
            for(y=this.height/2;y<0; y--)
            {
                this.setTile(x, y, this.getTile(x,y-1));
            }
        }
    }

    public void setTile(int x, int y, int value) {
        this.tiles[x+y*this.width] = value;
    }

    public int getTile(int x, int y) {
        return this.tiles[x+y*this.width];
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

    public void swapBlocks(int x, int y, int py) {
        int sourceTileValue = this.getTile(x,y);
        int targetTileValue = this.getTile(x,py);
        this.setTile(x,y, targetTileValue);
        this.setTile(x,py, sourceTileValue);
    }
}
