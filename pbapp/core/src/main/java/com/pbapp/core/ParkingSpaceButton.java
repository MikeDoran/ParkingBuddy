/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pbapp.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Andrew Smith
 */
public class ParkingSpaceButton extends Button {
    private String identifier;
    private float tilt;
    private Vector2 position;
    private Color color;
    // p is position on map
    // d is dimension
    public ParkingSpaceButton(String id, Vector2 p, Vector2 d) {
        super(p, d);
        identifier = id;
        position = p;
        color = new Color(0, 255, 0, 0.4f);
    }
    
    public void setColor(ParkingSpace ps){
        if(ps.isTaken()){
            if(ps.isObstructed) color = new Color(1,110/255f,0,0.4f);
            else color = new Color(1,0,0,0.4f);
        }else{
            color = new Color(0, 255, 0, 0.4f);
        }
    }
    
    public Color getColor(){
        return color;
    }
    
    public ParkingSpaceButton(String id, Vector2 p, Vector2 d, float t) {
        super(p, d);
        identifier = id;
        tilt = t;
        position = p;
        color = new Color(0, 255, 0, 0.4f);
    }
    
     public ParkingSpaceButton(String id, Vector2 p, Vector2 d, float t, String test) {
        super(test,p, d);
        identifier = id;
        tilt = t;
        position = p;
        color = new Color(0, 255, 0, 0.4f);
    }
    
    public String getIdentifier(){
        return identifier;
    }
    
    public float getTilt(){
        return tilt;
    }
    
    public void update(int x, int y){
       Vector2 deltapos = new Vector2(x,y);
       position.add(deltapos);
    }
    
    public void secondUpdate(int x, int y){
        super.setXpos(super.getXpos() + x);
        super.setYpos(super.getYpos() + y);
    }

    private void Color(int i, int i0, int i1, float f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
