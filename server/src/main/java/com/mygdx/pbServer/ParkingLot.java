package com.mygdx.pbServer;

import java.util.Hashtable;
import java.util.Set;

public class ParkingLot {
    
    //A hashtable to hold all parking spaces
    public final Hashtable<String,ParkingSpace> spaces = new Hashtable();
    
    public ParkingLot(String[] ids) {
        for(String id : ids){
            spaces.put(id, new ParkingSpace(id));
        }
    }
    
    //ISFULL: returns true if the parking lot has no empty spaces
    public boolean isFull() {
        Set<String> ids = spaces.keySet();
        for(String id : ids){
            if(!spaces.get(id).isTaken()) return false;
        }
        return true;
    }
    
    //OBSTRUCT: sets a space to obstructed
    public void obstruct(String id){
        spaces.get(id).obstruct();
    }
    
    //FILL: sets a space to full
    public void fill(String id){
        spaces.get(id).fill();
    }
    
    //FILLTIMED: sets a space to full for a certain amount of time
    public void fillTimed(String id, long h, long m, long s){
        spaces.get(id).fillFor(h, m, s);
    }
    
    //OPEN: sets a space to open
    public void open(String id){
        spaces.get(id).open();
    }
    
    //GETSPACE: returns a parkingspace object of a given id
    public ParkingSpace getSpace(String id){
        if(spaces.contains(id)){
            return spaces.get(id);
        }else{
            return null;
        }
    }
}
