package com.mygdx.pbServer;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

//CONNECTIONTHREAD: A thread to handle interactions between a single client and the
//server
public class ConnectionThread implements Runnable{
    protected Socket s;
    protected ObjectInputStream i;
    protected ObjectOutputStream o;
    private ParkingLot shineman;
    private ParkingLot sheldon;

    //Constructer takes a socket from a client that has been accepted by the server
    //socket and splits it into an object input and object output stream
    public ConnectionThread(Socket client, ParkingLot shinem, ParkingLot sheld){
        shineman = shinem;
        sheldon = sheld;
	s = client;
        try {
            //always initialize streams in this order
            o = new ObjectOutputStream(s.getOutputStream());
            o.flush();
            i = new ObjectInputStream(s.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
	System.out.printf("Connection thread for %s made successfully!\n", s.getInetAddress().getHostName());
    }
        @Override
	public void run(){
            send("Welcome!");
	    String message = "";
	    do{
		try {
		    //waits for messages from the client
		    message = (String) i.readObject();
                    //TODO: handle commands from client
                    String [] command = message.split(" ");
                    //command[0] = id
                    //command[1] = lot name
                    //command[2] = command
                    //command[3+] = data
                   if(command[0].equals("chk")){
                        //Sends spaces table to client
                        if(command[1].equals("Shineman")){
                            this.sendSpaces(shineman);
                        }else{
                            this.sendSpaces(sheldon);
                        }
                    }else if(!message.equals("Testing")){
                        if(command[2].equals("fil")){
                            //fill the space
                            if(command[1].equals("Shineman")){
                                shineman.fill(command[0]);
                            }else{
                                sheldon.fill(command[0]);
                            }
                            System.out.printf("Filled space %s in lot %s\n", command[0],command[1]);
                        }else if(command[2].equals("obs")){
                            //obstruct the space
                            if(command[1].equals("Shineman")){
                                shineman.obstruct(command[0]);
                            }else{
                                sheldon.obstruct(command[0]);
                            }
                            System.out.printf("Obstructed space %s in lot %s\n", command[0],command[1]);
                        }else if(command[2].equals("opn")){
                            //open the space
                            if(command[1].equals("Shineman")){
                                shineman.open(command[0]);
                            }else{
                                sheldon.open(command[0]);
                            }
                            System.out.printf("Opened space %s in lot %s\n", command[0],command[1]);
                        }else if(command[2].equals("tim")){
                            //timed fill the space
                            long h = Long.parseLong(command[3]);
                            long m = Long.parseLong(command[4]);
                            long s = Long.parseLong(command[5]);
                            if(command[1].equals("Shineman")){
                                shineman.fillTimed(command[0],h,m,s);
                            }else{
                                sheldon.fillTimed(command[0],h,m,s);
                            }
                            System.out.printf("Timed fill on space %s in lot %s\n", command[0],command[1]);
                            System.out.printf("\t Timer set for %d:%d:%d\n",h,m,s);
                        }
                    }
		    this.send("Message recieved.");
		    //System.out.println(message);
		} catch (IOException ex) {
		    ex.printStackTrace();
		} catch (ClassNotFoundException ex){
                    ex.printStackTrace();
                }

	    }while(!message.equals("exit"));

            System.out.println(s.getInetAddress().getHostName() + " disconnected.");
	    try {
		//closes all streams on exit
		i.close();
		o.close();
		s.close();
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }

	}
        
    public void sendSpaces(ParkingLot pl){
        try {
            String [] spaces = new String[pl.spaces.size()];
            Gson gson = new Gson();
            Hashtable<String,ParkingSpace> table = pl.spaces;
            Set<String> keys = table.keySet();
            int i = 0;
            for(Iterator it = keys.iterator(); it.hasNext(); i++){
                spaces[i] = gson.toJson(table.get((String)it.next()));
            }
            o.writeObject(spaces);
            o.flush();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }
    //SEND: sends a message to the client
    public void send(String message){
	try {
	    o.writeObject(message);
	    o.flush();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }
}
