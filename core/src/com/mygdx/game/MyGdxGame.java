package com.mygdx.game;

//@Created by Dmitry Markin

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGdxGame extends Game { //Основной класс игры, наследуемый от класса Game фреймворка LibGDX
	private SpriteBatch batch;

	@Override
	public void create () { //Инициализация главного меню при запуске приложения
		batch = new SpriteBatch();
		ScreenManager.getInstance().init(this, batch);
		ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
	}


	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		getScreen().render(dt);
	}


	@Override
	public void dispose () {
		batch.dispose();
	}
}
