import java.net.SocketException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


    public class Raft2{
        private Timer timer;
        private AtomicInteger timeLeft;
        private byte[] buf = new byte[512];
        boolean leaderExists;

        public Raft2() throws SocketException {
        }

        public void startElectionTimer(int secs) throws SocketException {
            timeLeft = new AtomicInteger(secs);
            byte[] test = new byte[512];
            DatagramSocket testSocket = new DatagramSocket();
            DatagramPacket inPacketTesting = new DatagramPacket(test, test.length);


            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    int tl = timeLeft.decrementAndGet();
                    boolean stillListing = true;
                    if (tl == 0) {
                        try {
                            startElection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        this.cancel();
                    } else if(stillListing){
                        try {
                            testSocket.setSoTimeout(950);
                            System.out.println("here");
                            testSocket.receive(inPacketTesting);
                            testSocket.setSoTimeout(999999999);
                            //System.out.println(inPacketTesting.getData());
                        } catch (IOException e) {
                            //e.printStackTrace();
                             stillListing = false;
                        }
                    }
                }
            };

            timer = new Timer();
            timer.schedule(task, 0, 1000);
        }

        public void follower(DatagramSocket leaderSocket) {
            //I think we need to include a switch statement to manage when we want to listen vs when we want to send
            try {
                leaderSocket.setSoTimeout(15000);
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
                System.out.println("listening at " + leaderSocket.toString());
                leaderSocket.receive(inPacket);

                InetAddress address = inPacket.getAddress();
                int port = inPacket.getPort();

                //send something back

            } catch (SocketException e) {
                e.printStackTrace();
                //start election here!!


            } catch (IOException c) {
                c.printStackTrace();
            }
        }

        public void leader() {

        }

        public void startElection() throws IOException {
            //Assume were on Pi//
            //PI: 129.3.20.26//
            //Wolf: 129.3.20.36
            //Rho: 129.3.20.24


            System.out.println("election started");
            timer.cancel();
            DatagramSocket socket1 = new DatagramSocket();
            DatagramSocket socket2 = new DatagramSocket();
            //May need to adjust
            InetAddress wolf = InetAddress.getByName("129.3.20.36");
            InetAddress rho = InetAddress.getByName("129.3.20.24");
            //need to re-write the buf here bf we send the packets out
            DatagramPacket outPacket1 = new DatagramPacket(buf, buf.length, wolf, 2811);
            DatagramPacket outPacket2 = new DatagramPacket(buf, buf.length, rho, 2812);//why do ports need to be explicitly written

            DatagramPacket inPacket1 = new DatagramPacket(buf, buf.length);
            DatagramPacket inPacket2 = new DatagramPacket(buf, buf.length);
            //update bool for other two servers
            socket1.send(outPacket1);
            socket2.send(outPacket2);
            //change buf to send vote request
            socket1.send(outPacket1);
            socket2.send(outPacket2);
            //check if vote request good
            socket1.setSoTimeout(3000);
            socket2.setSoTimeout(4000);
            socket1.receive(inPacket1);
            socket2.receive(inPacket2);


            //need sockets for each machine



            //3. tell everyone there is an election process and you voted with listeners in the run command of UDPService
            //4. block any incoming votes
        }
}
