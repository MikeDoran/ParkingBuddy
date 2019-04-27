package com.mygdx.pbServer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

/**
 * @author Mike
 */
public class MultiThreadServerTest {
    
    String port = "2525";
    
    //Runs the server in a thread
    private class serverThread extends Thread{
        @Override
        public void run() {
            String [] args = {port};
            MultiThreadServer.main(args);
        }
    }
    
    //Starts server thread before testing
    @Before
    public void setUp() {
        serverThread sr = new serverThread();
        sr.start();
    }
    
    /**
     * Test of main method, of class MultiThreadServer.
     */
    @org.junit.Test
    public void testMain() {
        System.out.println("main");
        try{
            InetAddress address = InetAddress.getLocalHost();
            String host = address.getHostAddress();
            
            Socket s = new Socket(host,Integer.parseInt(port));
            ObjectOutputStream o = new ObjectOutputStream (s.getOutputStream());
            o.flush();
            ObjectInputStream i = new ObjectInputStream (s.getInputStream());
            
            String response = "";
            while(response.equals("")){
                response = (String)i.readObject();
            }
            assertEquals("Welcome!",response);
            
            o.writeObject("Test");
            o.flush();
            response = "";
            while(response.equals("")){
                response = (String)i.readObject();
            }
            assertEquals("Message recieved.",response);
            
            o.writeObject("exit");
            o.flush();
            response = "";
            while(response.equals("")){
                response = (String)i.readObject();
            }
            assertEquals("Message recieved.",response);
            
        }catch(EOFException eof){
            System.out.println("Connection terminated.");
        }catch(UnknownHostException ex){
            System.out.println("Unable to identify local host");
            ex.printStackTrace();
            System.exit(1);
        }catch(IOException ex){
            System.out.println("Failed to intialize socket/streams");
            ex.printStackTrace();
            System.exit(1);
        }catch(ClassNotFoundException ex){
            System.out.println("Object recieved was not string");
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
