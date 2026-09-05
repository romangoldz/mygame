package com.mygame.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.game.model.*;

public class MapRenderer {
    private GameMap gameMap;
    private SpriteBatch batch;
    private FrameBuffer fbo;
    private Texture ownerOverlayTexture;
    private ShaderProgram overlayShader;
    
    private Pixmap ownerPixmap; // Для быстрого обновления цветов владельцев
    private boolean needsUpdate = true;
    
    public MapRenderer(GameMap gameMap, SpriteBatch batch) {
        this.gameMap = gameMap;
        this.batch = batch;
        initFBO();
        initOverlayPixmap();
        initShader();
    }
    
    private void initFBO() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
    }
    
    private void initOverlayPixmap() {
        // Создаем Pixmap для хранения цветов владельцев провинций
        Pixmap provincesPix = gameMap.getProvincesPixmap();
        ownerPixmap = new Pixmap(provincesPix.getWidth(), provincesPix.getHeight(), Pixmap.Format.RGBA8888);
        updateOwnerPixmap();
    }
    
    private void updateOwnerPixmap() {
        Pixmap provincesPix = gameMap.getProvincesPixmap();
        IntMap<Province> provinces = gameMap.getProvinces();
        
        // Очищаем пиксмап (прозрачный фон)
        ownerPixmap.setColor(0, 0, 0, 0);
        ownerPixmap.fill();
        
        for (Province province : provinces.values()) {
            String ownerTag = province.getOwnerTag();
            if (ownerTag == null) continue;
            
            Country owner = findCountryByTag(ownerTag);
            if (owner == null) continue;
            
            // Получаем цвет страны с альфа-каналом ~0.7 (180/255)
            Color countryColor = owner.getColor();
            int r = (int)(countryColor.r * 180);
            int g = (int)(countryColor.g * 180);
            int b = (int)(countryColor.b * 180);
            
            // Закрашиваем все пиксели провинции
            int provinceId = province.getId();
            for (int y = 0; y < ownerPixmap.getHeight(); y++) {
                for (int x = 0; x < ownerPixmap.getWidth(); x++) {
                    int pixel = provincesPix.getPixel(x, y) & 0xFFFFFF;
                    if (pixel == provinceId) {
                        ownerPixmap.drawPixel(x, y, (r << 24) | (g << 16) | (b << 8) | 180);
                    }
                }
            }
        }
        
        // Обновляем текстуру
        if (ownerOverlayTexture != null) {
            ownerOverlayTexture.dispose();
        }
        ownerOverlayTexture = new Texture(ownerPixmap);
        needsUpdate = false;
    }
    
    private void initShader() {
        String vertexShader = 
            "attribute vec4 a_position;\n" +
            "attribute vec2 a_texCoord0;\n" +
            "uniform mat4 u_projTrans;\n" +
            "varying vec2 v_texCoord;\n" +
            "void main() {\n" +
            "   v_texCoord = a_texCoord0;\n" +
            "   gl_Position = u_projTrans * a_position;\n" +
            "}\n";
            
        String fragmentShader = 
            "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 v_texCoord;\n" +
            "uniform sampler2D u_visualMap;\n" +
            "uniform sampler2D u_ownerOverlay;\n" +
            "void main() {\n" +
            "   vec4 visual = texture2D(u_visualMap, v_texCoord);\n" +
            "   vec4 overlay = texture2D(u_ownerOverlay, v_texCoord);\n" +
            "   gl_FragColor = mix(visual, overlay, overlay.a);\n" +
            "}\n";
            
        overlayShader = new ShaderProgram(vertexShader, fragmentShader);
        if (!overlayShader.isCompiled()) {
            Gdx.app.error("Shader", overlayShader.getLog());
        }
    }
    
    private Country findCountryByTag(String tag) {
        for (Country country : gameMap.getCountries()) {
            if (country.getTag().equals(tag)) {
                return country;
            }
        }
        return null;
    }
    
    public void render(Camera camera) {
        if (needsUpdate) {
            updateOwnerPixmap();
        }
        
        // Рендерим в FBO
        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // Рисуем визуальную карту
        batch.draw(gameMap.getVisualTexture(), 0, 0, 
                   camera.viewportWidth, camera.viewportHeight);
        
        // Рисуем оверлей с цветами владельцев через шейдер
        overlayShader.bind();
        overlayShader.setUniformMatrix("u_projTrans", camera.combined);
        overlayShader.setUniformi("u_visualMap", 0);
        overlayShader.setUniformi("u_ownerOverlay", 1);
        
        gameMap.getVisualTexture().bind(0);
        ownerOverlayTexture.bind(1);
        
        batch.draw(ownerOverlayTexture, 0, 0, 
                   camera.viewportWidth, camera.viewportHeight);
        
        batch.end();
        fbo.end();
        
        // Отрисовываем результат на экран
        batch.begin();
        batch.draw(fbo.getColorBufferTexture(), 0, 0, 
                   Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }
    
    public void setNeedsUpdate() {
        this.needsUpdate = true;
    }
    
    public void dispose() {
        if (fbo != null) fbo.dispose();
        if (ownerOverlayTexture != null) ownerOverlayTexture.dispose();
        if (ownerPixmap != null) ownerPixmap.dispose();
        if (overlayShader != null) overlayShader.dispose();
    }
}