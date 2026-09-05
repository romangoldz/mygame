package com.mygame.model;

import com.badlogic.gdx.graphics.Color;

public class Province {
    private int id; // RGB-код как int
    private String ownerTag;
    private int population;
    private int economy;
    private float x, y; // Центр провинции для UI
    
    public Province(int id) {
        this.id = id;
        this.ownerTag = null;
        this.population = 1000;
        this.economy = 100;
    }
    
    public int getId() { return id; }
    public String getOwnerTag() { return ownerTag; }
    public void setOwnerTag(String tag) { this.ownerTag = tag; }
    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }
    public int getEconomy() { return economy; }
    public void setEconomy(int economy) { this.economy = economy; }
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
}