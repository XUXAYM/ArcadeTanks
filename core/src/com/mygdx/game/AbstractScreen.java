package com.mygdx.game;

import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {//Абстрактный класс экранов игры
    @Override
    public void resize(int width, int height) {//Отвечает за перерисовку экрана с новыми парметрами
        ScreenManager.getInstance().resize(width,height);
    }

    @Override
    public void pause() {//Останавливает отрисовку игры
    }

    @Override
    public void resume() {//Продолжает отрисовку игры
    }
    @Override
    public void hide() {
    }
}



