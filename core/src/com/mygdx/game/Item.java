package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Item {//Класс отвечает за усиления которые игрок может поднять с убитых противников
    public enum Type {
        SPEEDUP(0), MEDKIT(1);

        int index;

        Type(int index) {
            this.index = index;
        }
    }

    private Vector2 position;
    private Vector2 velocity;
    private Type type;
    private float time;
    private float timeMax;
    private boolean active;
    private GameScreen gameScreen;


    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Type getType() {
        return type;
    }

    public float getTime() {
        return time;
    }

    public Item(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.type = Type.SPEEDUP;
        this.timeMax = 5.0f;
        this.time = 0.0f;
        this.active = false;
    }

    public void setup(float x, float y, Type type) {//Инициализирует усиление в игру
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
        this.type = type;
        this.time = 0.0f;
        this.active = true;
    }

    public void update(float dt) {
        time += dt;
        position.mulAdd(velocity, dt * 0.4f);
        if (time > timeMax) {
            deactivate();
        }
    }
}
