 package com.pbapp.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Mike
 */
public class Button {
    private final Vector2 position;
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
    
    public boolean wasTouchedTilted(int x, int y, float tilt){
        if(tilt == 90f){
            Boolean withinY = (y < position.y) && (y > (position.y-dimensions.y));
            Boolean withinX = (x < position.x) && (x > (position.x-dimensions.y));
            return withinY && withinX;
        }else if (tilt == 0f) {
            Boolean withinY = (y < position.y) && (y > (position.y-dimensions.y));
            Boolean withinX = (x > position.x) && (x < (position.x+dimensions.x));
            return withinY && withinX;
        } else{
            Boolean withinX = (x < position.x + 8) && (x > position.x - 8);
            if(!withinX){
                //System.out.println("not within x");
                return false;
            }
            Boolean withinY = (y < position.y) && (y > position.y - 40);
            return withinY;
                
        }
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
    
    public int getWidth(){
        return (int)dimensions.x;
    }
    
    public int getHeight(){
        return (int)dimensions.y;
    }
    
    public void setXpos(int x){
        position.x = x;
    }
    
    public void setYpos(int y){
        position.y = y;
    }
}
