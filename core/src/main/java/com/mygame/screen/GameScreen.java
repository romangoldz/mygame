package com.mygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.game.GameApp;
import com.game.model.*;
import com.game.rendering.*;
import com.game.input.GameInputProcessor;
import com.game.logic.GameTurnManager;
import com.game.logic.events.TurnEvent;
import com.game.logic.events.TurnEventListener;
import com.game.data.CountryLoader;

public class GameScreen implements Screen, TurnEventListener {
    private GameApp game;
    private Stage stage;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    
    // Модели
    private GameMap gameMap;
    private GameTurnManager turnManager;
    
    // Рендеринг
    private MapRenderer mapRenderer;
    private MinimapRenderer minimapRenderer;
    
    // UI
    private Table uiTable;
    private Label turnLabel;
    private Label infoLabel;
    private TextButton endTurnButton;
    private TextButton selectCountryButton;
    
    // Ввод
    private GameInputProcessor inputProcessor;
    private Vector3 touchPos = new Vector3();
    
    // Состояние
    private Province selectedProvince = null;
    private Country playerCountry = null;
    private boolean isSelectingCountry = false;
    
    public GameScreen(GameApp game) {
        this.game = game;
        this.batch = game.getBatch();
    }
    
    @Override
    public void show() {
        initializeCamera();
        initializeUI();
        loadGameData();
        setupInput();
        setupTurnManager();
    }
    
    private void initializeCamera() {
        float aspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        camera = new OrthographicCamera(1920 * aspectRatio, 1920);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        
        stage = new Stage(new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera));
        Gdx.input.setInputProcessor(stage);
    }
    
    private void initializeUI() {
        uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.top().right();
        
        // Стили для UI
        Skin skin = createSkin();
        
        // Информационная панель
        Table infoPanel = new Table();
        infoPanel.setBackground(skin.getDrawable("default-round"));
        infoPanel.pad(10f);
        
        turnLabel = new Label("Ход: 0", skin);
        infoLabel = new Label("Выберите страну", skin);
        
        infoPanel.add(turnLabel).padRight(20f);
        infoPanel.add(infoLabel).padRight(20f);
        
        endTurnButton = new TextButton("Следующий ход", skin);
        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (playerCountry != null && !turnManager.isProcessing()) {
                    turnManager.startTurn();
                    endTurnButton.setDisabled(true);
                }
            }
        });
        infoPanel.add(endTurnButton);
        
        selectCountryButton = new TextButton("Выбрать страну", skin);
        selectCountryButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                isSelectingCountry = !isSelectingCountry;
                selectCountryButton.setText(isSelectingCountry ? "Подтвердить выбор" : "Выбрать страну");
                infoLabel.setText(isSelectingCountry ? "Кликните по провинции для выбора страны" : "Выберите страну");
            }
        });
        infoPanel.add(selectCountryButton).padLeft(10f);
        
        uiTable.add(infoPanel).pad(20f);
        stage.addActor(uiTable);
    }
    
    private Skin createSkin() {
        Skin skin = new Skin();
        
        // Создаем простой стиль через Pixmap
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.2f, 0.2f, 0.3f, 0.8f);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        skin.add("default-round", new TextureRegionDrawable(new TextureRegion(texture)));
        
        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.getFont();
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);
        
        // TextButton style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.GRAY;
        skin.add("default", buttonStyle);
        
        return skin;
    }
    
    private void loadGameData() {
        // Загрузка карты
        Texture visualTex = new Texture(Gdx.files.internal("maps/default/map_visual.png"));
        Pixmap provincesPix = new Pixmap(Gdx.files.internal("maps/default/map_provinces.png"));
        gameMap = new GameMap(visualTex, provincesPix);
        
        // Загрузка стран
        CountryLoader.loadCountries(gameMap, "maps/default/countries.json");
        
        // Инициализация владельцев провинций (пример)
        initializeProvinceOwners();
        
        // Инициализация рендереров
        mapRenderer = new MapRenderer(gameMap, batch);
        minimapRenderer = new MinimapRenderer(gameMap);
    }
    
    private void initializeProvinceOwners() {
        // Пример: распределяем провинции между странами
        if (gameMap.getCountries().size > 0) {
            Country firstCountry = gameMap.getCountries().get(0);
            Country secondCountry = gameMap.getCountries().size > 1 ? gameMap.getCountries().get(1) : null;
            
            int count = 0;
            for (Province province : gameMap.getProvinces().values()) {
                if (count < 50) {
                    province.setOwnerTag(firstCountry.getTag());
                } else if (secondCountry != null && count < 100) {
                    province.setOwnerTag(secondCountry.getTag());
                }
                count++;
            }
            
            // Устанавливаем игроку первую страну
            playerCountry = firstCountry;
            playerCountry.setPlayerControlled(true);
            infoLabel.setText("Игрок: " + playerCountry.getTag());
        }
    }
    
    private void setupInput() {
        inputProcessor = new GameInputProcessor(camera, gameMap, this);
        stage.addListener(inputProcessor);
    }
    
    private void setupTurnManager() {
        turnManager = new GameTurnManager(gameMap);
        turnManager.addListener(this);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        // Рендеринг карты
        mapRenderer.render(camera);
        
        // Рендеринг UI
        stage.act(delta);
        stage.draw();
        
        // Обновление состояния
        turnManager.update(delta);
        
        // Обновляем выделенную провинцию
        if (selectedProvince != null) {
            // Визуализация выделения
        }
    }
    
    @Override
    public void onTurnEvent(TurnEvent event) {
        Gdx.app.postRunnable(() -> {
            if (event.getType() == TurnEvent.Type.START) {
                turnLabel.setText("Ход: " + event.getTurnNumber());
                infoLabel.setText("Ход " + event.getTurnNumber() + " начат");
            } else if (event.getType() == TurnEvent.Type.END) {
                infoLabel.setText("Ход " + event.getTurnNumber() + " завершен");
                endTurnButton.setDisabled(false);
                
                // Обновляем карту
                mapRenderer.setNeedsUpdate();
                
                // Обновляем информацию о провинциях
                if (selectedProvince != null) {
                    updateProvinceInfo(selectedProvince);
                }
            }
        });
    }
    
    public void onProvinceSelected(Province province) {
        this.selectedProvince = province;
        
        if (isSelectingCountry && province.getOwnerTag() != null) {
            // Выбор страны игрока
            for (Country country : gameMap.getCountries()) {
                if (country.getTag().equals(province.getOwnerTag())) {
                    playerCountry = country;
                    playerCountry.setPlayerControlled(true);
                    isSelectingCountry = false;
                    selectCountryButton.setText("Выбрать страну");
                    infoLabel.setText("Игрок: " + country.getTag() + " (Выбрано)");
                    break;
                }
            }
        }
        
        updateProvinceInfo(province);
    }
    
    private void updateProvinceInfo(Province province) {
        String owner = province.getOwnerTag() != null ? province.getOwnerTag() : "Нейтральная";
        String info = String.format("Провинция ID: %d\nВладелец: %s\nНаселение: %d\nЭкономика: %d",
            province.getId(), owner, province.getPopulation(), province.getEconomy());
        infoLabel.setText(info);
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        
        float aspectRatio = (float) width / height;
        camera.viewportWidth = 1920 * aspectRatio;
        camera.viewportHeight = 1920;
        camera.update();
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        mapRenderer.dispose();
        gameMap.getVisualTexture().dispose();
        gameMap.getProvincesPixmap().dispose();
    }
    
    // Другие методы...
}