package com.mygame.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.utils.Array;
import com.game.model.GameMap;
import com.game.model.Province;

public class MapLoader {
    
    public static GameMap loadMap(String mapPath) {
        try {
            // Загрузка визуальной карты
            Texture visualTexture = new Texture(Gdx.files.internal(mapPath + "/map_visual.png"));
            
            // Загрузка технической карты
            Pixmap provincesPixmap = new Pixmap(Gdx.files.internal(mapPath + "/map_provinces.png"));
            
            // Загрузка стран
            GameMap gameMap = new GameMap(visualTexture, provincesPixmap);
            CountryLoader.loadCountries(gameMap, mapPath + "/countries.json");
            
            return gameMap;
            
        } catch (Exception e) {
            Gdx.app.error("MapLoader", "Error loading map", e);
            return null;
        }
    }
    
    public static void validateMap(String visualPath, String provincesPath) {
        FileHandle visualFile = Gdx.files.internal(visualPath);
        FileHandle provincesFile = Gdx.files.internal(provincesPath);
        
        if (!visualFile.exists() || !provincesFile.exists()) {
            Gdx.app.error("MapLoader", "Map files not found!");
            return;
        }
        
        // Проверка размеров
        Texture visualTex = new Texture(visualFile);
        Pixmap provincesPix = new Pixmap(provincesFile);
        
        if (visualTex.getWidth() != provincesPix.getWidth() || 
            visualTex.getHeight() != provincesPix.getHeight()) {
            Gdx.app.error("MapLoader", "Map dimensions mismatch!");
        }
        
        visualTex.dispose();
        provincesPix.dispose();
    }
}