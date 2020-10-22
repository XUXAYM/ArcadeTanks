package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.game.Units.PlayerTank;
import com.mygdx.game.Utils.GameType;

import java.util.List;

public class ScreenManager {//Класс используется для работы с отображением
                            //Отвечает за переключения между экранами программы
    public enum ScreenType {
        MENU, GAME, GAME_OVER, GAME_VICTORY
    }

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private ScreenManager() {
    }

    public static final int WORLD_WIDTH = 1280;
    public static final int WORLD_HEIGHT = 720;

    private Game game;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private GameOverScreen gameOverScreen;
    private GameVictoryScreen gameVictoryScreen;
    private Viewport viewport;
    private Camera camera;

    public Camera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void init(Game game, SpriteBatch batch) {//Инициализаця визуального отображения и типов экранов
        this.game = game;
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2,0);
        this.camera.update();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch);
        this.gameVictoryScreen = new GameVictoryScreen(batch);
    }
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public void setScreen(ScreenType screenType, Object... args) {
        Gdx.input.setCursorCatched(false);
        Screen currentScreen = game.getScreen();
        switch (screenType) {
            case MENU:
                game.setScreen(menuScreen);
                break;
            case GAME:
                gameScreen.setGameType((GameType)args[0]);
                game.setScreen(gameScreen);
                break;
            case GAME_OVER:
                gameOverScreen.setPlayersInfo((List<PlayerTank>) args[0]);
                game.setScreen(gameOverScreen);
                break;
            case GAME_VICTORY:
                gameVictoryScreen.setPlayersInfo((List<PlayerTank>) args[0]);
                game.setScreen(gameVictoryScreen);
                break;
        }
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}