package components.Network;

import components.MPPlayerController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * @author WenhanWei
 * @date 2020/3/16
 */

public class UDPSend implements Runnable{

    private static DatagramSocket sendSocket;
    private static DatagramPacket sendPacket;
    private static ByteArrayOutputStream byteOut;
    private static ObjectOutputStream oops;
    private String opponents_ip;
    private int port;
    private Command commandToBeSend;
    public UDPSend(DatagramSocket sendSocket,String ip,int port,Command command)
    {
        this.sendSocket = sendSocket;
        this.opponents_ip = ip;
        this.port = port;
        this.commandToBeSend = command;
    }

    @Override
    public void run() {
            send_data(commandToBeSend);
    }

    public void send_data(Command data) {

        try {
            System.out.println("Player sends to port " +port);
            if(data == null) {
                data = new Command("Nothing");
            }


                byteOut = new ByteArrayOutputStream();
                oops = new ObjectOutputStream(byteOut);

                oops.writeObject(data);
                oops.reset();
                oops.flush();

                byte[] objectData = byteOut.toByteArray();

                sendPacket = new DatagramPacket(objectData, objectData.length, InetAddress.getByName(opponents_ip), port);
                if(sendPacket!=null && sendSocket!=null) {
                    sendSocket.send(sendPacket);
                }
            System.out.println("Command: "+data);

        }catch (Exception e){
            e.printStackTrace();
            if(sendSocket != null){
                sendSocket.close();
            }
            if(oops != null){
                try {
                    oops.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if(byteOut != null){
                try {
                    byteOut.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
    public void set_ip(String ip){
        this.opponents_ip =ip;
    }
    public void set_port(int port){
        this.port = port;

    }
}