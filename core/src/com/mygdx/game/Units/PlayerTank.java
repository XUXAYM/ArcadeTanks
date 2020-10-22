package com.mygdx.game.Units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Item;
import com.mygdx.game.ScreenManager;
import com.mygdx.game.Utils.Direction;
import com.mygdx.game.Utils.KeysControl;
import com.mygdx.game.Utils.TankOwner;
import com.mygdx.game.Utils.Utils;
import com.mygdx.game.Weapon;


public class PlayerTank extends Tank {//Класс танка игрока/игроков
    KeysControl keysControl;
    StringBuilder tmpString;
    int lives;
    int index;
    float gameTimer;
    public int getIndex() {
        return index;
    }

    public int getScore() {
        return score;
    }

    public PlayerTank(int index, GameScreen game, KeysControl keysControl, TextureAtlas atlas){
        super(game);
        this.index = index;
        this.ownerType = TankOwner.PLAYER;
        this.keysControl = keysControl;
        this.textureBody = atlas.findRegion("tankbody");
        this.textureHp = atlas.findRegion("hpbar");
        this.weapon = new Weapon(atlas);
        this.position = new Vector2(110.0f, 115.0f);
        this.speed = 100.0f;
        this.angle = 0.0f;
        this.hpMax = 5;
        this.hp = this.hpMax;
        this.circle = new Circle(position.x, position.y,32.0f);
        this.lives = 3;
        this.score = 0;
        this.tmpString = new StringBuilder();
        this.gameTimer = 0.0f;
    }

    public boolean isAlive() {
        return lives > 0;
    }


    @Override
    public void destroy() {
        lives --;
        hp = hpMax;
    }



    public void update(float dt) {
        checkMovement(dt);
        if(speed == 200) {
            gameTimer += dt;
            if (gameTimer > 8) {
                speed = 100.0f;
                gameTimer = 0;
            }
        }
        tmp.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(tmp);

        if (keysControl.getTargeting() == KeysControl.Targeting.MOUSE) {
            rotateTurretToPoint(gameScreen.getMousePosition().x, gameScreen.getMousePosition().y, dt);
            if (Gdx.input.isTouched()) {
                fire();
            }
        }else {
            if (Gdx.input.isKeyPressed(keysControl.getRotateTurretLeft())) {
                turretAngle = Utils.makeRotation(turretAngle, turretAngle + 30.0f, 280.0f, dt);
                turretAngle = Utils.angleToFromNegPiToPosPi(turretAngle);
            }
            if (Gdx.input.isKeyPressed(keysControl.getRotateTurretRight())) {
                turretAngle = Utils.makeRotation(turretAngle, turretAngle - 30.0f, 280.0f, dt);
                turretAngle = Utils.angleToFromNegPiToPosPi(turretAngle);
            }
            if (Gdx.input.isKeyPressed(keysControl.getFire())) {
                fire();
            }
        }
        super.update(dt);
    }

    public void consumePowerUp(Item item){//Описывает как влияют усиления на свойтсва танка игрока
        switch (item.getType()){
            case MEDKIT:
                hp += 4;
                if (hp>hpMax){
                    hp = hpMax;
                }
                break;
            case SPEEDUP:
                speed = 200.0f;
                break;
        }
    }

    public  void renderHUD(SpriteBatch batch, BitmapFont font24){//Отображение статистики игрока во время игры
        tmpString.setLength(0);
        tmpString.append("Игрок: ").append(index);
        tmpString.append("\nОчки: ").append(score);
        tmpString.append("\nЖизни:").append(lives);
        font24.draw(batch, tmpString,25 + (index -1) * 200,700 );
    }

    public void checkMovement(float dt){//Управление движением танка
        if (Gdx.input.isKeyPressed(keysControl.getLeft()))
        {
            move(Direction.LEFT,dt);
        }else if (Gdx.input.isKeyPressed(keysControl.getRight()))
        {
            move(Direction.RIGHT,dt);
        }else if (Gdx.input.isKeyPressed(keysControl.getUp()))
        {
            move(Direction.UP,dt);
        }else if (Gdx.input.isKeyPressed(keysControl.getDown()))
        {
            move(Direction.DOWN,dt);
        }
    }
}
