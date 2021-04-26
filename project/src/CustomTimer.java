import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomTimer extends Thread{

    private AtomicBoolean timerIsRunning;
    private AtomicInteger amount;
    public CustomTimer(int amount){
        this.amount = new AtomicInteger(amount);
        timerIsRunning = new AtomicBoolean(false);
        startTimer();
    }

    public void startTimer(){
        run(); 
    }

    public void stopTimer(){
        while (Thread.currentThread().isAlive()){
            timerIsRunning.compareAndExchange(true, false);
            Thread.currentThread().stop();
        }
    }

    //public void resetTimer(int newTime) {

    //}

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        long end = start + amount.get()* 1000L;
        timerIsRunning.compareAndExchange(false, true);
        while (System.currentTimeMillis() < end){
            //stuck here
        }
        System.out.println(System.currentTimeMillis()/ 1000);
        stopTimer();
    }

    public boolean timerIsAlive() {
        return timerIsRunning.get();
    }
}
