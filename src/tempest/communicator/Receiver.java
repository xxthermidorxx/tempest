package tempest.communicator;

import org.msgpack.MessagePack;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import java.util.Random;
import java.io.IOException;

public class Receiver {

    public static void main(String[] args) {

        int[] _featureVector = new int[10];

        Socket _receiverSocket = ZMQ.context(1).socket(ZMQ.REP);
        String _requestAddress = "tcp://133.11.238.73:5050";
        MessagePack _msgpack = new MessagePack();

        /** connect to client */
        _receiverSocket.connect(_requestAddress);
        byte[] recvBytes = _receiverSocket.recv(0);
        System.out.println("client - connect to "+_requestAddress);

        /** receive */
        String recvString = "";
        try {
            recvString = _msgpack.read(recvBytes, String.class);
        } catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("receiver - "+recvString);

        /** send */
        try {
            byte[] respondStringBytes = _msgpack.write("respond!!!!!");
            _receiverSocket.send(respondStringBytes, 0);
        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("Receiver Success!");
    }
}
