package com.pbapp.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Mike
 */
public class Button {
    private Vector2 position;
    private final Vector2 dimensions;
    private final Texture image;
    
    public Button(String file, Vector2 p, Vector2 d){
        image = new Texture(file);
        position = p;
        dimensions = d;
    }
    
    public Button(Vector2 p, Vector2 d){
        image = null;
        position = p;
        dimensions = d;
    }
    
    public boolean wasTouched(int x, int y){
        Boolean withinY = (y < position.y) && (y > (position.y-dimensions.y));
        Boolean withinX = (x > position.x) && (x < (position.x+dimensions.x));
        return withinY && withinX;
    }
    
    public Texture getTexture(){
        return image;
    }
    
    public int getXpos(){
        return (int)position.x;
    }
    
    public int getYpos(){
        int realPos = PBApp.height;
        realPos -= (int)position.y;
        
        return realPos;
    }
    
    public void setPos(Vector2 p){
        position = p;
    }
    public Vector2 getPos(){
        return position;
    }
    
    public int getWidth(){
        return (int)dimensions.x;
    }
    
    public int getHeight(){
        return (int)dimensions.y;
    }
}
