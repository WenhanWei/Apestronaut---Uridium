package components.Network;

import components.MPOpponentController;
import components.MPOpponentController3Players;
import components.MPOpponentController4Players;
import managers.Engine;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author WenhanWei
 * @date 2020/3/16
 */
public class UDPReceive2 implements Runnable {
    static DatagramSocket receiveSocket = null;
    static ByteArrayInputStream byteArrayInputStream;
    static ObjectInputStream objectInputStream;
    private int port;


    public UDPReceive2(DatagramSocket receiveSocket,int port){
        this.receiveSocket = receiveSocket;
        this.port = port;
    }


    @Override
    public void run() {

        byte[] buffer = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {

                receiveSocket.receive(receivePacket);

                byteArrayInputStream = new ByteArrayInputStream(buffer);

                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                MPOpponentController3Players.command = (Command) objectInputStream.readObject();
                System.out.println("Player 3 received: " +MPOpponentController3Players.command.toString());

            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }
}
