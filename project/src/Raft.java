import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.ByteBuffer;

//ASSUME WE ARE ON PI CURRENTLY
//ASSUME WE ARE ON PI CURRENTLY
//ASSUME WE ARE ON PI CURRENTLY
//ASSUME WE ARE ON PI CURRENTLY
//ASSUME WE ARE ON PI CURRENTLY
//ASSUME WE ARE ON PI CURRENTLY

public class Raft {

    private AtomicInteger timeLeft;
    private UDPL wolfL;
    private UDPL rhoL;
    private UDPW wolfC;
    private UDPW rhoC;
    static final ExecutorService listeners = Executors.newFixedThreadPool(2);
    static final ExecutorService followers = Executors.newFixedThreadPool(2);
    static final ExecutorService commanders = Executors.newFixedThreadPool(2);
    private List<Future<DatagramPacket>> futures = new ArrayList<>();
    private int myVotes;
    private Random r = new Random();
    private int randTime;
    private InetAddress rhoAddress;
    private InetAddress wolfAddress;
    //Things I think we are going to use (Matt L)
    //private boolean didVote = false;

    public Raft(int time) throws IOException {
        randTime = r.nextInt(time) + 1;
        wolfL = new UDPL(2811, (randTime * 1000));
        rhoL = new UDPL(2812,(randTime * 1000));
        wolfAddress = InetAddress.getByName("129.3.20.36");
        rhoAddress = InetAddress.getByName("129.3.20.24");
    }
    //ASSUME WE ARE ON PI CURRENTLY
    public void initRaft() throws InterruptedException {
        //start random timer

        System.out.println(randTime);


        //Start Listening on two separate ports

        futures.add(0, listeners.submit(wolfL));
        futures.add(1, listeners.submit(rhoL));



        while (randTime > 0 || futures.get(0).isDone() || futures.get(1).isDone()) {
            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait(1000);
            }
            randTime--;
        }
        System.out.println(futures.get(0).isDone());
        System.out.println(futures.get(1).isDone());
        if (futures.get(0).isDone()) {
            try {
                //Either follower method goes off and we can leave here and go to follower
                while (!listeners.isShutdown()){
                    listeners.shutdownNow();
                }
                follower();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (futures.get(1).isDone()) {
        //Either follower method goes off and we can leave here and go to follower
            try {
                while (!listeners.isShutdown()){
                    listeners.shutdownNow();
                }
                follower();
                return;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        while (!listeners.isShutdown()) {
            System.out.println("Shutting Down");
            listeners.shutdownNow();
        }
        //listeners.submit(wolfL);

        startElection();


        //Look in to observables (https://stackoverflow.com/questions/6270132/create-a-custom-event-in-java /  https://en.wikipedia.org/wiki/Observer_pattern)
        //implement new class possibly or place above/ below this one

        //[SENDER ID |"PIECE NAME" | ORIGIN LOCATION | NEW LOCATION]

    }

    //Prerequisite- timer runs out
    public void startElection(){
        //packet Design
        //IP and address stored in UDP header

        //             [     Data      ]
        //[UDP HEADERS | "Vote For Me"| MAC Address of machine voted for] - Just for voting

        System.out.println("Election Started");
        myVotes = 1;

        wolfC = new UDPW(2813, true,"129.3.20.36");
        rhoC = new UDPW(2814, true,"129.3.20.24");
        futures.add(0, commanders.submit(wolfC));
        futures.add(1, commanders.submit(rhoC));
        System.out.println(futures.get(0).isDone());
        System.out.println(futures.get(1).isDone());

        System.out.println(futures.get(0).toString());
        System.out.println(futures.get(1).toString());
        while (!futures.get(0).isDone() || !futures.get(1).isDone()){

        }
        System.out.println("Checking futures");
        if(futures.get(0).isDone()){
            try {
                DatagramPacket vote = futures.get(0).get();
                myVotes++;
                if(myVotes > 1){
                    leader();
                }
            } catch (ExecutionException | InterruptedException weird){
                weird.printStackTrace();
            }
        }else if (!futures.get(1).isDone()){
            try {
                DatagramPacket vote2 = futures.get(1).get();
                myVotes++;
                if(myVotes > 1){
                    leader();
                }
            } catch (ExecutionException | InterruptedException weird){
                weird.printStackTrace();
            }
        }

        //signals election

        //3. tell everyone there is an election process and you voted with listeners in the run command of UDPService
        //4. block any incoming votes
    }

    public void leader(){
        wolfC = new UDPW(2813, false,"129.3.20.36");
        rhoC = new UDPW(2814, false,"129.3.20.24");
        futures.add(0, commanders.submit(wolfC));
        futures.add(1, commanders.submit(rhoC));

        while (!futures.get(0).isDone() || !futures.get(1).isDone());

    }

    public void follower(){
        //Once were here we can look in UDPL and the if will always fail
        System.out.println("following");
        futures.add(0, followers.submit(wolfL));
        futures.add(1, followers.submit(rhoL));

        System.out.println("wolf " + futures.get(0).isDone());
        System.out.println("rho " + futures.get(1).isDone());

        while (!futures.get(0).isDone() || !futures.get(1).isDone()){
        }

        int randTime = r.nextInt(5) + 1;
        while (randTime > 0) {
            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            randTime--;
        }
        startElection();

    }

}

