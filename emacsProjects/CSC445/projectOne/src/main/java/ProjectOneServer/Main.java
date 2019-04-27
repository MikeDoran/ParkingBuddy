package ProjectOne;

import java.io.*;
import java.net.*;

public class Main {
    private static final int PORT = 2525;
    public static void main(String [] args){
        //TODO: This will help -> https://stackoverflow.com/questions/49208760/how-to-send-tcp-and-udp-packet-in-java
        //Displays local server information
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("IP address: " + address.getHostAddress());
            System.out.println("Host name : " + address.getHostName());
            System.out.println("Port      : " + PORT);
        } catch (UnknownHostException uhEx) {
            uhEx.printStackTrace();
            System.exit(-1);
        }

        try {
            ServerSocket s = new ServerSocket();
            s.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), PORT));

            System.out.println();
            System.out.println("Server is awaiting client...");
            Socket c = s.accept();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            out.flush();
            System.out.println("Client socket accepted, commencing packet exchange.");
            System.out.println();

            //client and server need to agree on a number of packets
            int numPackets = in.read();
            System.out.println(numPackets + " total packets.");

            //echo those packets
            for(int i = 0; i < numPackets; i++){
                out.write(in.read());
                out.flush();
            }

            //shut down
            in.close();
            out.close();
            c.close();
            s.close();

        }catch(IOException ex){
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
