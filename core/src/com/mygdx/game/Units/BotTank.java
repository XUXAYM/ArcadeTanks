package com.mygdx.game.Units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Utils.Direction;
import com.mygdx.game.Utils.TankOwner;
import com.mygdx.game.Weapon;

public class BotTank extends Tank {//Класс ботов-противников
    Direction preferedDirection;
    float aiTimer;
    float aiTimerTo;
    float pursuitRadius;
    boolean active;
    Vector3 lastPosition;

    public boolean isActive() {
        return active;
    }

    public BotTank(GameScreen game, TextureAtlas atlas){
            super(game);
            this.ownerType = TankOwner.AI;
            this.textureBody = atlas.findRegion("bottankbody");
            this.textureHp = atlas.findRegion("hpbar");
            this.weapon = new Weapon(atlas);
            this.position = new Vector2(500.0f, 500.0f);
            this.lastPosition = new Vector3(0.0f,0.0f,0.0f);
            this.speed = 100.0f;
            this.angle = 0.0f;
            this.aiTimerTo = 3.0f;
            this.pursuitRadius = 300.0f;
            this.preferedDirection = Direction.UP;
            this.circle = new Circle(position.x, position.y,32.0f);
        }

        public void activate(float x, float y){
        hpMax = 3;
        hp = hpMax;
        preferedDirection = Direction.values()[MathUtils.random(0,Direction.values().length-1)];
        angle = preferedDirection.getAngle();
        position.set(x, y);
        aiTimer = 0.0f;
        active = true;
        }
        @Override
        public void destroy() {
            gameScreen.getItemsEmitter().generateRandomItem(position.x, position.y, 2, 0.5f);
            gameScreen.getKillSound().play();
            active = false;
        }

    public void update(float dt) {
        //Здесь описаны правила движения ботов и выбор приоритетных целей для выстрела
            aiTimer += dt;
            if (aiTimer >= aiTimerTo){
                aiTimer = 0.0f;
                aiTimerTo = MathUtils.random(3.5f,6.0f);
                preferedDirection = Direction.values()[MathUtils.random(0,Direction.values().length-1)];
            }
            angle = preferedDirection.getAngle();
            move(preferedDirection,dt);

            PlayerTank preferedTarget = null;
            if(gameScreen.getPlayers().size() == 1){
                preferedTarget = gameScreen.getPlayers().get(0);
            }else {
                float minDist = Float.MAX_VALUE;
                for (int i = 0; i < gameScreen.getPlayers().size(); i++) {
                    PlayerTank player = gameScreen.getPlayers().get(i);
                    float dst = this.position.dst(player.getPosition());
                    if(dst < minDist && player.isAlive()){
                        minDist = dst;
                        preferedTarget = player;
                    }
                }
            }
            float dst = this.position.dst(preferedTarget.getPosition());
            if (dst < pursuitRadius){
                rotateTurretToPoint(preferedTarget.getPosition().x, preferedTarget.getPosition().y, dt);
                fire();
            }
            if(Math.abs(position.x - lastPosition.x)<0.5f && Math.abs(position.y - lastPosition.y)<0.5f){
                lastPosition.z += dt;
                if (lastPosition.z > 0.25f){
                    aiTimer += 10.0f;
                }
            }else{
                lastPosition.x = position.x;
                lastPosition.y = position.y;
                lastPosition.z = 0.0f;
            }
            super.update(dt);
       }
}
