import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class UDPW  implements Callable<DatagramPacket> {
    protected DatagramSocket socket = null;
    private boolean onElection;
    private byte[] buf = new byte[512];

    //PI: 129.3.20.26
    //Wolf: 129.3.20.36
    //Rho: 129.3.20.24

    public UDPW(int port, boolean onElection) throws SocketException {
        socket = new DatagramSocket(port);
        this.onElection = onElection;
    }


    @Override
    public DatagramPacket call() {
        //I think we need to include a switch statement to manage when we want to listen vs when we want to send
        if (onElection) {
            try {
                byte[] voteForMe = "Vote For Me".getBytes(StandardCharsets.UTF_8);

                try {
                    DatagramPacket startElection = new DatagramPacket(voteForMe, voteForMe.length, InetAddress.getByName("129.3.20.36"), 2811);
                    socket.send(startElection);
                    DatagramPacket RecElection = new DatagramPacket(new byte[6], 6);
                    socket.receive(RecElection);
                } catch (UnknownHostException u) {
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

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            //regular packets

        }
        return null;
    }


    //public boolean setCommand(String expectedValue, String newValue) {
    //    if(!command.equals(expectedValue)) {
    //        return false;
    //    }
    //    command = newValue;
    //    return true;
    //}
}
