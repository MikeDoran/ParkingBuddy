/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pbapp.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Andrew Smith
 */
public class ParkingSpaceButton extends Button {
    private String identifier;
    private float tilt;
    private Vector2 xConstraints;
    private Vector2 yConstraints;
    //private Vector2 position;
    
    // p is position on map
    // d is dimension
    public ParkingSpaceButton(String id, Vector2 p, Vector2 d) {
        super(p, d);
        identifier = id;
        //position = p;
    }
    
    public ParkingSpaceButton(String id, Vector2 p, Vector2 d, float t) {
        super(p, d);
        identifier = id;
        tilt = t;
        //position = p;
    }
    
    public ParkingSpaceButton(String id, Vector2 p, Vector2 d, float t, String test, int xHigh, int xLow, int yHigh, int yLow){
        super(test,p,d);
        identifier = id;
        tilt = t;
        xConstraints = new Vector2(xHigh, xLow);
        yConstraints = new Vector2(yHigh,yLow);
        //position = p;
    }
    
    public ParkingSpaceButton(String id, Vector2 p, Vector2 d, float t, String test){
        super(test,p,d);
        identifier = id;
        tilt = t;
        //position = p;
    }
    
    public String getIdentifier(){
        return identifier;
    }
    
    public float getTilt(){
        return tilt;
    }
    
    public void update(int x, int y){
       Vector2 deltapos = new Vector2(x,y);
       Vector2 position = super.getPos();
//       if(position.x > xConstraints.x) position.x = xConstraints.x;
//       if(position.x < xConstraints.y) position.x = xConstraints.y;
//       if((PBApp.height - position.y) > yConstraints.x) position.y = (PBApp.height - yConstraints.x);
//       if((PBApp.height - position.y) < yConstraints.y) position.y = (PBApp.height - yConstraints.y);
       super.setPos(position.add(deltapos));
       
    }
    

}
