import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    static final ExecutorService listeners = Executors.newFixedThreadPool(2);
    static final ExecutorService followers = Executors.newFixedThreadPool(2);
    private List<Future<DatagramPacket>> futures = new ArrayList<>();
    private int myVotes;
    private Random r = new Random();
    private int randTime;
    //Things I think we are going to use (Matt L)
    //private boolean didVote = false;

    public Raft(int time) throws SocketException {
        wolfL = new UDPL(2811, (time * 1000));
        rhoL = new UDPL(2812,(time * 1000));
        randTime = r.nextInt(time) + 1;

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

        }catch (SocketException s){
            s.printStackTrace();
        }


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
        //This might be an error
        //listeners.submit();
        //listeners.submit();



        System.out.println("Election Started");
        //1. Check if we already voted. If so stop here. -> IF WE CALLED THIS METHOD, WE DID NOT VOTE!
        //2. vote for yourself

        //3. Send out requests for votes and


        System.out.println("finished");

        //signals election


        //3. tell everyone there is an election process and you voted with listeners in the run command of UDPService
        //4. block any incoming votes
    }

    public void leader(){


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

