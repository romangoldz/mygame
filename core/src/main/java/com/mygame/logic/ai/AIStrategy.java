package com.mygame.logic.ai;

import com.game.model.Country;
import com.game.model.GameMap;
import com.game.model.Province;

public class AIStrategy {
    private GameMap gameMap;
    
    public AIStrategy(GameMap gameMap) {
        this.gameMap = gameMap;
    }
    
    public void processTurn(Country country) {
        // Базовый AI: расширение на соседние нейтральные провинции
        for (Province province : gameMap.getProvinces().values()) {
            if (province.getOwnerTag() == null) {
                // Проверяем, граничит ли с нашей страной
                if (isBorderProvince(province, country)) {
                    // Захватываем провинцию с вероятностью
                    if (Math.random() < 0.3f) {
                        province.setOwnerTag(country.getTag());
                    }
                }
            }
        }
        
        // Развитие экономики
        for (Province province : gameMap.getProvinces().values()) {
            if (country.getTag().equals(province.getOwnerTag())) {
                province.setEconomy(province.getEconomy() + 10);
                province.setPopulation(province.getPopulation() + 20);
            }
        }
    }
    
    private boolean isBorderProvince(Province province, Country country) {
        // Упрощенная проверка границ
        // В реальной игре нужно проверять соседние провинции
        return Math.random() < 0.5f; // Заглушка
    }
}