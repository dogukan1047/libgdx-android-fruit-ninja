package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class PlanetNinja extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture background, apple, bamb, cherry, greenApple, banana, ananas, kavun, coconut, healBar;
    BitmapFont font;
    FreeTypeFontGenerator freeTypeFontGenerator;
    int lives = 4;
    int score = 5;
    Sound sound;
    private double currentTime;
    private double gameOverTime = -1.0f;
    Music music;

    @Override
    public void create() {
        batch = new SpriteBatch();
        healBar = new Texture("healBar.png");
        background = new Texture("bg.png");
        apple = new Texture("apple1.png");
        cherry = new Texture("cherry.png");
        bamb = new Texture("FatalBomb.png");
        greenApple = new Texture("greenApple.png");
        coconut = new Texture("Coconut.png");
        banana = new Texture("muz.png");
        // ananas = new Texture("ananas.jpg");
        //kavun = new Texture("kavun.jpg");

        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        Gdx.input.setInputProcessor(this);

        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("robotobold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.RED;
        parameter.size = 60;
        parameter.characters = "0123456789  PCScrecutoplay .;: +-*#";
        font = freeTypeFontGenerator.generateFont(parameter);

    }

    @Override
    public void render() {

        batch.begin();

        double newTime= TimeUtils.millis()/1000.0;//real time
        System.out.println("newTime:"+newTime);
        double frameTime=Math.min(newTime-currentTime,0.3);
        float deltaTime=(float)frameTime;
        System.out.println("detaTime"+deltaTime);
        currentTime=newTime;
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        music.play();
        music.setVolume(10f);
        music.setLooping(true);


        if (lives <= 0 && gameOverTime == 0f) {
            //Oyun biiti

            gameOverTime = currentTime;
        }
        if (lives > 0) {
            //oyun modu

            for (int i = 0; i < lives; i++) {
//çizeceği texture,konum
                batch.draw(bamb, i * 22f * 3, Gdx.graphics.getHeight() - 125f, 125f, 125f);
            }


        }
        font.draw(batch, " #Score:", 20, 60);
        //font.draw(batch, "Cut to Play", Gdx.graphics.getWidth() * 0.39f, Gdx.graphics.getHeight() * 0.5f);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        freeTypeFontGenerator.dispose();
        music.dispose();


    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    //dokunduğunda ne olacak
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
