package com.mygame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

class Game : ApplicationAdapter() {
    override fun create() {
        
    }
    
    override fun render() {
        Gdx.gl.glClearColor(1f, 0.5f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }
    
    override fun resize(width: Int, height: Int) {
        
    }
    
    override fun pause() {
        
    }
    
    override fun resume() {
        
    }
    
    override fun dispose() {
        
    }
}