package com.seventh_root.mummy_nose_best;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cursor {

    public final Player player;
    private Animation animation;
    private float stateTime;
    public float x;
    public float xOffset;
    public float y;
    private Texture texture;

    public Cursor(Player player) {
        this.player = player;
        switch (player.index) {
            case 0:
            default:
                texture = new Texture(Gdx.files.internal("cursor_player_1.png"));
                break;
            case 1:
                texture = new Texture(Gdx.files.internal("cursor_player_2.png"));
                break;
        }
        animation = new Animation(
                0.25F,
                new TextureRegion(texture, 0, 0, 64, 64),
                new TextureRegion(texture, 64, 0, 64, 64),
                new TextureRegion(texture, 128, 0, 64, 64),
                new TextureRegion(texture, 192, 0, 64, 64)
        );
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    public void render(float delta, SpriteBatch spriteBatch) {
        stateTime += delta;
        spriteBatch.draw(animation.getKeyFrame(stateTime), x + xOffset, y);
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void dispose() {
        texture.dispose();
    }

}
