package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Main extends ApplicationAdapter {

    Texture imageTexture;
    Sprite image;
    SpriteBatch batch;
    Rectangle button;
    

    @Override
    public void create() {
        imageTexture = new Texture("images.png");

        image = new Sprite(imageTexture);
        image.setPosition(100, 100);
        image.setSize(1250, 720);

        button = new Rectangle(300, 200, 200, 100);

        batch = new SpriteBatch();
        camera= new OrthographicCamera();
        camera.setToOrtho(false, 1250, 720);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (Gdx.input.isTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            if (button.contains(x, y)) {
                image.draw(batch);
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        imageTexture.dispose();
        batch.dispose();
    } 
}
