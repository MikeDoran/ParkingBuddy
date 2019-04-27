/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.pbServer;

import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author emmabrunell
 */
public class ParkingLotTest {

    public ParkingLotTest() {
    }

    /**
     * Test of isFull method, of class ParkingLot.
     */
    @Test
    public void testIsFull() {
        System.out.println("isFull");
        String[] ids = {"1", "2", "3"};
        boolean expResult = false;
        ParkingLot pl = new ParkingLot(ids);
        if (pl.getSpace(ids[0]) != null && pl.getSpace(ids[1]) != null && pl.getSpace(ids[2]) != null) {
            expResult = true;
        }
        boolean result = pl.isFull();
        assertEquals(expResult, result);
    }

    /**
     * Test of obstruct method, of class ParkingLot.
     */
    @Test
    public void testObstruct() {
        System.out.println("obstruct");
        String[] ids = {"1", "2", "3"};
        ParkingLot pl = new ParkingLot(ids);
        pl.obstruct("2");
        if (pl.getSpace("2") != null) {
            assertEquals(pl.getSpace("2").isTaken(), true);
        }
    }

    /**
     * Test of fill method, of class ParkingLot.
     */
    @Test
    public void testFill() {
        System.out.println("fill");
        String[] ids = {"1", "2", "3"};
        ParkingLot pl = new ParkingLot(ids);
        pl.fill("2");
        if (pl.getSpace("2") != null) {
            assertEquals(pl.getSpace("2").isTaken(), true);
        }
    }

    /**
     * Test of fillTimed method, of class ParkingLot.
     */
    @Test
    public void testFillTimed() {
        System.out.println("fillTimed");
        String[] ids = {"1", "2", "3"};
        ParkingLot pl = new ParkingLot(ids);
        pl.fillTimed("1", 0, 0, 0);
        if (pl.getSpace("1") != null) {
            assertEquals(pl.getSpace("1").isTaken(), false);
        }
    }
    
    /**
     * Test of open method, of class ParkingLot.
     */
    @Test
    public void testOpen() {
        System.out.println("open");
        String[] ids = {"1", "2", "3"};
        ParkingLot pl = new ParkingLot(ids);
        pl.fill("3");
        pl.open("3");
        if (pl.getSpace("3") != null) {
            assertEquals(pl.getSpace("3").isTaken(), false);
        }
    }
    
    /**
     * Test of getSpace method, of class ParkingLot. 
     */
    @Test 
    public void testGetSpace(){
        System.out.println("getSpace");
        String[] ids = {"1", "2", "3"};
        ParkingLot pl = new ParkingLot(ids);
        if (pl.getSpace("1") != null) {
            assertEquals(pl.getSpace("1"),"1");
        }
    }
}
