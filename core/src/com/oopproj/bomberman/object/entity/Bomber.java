package com.oopproj.bomberman.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.oopproj.bomberman.object.GameObject;
import com.oopproj.bomberman.object.bomb.Bomb;
import com.oopproj.bomberman.object.ground.Grass;
import com.oopproj.bomberman.ui.GameSound;
import com.oopproj.bomberman.utils.Assets;
import com.oopproj.bomberman.utils.Direction;
import com.oopproj.bomberman.utils.Map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bomber extends Entity {
    private final float startX; // hoi sinh tai x,y
    private final float startY;
    protected List<Bomb> bombList = new ArrayList<>();
    protected int bomRate = 1;
    float deltaTime = 0;
    float blinkDuration = 0.1f;
    private int life = 3;
    private int flameLength = 2;
    public Bomber(Texture texture, int numberOfFrame, float x, float y) {
        super(texture, numberOfFrame, x, y);
        pos.width = (float) texture.getWidth() / numberOfFrame;
        pos.height = (float) texture.getWidth() / numberOfFrame - 15;
        movingSpeed = 200;
        startX = x;
        startY = y;
        currentDirection = Direction.RIGHT;
    }

    public void resetPlayer(int life) {
        System.out.println("MANG HIEN TAI: " + life);
        this.life = life;
        movingSpeed = 200;
        bomRate = 1;
        state = EntityState.PROTECTED;
        pos.x = startX;
        pos.y = startY;
        currentDirection = Direction.RIGHT;
        flameLength = 2;
    }

    public void placeBomB(float x, float y, Map map) {
        if ((state == EntityState.PROTECTED || state == EntityState.ALIVE) && bombList.size() <= bomRate) {
            Rectangle bomTmp = new Rectangle(x, y, pos.width, pos.height);
            for (Bomb bomb : bombList) {
                if (bomb.getPos().overlaps(bomTmp)) {
                    GameSound.playPlaceBomb();
                    return;
                }
            }
            Texture t = assets.get(Assets.BOMB);
            // Tim o ma 1/2 nguoi dang dung
            GameObject tmp = new Grass(t, pos.x + pos.width / 2, pos.y + pos.height / 2);
            GameObject grass = map.getMap().get(tmp.getPositionAtMap(map)); // co chua bom trong class
            // vi tri bomb:
            float bombX = grass.getPos().x + Math.abs((grass.getPos().getWidth() - (float) t.getWidth() / 3) / 2);
            float bombY = grass.getPos().y + Math.abs((grass.getPos().getHeight() - t.getHeight()) / 2);
            Bomb bomb = new Bomb(t, bombX, bombY, flameLength, map);
            bombList.add(bomb);
        }
    }

    /**
     * Xoa bom neu no qua gio
     */
    public void destroyBomb() {
        for (Iterator<Bomb> iter = bombList.iterator(); iter.hasNext(); ) {
            Bomb bomb = iter.next();
            if (bomb.isExploded()) iter.remove();
        }
    }

    public int getFlameLength() {
        return flameLength;
    }
    public void setFlameLength(int flameLength) {
        this.flameLength = flameLength;
    }
    public void increaseBomb() {
        bomRate++;
    }

    public void increaseSpeed() {
        movingSpeed += 50;
    }

    public void increaseFlameLength() {
        flameLength++;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public void move(Map map) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currentDirection = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentDirection = Direction.DOWN;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currentDirection = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentDirection = Direction.RIGHT;
        } else {
            currentDirection = Direction.NOTMOVE;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            GameSound.playPlaceBomb();
            placeBomB(pos.x, pos.y, map);
        }
        destroyBomb();
        super.move(map);
    }

    public List<Bomb> getBombList() {
        return bombList;
    }

    public void setBombList(List<Bomb> bombList) {
        this.bombList = bombList;
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Bomb bomb : bombList) {
            bomb.render(batch);
        }
        if (state == EntityState.PROTECTED) {
            deltaTime += Gdx.graphics.getDeltaTime();
            if (Math.sin(deltaTime * Math.PI / (2 * blinkDuration)) > 0) {
                super.render(batch);
            }
            if (deltaTime >= 3) {
                state = EntityState.ALIVE;
                deltaTime = 0;
            }
        } else {
            super.render(batch);
        }
    }

}
