package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Units.PlayerTank;
import com.mygdx.game.Units.Tank;

public class BulletEmitter {//Класс генерации объектов класса Bullet в игру
    private TextureRegion bulletTexture;
    private Bullet[] bullets;

    public Bullet[] getBullets() {
        return bullets;
    }

    public static final  int MAX_BULLETS_COUNT = 500;

    public BulletEmitter(TextureAtlas atlas) {
        this.bulletTexture = atlas.findRegion("bullet");
        this.bullets = new Bullet[MAX_BULLETS_COUNT];
        for (int i = 0; i < bullets.length; i++){
            this.bullets[i] = new Bullet();
        }
    }

    public void activate(Tank owner, float x, float y, float vx, float vy, float bulletAngle, int damage, float maxTime){
        //Отвечает за инициализацию экземпляра класса Bullet
        for (int i = 0; i < bullets.length; i++){
            if (!bullets[i].isActive()){
                bullets[i].activate(owner, x, y, vx, vy, bulletAngle, damage, maxTime);
                break;
            }
        }
    }

    public  void render(SpriteBatch batch){
        for (int i = 0; i <bullets.length; i++){
            if (bullets[i].isActive()){
                batch.draw(bulletTexture, bullets[i].getPosition().x - 16, bullets[i].getPosition().y - 16,16, 16, 32, 32,1,1, bullets[i].getAngle()-90);
            }
        }
    }

    public void  update (float dt){
        for (int i =0; i < bullets.length; i++) {
            if (bullets[i].isActive()){
                bullets[i].update(dt);
            }
        }
    }
}
