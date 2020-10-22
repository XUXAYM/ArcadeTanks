package com.mygdx.game.Units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Bullet;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Utils.Direction;
import com.mygdx.game.Utils.TankOwner;
import com.mygdx.game.Utils.Utils;
import com.mygdx.game.Weapon;

public abstract class Tank {//Абстрактный класс боевых единиц
    GameScreen gameScreen;
    TankOwner ownerType;
    Weapon weapon;
    TextureRegion textureBody;
    TextureRegion textureHp;
    Vector2 position;
    Vector2 tmp;
    Circle circle;

    float speed;
    float angle;

    float turretAngle;
    float fireTimer;

    int hp;
    int hpMax;
    int score;

    public TankOwner getOwnerType() {
        return ownerType;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getCircle() {
        return circle;
    }

    public Tank (GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.tmp = new Vector2(0.0f,0.0f);
    }

    public void render(SpriteBatch batch){
        batch.draw(textureBody, position.x - 32, position.y - 32, 32, 32, 64, 64,1,1,angle);
        batch.draw(weapon.getTexture(), position.x - 32, position.y - 32, 32, 32, 64, 64,1,1,turretAngle);
        if (hp < hpMax) {
            batch.setColor(0, 0, 0, 0.8f);
            batch.draw(textureHp, position.x - 34, position.y + 30,68,16);
            batch.setColor(1, 0, 0, 0.8f);
            batch.draw(textureHp, position.x - 32, position.y + 32);
            batch.setColor(0, 1, 0, 0.8f);
            batch.draw(textureHp, position.x - 32, position.y + 32, ((float) hp / hpMax) * 64, 12);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void takeDamage(int damage){//Получение урона от попаданий
        hp -= damage;
        if(hp <= 0){
            destroy();
        }
    }

    public void addScore(int amount){
        score += amount;
    }

    public abstract void destroy();

    public void update(float dt){
        fireTimer += dt;
        if(position.x < 32.0f){
            position.x = 32.0f;
        }
        if(position.x > Gdx.graphics.getWidth()-32.0f){
            position.x = Gdx.graphics.getWidth()-32.0f;
        }
        if(position.y < 32.0f){
            position.y = 32.0f;
        }
        if(position.y > Gdx.graphics.getHeight()-32.0f){
            position.y = Gdx.graphics.getHeight()-32.0f;
        }
        circle.setPosition(position.x,position.y);
    }

    public void move(Direction direction, float dt){
        tmp.set(position);
        tmp.add(speed*direction.getVx()*dt,speed*direction.getVy()*dt);
        if (gameScreen.getMap().isAreaClear(tmp.x,tmp.y,32.0f)){
            angle = direction.getAngle();
            position.set(tmp);
        }
    }

    public void rotateTurretToPoint(float pointX, float pointY, float dt){
        //поворот орудия в сторону определённой точки игрового поля
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        turretAngle = Utils.makeRotation(turretAngle, angleTo, 280.0f, dt);
        turretAngle = Utils.angleToFromNegPiToPosPi(turretAngle);
    }



    public void fire() {
        //Отвечает за выстрелы танков
        if (fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            gameScreen.getShootSound().play(0.5f);
            float angleRad = (float) Math.toRadians(turretAngle);
            gameScreen.getBulletEmitter().activate(this, position.x, position.y,
                    weapon.getProjectileSpeed() * (float) Math.cos(angleRad),
                    weapon.getProjectileSpeed() * (float) Math.sin(angleRad),
                    turretAngle, weapon.getDamage(), weapon.getProjectileLifetime());
        }
    }
}
