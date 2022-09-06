package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.graphics.Color;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class PlanetNinja extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    ;
    //instance
    Texture background, apple, bamb, cherry, greenApple, banana, coconut, healBar, heart;
    Texture image, image2,image3;
    BitmapFont font;

    FreeTypeFontGenerator freeTypeFontGenerator;

    int lives = 0;
    int score = 0;


    private double currentTime;
    private double gameOverTime = -1.0f;

    Music music;

    Random random = new Random();
    Array<Fruit> fruitArray = new Array<Fruit>();


    float genCounter;
    private final float genSpeedStart = 1.1f;
    float genSpeed = genSpeedStart;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    int startStop = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();


        //instance
        image3=new Texture("Tekraroyna.png");
        heart = new Texture("heart.png");
        healBar = new Texture("healBar.png");
        background = new Texture("bg.png");
        apple = new Texture("apple1.png");
        cherry = new Texture("cherry.png");
        bamb = new Texture("FatalBomb.png");
        greenApple = new Texture("greenApple.png");
        coconut = new Texture("Coconut.png");
        banana = new Texture("muz.png");
        image = new Texture("1425569252372.png");
        image2 = new Texture("template.png");


        Fruit.radius = Math.max(Gdx.graphics.getHeight(), Gdx.graphics.getWidth()) / 17f;
/*

        Pixmap pixmap = new Pixmap(Gdx.files.internal("Blade.png"));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
*/

        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));


        Gdx.input.setInputProcessor(this);

        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("robotobold.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.GOLD;
        parameter.size = 60;
        parameter.characters = "0123456789 GAMEOVERLYPCScrecutoplay .;+:<)(>+-*#";
        font = freeTypeFontGenerator.generateFont(parameter);
//0123456789 GAMEOVERLYPCScrecutoplay .;:+-*#"
    }

    @Override
    public void render() {

        batch.begin();

        double newTime = TimeUtils.millis() / 1000.0;//real time
        System.out.println("newTime:" + newTime);
        double frameTime = Math.min(newTime - currentTime, 0.3);
        float deltaTime = (float) frameTime;
        System.out.println("detaTime" + deltaTime);
        currentTime = newTime;
        //konum -- background size
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        music.play();
        music.setVolume(10f);
        music.setLooping(true);


        if (lives <= 0 && gameOverTime == 0f) {
            //game Over

            gameOverTime = currentTime;
        }
        if (lives > 0) {
            //game Mod
            genSpeed -= deltaTime * 0.008f;
            if (genCounter <= 0f) {
                genCounter = genSpeed * 1.6f;
                addItem();
            } else {
                genCounter -= deltaTime * 1.5f;
            }


            for (int i = 0; i < lives; i++) {
                //çizeceği texture,konum
                batch.draw(bamb, i * 22f * 3, Gdx.graphics.getHeight() - 125f, 125f, 125f);
            }

            for (Fruit fruit : fruitArray) {
                fruit.update(deltaTime);
                switch (fruit.type) {
                    case REGULAR:
                        batch.draw(apple, fruit.getPos().x, fruit.getPos().y, Fruit.radius, Fruit.radius);
                        break;
                    case ENEMY:
                        batch.draw(bamb, fruit.getPos().x, fruit.getPos().y, Fruit.radius * 1.8f, Fruit.radius * 1.8f);
                        break;
                    case EXTRA:
                        batch.draw(cherry, fruit.getPos().x, fruit.getPos().y, Fruit.radius, Fruit.radius);
                        break;
                    case LIFE:
                        batch.draw(heart, fruit.getPos().x, fruit.getPos().y, Fruit.radius * 0.9f, Fruit.radius * 0.9f);
                        break;

                    case GREENAPPLE:
                        batch.draw(greenApple, fruit.getPos().x, fruit.getPos().y, Fruit.radius * 0.9f, Fruit.radius * 0.9f);

                }


            }


        }if(lives>0)
        font.draw(batch, " #Score:" + (score-1), 20, 60);

        //   if (lives == 0) {
        if (score == 0) {
            batch.draw(image,Gdx.graphics.getWidth() * 0.37f, Gdx.graphics.getHeight() * 0.5f);

        } else if (score > 1 && lives <= 0) {
            batch.draw(image3, Gdx.graphics.getWidth() * 0.37f, Gdx.graphics.getHeight() * 0.45f);//tekrar oyna btonu gelecek
            font.draw(batch, "  + GAMEOVER +", Gdx.graphics.getWidth() * 0.365f, Gdx.graphics.getHeight() * 0.40f);//game over image
            font.draw(batch, " # SCORE :" + (score-1), Gdx.graphics.getWidth() * 0.38f, Gdx.graphics.getHeight() * 0.32f);
        }
//}


        //font.draw(batch, "Cut to Play", Gdx.graphics.getWidth() * 0.39f, Gdx.graphics.getHeight() * 0.5f);

     /*   if (lives <= 0 && currentTime - gameOverTime > 2f)

            font.draw(batch,"GAME OVER",Gdx.graphics.getWidth() *0.37f,Gdx.graphics.getHeight() * 0.5f);


        batch.draw(image, Gdx.graphics.getWidth() * 0.37f, Gdx.graphics.getHeight() * 0.5f);
*/
        batch.end();
    }


    private void addItem() {

        float pos = random.nextFloat() * (Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        //Konum ve Hız
        Fruit item = new Fruit(new Vector2(pos, -Fruit.radius), new Vector2((Gdx.graphics.getWidth() * 0.3f) * (random.nextFloat() - 0.1f), (Gdx.graphics.getHeight() * 0.65f)));
        float type = random.nextFloat();
        if (type > 0.94) {
            item.type = Fruit.Type.LIFE;
        } else if (type > 0.8) {
            item.type = Fruit.Type.EXTRA;
        } else if (type > 0.6) {
            item.type = Fruit.Type.ENEMY;
        } else if (type > 0.5) {
            item.type = Fruit.Type.GREENAPPLE;
        }


        fruitArray.add(item);


    }


    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        freeTypeFontGenerator.dispose();
        music.dispose();


    }

    //Input proCosser
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

        if (score == 0) {
            score = 1;
        }

        return false;
    }

    //dokunduğunda ne olacak
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {


        if (lives <= 0 && currentTime - gameOverTime > 2f) {

            //menu mod
            gameOverTime = 0f;
            lives = 4;
            score = 0;
            genSpeed = genSpeedStart;
            fruitArray.clear();


        } else {
            int plusScore = 0;
            Array<Fruit> toRemove = new Array<Fruit>();
            Vector2 pos = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
            for (Fruit f : fruitArray) {
                System.out.println("getHeight - y: " + screenY);
                System.out.println("getHeight - y: " + (Gdx.graphics.getHeight() - screenY));
                System.out.println("getHeight - y: " + f.getPos());
                System.out.println("distance: " + pos.dst2(f.pos));
                System.out.println("distance: " + f.clicked(pos));
                System.out.println("distance: " + Fruit.radius * Fruit.radius + 1);
                if (f.clicked(pos)) {
                    toRemove.add(f);


                    switch (f.type) {
                        case REGULAR:
                            plusScore++;
                            break;
                        case ENEMY:
                            lives--;
                            break;
                        case EXTRA:
                            plusScore += 2;
                            break;
                        case LIFE:
                            lives++;
                            break;
                        case GREENAPPLE:
                            plusScore += 1;
                            break;


                    }
                }
            }

            score += plusScore * plusScore;


            for (Fruit f : toRemove) {
                fruitArray.removeValue(f, true);
            }

        }


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
