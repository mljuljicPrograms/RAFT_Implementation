import com.sun.jdi.ThreadReference;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;

public class UDPService extends Thread {
    protected DatagramSocket socket = null;
    protected BufferedReader inStream = null; //not to sure about this
    private byte[] buf = new byte[512];
    private String command;


    //PI: 129.3.20.26
    //Wolf: 129.3.20.36
    //Rho: 129.3.20.24

    public UDPService(int port, String input) throws SocketException {
        socket = new DatagramSocket(port);
        command = input;
    }




    @Override
    public void run() {
        //I think we need to include a switch statement to manage when we want to listen vs when we want to send
        System.out.println(command);
        if(command.equals("l")){
            try {
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
                System.out.println("listening " + socket.toString());
                socket.receive(inPacket);
                InetAddress address = inPacket.getAddress();
                int port = inPacket.getPort();

                /* Packet design
                 *
                 *
                 * */
                //blocked and waiting
                //figure out what packets were designing

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("socket open");
            //socket.close();
            System.out.println("socket closed");
        }
    }

    public boolean setCommand(String expectedValue, String newValue) {
        if(!command.equals(expectedValue)) {
            return false;
        }
        command = newValue;
        return true;
    }
}
