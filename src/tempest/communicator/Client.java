package tempest.communicator;

import org.msgpack.MessagePack;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import java.util.Random;
import java.io.IOException;

public class Client {

    public static void main(String[] args) {

        /** initilize featureVector */
        int[] _featureVector = new int[10];
        Random rnd = new Random();
        for(int i=0; i<_featureVector.length; i++){
          _featureVector[i] = rnd.nextInt(1);
        }

        Socket _clientSocket = ZMQ.context(1).socket(ZMQ.REQ);
        String _requestAddress = "tcp://192.168.33.10:5050";
        MessagePack _msgpack = new MessagePack();

        _clientSocket.connect(_requestAddress);
        System.out.println("client - connect to "+_requestAddress);

        /** send */
        try {
            byte[] featureVectorBytes = _msgpack.write(_featureVector);
            _clientSocket.send(featureVectorBytes, 0);
        } catch(IOException e) {
            e.printStackTrace();
        }

        /** receive */
        try {
            byte[] averagedFeatureVectorBytes = _clientSocket.recv(0);
            _featureVector = _msgpack.read(averagedFeatureVectorBytes, int[].class);
        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("Client Success!");
    }
}
