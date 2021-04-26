import java.net.SocketException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException, SocketException {
        //Random r = new Random();
        //int randTime = r.nextInt(5) + 1;
        //System.out.println(randTime);
        //Raft testingRaft = new Raft();
        Raft2 testingRaft2 = new Raft2();
        testingRaft2.startElectionTimer(5);
        //testingRaft.initRaft();
        //while(randTime > 0){
        //    synchronized (Thread.currentThread()) {
        //        Thread.currentThread().wait(1000);
        //    }
        //    randTime--;
        //}
        //testingRaft.startElection();
        //CustomTimer timer = new CustomTimer(10);
        //timer.startTimer();


    }
}
