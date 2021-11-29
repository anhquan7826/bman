package com.oopproj.bomberman.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.oopproj.bomberman.ui.Button;
import com.oopproj.bomberman.ui.ScreenRes;

public class Menu implements Screen {
    private BombermanGame game;
    Button button;

    public Menu(BombermanGame game) {
        this.game = game;
        Texture texture = new Texture(Gdx.files.internal("buttons/play.png"));
        button = new Button(texture, ScreenRes.getWidth() / 2, 100);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();
        button.render(game.batch);
        game.font.draw(game.batch, "BOMBERMAN", ScreenRes.getWidth() / 2, ScreenRes.getHeight() - 50);
        game.batch.end();
        if (button.process()) {
            try {
                game.setScreen(new Gameplay(game));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
