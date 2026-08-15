package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class Main extends ApplicationAdapter {
  Texture images;
  SpriteBatch batch;
  
  @Override
  public void create(){
    images = new Texture("images.png");
    batch = new SpriteBatch();
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    batch.begin();
    batch.draw(images, 300, 300);
    batch.end();
  }

  @Override
  public void dispose() {
    images.dispose();
    batch.dispose();
  }
}
