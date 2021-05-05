import java.io.IOException;
import java.net.SocketException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Raft testingRaft = new Raft(10);
        testingRaft.initRaft();
    }
}
