package com.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.screen.GameScreen;
import com.game.screen.LoadingScreen;

public class Main extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    private GameScreen gameScreen;
    private LoadingScreen loadingScreen;
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        
        // Устанавливаем начальный экран загрузки
        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
        
        // Асинхронная загрузка ассетов
        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                gameScreen = new GameScreen(this);
                setScreen(gameScreen);
            });
        }).start();
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (gameScreen != null) {
            gameScreen.resize(width, height);
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
        if (gameScreen != null) gameScreen.dispose();
    }
    
    public SpriteBatch getBatch() { return batch; }
    public BitmapFont getFont() { return font; }
}