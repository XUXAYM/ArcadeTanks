package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Units.BotTank;

public class BotEmitter {//Класс генерации объектов класса BotTank в игру
    private BotTank[] bots;

    public static final  int MAX_BOTS_COUNT = 50;

    public BotTank[] getBots() {
        return bots;
    }

    public BotEmitter(GameScreen gameScreen, TextureAtlas atlas) {
        this.bots = new BotTank[MAX_BOTS_COUNT];
        for (int i = 0; i < bots.length; i++){
            this.bots[i] = new BotTank(gameScreen, atlas);
        }
    }

    public void activate(float x, float y){//Отвечает за инициализацию экземпляра класса BotTank
        for (int i = 0; i < bots.length; i++){
            if (!bots[i].isActive()){
                bots[i].activate(x, y);
                break;
            }
        }
    }

    public  void render(SpriteBatch batch){
        for (int i = 0; i <bots.length; i++){
            if (bots[i].isActive()){
                bots[i].render(batch);
            }
        }
    }

    public void  update (float dt){
        for (int i =0; i < bots.length; i++) {
            if (bots[i].isActive()){
                bots[i].update(dt);
            }
        }
    }
}
