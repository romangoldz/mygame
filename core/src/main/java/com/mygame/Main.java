package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.graphics.GL20;

public class Main extends ApplicationAdapter {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    @Override
    public void create() {
        // Грузим нашу карту из ассетов
        map = new TmxMapLoader().load("map.tmx");
        
        // Создаем рендерер. 1f - это масштаб, если надо растянуть
        renderer = new OrthogonalTiledMapRenderer(map);
        
        // Камера, чтоб видеть мир
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480); // Поставь свои размеры экрана, бро
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Важно: говорим рендереру, через какую камеру смотреть
        renderer.setView(camera);
        renderer.render();
    }

    @Override
    public void dispose() {
        // Убираем за собой мусор, не будь свиньей
        map.dispose();
        renderer.dispose();
    }
}