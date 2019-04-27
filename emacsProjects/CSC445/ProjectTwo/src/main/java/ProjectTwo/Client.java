package ProjectTwo;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class Client {

    public static final int PORT = 2727;
    public static final int TIMEOUT = 5000;
    public static final int RETRIES = 5;
    public static void main(String [] args){

        String fileName = args[args.length-1];
        String hostName = args[0];
        System.out.println("Sending " + fileName + " to " + hostName + "...");
        boolean slidingWindow = false;
        File file = new File(fileName);


        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        InetAddress hostIP = null;
        try {
            hostIP = InetAddress.getByName(hostName);
            System.out.println("Host IP: " + hostIP.getHostAddress());
            System.out.println();
            //hostIP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        DatagramPacket wrq = createWriteRequest(fileName, slidingWindow, PORT, hostIP);
        try {
            if(getAck(wrq,socket)){
                System.out.println("Write request successful!");
                DatagramPacket [] packets = packUp(file, PORT, hostIP);
                for(DatagramPacket p : packets){
                    if(!getAck(p,socket))
                        System.err.println("Connection timed out " + RETRIES + " times.");
                }
            }else{
                System.err.println("Connection timed out " + RETRIES + " times.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //GETACK: sends the given datagram packet with the given socket and awaits an ack from the
    //server. If the ack isn't received after RETRIES number of retries then returns false. else
    //returns true.
    //TODO: test me
    private static boolean getAck(DatagramPacket msg, DatagramSocket s) throws IOException {
        boolean received = false;
        for(int i = 0; i < RETRIES; i++){
            s.send(msg);
            try {
                s.receive(new DatagramPacket(new byte[4], 4));
                received = true;
            }catch(SocketTimeoutException ste){
                System.out.println("Timeout #" + (i+1) + "...");
                received = false;
            }finally {
                if(received) break;
            }
        }
        return received;
    }

    //CREATEWRITEREQUEST: constructs a datagram packet in the TFTP format for a WRQ (write request) with the
    //given information.
    //
    //            2 bytes     string    2 bytes   string    2 bytes  string    2 bytes
    //            --------------------------------------------------------------------
    //           | Opcode |  Filename  |   0  |    Mode    |   0  |   Option   |   0  |
    //            --------------------------------------------------------------------
    //                                       RRQ/WRQ packet
    private static DatagramPacket createWriteRequest(String filename, boolean slidingWindow, int port, InetAddress ip) {
        byte [] nameBytes = new byte[0];
        byte [] modeBytes = new byte[0];
        byte [] optnBytes = new byte[0];
        try {
            String enc = "UTF-16";
            nameBytes = filename.getBytes(enc);
            modeBytes = "octet".getBytes(enc);
            optnBytes = (slidingWindow)? "win".getBytes(enc) : "std".getBytes(enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int nameLength = nameBytes.length;
        int modeLength = modeBytes.length;
        int optnLength = optnBytes.length;
        byte [] m = new byte [8 + nameLength + modeLength + optnLength];
        ByteBuffer mb = ByteBuffer.wrap(m);
        mb.putChar((char)2);
        mb.put(nameBytes);
        mb.putChar('\0');
        mb.put(modeBytes);
        mb.putChar('\0');
        mb.put(optnBytes);
        mb.putChar('\0');
        ////Reading these server-side/////////////////
        //        String filename = "";             //
        //        char current = ' ';               //
        //        while(current != '\0'){           //
        //            current = buff.getChar();     //
        //            filename += current;          //
        //        }                                 //
        //        System.out.println(filename);     //
        //                                          //
        //        String mode = "";                 //
        //        current = ' ';                    //
        //        while(current != '\0'){           //
        //            current = buff.getChar();     //
        //            mode += current;              //
        //        }                                 //
        //        System.out.println(mode);         //
        //////////////////////////////////////////////
        return new DatagramPacket(m, m.length, ip, port);
    }

    //PACKUP: packs the binary contents of a given file into an array of datagram packets following the TFTP
    //format for a DATA packet. The block numbers will be ordered sequentially, starting with a random int.
    //
    //            2 bytes     2 bytes      n bytes
    //            ----------------------------------
    //           | Opcode |   Block #  |   Data     |
    //            ----------------------------------
    //                      DATA packet
    private static DatagramPacket [] packUp(File f, int port, InetAddress ip) throws IOException {
        byte [] fileContents = Files.readAllBytes(f.toPath());
        DatagramPacket [] packets = new DatagramPacket[(fileContents.length/512)+1];
        char blockNum = (char) (Math.random()*65535);
        for(int i = 0; i < packets.length; i++){
            byte [] data = new byte[((i+1) == packets.length)? fileContents.length % 512 : 512];
            System.arraycopy(fileContents, i*512, data, 0, data.length);

            byte [] m = new byte [4 + data.length];
            ByteBuffer buff = ByteBuffer.wrap(m);
            buff.putChar((char)3);
            buff.putChar(blockNum);
            blockNum = (char)((blockNum + 1) % 65535);
            buff.put(data);

            packets[i] = new DatagramPacket(m,m.length,ip,port);
        }
        ////Code for unpacking////////////////////////////////////////////////////////////////
        //        try {                                                                     //
        //            FileOutputStream fos = new FileOutputStream(outputPath);              //
        //            for(DatagramPacket p : packets){                                      //
        //                byte [] data = Arrays.copyOfRange(p.getData(),4, p.getLength());  //
        //                fos.write(data);                                                  //
        //            }                                                                     //
        //        } catch (IOException e) {                                                 //
        //            e.printStackTrace();                                                  //
        //        }                                                                         //
        //////////////////////////////////////////////////////////////////////////////////////
        return packets;
    }
}

