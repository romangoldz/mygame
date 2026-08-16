package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Main extends ApplicationAdapter {
  Texture imageTexture;
  Sprite image;
  SpriteBatch batch;
  
  @Override
  public void create(){
    imageTexture = new Texture("images.png");
    image = new Sprite(imageTexture);
    image.setPosition(100, 100);
    image.setSize (64, 64);

    batch = new SpriteBatch();
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    batch.begin();
    image.draw(batch);
    batch.end();
  }

  @Override
  public void dispose() {
    image.dispose();
    batch.dispose();
  }
}
