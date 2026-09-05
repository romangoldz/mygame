package com.mygame.rendering;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.game.model.GameMap;

public class MinimapRenderer {
    private GameMap gameMap;
    private FrameBuffer minimapFBO;
    private Texture minimapTexture;
    private int minimapSize = 200; // Размер мини-карты
    
    public MinimapRenderer(GameMap gameMap) {
        this.gameMap = gameMap;
        generateMinimap();
    }
    
    private void generateMinimap() {
        // Создаем уменьшенную версию карты
        Pixmap minimapPix = new Pixmap(minimapSize, minimapSize, Pixmap.Format.RGBA8888);
        Pixmap provincesPix = gameMap.getProvincesPixmap();
        Texture visualTex = gameMap.getVisualTexture();
        
        // Рендерим уменьшенную карту
        // ... (код для масштабирования и рисования)
        
        minimapTexture = new Texture(minimapPix);
        minimapPix.dispose();
    }
    
    public void render(SpriteBatch batch, float x, float y) {
        batch.begin();
        batch.draw(minimapTexture, x, y, minimapSize, minimapSize);
        
        // Рисуем прямоугольник видимой области камеры
        // ... (код для отображения области видимости)
        batch.end();
    }
}