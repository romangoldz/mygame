package com.mygame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import com.game.model.GameMap;
import com.game.model.Province;

public class GameInputProcessor implements InputProcessor {
    private Camera camera;
    private GameMap gameMap;
    private Vector3 touchPos = new Vector3();
    private float zoomLevel = 1.0f;
    
    public GameInputProcessor(Camera camera, GameMap gameMap) {
        this.camera = camera;
        this.gameMap = gameMap;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        camera.unproject(touchPos);
        
        // Преобразование в координаты карты (0-1)
        float mapX = touchPos.x / camera.viewportWidth;
        float mapY = touchPos.y / camera.viewportHeight;
        
        Province province = gameMap.getProvinceAt(mapX, mapY);
        if (province != null) {
            Gdx.app.log("Province", "ID: " + province.getId() + 
                       ", Owner: " + province.getOwnerTag());
            // Здесь можно добавить выделение или открыть UI провинции
        }
        return true;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Перемещение камеры
        Vector3 newPos = new Vector3(screenX, screenY, 0);
        camera.unproject(newPos);
        camera.position.add(touchPos.x - newPos.x, touchPos.y - newPos.y, 0);
        camera.update();
        touchPos.set(newPos);
        return true;
    }
    
    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Зум колесиком мыши (для тестирования на ПК)
        float zoomFactor = 1.1f;
        if (amountY > 0) {
            zoomLevel *= zoomFactor;
        } else {
            zoomLevel /= zoomFactor;
        }
        camera.zoom = 1 / zoomLevel;
        camera.update();
        return true;
    }
    
    // Другие методы InputProcessor...
}