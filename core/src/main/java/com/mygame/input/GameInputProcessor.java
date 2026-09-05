package com.mygame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.model.GameMap;
import com.game.model.Province;
import com.game.screen.GameScreen;

public class GameInputProcessor extends InputListener {
    private Camera camera;
    private GameMap gameMap;
    private GameScreen gameScreen;
    private Vector3 touchPos = new Vector3();
    private Vector3 lastTouch = new Vector3();
    private boolean isDragging = false;
    private float pinchZoomStart = 0f;
    private float pinchDistance = 0f;
    
    // Параметры зума
    private float minZoom = 0.5f;
    private float maxZoom = 3.0f;
    
    public GameInputProcessor(Camera camera, GameMap gameMap, GameScreen gameScreen) {
        this.camera = camera;
        this.gameMap = gameMap;
        this.gameScreen = gameScreen;
    }
    
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (event.getTarget() != event.getListenerActor()) {
            return false;
        }
        
        touchPos.set(x, y, 0);
        camera.unproject(touchPos);
        lastTouch.set(touchPos);
        isDragging = false;
        
        return true;
    }
    
    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        Vector3 newPos = new Vector3(x, y, 0);
        camera.unproject(newPos);
        
        // Перемещение камеры
        camera.position.add(lastTouch.x - newPos.x, lastTouch.y - newPos.y, 0);
        camera.update();
        
        lastTouch.set(newPos);
        isDragging = true;
    }
    
    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (!isDragging) {
            // Это был клик, а не перетаскивание
            touchPos.set(x, y, 0);
            camera.unproject(touchPos);
            
            // Определение провинции
            float mapX = touchPos.x / camera.viewportWidth;
            float mapY = touchPos.y / camera.viewportHeight;
            
            Province province = gameMap.getProvinceAt(mapX, mapY);
            if (province != null) {
                gameScreen.onProvinceSelected(province);
            }
        }
    }
    
    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        // Для подсветки провинции при наведении (опционально)
        return true;
    }
    
    @Override
    public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
        // Зум колесиком мыши
        float zoomFactor = 1.1f;
        if (amountY > 0) {
            camera.zoom /= zoomFactor;
        } else {
            camera.zoom *= zoomFactor;
        }
        
        // Ограничение зума
        camera.zoom = Math.max(minZoom, Math.min(maxZoom, camera.zoom));
        camera.update();
        return true;
    }
    
    // Метод для пинч-зума (будет вызван из GestureDetector)
    public void zoom(float initialDistance, float currentDistance) {
        float zoomFactor = initialDistance / currentDistance;
        camera.zoom *= zoomFactor;
        camera.zoom = Math.max(minZoom, Math.min(maxZoom, camera.zoom));
        camera.update();
    }
}