package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Units.Tank;

public class Bullet {//Класс отвечает за свойства и методы пуль
    private Tank owner;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private boolean active;
    private int damage;
    private float currentTime;
    private float maxTime;



    public int getDamage() {
        return damage;
    }

    public Tank getOwner() {
        return owner;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }


    public Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
        this.damage = 0;
    }


    public void activate(Tank owner, float x, float y, float vx, float vy,
                         float bulletAngle, int damage, float maxTime){
        //Инициализирует пулю в игру, задаёт начальную точку и направление по которому
        //она будет линейно двигаться
        this.owner = owner;
        this.active = true;
        this.angle = bulletAngle;
        this.position.set(x,y);
        this.velocity.set(vx,vy);
        this.damage = damage;
        this.maxTime = maxTime;
        this.currentTime = 0.0f;
    }

    public void deactivate(){
        active = false;
    }

    public void update (float dt){
        position.mulAdd(velocity, dt);
        currentTime += dt;
        if (currentTime >= maxTime){
            deactivate();
        }
        if(position.x < 0.0f || position.x > 1280.0f || position.y < 0.0f || position.y > 720.0f){
            deactivate();
        }
    }
}
