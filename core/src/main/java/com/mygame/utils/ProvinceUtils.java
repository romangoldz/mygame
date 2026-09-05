package com.mygame.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.IntMap;
import com.game.model.Province;

public class ProvinceUtils {
    
    public static int getProvinceIdAt(Pixmap provincesPix, int x, int y) {
        if (x < 0 || x >= provincesPix.getWidth() || 
            y < 0 || y >= provincesPix.getHeight()) {
            return -1;
        }
        return provincesPix.getPixel(x, y) & 0xFFFFFF;
    }
    
    public static void setProvinceOwners(Pixmap provincesPix, 
                                         IntMap<Province> provinces,
                                         IntMap<String> ownerMap) {
        for (IntMap.Entry<String> entry : ownerMap.entries()) {
            int provinceId = entry.key;
            String ownerTag = entry.value;
            
            Province province = provinces.get(provinceId);
            if (province != null) {
                province.setOwnerTag(ownerTag);
            }
        }
    }
    
    public static int calculatePopulation(int provinceId, int basePopulation) {
        // Можно добавить различные модификаторы
        return basePopulation + (provinceId % 1000);
    }
}