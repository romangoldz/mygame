package com.mygame.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.game.model.Country;
import com.game.model.GameMap;
import com.game.logic.ai.AIStrategy;
import com.game.logic.events.TurnEvent;
import com.game.logic.events.TurnEventListener;

public class GameTurnManager {
    private GameMap gameMap;
    private AIStrategy aiStrategy;
    private int currentTurn = 0;
    private boolean isProcessingTurn = false;
    private Array<TurnEventListener> listeners = new Array<>();
    private Thread aiThread;
    
    public GameTurnManager(GameMap gameMap) {
        this.gameMap = gameMap;
        this.aiStrategy = new AIStrategy(gameMap);
    }
    
    public void update(float delta) {
        // Автоматический переход хода каждые 5 секунд для демонстрации
        if (!isProcessingTurn) {
            startTurn();
        }
    }
    
    public void startTurn() {
        if (isProcessingTurn) return;
        
        isProcessingTurn = true;
        currentTurn++;
        
        notifyListeners(new TurnEvent(TurnEvent.Type.START, currentTurn));
        
        // Запускаем AI в отдельном потоке
        aiThread = new Thread(() -> {
            try {
                // AI для всех стран, кроме игрока
                for (Country country : gameMap.getCountries()) {
                    if (!country.isPlayerControlled()) {
                        aiStrategy.processTurn(country);
                    }
                }
                
                // Имитация работы AI
                Thread.sleep(200);
                
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