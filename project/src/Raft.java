import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Raft {
    private Timer timer;
    private AtomicInteger timeLeft;

    public Raft() {

    }


    public void initRaft() {
        //start random timer
        Random r = new Random();
        int randTime = r.nextInt(30) + 1;
        initTimer(randTime);

        //Start Listening on two separate ports
        //(i think we need another class here to extend thread matt-l) UDP service created for this reason
        UDPService udpService = new UDPService();
        udpService.start();

        //[SENDER ID |"PIECE NAME" | ORIGIN LOCATION | NEW LOCATION]

    }

    public void initTimer(int secs) {
        timeLeft = new AtomicInteger(secs);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int tl = timeLeft.decrementAndGet();
                if (tl == 0) {
                    //timer runs out
                    //1. vote for yourself
                    //2. interrupt listeners
                    //3. tell everyone there is an election process and you voted with listeners
                    //4. block any incoming votes


                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

}

