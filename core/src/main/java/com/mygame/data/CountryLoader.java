package com.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.game.model.Country;
import com.game.model.GameMap;

public class CountryLoader {
    public static void loadCountries(GameMap gameMap, String jsonPath) {
        try {
            FileHandle file = Gdx.files.internal(jsonPath);
            String json = file.readString();
            
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(json);
            
            for (JsonValue entry : root.iterator()) {
                String tag = entry.getString("tag");
                JsonValue colorArray = entry.get("color");
                
                float r = colorArray.getInt(0) / 255f;
                float g = colorArray.getInt(1) / 255f;
                float b = colorArray.getInt(2) / 255f;
                
                Country country = new Country(tag, new Color(r, g, b, 1));
                gameMap.getCountries().add(country);
                
                Gdx.app.log("CountryLoader", "Loaded: " + tag);
            }
            
        } catch (Exception e) {
            Gdx.app.error("CountryLoader", "Error loading countries", e);
        }
    }
}