package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Main extends ApplicationAdapter {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    @Override
    public void create() {
        // 1. Налаштування камери (наприклад, охоплює область у 30х20 умовних одиниць-метрів)
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 30, 30 * (h / w));

        // 2. Завантаження карти з папки assets
        map = new TmxMapLoader().load("map.tmx");
        
        // 3. Ініціалізація рендерера
        // Коефіцієнт 1/32 означає, що 1 умовна одиниця дорівнює 32 пікселям (розмір тайла)
        renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
    }

    @Override
    public void render() {
        // Очищення екрана
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Оновлення матриці камери
        camera.update();

        // Передача матриці камери рендереру карти
        renderer.setView(camera);
        
        // Малювання карти
        renderer.render();
        
        // Тут зазвичай малюються інші об'єкти (гравець, вороги тощо)
    }

    @Override
    public void dispose() {
        // Обов'язкове звільнення пам'яті
        map.dispose();
        renderer.dispose();
    }
}

        camera.setToOrtho(false, 30, 30 * (h / w));
        
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f / 64f);
        
    }
    
    @Override
    public void resize (int width, int height) {
        
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        
    }
    
    @Override
    public void pause() {
        
    }
    
    @Override
    public void resume() {
        
    }
    
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        
    }
}
