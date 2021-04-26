import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;

public class UDPService extends Thread {
    protected DatagramSocket socket = null;
    protected BufferedReader inStream = null; //not to sure about this
    private byte[] buf = new byte[512];
    private String command;
    private CustomTimer electionTimer;


    //PI: 129.3.20.26
    //Wolf: 129.3.20.36
    //Rho: 129.3.20.24

    public UDPService(int port, String input /*CustomTimer timer*/) throws SocketException {
        socket = new DatagramSocket(port);
        command = input;
        //this.electionTimer = timer;
    }




    @Override
    public void run() {
        //I think we need to include a switch statement to manage when we want to listen vs when we want to send
        System.out.println(command);
        if(command.equals("l")){
            try {
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
                System.out.println("listening at " + socket.toString());
                //blocked and waiting
                while (!socket.isClosed()){
                    try {
                        socket.receive(inPacket);
                        break;
                    } catch (SocketException e){
                        System.out.println("done done");
                    }
                }



                //electionTimer.stopTimer();
                //need a ref to Raft bc we can set didVote to a bool
                InetAddress address = inPacket.getAddress();
                int port = inPacket.getPort();

                /* Packet design
                 * send vote back
                 * switch to constant listening
                 *
                 * */


                //figure out what packets were designing

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            System.out.println("socket open");
            try {
                socket.send(inPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
