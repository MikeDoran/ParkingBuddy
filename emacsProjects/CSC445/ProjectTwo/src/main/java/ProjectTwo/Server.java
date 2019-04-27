package ProjectTwo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Server {

    public static final int PORT = 2727;
    public static final int TIMEOUT = 5000;
    public static final int RETRIES = 5;

    public static boolean slidingWindow = false;
    public static String filename = "";

    public static void main(String [] args) {

        try {
            System.out.println("Host: " + InetAddress.getLocalHost().getHostName());
            System.out.println("Host IP: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("Port: " + PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(PORT);
            //socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        for(;;){
            DatagramPacket ack = null;
            try { ack = getRequest(socket); }
            catch (IOException e) { e.printStackTrace(); }
            System.out.print("Performing data transfer...");

            if(slidingWindow){
                //TODO: implement sliding window
            }else{
                try {
                    FileOutputStream fos = new FileOutputStream(filename);
                    boolean endOfTransfer = false;
                    while(!endOfTransfer) {
                        DatagramPacket p = getNext(ack,socket);
                        if(p.getData().length < 516) endOfTransfer = true;
                        else ack = makeAck(p);
                        byte[] data = Arrays.copyOfRange(p.getData(), 4, p.getLength());
                        fos.write(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Done!");
        }
    }

    //GETREQUEST: Awaits a WRQ packet from the client, ignoring TIMEOUT and blocking until one is received.
    //Once the WRQ packet is received it is processed, and the appropriate ACK packet is constructed and returned.
    public static DatagramPacket getRequest(DatagramSocket s) throws IOException {

        System.out.println("Awaiting request...");

        s.setSoTimeout(0);
        DatagramPacket rq = new DatagramPacket(new byte [512], 512);
        s.receive(rq);
        s.setSoTimeout(TIMEOUT);

        System.out.println("Write request received!");

        ByteBuffer data = ByteBuffer.wrap(rq.getData());
        int optcode = data.getChar();
        char current = ' ';
        String temp = "";
        while (current != '\0') {
            current = data.getChar();
            temp += current;
        }
        filename = temp;
        System.out.println("FILE: " + filename);

        String mode = "";
        current = ' ';
        while (current != '\0') {
            current = data.getChar();
            mode += current;
        }
        System.out.println("MODE: " + mode);

        String opt = "";
        current = ' ';
        while (current != '\0') {
            current = data.getChar();
            opt += current;
        }
        slidingWindow = opt.equals("win");
        System.out.println("SLIDING WINDOW? " + slidingWindow);
        return makeAck(rq);
    }

    //MAKEACK: produces an apropriate ack packet for a given syn
    //                     2 bytes     2 bytes
    //                     ---------------------
    //                    | Opcode |   Block #  |
    //                     ---------------------
    //                          ACK packet
    public static DatagramPacket makeAck(DatagramPacket syn){
        //Get block number for ack
        ByteBuffer synBuff = ByteBuffer.wrap(syn.getData());
        char synOpt = synBuff.getChar();
        char blockNum = (synOpt == 3)? synBuff.getChar(): (char)0;

        //assemble ack
        byte [] contents = new byte [4];
        ByteBuffer ackBuff = ByteBuffer.wrap(contents);
        char optcode = (char) 4;
        ackBuff.putChar(optcode);
        ackBuff.putChar(blockNum);

        return new DatagramPacket(contents, contents.length, syn.getAddress(), syn.getPort());
    }

    //GETNEXT: Sends and ack and awaits the next data packet from the server. If the message
    //isn't received after RETRIES number of retries then returns null. else returns the data
    //packet.
    public static DatagramPacket getNext(DatagramPacket ack, DatagramSocket s) throws IOException {
        boolean received = false;
        DatagramPacket message = new DatagramPacket(new byte[516], 516);
        for(int i = 0; i < RETRIES; i++){
            s.send(ack);
            try {
                s.receive(message);
                received = true;
            }catch(SocketTimeoutException ste){
                received = false;
            }finally {
                if(received) break;
            }
        }
        if(received) return message;
        else return null;
    }
}
