package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Input;

public class MyGame extends ApplicationAdapter {

    private ShapeRenderer shape;
    private SpriteBatch batch;

    private float playerX = 300;
    private float playerY = 200;

    private final float playerSize = 50;
    private final float speed = 250;

    @Override
    public void create() {
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // Фон
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Рух
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
            Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
            Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) ||
            Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerY += speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
            Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerY -= speed * delta;
        }

        // Не даємо квадрату вийти за межі екрана
        playerX = Math.max(0, Math.min(
            playerX,
            Gdx.graphics.getWidth() - playerSize
        ));

        playerY = Math.max(0, Math.min(
            playerY,
            Gdx.graphics.getHeight() - playerSize
        ));

        // Малюємо гравця
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(0.2f, 0.6f, 1f, 1f);
        shape.rect(playerX, playerY, playerSize, playerSize);

        shape.end();

        // Заголовок у назві вікна
        Gdx.graphics.setTitle(
            "LibGDX Test | FPS: " +
            Gdx.graphics.getFramesPerSecond() +
            " | X: " + (int) playerX +
            " Y: " + (int) playerY
        );
    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
    }
}
