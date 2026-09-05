package com.mygame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class GestureDetectorWrapper extends InputListener {
    private GestureDetector gestureDetector;
    private GameInputProcessor inputProcessor;
    
    public GestureDetectorWrapper(GameInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
        
        gestureDetector = new GestureDetector(new GestureDetector.GestureAdapter() {
            private float initialDistance = 0f;
            
            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, 
                               Vector2 pointer1, Vector2 pointer2) {
                float initialDist = initialPointer1.dst(initialPointer2);
                float currentDist = pointer1.dst(pointer2);
                
                if (initialDist > 0) {
                    inputProcessor.zoom(initialDist, currentDist);
                }
                return true;
            }
            
            @Override
            public boolean zoom(float initialDistance, float distance) {
                // Альтернативный метод зума
                return true;
            }
        });
    }
    
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return gestureDetector.touchDown(x, y, pointer, button);
    }
    
    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        gestureDetector.touchUp(x, y, pointer, button);
    }
    
    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        gestureDetector.touchDragged(x, y, pointer);
    }
}