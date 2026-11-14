package com.mygame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main implements ApplicationListener{
    private SpriteBatch batch;
    private Texture MyImage;
    @Override
    public void create() {
        batch = new SpriteBatch();
        
        MyImage = new Texture("image.png");
    }
    
    @Override
    public void resize (int width, int height) {
        
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(MyImage, 100, 200);
        batch.end();
    }
    
    @Override
    public void pause() {
        
    }
    
    @Override
    public void resume() {
        
    }
    
    @Override
    public void dispose() {
    }
}
