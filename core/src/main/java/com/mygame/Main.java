package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;

public class TouchGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerTexture;
    private OrthographicCamera camera;
    private Viewport viewport;
    
    // -------- ГРАВЕЦЬ --------
    private Vector3 playerPos = new Vector3(400, 240, 0);
    private float playerSize = 64;
    
    // -------- УПРАВЛІННЯ ПАЛЬЦЯМИ --------
    private Vector2 touchStartPos = new Vector2();    // де торкнулись
    private Vector3 targetPos = new Vector3();        // куди рухатись
    private boolean isMoving = false;
    private float moveSpeed = 300f;
    
    // Для мультитач (два пальці - зум)
    private ArrayList<Vector2> activeTouches = new ArrayList<>();
    private float initialZoom = 1f;
    private float initialPinchDistance = 0f;
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTexture = new Texture("player.png");
        
        // Камера
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        viewport = new FitViewport(800, 480, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        
        targetPos.set(playerPos);
    }
    
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        
        // -------- ОБРОБКА ДОТИКІВ --------
        handleTouches();
        
        // -------- РУХ ГРАВЦЯ ДО ЦІЛІ --------
        if (isMoving) {
            // Плавно рухаємось до targetPos
            playerPos.lerp(targetPos, 5f * delta);
            
            // Якщо майже дійшли - зупиняємось
            if (playerPos.dst(targetPos) < 1f) {
                playerPos.set(targetPos);
                isMoving = false;
            }
        }
        
        // -------- КАМЕРА СЛІДКУЄ --------
        camera.position.lerp(playerPos, 0.1f);
        
        // Обмеження камери (світ 2000x2000)
        float halfW = camera.viewportWidth / 2 * camera.zoom;
        float halfH = camera.viewportHeight / 2 * camera.zoom;
        camera.position.x = MathUtils.clamp(camera.position.x, halfW, 2000 - halfW);
        camera.position.y = MathUtils.clamp(camera.position.y, halfH, 2000 - halfH);
        
        camera.update();
        
        // -------- МАЛЮВАННЯ --------
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // Малюємо гравця
        batch.draw(playerTexture, 
            playerPos.x - playerSize/2, 
            playerPos.y - playerSize/2, 
            playerSize, playerSize
        );
        
        // Малюємо лінію до цілі (дебаг)
        if (isMoving) {
            // Тут можна намалювати лінію від гравця до targetPos
        }
        
        batch.end();
    }
    
    // -------- ОСНОВНА ЛОГІКА ДОТИКІВ --------
    private void handleTouches() {
        // Отримуємо всі активні дотики
        activeTouches.clear();
        for (int i = 0; i < 20; i++) {
            if (Gdx.input.isTouched(i)) {
                activeTouches.add(new Vector2(Gdx.input.getX(i), Gdx.input.getY(i)));
            }
        }
        
        // --- ВИПАДОК 1: ОДИН ПАЛЕЦЬ (рух) ---
        if (activeTouches.size() == 1) {
            Vector2 touch = activeTouches.get(0);
            
            // Перетворюємо екранні координати у світові
            Vector3 worldTouch = camera.unproject(
                new Vector3(touch.x, touch.y, 0)
            );
            
            // Якщо тільки торкнулись - запам'ятовуємо
            if (Gdx.input.justTouched()) {
                touchStartPos.set(touch);
                targetPos.set(worldTouch);
                isMoving = true;
            }
            
            // Якщо палець рухається - оновлюємо ціль (додатково)
            if (Gdx.input.isTouched(0) && !Gdx.input.justTouched()) {
                // Перевіряємо чи палець змістився більше ніж на 10 пікселів
                if (touch.dst(touchStartPos) > 10) {
                    targetPos.set(worldTouch);
                    isMoving = true;
                    touchStartPos.set(touch);
                }
            }
        }
        
        // --- ВИПАДОК 2: ДВА ПАЛЬЦІ (зум) ---
        if (activeTouches.size() == 2) {
            Vector2 touch1 = activeTouches.get(0);
            Vector2 touch2 = activeTouches.get(1);
            
            // Відстань між пальцями
            float currentDistance = touch1.dst(touch2);
            
            if (Gdx.input.justTouched()) {
                // Почали пінч - запам'ятовуємо початковий зум
                initialPinchDistance = currentDistance;
                initialZoom = camera.zoom;
            } else {
                // Змінюємо зум пропорційно відстані між пальцями
                float scale = currentDistance / initialPinchDistance;
                camera.zoom = MathUtils.clamp(initialZoom / scale, 0.5f, 3f);
            }
        }
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
    }
}
