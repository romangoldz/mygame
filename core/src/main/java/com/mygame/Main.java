package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    ShapeRenderer shape;
    OrthographicCamera camera;

    // Гравець
    Rectangle player;
    float playerSpeed = 300f;

    // Зірки та вороги
    Array<Rectangle> stars;
    Array<Rectangle> enemies;
    float spawnTimer = 0;
    float spawnInterval = 1.5f;

    int score = 0;
    boolean gameOver = false;

    @Override
    public void create() {
        shape = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        player = new Rectangle();
        player.x = 400 - 16;
        player.y = 240 - 16;
        player.width = 32;
        player.height = 32;

        stars = new Array<>();
        enemies = new Array<>();
    }

    @Override
    public void render() {
        // Оновлення
        update();

        // Малювання
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        shape.setProjectionMatrix(camera.combined);

        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Гравець (зелений)
        shape.setColor(0, 1, 0, 1);
        shape.rect(player.x, player.y, player.width, player.height);

        // Зірки (жовті)
        shape.setColor(1, 1, 0, 1);
        for (Rectangle star : stars) {
            shape.circle(star.x + 8, star.y + 8, 8);
        }

        // Вороги (червоні)
        shape.setColor(1, 0, 0, 1);
        for (Rectangle enemy : enemies) {
            shape.rect(enemy.x, enemy.y, enemy.width, enemy.height);
        }

        shape.end();

        // Якщо Game Over – виводимо повідомлення
        if (gameOver) {
            // Тут можна додати BitmapFont для тексту
        }
    }

    void update() {
        if (gameOver) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                restart();
            }
            return;
        }

        // Рух гравця (клавіші / сенсор)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.x -= playerSpeed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.x += playerSpeed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) player.y += playerSpeed * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) player.y -= playerSpeed * Gdx.graphics.getDeltaTime();

        // Для Android – торкніться лівої/правої половини екрану
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();
            // Перетворення координат (спрощено)
            if (touchX < Gdx.graphics.getWidth() / 2) player.x -= playerSpeed * Gdx.graphics.getDeltaTime();
            else player.x += playerSpeed * Gdx.graphics.getDeltaTime();
        }

        // Межі екрану
        player.x = MathUtils.clamp(player.x, 0, 800 - player.width);
        player.y = MathUtils.clamp(player.y, 0, 480 - player.height);

        // Спавн об'єктів
        spawnTimer += Gdx.graphics.getDeltaTime();
        if (spawnTimer > spawnInterval) {
            spawnTimer = 0;
            spawnInterval = MathUtils.random(0.8f, 1.8f);

            // Зірка
            Rectangle star = new Rectangle();
            star.x = MathUtils.random(0, 800 - 16);
            star.y = 480;
            star.width = 16;
            star.height = 16;
            stars.add(star);

            // Ворог (з меншою ймовірністю)
            if (MathUtils.random() < 0.4f) {
                Rectangle enemy = new Rectangle();
                enemy.x = MathUtils.random(0, 800 - 24);
                enemy.y = 480;
                enemy.width = 24;
                enemy.height = 24;
                enemies.add(enemy);
            }
        }

        // Рух об'єктів вниз
        float delta = Gdx.graphics.getDeltaTime();
        for (int i = stars.size - 1; i >= 0; i--) {
            Rectangle star = stars.get(i);
            star.y -= 120 * delta;
            if (star.y + star.height < 0) stars.removeIndex(i);
        }
        for (int i = enemies.size - 1; i >= 0; i--) {
            Rectangle enemy = enemies.get(i);
            enemy.y -= 100 * delta;
            if (enemy.y + enemy.height < 0) enemies.removeIndex(i);
        }

        // Перевірка зіткнень
        // Зірки
        for (int i = stars.size - 1; i >= 0; i--) {
            Rectangle star = stars.get(i);
            if (star.overlaps(player)) {
                stars.removeIndex(i);
                score++;
                Gdx.app.log("Game", "Score: " + score);
            }
        }

        // Вороги
        for (Rectangle enemy : enemies) {
            if (enemy.overlaps(player)) {
                gameOver = true;
                Gdx.app.log("Game", "Game Over! Score: " + score);
            }
        }
    }

    void restart() {
        gameOver = false;
        score = 0;
        player.x = 400 - 16;
        player.y = 240 - 16;
        stars.clear();
        enemies.clear();
        spawnTimer = 0;
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
    }
