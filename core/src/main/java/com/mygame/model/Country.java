package com.mygame.model;

import com.badlogic.gdx.graphics.Color;

public class Country {
    private String tag;
    private Color color;
    private boolean isPlayerControlled;
    
    public Country(String tag, Color color) {
        this.tag = tag;
        this.color = color;
        this.isPlayerControlled = false;
    }
    
    public String getTag() { return tag; }
    public Color getColor() { return color; }
    public boolean isPlayerControlled() { return isPlayerControlled; }
    public void setPlayerControlled(boolean controlled) { isPlayerControlled = controlled; }
}