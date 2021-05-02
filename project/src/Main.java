import java.net.SocketException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException, SocketException {
        Raft testingRaft = new Raft(5);
        testingRaft.initRaft();
    }
}
