package com.mygdx.pbServer;

import java.time.LocalDateTime;

public class ParkingSpace{
    
    private final String identifier;
    private Boolean isObstructed;
    private Boolean isFilled;
    private Boolean onTimer;
    private LocalDateTime timeToOpen;
    
    public ParkingSpace(String id){
        identifier = id;
        isObstructed = false;
        isFilled = false;
        onTimer = false;
        timeToOpen = LocalDateTime.now();
    }
    
    //GETID: gets the id of a parking space
    public String getId(){
        return identifier;
    }
    
    //OBSTRUCT: sets the space to obstructed
    public void obstruct(){
        isObstructed = true;
    }
    
    //FILL: sets the space to full
    public void fill(){
        isFilled = true;
    }
    
    //FILLFOR: sets the space to 'on timer' then gets the current localdatetime
    //and calculates the time the space should be open again, and stores it in
    //'timeToOpen'
    public void fillFor(long h, long m, long s){
        onTimer = true;
        LocalDateTime expiration = LocalDateTime.now();
        expiration.plusHours(h);
        expiration.plusMinutes(m);
        expiration.plusSeconds(s);
        timeToOpen = expiration;
    }
    
    //OPEN: sets the space to open
    public void open(){
        isObstructed = false;
        isFilled = false;
        onTimer = false;
    }
    
    //ISTAKEN: checks if the space is open. If it is on the timer, it checks
    //to see if the timeToOpen is before the current time. If it is, the space is
    //open and onTimer is set to false again
    public boolean isTaken(){
        if(!onTimer) return isObstructed || isFilled;
        else if(timeToOpen.isBefore(LocalDateTime.now())){
            onTimer = false;
            return false;
        }
        else return true;
    }
}
