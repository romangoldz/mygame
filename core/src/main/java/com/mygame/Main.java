package com.mygame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Main implements ApplicationListener {
    Texture bgTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Music music;
    
    SpriteBatch spriteBatch;
    FitViewport viewport;
    
    @Override
    public void create() {
        bgTexture = new Texture("textures/bgTexture.png");
        bucketTexture = new Texture("textures/bucketTexture.png");
        dropTexture = new Texture ("textures/dropTexture.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("sound/dropSound.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music/music.mp3"));
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void render() {
        input();
        logic();
        draw();
    }
    
    private void input() {
        
    }
    
    private void logic () {
        
    }
    
    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.appy();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        
        spriteBatch.draw(bucketTexture, 0, 0, 1, 1);
        
        spriteBatch.end();
    }
}
