package tempest.communicator;

import org.msgpack.MessagePack;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import java.util.Random;
import java.io.IOException;

public class Synchronizer {

    private int[] _featureVector = new int[10];
    private Socket _clientSocket;
    private String _requestAddress;
    private MessagePack _msgpack;

    /**
     * consructor
     * @param void
     * @return void
     */
    public Synchronizer() {
        Random rnd = new Random();
        for(int i=0; i<_featureVector.length; i++){
            _featureVector[i] = rnd.nextInt(1);
        }

        _clientSocket = ZMQ.context(1).socket(ZMQ.REQ);
        _requestAddress = "tcp://192.168.33.10:5050";
        _msgpack = new MessagePack();
    }

    /**
     * reveive averagedFeatureVector from communicator
     * @param void
     * @return void
     * @throw IOException
     */
    public void receive() {
        try {
            byte[] averagedFeatureVectorBytes = _clientSocket.recv(0);
            _featureVector = _msgpack.read(averagedFeatureVectorBytes, int[].class);
        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("client - receive "+ _featureVector);
    }

    public void balance(){
    
    
    }

    /**
     * send _featureVector to communicator
     * @param void
     * @return void
     * @throw IOException
     */
    public void send() {
        _clientSocket.connect(_requestAddress);
        System.out.println("client - connect to "+_requestAddress);

        try {
            byte[] featureVectorBytes = _msgpack.write(_featureVector);
            _clientSocket.send(featureVectorBytes, 0);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
