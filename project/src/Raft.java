import java.net.SocketException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Raft {
    private Timer timer;
    private AtomicInteger timeLeft;
    private UDPService wolf;
    private UDPService rho;
    private int myVotes;

    public Raft() {

    }


    public void initRaft() {
        //start random timer
        Random r = new Random();
        int randTime = r.nextInt(300) + 100;
        System.out.println(randTime);

        //Start Listening on two separate ports
        //(i think we need another class here to extend thread matt-l) UDP service created for this reason
        try {
            wolf = new UDPService(2811, "l");
            rho = new UDPService(2812, "l");
            wolf.start();
            rho.start();
        }catch (SocketException s){
            s.printStackTrace();
        }
        startElection();
        //initTimer(randTime);

        //[SENDER ID |"PIECE NAME" | ORIGIN LOCATION | NEW LOCATION]

    }

    public void initTimer(int secs) {
        timeLeft = new AtomicInteger(secs);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int tl = timeLeft.decrementAndGet();
                if (tl == 0) {
                    startElection();

                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1); //changed to milliseconds

    }

    //Prerequisite- timer runs out
    public void startElection(){
        System.out.println("election started");

        //1. interrupt listeners
        wolf.interrupt();
        rho.interrupt();
        //2. vote for yourself
        myVotes = 1;

        boolean doneWolf = false;
        boolean doneRho = false;
        while (!doneWolf){
            System.out.println("while loop wolf");
            doneWolf = wolf.setCommand("l", "r");
        }
        wolf.run();

        while (!doneRho){
            System.out.println("while loop rho");
            doneRho = rho.setCommand("l", "r");
        }

        rho.run();


        System.out.println("finished");

        //signals election


        //3. tell everyone there is an election process and you voted with listeners
        //4. block any incoming votes
    }

}

