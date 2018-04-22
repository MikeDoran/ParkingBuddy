package com.mygdx.pbServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

//MULTITHREADSERVER: A multi-threaded server to accept client connections
public class MultiThreadServer {
    public static void main(String args[]){
        
        
        String [] shinemanSpaces = new String[230];
        for(int i = 0; i < 230; i++){
            shinemanSpaces[i] = String.valueOf(i);
        }
        String [] sheldonSpaces = new String[66];
        for(int i = 1; i <= 66; i++){
            sheldonSpaces[i-1] = String.valueOf(i);
        }
        ParkingLot shineman = new ParkingLot(shinemanSpaces);
        ParkingLot sheldon = new ParkingLot(sheldonSpaces);
        
	//Displays local server information
        int port;
        if(args.length == 0){
            try {
                InetAddress address = InetAddress.getLocalHost();
                System.out.println("IP address: " + address.getHostAddress());
                System.out.println("Host name : " + address.getHostName());
            } catch (UnknownHostException uhEx) {
                System.out.println("Local adress unknown");
            }
            //specifies which port to run on
            Scanner kb = new Scanner(System.in);
            System.out.println("Port to run on?");
            port = kb.nextInt();
        }else{
            port = Integer.parseInt(args[0]);
        }
	ServerSocket s;
        try {
            s = new ServerSocket();
            s.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), port));
            for(;;) {
                //waits for a connection and then passes the socket to a new connection
                //thread
                Socket client = s.accept();
                System.out.println(client.getInetAddress().getHostName() + " just connected.");
                new Thread(new ConnectionThread(client,shineman,sheldon)).start();
                //TODO: add connection threads to a collection of some sort?
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}