package com.mygame.logic.events;

public class TurnEvent {
    public enum Type { START, END }
    private Type type;
    private int turnNumber;
    
    public TurnEvent(Type type, int turnNumber) {
        this.type = type;
        this.turnNumber = turnNumber;
    }
    
    public Type getType() { return type; }
    public int getTurnNumber() { return turnNumber; }
}