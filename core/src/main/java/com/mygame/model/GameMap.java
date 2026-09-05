package com.mygame.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

public class GameMap {
    private Texture visualTexture;
    private Pixmap provincesPixmap;
    private IntMap<Province> provinces = new IntMap<>();
    private Array<Country> countries = new Array<>();
    private int width, height;
    
    public GameMap(Texture visual, Pixmap provincesPix) {
        this.visualTexture = visual;
        this.provincesPixmap = provincesPix;
        this.width = provincesPix.getWidth();
        this.height = provincesPix.getHeight();
        loadProvinces();
    }
    
    private void loadProvinces() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = provincesPixmap.getPixel(x, y);
                if ((rgb & 0xFFFFFF) != 0) { // Не черный
                    int id = rgb & 0xFFFFFF;
                    if (!provinces.containsKey(id)) {
                        Province province = new Province(id);
                        province.setX(x);
                        province.setY(y);
                        provinces.put(id, province);
                    }
                }
            }
        }
    }
    
    public Province getProvinceAt(int worldX, int worldY) {
        int pixelX = Math.round(worldX * width);
        int pixelY = Math.round((1 - worldY) * height);
        
        if (pixelX >= 0 && pixelX < width && pixelY >= 0 && pixelY < height) {
            int rgb = provincesPixmap.getPixel(pixelX, pixelY) & 0xFFFFFF;
            return provinces.get(rgb);
        }
        return null;
    }
    
    // Геттеры
    public Texture getVisualTexture() { return visualTexture; }
    public Pixmap getProvincesPixmap() { return provincesPixmap; }
    public IntMap<Province> getProvinces() { return provinces; }
    public Array<Country> getCountries() { return countries; }
}