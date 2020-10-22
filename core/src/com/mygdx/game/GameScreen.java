package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Units.BotTank;
import com.mygdx.game.Utils.GameType;
import com.mygdx.game.Units.PlayerTank;
import com.mygdx.game.Units.Tank;
import com.mygdx.game.Utils.KeysControl;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends AbstractScreen { //Отвечает за отображения экрана игры
    private SpriteBatch batch;
    private BitmapFont font;
    private TextureAtlas atlas;
    private Map map;
    private GameType gameType;

    private List<PlayerTank> players;

    private ItemsEmitter itemsEmitter;
    private BulletEmitter bulletEmitter;
    private BotEmitter botEmitter;
    private float gameTimer;
    private float worldTimer;
    private Stage stage;
    private  boolean paused;
    private Vector2 mousePosition;
    private TextureRegion cursor;

    private Sound shootSound;
    private Sound deathSound;
    private Sound victorySound;
    private Sound loseSound;
    private Music music;
    private Sound killSound;


    private static final boolean FRIENDLY_FIRE = false;

    public ItemsEmitter getItemsEmitter() {
        return itemsEmitter;
    }

    public Sound getDeathSound() {
        return deathSound;
    }

    public Sound getKillSound() {
        return killSound;
    }

    public Sound getShootSound() {
        return shootSound;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }


    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        this.gameType = GameType.ONE_PLAYER;
    }

    public Map getMap() {
        return map;
    }

    public List<PlayerTank> getPlayers() {
        return players;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }


    @Override
    public void show() {
        deathSound = Gdx.audio.newSound(Gdx.files.internal("death.mp3"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
        killSound = Gdx.audio.newSound(Gdx.files.internal("kill.mp3"));
        loseSound = Gdx.audio.newSound(Gdx.files.internal("gameover.mp3"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("victory.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("dancelikebee.mp3"));
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

        atlas = new TextureAtlas("game.pack");
        font = new BitmapFont(Gdx.files.internal("font28.fnt"));
        cursor = new TextureRegion(atlas.findRegion("cursor"));
        map = new Map(atlas);

        players = new ArrayList<>();
        players.add(new PlayerTank(1,this, KeysControl.CreateStandardControl1() ,atlas));
        if (gameType == GameType.TWO_PLAYERS) {
            players.add(new PlayerTank(2, this, KeysControl.CreateStandardControl2(), atlas));
        }
        bulletEmitter = new BulletEmitter(atlas);
        itemsEmitter = new ItemsEmitter(atlas);
        botEmitter = new BotEmitter(this, atlas);
        gameTimer = 6.0f;
        stage = new Stage();
        mousePosition = new Vector2();
        Skin skin = new Skin();
        skin.add("simplebutton", new TextureRegion(atlas.findRegion("simplebutton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simplebutton");
        textButtonStyle.font = font;

        Group group = new Group();
        final  TextButton pauseButton = new TextButton("Pause",textButtonStyle);
        final  TextButton menuButton = new TextButton("Menu",textButtonStyle);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });

        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
            }
        });
        pauseButton.setPosition(0,40);
        menuButton.setPosition(0,0);
        group.addActor(pauseButton);
        group.addActor(menuButton);
        group.setPosition(1100,40);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        map.render(batch);
        bulletEmitter.render(batch);
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).isAlive()) players.get(i).render(batch);
        }

        itemsEmitter.render(batch);
        botEmitter.render(batch);

        for (int i = 0; i < players.size(); i++) {
            players.get(i).renderHUD(batch, font);
        }
        batch.end();
        stage.draw();
        batch.begin();
        batch.draw(cursor, mousePosition.x - cursor.getRegionWidth()/2, mousePosition.y - cursor.getRegionHeight()/2, cursor.getRegionWidth()/2, cursor.getRegionHeight()/2,cursor.getRegionWidth(), cursor.getRegionHeight(), 1, 1, -worldTimer * 45);
        batch.end();

    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(),Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);
        worldTimer += dt;
        if(!paused) {
            gameTimer += dt;
            if (gameTimer > 5.0f) {
                gameTimer = 0.0f;
                float coordX, coordY;
                do {
                    coordX = MathUtils.random(0, Gdx.graphics.getWidth());
                    coordY = MathUtils.random(0, Gdx.graphics.getHeight());
                } while (!map.isAreaClear(coordX, coordY, 32.0f));

                botEmitter.activate(coordX, coordY);
            }
            bulletEmitter.update(dt);
            for (int i = 0; i < players.size(); i++) {
                if(players.get(i).isAlive()) players.get(i).update(dt);
            }
            botEmitter.update(dt);
            itemsEmitter.update(dt);
            checkCollisions();
        }
        stage.act(dt);
    }

    public void checkCollisions(){//Метод определяет попала ли какая нибудь из пуль в ботов или игрока
                                    //Так же вызывает методы проверки взаимодействия всех объектов с объектами карты
        for (int i = 0; i <  bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if(bullet.isActive()){
                for (int j = 0; j < botEmitter.getBots().length; j++) {
                    BotTank bot = botEmitter.getBots()[j];
                    if (bot.isActive()){
                        if (checkBulletAndTank(bot, bullet ) && bot.getCircle().contains(bullet.getPosition())) {
                            bullet.deactivate();
                            bot.takeDamage(bullet.getDamage());
                            if(!bot.isActive()){
                                bullet.getOwner().addScore(100);
                            }
                            break;
                        }
                    }
                }
                for (int j = 0; j < players.size(); j++) {
                    PlayerTank player = players.get(j);
                    if(player.isAlive())
                    if (checkBulletAndTank(player, bullet) && player.getCircle().contains(bullet.getPosition())) {
                        bullet.deactivate();
                        player.takeDamage(bullet.getDamage());
                    }
                }
                map.checkWallsAndBulletsCollision(bullet);
            }
        }

        for (int i = 0; i < itemsEmitter.getItems().length; i++) {
            if (itemsEmitter.getItems()[i].isActive()){
                Item item = itemsEmitter.getItems()[i];
                for (int j = 0; j < players.size(); j++) {
                    if (players.get(j).getCircle().contains(item.getPosition())){
                        players.get(j).consumePowerUp(item);
                        item.deactivate();
                        break;
                    }
                }
            }
        }
        boolean isGameOver = true;
        int gameScore = 0;
        for (int i = 0; i < players.size(); i++) {
            gameScore = gameScore + players.get(i).getScore();
            if (players.get(i).isAlive()) {
                isGameOver = false;
            }
        }
        if(gameScore == 1000*players.size()){
            victorySound.play();
            ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_VICTORY, players);
        }

        if (isGameOver) {
            loseSound.play();
            ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_OVER, players);
        }
    }

    public boolean checkBulletAndTank(Tank tank, Bullet bullet){//Метод определяет к какому типу
                                                                // боту или игроку относится владелец пули
        if (!FRIENDLY_FIRE ){
            return tank.getOwnerType() != bullet.getOwner().getOwnerType();
        }else {
            return tank != bullet.getOwner();
        }
    }


    @Override
    public void dispose() {
        stage.dispose();
        music.dispose();
        atlas.dispose();
        font.dispose();
    }
}
