package com.mygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.model.*;
import com.game.rendering.*;
import com.game.input.GameInputProcessor;
import com.game.logic.GameTurnManager;

public class GameScreen implements Screen {
    private Stage stage;
    private SpriteBatch batch;
    private GameMap gameMap;
    private MapRenderer mapRenderer;
    private GameInputProcessor inputProcessor;
    private GameTurnManager turnManager;
    
    private OrthographicCamera camera;
    private Vector3 lastTouch = new Vector3();
    
    @Override
    public void show() {
        // Инициализация камеры (ландшафтная ориентация)
        float aspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        camera = new OrthographicCamera(1920 * aspectRatio, 1920);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        
        batch = new SpriteBatch();
        stage = new Stage(new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera));
        
        // Загрузка карты
        Texture visualTex = new Texture(Gdx.files.internal("maps/default/map_visual.png"));
        Pixmap provincesPix = new Pixmap(Gdx.files.internal("maps/default/map_provinces.png"));
        gameMap = new GameMap(visualTex, provincesPix);
        
        // Загрузка стран из JSON
        CountryLoader.loadCountries(gameMap, "maps/default/countries.json");
        
        // Инициализация рендерера
        mapRenderer = new MapRenderer(gameMap, batch);
        
        // Обработка ввода
        inputProcessor = new GameInputProcessor(camera, gameMap);
        Gdx.input.setInputProcessor(inputProcessor);
        
        // Менеджер ходов
        turnManager = new GameTurnManager(gameMap);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        // Рендеринг карты с наложением цветов стран
        mapRenderer.render(camera);
        
        // Обработка логики хода
        turnManager.update(delta);
        
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        mapRenderer.dispose();
    }
    
    // Другие методы...
}