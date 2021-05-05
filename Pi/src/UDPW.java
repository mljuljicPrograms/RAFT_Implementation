import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class UDPW  implements Callable<DatagramPacket> {
    protected DatagramSocket socket = null;
    private int port;
    private int sendingPort;
    private boolean onElection;
    private String address;
    private byte[] buf = new byte[512];

    //PI: 129.3.20.26
    //Wolf: 129.3.20.36
    //Rho: 129.3.20.24

    public UDPW(int port, boolean onElection, String address, int sendingPort) {
        this.port = port;
        this.sendingPort = sendingPort;
        try {
            socket = new DatagramSocket(port);
        }
        catch(SocketException s) {
            s.printStackTrace();
        }
        this.onElection = onElection;
        this.address = address;
    }


    @Override
    public DatagramPacket call() {
        //I think we need to include a switch statement to manage when we want to listen vs when we want to send
        //I think we can use a mix send and received to make the the if run like a switch - Matt L
        if (onElection) {

            byte[] voteForMe = "Vote For Me".getBytes(StandardCharsets.UTF_8);

            try {
                DatagramPacket startElection = new DatagramPacket(voteForMe, voteForMe.length, InetAddress.getByName(address), sendingPort);
                socket.send(startElection);
                DatagramPacket recElection = new DatagramPacket(new byte[6], 6);
                socket.setSoTimeout(15000);
                socket.receive(recElection);

                return recElection;
            } catch (IOException u) {
                //we start an new election
                u.printStackTrace();
                System.exit(0);
            }


                //electionTimer.stopTimer();
                //need a ref to Raft bc we can set didVote to a bool

                /* Packet design
                 * send vote back
                 * switch to constant listening
                 *
                 * */


                //figure out what packets were designing


            return null;
        }
        else {
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);

            while (true) {

                try {
                    socket.setSoTimeout(7500);
                    socket.receive(inPacket);

                } catch (IOException e) {
                    System.out.println("Sending heartbeat");
                    try {
                        DatagramPacket heartBeat = new DatagramPacket("alive".getBytes(StandardCharsets.UTF_8), "alive".getBytes(StandardCharsets.UTF_8).length, InetAddress.getByName(address), sendingPort);
                        socket.send(heartBeat);
                        socket.setSoTimeout(7500);
                        socket.receive(inPacket);
                    } catch (IOException i) {}

                }
            }
        }
    }


    //public boolean setCommand(String expectedValue, String newValue) {
    //    if(!command.equals(expectedValue)) {
    //        return false;
    //    }
    //    command = newValue;
    //    return true;
    //}
}
