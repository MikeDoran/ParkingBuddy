/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.pbServer;

import java.time.LocalDateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Smith
 */
public class ParkingSpaceTest {
    
    public ParkingSpaceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getId method, of class ParkingSpace.
     */
    @Test
    public void testGetId() {
        
    }

    /**
     * Test of obstruct method, of class ParkingSpace.
     */
    @Test
    public void testObstruct() {
    }

    /**
     * Test of fill method, of class ParkingSpace.
     */
    @Test
    public void testFill() {
    }

    /**
     * Test of fillFor method, of class ParkingSpace.
     */
    @Test
    public void testFillFor() {
        System.out.println("testFillFor");
        ParkingSpace space = new ParkingSpace("1");
        LocalDateTime timeTest = LocalDateTime.now();
        space.fillFor(0, 0, 0);
        assertEquals(timeTest,space.getTimeToOpen());
        assertEquals(space.isTimerOn(), true);
        timeTest = timeTest.plusHours(4);
        timeTest = timeTest.plusMinutes(25);
        timeTest = timeTest.plusSeconds(17);
        space.fillFor(4, 25, 17);
        assertEquals(timeTest,space.getTimeToOpen());
        System.out.println("timeTest equals timeToOPen");
    }

    /**
     * Test of open method, of class ParkingSpace.
     */
    @Test
    public void testOpen() {
        System.out.println("testOpen");
        ParkingSpace space = new ParkingSpace("1");
        space.fill();
        space.obstruct();
        space.setOnTimer(true);
        space.open();
        assertEquals(false, space.isFilled());
        assertEquals(false, space.isObstructed());
        assertEquals(false, space.isTimerOn());
    }

    /**
     * Test of isTaken method, of class ParkingSpace.
     */
    @Test
    public void testIsTaken() {
        System.out.println("testIsTaken");
        ParkingSpace space = new ParkingSpace("1");
        space.fill();
        space.obstruct();
        assertEquals(true,space.isTaken());
        space.open();
        space.obstruct();
        assertEquals(true,space.isTaken());
        space.open();
        assertEquals(false,space.isTaken());
        //space.fill();
        space.fillFor(-3, 0, 0);
        LocalDateTime timeToOpen = space.getTimeToOpen();
        assertEquals(false, space.isTaken());
        
    }
    
}
