package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {//Класс вооружения танка
    private TextureRegion texture;
    private float firePeriod;
    private float radius;
    private float projectileSpeed;
    private float projectileLifetime;
    private int damage;

    public float getProjectileLifetime() {
        return projectileLifetime;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public int getDamage() {
        return damage;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public Weapon(TextureAtlas atlas) {
        this.texture = atlas.findRegion("turret");
        this.firePeriod = 1.4f;
        this.damage = 1;
        this.radius = 600.0f;
        this.projectileSpeed = 320.0f;
        this.projectileLifetime = this.radius / this.projectileSpeed;
    }

}
