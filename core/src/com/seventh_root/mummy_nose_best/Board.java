package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.security.SecureRandom;

public class Board {

    private Player player;
    public float x;
    public float y;
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
    private ParticleEffect explosion;
    private float loseTimer;
    private MummyNoseBest game;

    public static class Builder {
        private Player player;
        private float x;
        private float y;
        private int width;
        private int height;

        public Builder player(Player player) {
            this.player = player;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }
        public Builder height(int height) {
            this.height = height;
            return this;
        }
        public Board build() {
            Board board = new Board(x, y, width, height);
            board.player = player;
            board.game = player.game;
            board.create();
            return board;
        }

        public Builder x(float x) {
            this.x = x;
            return this;
        }

        public Builder y(float y) {
            this.y = y;
            return this;
        }
    }

    private Board(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.tiles = new int[width][height];
        this.width = width;
        this.height = height;
        random = new SecureRandom();
        texture = new Texture("tools.png");
        textureRegions = new Array<>();
        for (int texX = 0; texX < texture.getWidth(); texX += 64) {
            textureRegions.add(new TextureRegion(texture, texX, 0, 64, 64));
        }
        lost = false;
        lose = new Texture(Gdx.files.internal("lose.png"));
        clearingBlocks = Gdx.audio.newSound(Gdx.files.internal("AudioEffects/clearingblocks.wav"));
        lostSound = Gdx.audio.newSound(Gdx.files.internal("AudioEffects/Gameover.ogg"));
        movingUpSlow = Gdx.audio.newSound(Gdx.files.internal("AudioEffects/Movingupslow.ogg"));
        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("explode.p"), Gdx.files.internal("explode"));
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

    private void create() {
        this.clear();

        addRows();
        addAdditionalRows();
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
        movingUpSlow.play();
        addRow(0);
        addRow(this.height-1);
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
            for(y=3;y>=0; y--)
            {
                int currentTile = this.getTile(x, y);
                if(currentTile>0) {
                    this.setTile(x, y + 1, currentTile);
                }
            }

            for(y=6;y<this.height; y++)
            {
                int currentTile = this.getTile(x, y);
                if(currentTile>0) {
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
        if (x >= 0 && x < tiles.length && y < tiles[x].length) {
            tiles[x][y] = value;
        }
    }

    public int getTile(int x, int y) {
        return x >= 0 && x < tiles.length && y < tiles[x].length ? tiles[x][y] : 0;
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
                    clearingBlocks.play();
                    explosion.setPosition((x * 64) + this.x + 32, (y * 64) + 32);
                    explosion.reset();
                    explosion.start();
                    player.mainScreen.openEyes();
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

    public void swapTopRow(int x) {
        int highestBottomTileY = getHighestBottomTileYAt(x);
        int lowestTopTileY = getLowestTopTileYAt(x);
        int source = getTile(x, highestBottomTileY);
        int target = getTile(x, lowestTopTileY);
        setTile(x, lowestTopTileY, source);
        setTile(x, highestBottomTileY, target);
        boolean boardCheckChanges = false;
        do {
            boardCheckChanges = checkBoardAndCompress();
        } while (boardCheckChanges);
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
                offset = 0;
            }
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    int i = getTile(x, y);
                    if (i != 0) {
                        if (y < 5) {
                            spriteBatch.draw(textureRegions.get(i), this.x + (64 * x), (64 * (y - 1)) + offset);
                        } else {
                            spriteBatch.draw(textureRegions.get(i), this.x + (64 * x), (64 * (y + 1)) - offset);
                        }
                    }
                }
            }
        } else {
            spriteBatch.draw(lose, 0, 256);
            loseTimer += delta;
            if (loseTimer >= 5F) {
                game.mainScreen = new MainScreen(game);
                game.setScreen(game.splashScreen);
            }
        }
        explosion.update(delta);
        explosion.draw(spriteBatch);
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

        boolean boardCheckChanges = false;
        do {
            boardCheckChanges = checkBoardAndCompress();
        } while (boardCheckChanges);
    }

    public void renderShapes(float delta, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(x, 320, 0, x + 384, 320, 0);
    }

    public void dispose() {
        texture.dispose();
        lose.dispose();
        clearingBlocks.dispose();
        lostSound.dispose();
        movingUpSlow.dispose();
        explosion.dispose();
    }
}
