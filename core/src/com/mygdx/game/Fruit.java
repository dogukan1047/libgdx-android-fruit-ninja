package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Fruit {

    public static float radius = 60f;

    public enum Type {
        REGULAR, EXTRA, ENEMY, LIFE,GREENAPPLE
    }

    Type type;
    Vector2 pos;
    Vector2 velocity;
    public boolean living=true;

    //pozisyon and hız
    Fruit(Vector2 pos, Vector2 velocity) {
        this.pos = pos;
        this.velocity = velocity;
        type = Type.REGULAR;


    }
//kullanıcı tıklanma yerine en yakın yer bulan fonk
    public boolean clicked(Vector2 clicked){
        if(pos.dst2(clicked)<=radius*radius+1){
            return true;
        }
        return  false;
    }

    public final Vector2 getPos(){
        return pos;
    }

    public boolean outofScreen(){

        return(pos.y< -2f*radius);
    }

    public void update(float dt){

        velocity.y -= dt*(Gdx.graphics.getHeight()*0.2f);
        velocity.x -= dt*Math.signum(velocity.x)*3f;
        pos.mulAdd(velocity,dt);



    }

}
