package com.mygame.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.game.model.*;
import com.game.logic.events.*;

public class GameTurnManager {
    private GameMap gameMap;
    private int currentTurn = 0;
    private boolean isProcessingTurn = false;
    private Array<TurnEventListener> listeners = new Array<>();
    private Thread aiThread;
    
    public GameTurnManager(GameMap gameMap) {
        this.gameMap = gameMap;
    }
    
    public void update(float delta) {
        // Проверка нажатия кнопки "Следующий ход"
        // или автоматический переход
        if (!isProcessingTurn) {
            startTurn();
        }
    }
    
    private void startTurn() {
        isProcessingTurn = true;
        currentTurn++;
        
        // Уведомляем слушателей о начале хода
        notifyListeners(new TurnEvent(TurnEvent.Type.START, currentTurn));
        
        // Запускаем AI в отдельном потоке
        aiThread = new Thread(() -> {
            try {
                // Вычисление AI (имитация)
                Thread.sleep(100); // Имитация работы AI
                
                // Обновление провинций
                for (Province province : gameMap.getProvinces().values()) {
                    // Рост населения
                    int growth = (int)(province.getPopulation() * 0.01f);
                    province.setPopulation(province.getPopulation() + growth);
                    
                    // Рост экономики
                    province.setEconomy(province.getEconomy() + 5);
                }
                
                // Уведомляем о завершении хода
                Gdx.app.postRunnable(() -> {
                    notifyListeners(new TurnEvent(TurnEvent.Type.END, currentTurn));
                    isProcessingTurn = false;
                });
                
            } catch (InterruptedException e) {
                Gdx.app.error("TurnManager", "AI thread interrupted", e);
            }
        });
        aiThread.start();
    }
    
    public void addListener(TurnEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(TurnEventListener listener) {
        listeners.removeValue(listener, true);
    }
    
    private void notifyListeners(TurnEvent event) {
        for (TurnEventListener listener : listeners) {
            listener.onTurnEvent(event);
        }
    }
    
    public int getCurrentTurn() { return currentTurn; }
    public boolean isProcessing() { return isProcessingTurn; }
}