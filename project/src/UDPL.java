import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class UDPL  implements Callable<DatagramPacket> {
    protected DatagramSocket socket = null;
    protected BufferedReader inStream = null; //not to sure about this
    private byte[] buf = new byte[512];
    protected boolean voted = false;
    private int time;

    //PI: 129.3.20.26
    //Wolf: 129.3.20.36
    //Rho: 129.3.20.24

    public UDPL(int port, int time) throws SocketException {
        socket = new DatagramSocket(port);
        this.time = time;
    }


    @Override
    public DatagramPacket call()  {
        //I think we need to include a switch statement to manage when we want to listen vs when we want to send
        try {
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            System.out.println("listening at " + socket.toString());
            socket.setSoTimeout(time);
            socket.receive(inPacket);

            //need a ref to Raft bc we can set didVote to a bool
            InetAddress address = inPacket.getAddress();
            int port = inPacket.getPort();
            byte[] didVote = inPacket.getData();

            //[UDP HEADERS | "Vote For Me"| MAC Address of machine voted for] - Just for voting
            System.out.println(Arrays.toString(didVote).equals("Vote For Me"));
            if(Arrays.toString(didVote).equals("Vote For Me") && !voted){
                DatagramPacket outPacket = new DatagramPacket(buf, buf.length);
                outPacket.setAddress(address);
                outPacket.setPort(port);
                outPacket.setData("Voting".getBytes(StandardCharsets.UTF_8));
                socket.send(outPacket);
                voted = true;
                return outPacket;// For initial vote

            } else {
                System.out.println("GOING HERE");
                FileWriter logging = new FileWriter("log.txt", true);
                while (true) {
                    byte[] logPiece = inPacket.getData();
                    logging.write(inPacket.getAddress().toString() + ":" + inPacket.getPort() + "-->" + Arrays.toString(logPiece));
                    socket.setSoTimeout(1);
                    socket.receive(inPacket);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            //return null;
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
