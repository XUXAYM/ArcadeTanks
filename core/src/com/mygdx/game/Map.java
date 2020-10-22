package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {//Класс отвечает за генерацию боевого поля

    public enum WallType {//Описывает типы блоков на карте и их свойства
        SOFT(0,2,true, false, false),
        INDESTRUCTIBLE(1,1,false,false,false),
        WATER(2,1,false,false,true),
        NONE(0,0,false,true,true);

        int index;
        int maxHP;
        boolean unitPassable;
        boolean projectilePassable;
        boolean destructible;

        WallType(int index, int maxHP, boolean destructible, boolean unitPassable, boolean projectilePassable) {
            this.index = index;
            this.maxHP = maxHP;
            this.destructible = destructible;
            this.unitPassable = unitPassable;
            this.projectilePassable = projectilePassable;
        }
    }

    private class Cell{//Класс ячейки на которые делится вся карта
        WallType type;
        int hp;

        public Cell(WallType type) {
            this.type = type;
            this.hp = type.maxHP;
        }

        public void damage(){
            if (type.destructible) {
                hp--;
                if (hp <= 0) {
                    type = WallType.NONE;
                }
            }
        }

        public void changeType(WallType type){
            this.type = type;
            this.hp = type.maxHP;
        }
    }

    public static final int SIZE_X =32;
    public static final int SIZE_Y =18;
    public static final int CELL_SIZE =40;

    private TextureRegion groundTexture;
    private TextureRegion[][] wallTexture;
    private  Cell cells[][];


    public Map(TextureAtlas atlas) {//Отвечает за отображения всей карты и расположения типов блоков по ячейкам
        this.groundTexture = atlas.findRegion("dirt");
        this.wallTexture = new TextureRegion(atlas.findRegion("walls")).split(CELL_SIZE,CELL_SIZE);
        this.cells = new Cell[SIZE_X][SIZE_Y];
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y ; j++) {
                cells[i][j] = new Cell(WallType.NONE);
                int cx = i / 2;
                int cy = j / 2;
                if (cx % 2 == 0 && cy % 2 == 0){
                    cells[i][j].changeType(WallType.SOFT);
                }
            }
        }
        for (int i = 0; i < SIZE_X; i++) {
            cells[i][0].changeType(WallType.INDESTRUCTIBLE);
            cells[i][SIZE_Y-1].changeType(WallType.INDESTRUCTIBLE);
        }
        for (int i = 0; i < SIZE_Y; i++) {
            cells[0][i].changeType(WallType.INDESTRUCTIBLE);
            cells[SIZE_X-1][i].changeType(WallType.INDESTRUCTIBLE);
        }
    }

    public void checkWallsAndBulletsCollision(Bullet bullet){//Проверка столкновения пули с ячейкой карты
        int cx = (int)(bullet.getPosition().x / CELL_SIZE);
        int cy = (int)(bullet.getPosition().y / CELL_SIZE);

        if(cx >=0 && cy >= 0 && cx < SIZE_X && cy <= SIZE_Y){
            if (!cells[cx][cy].type.projectilePassable){
                cells[cx][cy].damage();
                bullet.deactivate();
            }
        }
    }

    public boolean isAreaClear (float x, float y, float halfsize){//Проверка столкновения танка с ячейкой карты
        int leftX = (int)((x -  halfsize) / CELL_SIZE);
        int rightX = (int)((x +  halfsize) / CELL_SIZE);

        int bottomY = (int)((y -  halfsize) / CELL_SIZE);
        int topY = (int)((y +  halfsize) / CELL_SIZE);

        if(leftX < 0) {
            leftX = 0;
        }
        if (rightX >= SIZE_X){
            rightX = SIZE_X-1;
        }
        if(bottomY < 0) {
            bottomY = 0;
        }
        if (topY >= SIZE_Y){
            topY = SIZE_Y-1;
        }

        for (int i = leftX; i <= rightX; i++) {
            for (int j = bottomY; j <= topY; j++) {
                if(!cells[i][j].type.unitPassable){
                    return false;
                }
            }
        }
        return true;
    }

    public void render (SpriteBatch batch){
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                batch.draw(groundTexture, i * CELL_SIZE, j * CELL_SIZE);
                if(cells[i][j].type != WallType.NONE){
                    batch.draw(wallTexture[cells[i][j].type.index][cells[i][j].hp-1], i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }
}
