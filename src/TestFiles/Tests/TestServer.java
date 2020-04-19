package Tests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args){
        try {
            Socket c1 = new Socket("localhost",5555);
            DataOutputStream do1 = new DataOutputStream(c1.getOutputStream());
            do1.writeUTF("1.0");
            DataInputStream d1 = new DataInputStream(c1.getInputStream());
            Socket c2 = new Socket("localhost",5555);
            DataOutputStream do2 = new DataOutputStream(c2.getOutputStream());
            do2.writeUTF("1.0");
            DataInputStream d2 = new DataInputStream(c2.getInputStream());
            String first = d1.readUTF();
            System.out.println(first + " 1");
            String second = d2.readUTF();
            System.out.println(second + " 2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
