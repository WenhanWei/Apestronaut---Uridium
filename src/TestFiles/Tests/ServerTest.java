package Tests;

import com.mysql.cj.xdevapi.Client;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class ServerTest {
    private Socket connect;
    private Socket file;
    private Socket Client1;
    private Socket Client2;
    private Socket Client3;
    private Socket Client4;
    private DataInputStream dis1;
    private DataOutputStream dos1;
    private DataInputStream dis2;
    private DataOutputStream dos2;
    private DataInputStream dis3;
    private DataOutputStream dos3;
    private DataInputStream dis4;
    private DataOutputStream dos4;
    private DataInputStream file_in;
    private DataOutputStream file_out;


    @Test
    void run() {
        try {
            connect = new Socket("localhost",5555);
            assertNotNull(connect);
            connect.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void main() {
    }

    @Test
    void start() {
    }

    @Test
    void sendFile() {
        try {
            file = new Socket("localhost",5555);
            file_in= new DataInputStream(file.getInputStream());
            file_out = new DataOutputStream(file.getOutputStream());
            file_out.writeUTF("0.5");
            String answer = file_in.readUTF();
            if(answer.equals("true")){
                System.out.println("Receiving Update");
                Files.copy(file_in, Paths.get("test.zip"), StandardCopyOption.REPLACE_EXISTING);
            }
            assertTrue(Files.exists(Paths.get("test.zip")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void view_players() {
        try {
            Client1 = new Socket("localhost",5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if(answer.equals("false")) {
                dos1.writeUTF("Ready 4");
                dos1.writeUTF("Testing");
                String map = dis1.readUTF();
                assertEquals("Entered Queue 4 Players", dis1.readUTF());
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                int socket_index = dis1.readInt();
                int usernames_size = dis1.readInt();
                assertEquals("Testing", dis1.readUTF());
            }
                Client2 = new Socket("localhost",5555);
                dis2 = new DataInputStream(Client2.getInputStream());
                dos2 = new DataOutputStream(Client2.getOutputStream());
                dos2.writeUTF("1.0");
                String answer2 = dis2.readUTF();
                if(answer2.equals("false")) {
                    dos2.writeUTF("View 4");
                    int user_size = dis2.readInt();
                    String username = dis2.readUTF();
                    assertEquals(username,"Testing");
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Test
    void add_4Players() {
        try {
            Client1 = new Socket("localhost",5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if(answer.equals("false")){
                dos1.writeUTF("Ready 4");
                dos1.writeUTF("Testing 1 4");
                String map = dis1.readUTF();
                assertEquals("Entered Queue 4 Players",dis1.readUTF());
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                int socket_index = dis1.readInt();
                int usernames_size = dis1.readInt();
                assertEquals("Testing 1 4",dis1.readUTF());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void remove_4Player() {
        try {
            Client1 = new Socket("localhost",5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if(answer.equals("false")){
                dos1.writeUTF("Remove 4");
                dos1.writeInt(1);
                dos1.writeUTF("Testing 1 4");
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                String result = dis1.readUTF();
                assertEquals("Done",result);

//                int players_active = dis1.readInt();
//                int usernames_size = dis1.readInt();
//                assertEquals("Testing 1",dis1.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void remove_3Player() {
        try {
            Client1 = new Socket("localhost",5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if(answer.equals("false")){
                dos1.writeUTF("Remove 3");
                dos1.writeInt(1);
                dos1.writeUTF("Testing T 3");
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                String result = dis1.readUTF();
                assertEquals("Done",result);

//                int players_active = dis1.readInt();
//                int usernames_size = dis1.readInt();
//                assertEquals("Testing 1",dis1.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void remove_2Player() {
        try {
            Client1 = new Socket("localhost",5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if(answer.equals("false")){
                dos1.writeUTF("Remove 2");
                dos1.writeInt(1);
                dos1.writeUTF("Testing T 2");
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                String result = dis1.readUTF();
                assertEquals("Done",result);

//                int players_active = dis1.readInt();
//                int usernames_size = dis1.readInt();
//                assertEquals("Testing 1",dis1.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void serve_4Players() {
        try {
            Client1 = new Socket("localhost",5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if(answer.equals("false")){
                dos1.writeUTF("Ready 4");
                dos1.writeUTF("Testing 1 4");
                String map = dis1.readUTF();
                assertEquals("Entered Queue 4 Players",dis1.readUTF());
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                int socket_index = dis1.readInt();
                int usernames_size = dis1.readInt();
                assertEquals("Testing 1 4",dis1.readUTF());
            }
            Client2 = new Socket("localhost",5555);
            dis2 = new DataInputStream(Client2.getInputStream());
            dos2 = new DataOutputStream(Client2.getOutputStream());
            dos2.writeUTF("1.0");
            String answer2 = dis2.readUTF();
            if(answer2.equals("false")){
                dos2.writeUTF("Ready 4");
                dos2.writeUTF("Testing 2 4");
                String map = dis2.readUTF();
                assertEquals("Entered Queue 4 Players",dis2.readUTF());
                dos2.writeInt(4);
                dos2.writeInt(5);
                dos2.writeInt(6);
                int socket_index = dis2.readInt();
                int usernames_size = dis2.readInt();
                assertEquals("Testing 1 4",dis2.readUTF());
                assertEquals("Testing 2 4",dis2.readUTF());
            }
            Client3 = new Socket("localhost",5555);
            dis3 = new DataInputStream(Client3.getInputStream());
            dos3 = new DataOutputStream(Client3.getOutputStream());
            dos3.writeUTF("1.0");
            String answer3 = dis3.readUTF();
            if(answer3.equals("false")){
                dos3.writeUTF("Ready 4");
                dos3.writeUTF("Testing 3 4");
                String map = dis3.readUTF();
                assertEquals("Entered Queue 4 Players",dis3.readUTF());
                dos3.writeInt(7);
                dos3.writeInt(8);
                dos3.writeInt(9);
                int socket_index = dis3.readInt();
                int usernames_size = dis3.readInt();
                assertEquals("Testing 1 4",dis3.readUTF());
                assertEquals("Testing 2 4",dis3.readUTF());
                assertEquals("Testing 3 4",dis3.readUTF());
            }
            Client4 = new Socket("localhost",5555);
            dis4 = new DataInputStream(Client4.getInputStream());
            dos4 = new DataOutputStream(Client4.getOutputStream());
            dos4.writeUTF("1.0");
            String answer4 = dis4.readUTF();
            if(answer4.equals("false")){
                dos4.writeUTF("Ready 4");
                dos4.writeUTF("Testing 4 4");
                String map = dis4.readUTF();
                assertEquals("Entered Queue 4 Players",dis4.readUTF());
                dos4.writeInt(10);
                dos4.writeInt(11);
                dos4.writeInt(12);
                int socket_index = dis4.readInt();
                int usernames_size = dis4.readInt();
                assertEquals("Testing 1 4",dis4.readUTF());
                assertEquals("Testing 2 4",dis4.readUTF());
                assertEquals("Testing 3 4",dis4.readUTF());
                assertEquals("Testing 4 4",dis4.readUTF());
            }
            String[] ip_1 = new String[4];
            int[] ports_1  = new int[4];
            String[] ip_2 = new String[4];
            int[] ports_2 = new int[4];
            String[] ip_3 = new String[4];
            int[] ports_3 = new int[4];
            String[] ip_4 = new String[4];
            int[] ports_4 = new int[4];
            ip_1[0]=dis1.readUTF();
            ports_1[0] = dis1.readInt();

            ip_2[0]=dis2.readUTF();
            ports_2[0]=dis2.readInt();

            ip_3[0] = dis3.readUTF();
            ports_3[0] = dis3.readInt();

            ip_4[0]=dis4.readUTF();
            ports_4[0]=dis4.readInt();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void serve_3Players() {
        try {
            Client1 = new Socket("localhost", 5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if (answer.equals("false")) {
                dos1.writeUTF("Ready 3");
                dos1.writeUTF("Testing 1 3");
                String map = dis1.readUTF();
                assertEquals("Entered Queue 3 Players", dis1.readUTF());
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                int socket_index = dis1.readInt();
                int usernames_size = dis1.readInt();
                assertEquals("Testing 1 3", dis1.readUTF());
            }
            Client2 = new Socket("localhost", 5555);
            dis2 = new DataInputStream(Client2.getInputStream());
            dos2 = new DataOutputStream(Client2.getOutputStream());
            dos2.writeUTF("1.0");
            String answer2 = dis2.readUTF();
            if (answer2.equals("false")) {
                dos2.writeUTF("Ready 3");
                dos2.writeUTF("Testing 2 3");
                String map = dis2.readUTF();
                assertEquals("Entered Queue 3 Players", dis2.readUTF());
                dos2.writeInt(4);
                dos2.writeInt(5);
                dos2.writeInt(6);
                int socket_index = dis2.readInt();
                int usernames_size = dis2.readInt();
                assertEquals("Testing 1 3", dis2.readUTF());
                assertEquals("Testing 2 3", dis2.readUTF());
            }
            Client3 = new Socket("localhost", 5555);
            dis3 = new DataInputStream(Client3.getInputStream());
            dos3 = new DataOutputStream(Client3.getOutputStream());
            dos3.writeUTF("1.0");
            String answer3 = dis3.readUTF();
            if (answer3.equals("false")) {
                dos3.writeUTF("Ready 3");
                dos3.writeUTF("Testing 3 3");
                String map = dis3.readUTF();
                assertEquals("Entered Queue 3 Players", dis3.readUTF());
                dos3.writeInt(7);
                dos3.writeInt(8);
                dos3.writeInt(9);
                int socket_index = dis3.readInt();
                int usernames_size = dis3.readInt();
                assertEquals("Testing 1 3", dis3.readUTF());
                assertEquals("Testing 2 3", dis3.readUTF());
                assertEquals("Testing 3 3", dis3.readUTF());
            }
            String[] ip_1 = new String[4];
            int[] ports_1  = new int[4];
            String[] ip_2 = new String[4];
            int[] ports_2 = new int[4];
            String[] ip_3 = new String[4];
            int[] ports_3 = new int[4];
            ip_1[0]=dis1.readUTF();
            ports_1[0] = dis1.readInt();

            ip_2[0]=dis2.readUTF();
            ports_2[0]=dis2.readInt();

            ip_3[0] = dis3.readUTF();
            ports_3[0] = dis3.readInt();

        }catch (Exception e){

        }
    }

    @Test
    void add_3Players() {
        try {
            Client1 = new Socket("localhost",5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if(answer.equals("false")){
                dos1.writeUTF("Ready 3");
                dos1.writeUTF("Testing T 3");
                String map = dis1.readUTF();
                assertEquals("Entered Queue 3 Players",dis1.readUTF());
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                int players_active = dis1.readInt();
                int usernames_size = dis1.readInt();
                assertEquals("Testing T 3",dis1.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void add_2Players() {
        try {
            Client1 = new Socket("localhost",5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if(answer.equals("false")){
                dos1.writeUTF("Ready 2");
                dos1.writeUTF("Testing T 2");
                String map = dis1.readUTF();
                assertEquals("Entered Queue 2 Players",dis1.readUTF());
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                int players_active = dis1.readInt();
                int usernames_size = dis1.readInt();
                assertEquals("Testing T 2",dis1.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void serve_2Players() {
        try {
            Client1 = new Socket("localhost", 5555);
            dis1 = new DataInputStream(Client1.getInputStream());
            dos1 = new DataOutputStream(Client1.getOutputStream());
            dos1.writeUTF("1.0");
            String answer = dis1.readUTF();
            if (answer.equals("false")) {
                dos1.writeUTF("Ready 2");
                dos1.writeUTF("Testing 1 2");
                String map = dis1.readUTF();
                assertEquals("Entered Queue 2 Players", dis1.readUTF());
                dos1.writeInt(1);
                dos1.writeInt(2);
                dos1.writeInt(3);
                int socket_index = dis1.readInt();
                int usernames_size = dis1.readInt();
                assertEquals("Testing 1 2", dis1.readUTF());
            }
            Client2 = new Socket("localhost", 5555);
            dis2 = new DataInputStream(Client2.getInputStream());
            dos2 = new DataOutputStream(Client2.getOutputStream());
            dos2.writeUTF("1.0");
            String answer2 = dis2.readUTF();
            if (answer2.equals("false")) {
                dos2.writeUTF("Ready 2");
                dos2.writeUTF("Testing 2 2");
                String map = dis2.readUTF();
                assertEquals("Entered Queue 2 Players", dis2.readUTF());
                dos2.writeInt(4);
                dos2.writeInt(5);
                dos2.writeInt(6);
                int socket_index = dis2.readInt();
                int usernames_size = dis2.readInt();
                assertEquals("Testing 1 2", dis2.readUTF());
                assertEquals("Testing 2 2", dis2.readUTF());
            }

            String[] ip_1 = new String[4];
            int[] ports_1  = new int[4];
            String[] ip_2 = new String[4];
            int[] ports_2 = new int[4];
            ip_1[0]=dis1.readUTF();
            ports_1[0] = dis1.readInt();

            ip_2[0]=dis2.readUTF();
            ports_2[0]=dis2.readInt();

        }catch (Exception e){

        }
    }
}