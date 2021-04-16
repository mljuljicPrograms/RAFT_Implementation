import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramSocket;

public class UDPService extends Thread {
    protected DatagramSocket socket = null;
    protected BufferedReader inStream = null;


    //PI: 129.3.20.26
    //Wolf: 129.3.20.36
    //Rho: 129.3.20.24

    public UDPService(int port){

    }


    @Override
    public void run() {
        try{

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
