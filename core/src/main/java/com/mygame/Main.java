package com.mygame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Main implements ApplicationListener {
    Texture bgTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Music music;
    Sprite bucketSprite;
    
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Vector2 touchPos;
    
    @Override
    public void create() {
        bgTexture = new Texture("textures/bgTexture.png");
        bucketTexture = new Texture("textures/bucketTexture.png");
        dropTexture = new Texture("textures/dropTexture.png"); // додайте .png
        dropSound = Gdx.audio.newSound(Gdx.files.internal("sound/dropSound.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music/music.mp3"));
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        music.play(); // якщо хочете одразу запустити музику
        bucketSprite = new Sprite (bucketTexture);
        bucketSprite.setSize(1, 1);
        touchPos = new Vector2();
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        input();
        logic();
        draw();
    }
    
    private void input() {
      float speed = 4f;
      float delta = Gdx.graphics.getDeltaTime();

      if (Gdx.input.isTouched()) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos);
        bucketSprite.setCenterX(touchPos.x);
      }
    }
    
    private void logic() {}
    
    private void draw() {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        spriteBatch.draw(bgTexture, 0, 0, WorldWidth, worldHeight);
        bucketSprite.draw(spriteBatch);
        spriteBatch.end();
    }
    
    @Override
    public void pause() {}
    
    @Override
    public void resume() {}
    
    @Override
    public void dispose() {
        bgTexture.dispose();
        bucketTexture.dispose();
        dropTexture.dispose();
        dropSound.dispose();
        music.dispose();
        spriteBatch.dispose();
    }
}
