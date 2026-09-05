package com.mygame.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.game.model.Country;
import com.game.model.GameMap;
import com.game.model.Province;
import com.game.screen.GameScreen;

public class ProvinceInfoUI extends Window {
    private GameMap gameMap;
    private GameScreen gameScreen;
    private Province currentProvince;
    
    private Label nameLabel;
    private Label ownerLabel;
    private Label populationLabel;
    private Label economyLabel;
    private TextButton assignButton;
    private SelectBox<String> countrySelect;
    
    public ProvinceInfoUI(GameMap gameMap, GameScreen gameScreen, Skin skin) {
        super("Информация о провинции", skin);
        this.gameMap = gameMap;
        this.gameScreen = gameScreen;
        
        setModal(false);
        setMovable(true);
        setSize(300, 400);
        setPosition(20, 20);
        
        Table table = new Table();
        table.defaults().pad(5f);
        
        nameLabel = new Label("Провинция", skin);
        ownerLabel = new Label("Владелец: -", skin);
        populationLabel = new Label("Население: -", skin);
        economyLabel = new Label("Экономика: -", skin);
        
        table.add(nameLabel).colspan(2).center();
        table.row();
        table.add(ownerLabel).colspan(2).left();
        table.row();
        table.add(populationLabel).colspan(2).left();
        table.row();
        table.add(economyLabel).colspan(2).left();
        table.row();
        
        // Выбор страны
        Table assignTable = new Table();
        assignTable.defaults().pad(2f);
        
        countrySelect = new SelectBox<>(skin);
        updateCountryList();
        assignTable.add(new Label("Назначить:", skin));
        assignTable.add(countrySelect);
        
        assignButton = new TextButton("Применить", skin);
        assignButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (currentProvince != null) {
                    String selectedTag = countrySelect.getSelected();
                    currentProvince.setOwnerTag(selectedTag);
                    updateInfo(currentProvince);
                    gameScreen.onProvinceSelected(currentProvince);
                    
                    // Обновляем карту
                    gameScreen.updateMap();
                }
            }
        });
        assignTable.add(assignButton);
        
        table.add(assignTable).colspan(2).fillX();
        
        add(table).fill().expand();
    }
    
    public void updateInfo(Province province) {
        this.currentProvince = province;
        
        if (province == null) {
            nameLabel.setText("Нет провинции");
            ownerLabel.setText("Владелец: -");
            populationLabel.setText("Население: -");
            economyLabel.setText("Экономика: -");
            return;
        }
        
        nameLabel.setText("Провинция #" + province.getId());
        
        String owner = province.getOwnerTag() != null ? 
            province.getOwnerTag() : "Нейтральная";
        ownerLabel.setText("Владелец: " + owner);
        populationLabel.setText("Население: " + province.getPopulation());
        economyLabel.setText("Экономика: " + province.getEconomy());
        
        // Обновляем список стран
        updateCountryList();
        
        // Выбираем текущего владельца
        if (province.getOwnerTag() != null) {
            countrySelect.setSelected(province.getOwnerTag());
        }
    }
    
    private void updateCountryList() {
        String[] countryTags = new String[gameMap.getCountries().size + 1];
        countryTags[0] = "Нейтральная";
        
        for (int i = 0; i < gameMap.getCountries().size; i++) {
            countryTags[i + 1] = gameMap.getCountries().get(i).getTag();
        }
        
        countrySelect.setItems(countryTags);
    }
}