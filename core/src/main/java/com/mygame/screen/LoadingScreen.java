package com.mygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.GameApp;

public class LoadingScreen implements Screen {
    private GameApp game;
    private Stage stage;
    private Label loadingLabel;
    private float progress = 0f;
    private boolean loadingComplete = false;
    
    public LoadingScreen(GameApp game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        
        Table table = new Table();
        table.setFillParent(true);
        
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = game.getFont();
        
        loadingLabel = new Label("Загрузка...", style);
        table.add(loadingLabel).pad(20f);
        
        stage.addActor(table);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        progress += delta * 0.5f;
        if (progress > 1f) progress = 1f;
        
        loadingLabel.setText("Загрузка: " + (int)(progress * 100) + "%");
        
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }
    
    // Другие методы...
}