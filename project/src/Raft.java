import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Raft {

    private AtomicInteger timeLeft;
    private UDPService wolf;
    private UDPService rho;
    private int myVotes;

    public Raft() {

    }

    public void initRaft() throws InterruptedException {
        //start random timer

        //CustomTimer cTimer = new CustomTimer(randTime);

        //Start Listening on two separate ports
        //(i think we need another class here to extend thread matt-l) UDP service created for this reason
        try {
            //may end up doing one thread if its simpler
            wolf = new UDPService(2811, "l");
            rho = new UDPService(2812, "l");
            wolf.start();
            rho.start();

        }catch (SocketException s){
            s.printStackTrace();
        }


        //Look in to observables (https://stackoverflow.com/questions/6270132/create-a-custom-event-in-java /  https://en.wikipedia.org/wiki/Observer_pattern)
        //implement new class possibly or place above/ below this one

        //[SENDER ID |"PIECE NAME" | ORIGIN LOCATION | NEW LOCATION]

    }

    //Prerequisite- timer runs out
    public void startElection(){
        System.out.println("election started");
        //. Check if we already voted. If so stop here.

        //. check wolf and rho for sent votes

        //1. interrupt listeners
        //2. vote for yourself
        myVotes = 1;

        boolean doneWolf = false;
        boolean doneRho = false;

        while (!doneWolf){
            System.out.println("while loop wolf");
            doneWolf = wolf.setCommand("l", "r");
        }

       while (!doneRho){
            System.out.println("while loop rho");
            doneRho = rho.setCommand("l", "r");
        }

       //get these running in parallel!
        wolf.run();
        rho.run();


        System.out.println("finished");

        //signals election


        //3. tell everyone there is an election process and you voted with listeners in the run command of UDPService
        //4. block any incoming votes
    }

}

