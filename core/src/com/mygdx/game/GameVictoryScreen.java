package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Units.PlayerTank;

import java.util.List;

public class GameVictoryScreen extends AbstractScreen {//Отвечает за окно победы в игре
                                                        //Показывает статистику игрока/игроков за игру
    private SpriteBatch batch;
    private BitmapFont font;
    private List<PlayerTank> playersInfo;
    private StringBuilder tmpString;
    private Texture backGroundImage;
    private float timer;


    public void setPlayersInfo(List<PlayerTank> playersInfo) {
        this.playersInfo = playersInfo;
    }

    public GameVictoryScreen(SpriteBatch batch) {
        this.batch = batch;
        this.tmpString = new StringBuilder();
        backGroundImage = new Texture("victory.jpg");
    }

    @Override
    public void show() {
        font = new BitmapFont(Gdx.files.internal("font24.fnt"));
    }

    @Override
    public void render(float dt) {
        update(dt);
        batch.begin();
        batch.draw(backGroundImage, 0, 0);
        for (int i = 0; i < playersInfo.size(); i++) {
            tmpString.setLength(0);
            tmpString.append("Player: ").append(playersInfo.get(i).getIndex());
            tmpString.append("\nScore: ").append(playersInfo.get(i).getScore());
            font.draw(batch, tmpString, 400 + i * 480, 400);
        }
        font.draw(batch, "Victory", 0, 500, 1280, 1, false);
        font.draw(batch, "Tap on screen to return to menu...", 0, 50, 1280, 1, false);
        batch.end();
    }

    public void update(float dt) {
        timer += dt;
        if (Gdx.input.justTouched()&& timer > 5.0f){
            ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}