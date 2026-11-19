package com.mygame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Main : ApplicationAdapter() {
    // lateinit дозволяє ініціалізувати об'єкти пізніше в методі create()
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture

    override fun create() {
        // Виконується один раз при запуску
        // Файл має лежати в папці assets
    }

    override fun render() {
        // Виконується приблизно 60 разів на секунду (ігровий цикл)
        
        // 1. Очищуємо екран (колір у форматі RGBA: червоний, зелений, синій, прозорість)
        Gdx.gl.glClearColor(0.7f, 1.0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun dispose() {
        
    }
}
