package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.graphics.Color;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.TimeUtils;


import java.util.Random;


public class PlanetNinja extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    Texture img;
    Sprite sprite;
    //instance
    Texture background, apple, bamb, cherry, greenApple, banana, coconut, healBar, heart;
    Texture image, image2, image3, image4;
    BitmapFont font;

    FreeTypeFontGenerator freeTypeFontGenerator;

    int lives = 0;
    int score = 0;

    ShapeRenderer shapeRenderer;


    Pixmap pixmap;


    int scoreTouch = 0;

    private double currentTime;
    private double gameOverTime = -1.0f;

    Music music;

    Random random = new Random();
    Array<Fruit> fruitArray = new Array<Fruit>();

    private volatile boolean dragging = false;
    private volatile int lastDragX = 0;
    private volatile int lastDragY = 0;
    private volatile int lastUpY = 0;
    private volatile int lastUpX = 0;

    float genCounter;
    private final float genSpeedStart = 1.1f;
    float genSpeed = genSpeedStart;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;


    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();

        batch = new SpriteBatch();
       /* img = new Texture("healBar.png");*/
        img = new Texture("sword2-removebg-preview.png");
        sprite = new Sprite(img);

        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);

        pixmap = new Pixmap(64, 64, Pixmap.Format.RGB888);

        //instance

        image3 = new Texture("Tekraroyna.png");
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

        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        Gdx.input.setInputProcessor(this);
        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("robotobold.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.GOLD;
        parameter.size = 60;
        parameter.characters = "0123456789 XCMBGAMEOVERLYPCScrecutoplay .;+:<)(>+-*#";
        font = freeTypeFontGenerator.generateFont(parameter);

    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sprite.translateX(-1f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            sprite.translateX(1f);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            sprite.setPosition(Gdx.input.getX(),(Gdx.input.getY()-1050)*(-1));

            System.out.println(Gdx.input.getX()+" --"+(Gdx.input.getY()-100)*(-1));

        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();






        double newTime = TimeUtils.millis() / 1000.0;//real time

        double frameTime = Math.min(newTime - currentTime, 0.3);
        float deltaTime = (float) frameTime;

        currentTime = newTime;
        //konum -- background size
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        music.play();
        music.setVolume(10f);
        music.setLooping(true);
        //  drawer.line(0, 0, 100, 100);


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
                //texture,konum
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

            boolean holdlives = false;
            Array<Fruit> tofruits = new Array<Fruit>();
            for (Fruit fruit2 : fruitArray) {
                if (fruit2.outofScreen()) {
                    tofruits.add(fruit2);
                    if (fruit2.living && fruit2.type == Fruit.Type.REGULAR) {
                        lives--;
                        holdlives = true;
                        break;
                    }
                    if (fruit2.living && fruit2.type == Fruit.Type.GREENAPPLE) {
                        lives--;
                        break;
                    }
                    if (fruit2.living && fruit2.type == Fruit.Type.EXTRA) {
                        lives--;
                        break;
                    }
                }
            }

            if (holdlives) {
                for (Fruit f : fruitArray) {
                    f.living = false;
                }
            }
            for (Fruit f : tofruits) {

                fruitArray.removeValue(f, true);
            }
        }
        if (lives > 0) {
            font.draw(batch, " + SCORE:" + (score - 1), 20, 60);
            font.draw(batch, " + COMBO :+" + scoreTouch, Gdx.graphics.getWidth() * 0.81f, 60);
            batch.draw(sprite, sprite.getX(), sprite.getY());
        }
        //   if (lives == 0) {
        if (score == 0) {
            batch.draw(image, Gdx.graphics.getWidth() * 0.37f, Gdx.graphics.getHeight() * 0.5f);

        }
        if (score > 1 && lives <= 0) {
            batch.draw(image3, Gdx.graphics.getWidth() * 0.37f, Gdx.graphics.getHeight() * 0.45f);//tekrar oyna btonu gelecek
            font.draw(batch, "  + GAME OVER +", Gdx.graphics.getWidth() * 0.365f, Gdx.graphics.getHeight() * 0.40f);//game over image
            font.draw(batch, " $ SCORE: " + (score - 1), Gdx.graphics.getWidth() * 0.38f, Gdx.graphics.getHeight() * 0.32f);

        }


        batch.end();
    }


    private void addItem() {

        float pos = random.nextFloat() * (Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        //Konum ve Hız
        Fruit item = new Fruit(new Vector2(pos * 0.4f, -Fruit.radius), new Vector2((Gdx.graphics.getWidth() * 0.2f) * (random.nextFloat() - 0.1f), (Gdx.graphics.getHeight() * 0.65f)));
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
        shapeRenderer.dispose();
        img.dispose();


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
        dragging = true;
        lastDragX = screenX;
        lastDragY = screenY;
        System.out.println(screenX + " Down ---- " + screenY);
        if (lives <= 0 && currentTime - gameOverTime > 2f) {

            //menu mod
            gameOverTime = 0f;
            lives = 4;
            score = 0;
            genSpeed = genSpeedStart;
            fruitArray.clear();


        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        lastUpX = screenX;
        lastUpY = screenY;
        dragging = false;
        if (score == 0) {
            score = 1;
        }

        System.out.println(screenX + "Up  ---- " + screenY);

        return false;
    }

    //dokunduğunda ne olacak
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        /*   font.draw(batch, "---",  screenX, screenY);
         */
        if (dragging) {
            int deltaX = screenX - lastDragX;
            if (deltaX < 0) {
                deltaX *= -1;
            }
            int deltaY = screenY - lastDragY;
            if (deltaY < 0) {
                deltaY *= -1;
            }
            lastDragX = screenX;
            lastDragY = screenY;

            if (deltaX > Gdx.graphics.getWidth() * 0.035f || deltaY > Gdx.graphics.getHeight() * 0.020f) {
                System.out.println(screenX + "Move  ---- " + screenY);
                int plusScore = 0;
                Array<Fruit> toRemove = new Array<Fruit>();
                Vector2 pos = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
                for (Fruit f : fruitArray) {

                    if (f.clicked(pos)) {
                        toRemove.add(f);


                        switch (f.type) {
                            case REGULAR:
                                plusScore++;
                                scoreTouch = 1;
                                break;
                            case ENEMY:
                                lives--;
                                break;
                            case EXTRA:
                                plusScore += 2;
                                scoreTouch = 4;
                                break;
                            case LIFE:
                                lives++;
                                scoreTouch = 1;
                                break;
                            case GREENAPPLE:
                                plusScore += 1;
                                scoreTouch = 1;
                                break;


                        }

                    }
                }

                score += plusScore * plusScore;


                for (Fruit f : toRemove) {
                    fruitArray.removeValue(f, true);
                }
            }
        }


        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        sprite.setPosition(screenX,Gdx.graphics.getHeight()-screenY);

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
