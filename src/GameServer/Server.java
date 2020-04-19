package GameServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

public class Server extends Thread{
    /**
     * @author Constantin Onofras
     * @date 2020/03/30
     */

    private String gameVersion = "1.0";
    ServerSocket server;
    static ArrayList<DataInputStream> Clients_Connected2p = new ArrayList<>();
    static ArrayList<DataOutputStream> Clients_Connected2 = new ArrayList<>();
    static ArrayList<Socket> Clients_Connected2s = new ArrayList<>();
    static ArrayList<Integer> Clients_Ports2 = new ArrayList<>();
    static Server Instance;
    static String[] maps = new String[]{"level1m","level2m","level3m","level4m","level5m"};
    static ArrayList<DataInputStream> Clients_Connected3p = new ArrayList<>();
    static ArrayList<DataOutputStream> Clients_Connected3 = new ArrayList<>();
    static ArrayList<Socket> Clients_Connected3s = new ArrayList<>();
    static ArrayList<Integer> Clients_Ports3 = new ArrayList<>();
    static ArrayList<DataInputStream> Clients_Connected4p = new ArrayList<>();
    static ArrayList<DataOutputStream> Clients_Connected4 = new ArrayList<>();
    static ArrayList<Socket> Clients_Connected4s = new ArrayList<>();
    static ArrayList<Integer> Clients_Ports4= new ArrayList<>();
    static ArrayList<String>usernames2p = new ArrayList<>();
    static ArrayList<String>usernames3p= new ArrayList<>();
    static ArrayList<String>username4p = new ArrayList<>();
    private String selected_map2;
    private String selected_map3;
    private String selected_map4;

    /**
     * This function implements the main flow of events between the client and the server
     */
    @Override
    public void run() {
        selected_map2 = maps[new Random().nextInt(maps.length)];
        selected_map3 = maps[new Random().nextInt(maps.length)];
        selected_map4 = maps[new Random().nextInt(maps.length)];
        while(true) {

            try {
                Socket client = server.accept();
                System.out.println("ACCEPTED: "+ client.getInetAddress().getHostAddress());
                client.setSoTimeout(100000);
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                String game = in.readUTF();
                if(gameVersion.equals(game)) {
                    out.writeUTF("false");
                    String command = in.readUTF();
                    if(command.startsWith("View")){
                        view_players(command, in, out, client);
                    }
                    System.out.println("Command: " + command);
                    if (command.equals("Ready 2")) {
                        Add_2Players(in, out, client);
                    }
                    if(command.equals("Remove 2")){
                        System.out.println("Removing 2 Player");
                        Remove_2Players(in,out,client);
                    }
                    if (Clients_Connected2s.size() == 2) {
                        Serve_2Players();
                    }
                    if (command.equals("Ready 3")) {
                        Add_3Players(in, out, client);
                    }
                    if (command.equals("Remove 3")){
                        System.out.println("Removing 3 Player");
                        Remove_3Players(in,out,client);
                    }
                    if (Clients_Connected3s.size() == 3) {
                        Serve_3Players();
                    }
                    if (command.equals("Ready 4")) {
                        Add_4Players(in, out, client);
                    }
                    if(command.equals("Remove 4")){
                        System.out.println("Removing 4 Player");
                        Remove_4Players(in,out,client);
                    }
                    if (Clients_Connected4s.size() == 4) {
                        Serve_4Players();
                    }
                }else{
                    System.out.println("Pushing update.");
                    out.writeUTF("true");
                    sendFile(out);
                    in.close();
                    out.close();
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void main(String[] args){
        Instance = new Server();
        Instance.start();
        Instance.run();
    }

    /**
     * This function starts the server and allocates a specific port for the server's runtime
     */
    @Override
    public synchronized void start() {
        try {
            server = new ServerSocket(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function is used to push an update to the client (usually DLC's if available)
     * @param dos This is the DataOutputStream used to send the file
     */
    public static void sendFile(DataOutputStream dos){

        FileInputStream fis = null;
        try {
            File file = new File("C:\\Users\\onofr\\Desktop\\uridium\\src\\Update.zip");
            System.out.println(file.length());
            fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            while (fis.read(buffer) > 0) {
                dos.write(buffer);
            }

            fis.close();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This class sends the players from the queue to a potential player and then closes the connection to not waste resources
     * @param command This commands is used to see what queue the potential player is interested in
     * @param in This is the DataInputStream used to receive data from the potential player
     * @param out This is the DataOutputStream used to send data to the potential player
     * @param client This is the potential player's socket
     */
    public void view_players(String command,DataInputStream in,DataOutputStream out,Socket client){
        String players = command.split(" ")[1];
        System.out.println("Viewing " + players + " players");
        try {

        if(players.equals("2")){
            try {
                System.out.println("Trying to send data, username size: "+usernames2p.size());
                out.writeInt(usernames2p.size());
                for (int i = 0; i < usernames2p.size(); i++) {
                        out.writeUTF(usernames2p.get(i));
                }
                client.close();
            }catch (Exception e){

            }
        }
        else if(players.equals("3")){
            try {
                out.writeInt(usernames3p.size());
                for (int i = 0; i < usernames3p.size(); i++) {
                        out.writeUTF(usernames3p.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(players.equals("4")){
            try {
                out.writeInt(username4p.size());
                for (int i = 0; i < username4p.size(); i++) {
                    System.out.println(username4p.get(i));
                        out.writeUTF(username4p.get(i));
                }
                System.out.println("Finished Viewing");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        }catch (Exception e){

        }
    }

    /**
     * This class exchanges the IP address for 4 players
     */
    public void Serve_4Players(){



            DataOutputStream hold_1o = Clients_Connected4.get(0);
            DataOutputStream hold_2o = Clients_Connected4.get(1);
            DataOutputStream hold_3o = Clients_Connected4.get(2);
            DataOutputStream hold_4o = Clients_Connected4.get(3);
            Socket hold_1s = Clients_Connected4s.get(0);
            Socket hold_2s = Clients_Connected4s.get(1);
            Socket hold_3s = Clients_Connected4s.get(2);
            Socket hold_4s = Clients_Connected4s.get(3);
            try {

                System.out.println("Serving 4");
                hold_1o.writeUTF(hold_2s.getInetAddress().getHostAddress());
                hold_1o.writeInt(Clients_Ports4.get(3));
                hold_1o.writeUTF(hold_3s.getInetAddress().getHostAddress());
                hold_1o.writeInt(Clients_Ports4.get(6));
                hold_1o.writeUTF(hold_4s.getInetAddress().getHostAddress());
                hold_1o.writeInt(Clients_Ports4.get(9));
                hold_2o.writeUTF(hold_1s.getInetAddress().getHostAddress());
                hold_2o.writeInt(Clients_Ports4.get(0));
                hold_2o.writeUTF(hold_3s.getInetAddress().getHostAddress());
                hold_2o.writeInt(Clients_Ports4.get(7));
                hold_2o.writeUTF(hold_4s.getInetAddress().getHostAddress());
                hold_2o.writeInt(Clients_Ports4.get(10));
                hold_3o.writeUTF(hold_1s.getInetAddress().getHostAddress());
                hold_3o.writeInt(Clients_Ports4.get(1));
                hold_3o.writeUTF(hold_2s.getInetAddress().getHostAddress());
                hold_3o.writeInt(Clients_Ports4.get(4));
                hold_3o.writeUTF(hold_4s.getInetAddress().getHostAddress());
                hold_3o.writeInt(Clients_Ports4.get(11));
                hold_4o.writeUTF(hold_1s.getInetAddress().getHostAddress());
                hold_4o.writeInt(Clients_Ports4.get(2));
                hold_4o.writeUTF(hold_2s.getInetAddress().getHostAddress());
                hold_4o.writeInt(Clients_Ports4.get(5));
                hold_4o.writeUTF(hold_3s.getInetAddress().getHostAddress());
                hold_4o.writeInt(Clients_Ports4.get(8));
                Clients_Connected4 = new ArrayList<>();
                Clients_Connected4s = new ArrayList<>();
                Clients_Connected4p = new ArrayList<>();
                username4p = new ArrayList<>();
                Clients_Ports4 = new ArrayList<>();
            }catch (Exception e){

            }

    }

    /**
     * This class handles a add request from a client for the 4 player queue
     * @param in the input stream of that client
     * @param out the output stream of that client
     * @param client the socket of that client
     */
    public void Add_4Players(DataInputStream in,DataOutputStream out,Socket client){
        try {
            if(!Clients_Connected4s.contains(client)) {
                out.writeUTF(selected_map4);
                String username = in.readUTF();
                if(!username4p.contains(username)) {
                    out.writeUTF("Entered Queue 4 Players");
                    username4p.add(username);
                    int port = in.readInt();
                    System.out.println("Port : " + port);
                    Clients_Ports4.add(new Integer(port));
                    port = in.readInt();
                    System.out.println("Port : " + port);
                    Clients_Ports4.add(new Integer(port));
                    port = in.readInt();
                    System.out.println("Port : " + port);
                    Clients_Ports4.add(new Integer(port));


                    System.out.println(client.getInetAddress().getHostAddress());
                    Clients_Connected4s.add(client);
                    Clients_Connected4p.add(in);
                    Clients_Connected4.add(out);
                    out.writeInt(Clients_Connected4s.size());
                    System.out.println("Active players: " + Clients_Connected4s.size());
                    for (int i = 0; i < Clients_Connected4.size(); i++) {
                        System.out.println("Size : " + username4p.size());
                        try {
                            Clients_Connected4.get(i).writeInt(username4p.size());
                            for (int j = 0; j < username4p.size(); j++) {

                                Clients_Connected4.get(i).writeUTF(username4p.get(j));
                                System.out.println("Sending: " + username4p.get(j));
                            }
                        } catch (Exception e) {
                            Clients_Connected4s.remove(i);
                            Clients_Connected4p.remove(i);
                            Clients_Connected4.remove(i);
                            username4p.remove(i);
                        }

                    }
                }
                else{
                    out.writeUTF("Alredy in");
                }
            }else {
                client.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * This class handles a remove request from a client that's in a  4 player queue
     * @param in the input stream of that client
     * @param out the output stream of that client
     * @param client the socket of that client
     */
    public void Remove_4Players(DataInputStream in,DataOutputStream out,Socket client){
        try {
            int index = in.readInt();
            String username = in.readUTF();
            if(username4p.contains(username)){
                int port = in.readInt();
                if(Clients_Ports4.remove(new Integer(port))){
                    System.out.println("Removed port");
                }
                port = in.readInt();
                if(Clients_Ports4.remove(new Integer(port))){
                    System.out.println("Removed port");
                }
                port = in.readInt();
                if(Clients_Ports4.remove(new Integer(port))){
                    System.out.println("Removed port");
                }if((index-1) < 0){
                    Clients_Connected4 = new ArrayList<>();
                    Clients_Connected4s = new ArrayList<>();
                    Clients_Connected4p = new ArrayList<>();
                    username4p = new ArrayList<>();
                }
                else {
                    System.out.println(Clients_Connected4s.remove((index - 1)));
                    System.out.println(Clients_Connected4p.remove((index - 1)));
                    System.out.println(Clients_Connected4.remove((index - 1)));
                    username4p.remove((index - 1));
                }
                out.writeUTF("Done");

                for (int i = 0; i < Clients_Connected4.size(); i++) {
                    String hold1 = Clients_Connected4p.get(i).readUTF();
                    System.out.println("Supposed command: " +hold1);
                    System.out.println("Size : " + username4p.size());
//                    Clients_Connected4.get(i).writeInt(Clients_Connected4s.size());
                    try {
                        Clients_Connected4.get(i).writeInt(username4p.size());
                        for (int j = 0; j < username4p.size(); j++) {

                            Clients_Connected4.get(i).writeUTF(username4p.get(j));
                            System.out.println("Sending: " + username4p.get(j));
                        }
                    } catch (Exception e) {
                        Clients_Connected4s.remove(i);
                        Clients_Connected4p.remove(i);
                        Clients_Connected4.remove(i);
                        username4p.remove(i);
                    }

                }
            }
            else{
                out.writeUTF("Failed");
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This Class exchanges the IP addresses to 3 players
     */
    public void Serve_3Players(){
        try {
            DataOutputStream hold_1o = Clients_Connected3.get(0);
            DataOutputStream hold_2o = Clients_Connected3.get(1);
            DataOutputStream hold_3o = Clients_Connected3.get(2);
            Socket hold_1s = Clients_Connected3s.get(0);
            Socket hold_2s = Clients_Connected3s.get(1);
            Socket hold_3s = Clients_Connected3s.get(2);
            try {
                    hold_1o.writeUTF(hold_2s.getInetAddress().getHostAddress());
                    hold_1o.writeInt(Clients_Ports3.get(2).intValue()); // Continue
                    hold_1o.writeUTF(hold_3s.getInetAddress().getHostAddress());
                    hold_1o.writeInt(Clients_Ports3.get(4).intValue());
                    hold_2o.writeUTF(hold_1s.getInetAddress().getHostName());
                    hold_2o.writeInt(Clients_Ports3.get(0).intValue());
                    hold_2o.writeUTF(hold_3s.getInetAddress().getHostAddress());
                    hold_2o.writeInt(Clients_Ports3.get(5).intValue());
                    hold_3o.writeUTF(hold_1s.getInetAddress().getHostAddress());
                    hold_3o.writeInt(Clients_Ports3.get(1).intValue());
                    hold_3o.writeUTF(hold_2s.getInetAddress().getHostAddress());
                    hold_3o.writeInt(Clients_Ports3.get(3).intValue());
                    Clients_Connected3=new ArrayList<>();
                    Clients_Connected3s = new ArrayList<>();
                    Clients_Connected3p = new ArrayList<>();
                    Clients_Ports3= new ArrayList<>();
                    usernames3p = new ArrayList<>();
                    selected_map3 = maps[new Random().nextInt(maps.length)];
            }catch (SocketException e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * This class handles a remove request from a client that's in a queue for a 3 players game
     * @param in the input stream of that client
     * @param out the output stream of that client
     * @param client the socket of that client
     */
    public void Remove_3Players(DataInputStream in, DataOutputStream out,Socket client){
        try {
            int index = in.readInt();
            String username = in.readUTF();
            if(usernames3p.contains(username)){
                int port = in.readInt();
                if(Clients_Ports3.remove(new Integer(port))){
                    System.out.println("Removed port");
                }
                port = in.readInt();
                if(Clients_Ports3.remove(new Integer(port))){
                    System.out.println("Removed port");
                }
                if((index-1)==0 || (index-1) < 0){
                    Clients_Connected3 = new ArrayList<>();
                    Clients_Connected3s = new ArrayList<>();
                    Clients_Connected3p = new ArrayList<>();
                    usernames3p = new ArrayList<>();
                }
                else {
                    System.out.println(Clients_Connected3s.remove((index - 1)));
                    System.out.println(Clients_Connected3p.remove((index - 1)));
                    System.out.println(Clients_Connected3.remove((index - 1)));
                    usernames3p.remove((index - 1));
                }
                out.writeUTF("Done");

                for (int i = 0; i < Clients_Connected3.size(); i++) {
                    String hold1 = Clients_Connected3p.get(i).readUTF();
                    System.out.println("Supposed command: " +hold1);
                    System.out.println("Size : " + usernames3p.size());
//                    Clients_Connected4.get(i).writeInt(Clients_Connected4s.size());
                    try {
                        Clients_Connected3.get(i).writeInt(usernames3p.size());
                        for (int j = 0; j < usernames3p.size(); j++) {

                            Clients_Connected3.get(i).writeUTF(usernames3p.get(j));
                            System.out.println("Sending: " + usernames3p.get(j));
                        }
                    } catch (Exception e) {
                        Clients_Connected3s.remove(i);
                        Clients_Connected3p.remove(i);
                        Clients_Connected3.remove(i);
                        usernames3p.remove(i);
                    }

                }
            }
            else{
                out.writeUTF("Failed");
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This class handles a add request from a client for the 3 player queue
     * @param in the input stream of that client
     * @param out the output stream of that client
     * @param client the socket of that client
     */
    public void Add_3Players(DataInputStream in,DataOutputStream out,Socket client){
        try {
            if(!Clients_Connected3s.contains(client)) {
                out.writeUTF(selected_map3);
                String username = in.readUTF();
                if(!usernames3p.contains(username)) {
                    out.writeUTF("Entered Queue 3 Players");
                    usernames3p.add(username);
                    int port = in.readInt();
                    System.out.println("Port : " + port);
                    Clients_Ports3.add(new Integer(port));
                    port = in.readInt();
                    System.out.println("Port : " + port);
                    Clients_Ports3.add(new Integer(port));


                    System.out.println(client.getInetAddress().getHostAddress());
                    Clients_Connected3s.add(client);
                    Clients_Connected3p.add(in);
                    Clients_Connected3.add(out);
                    out.writeInt(Clients_Connected3s.size());
                    System.out.println("Active players: " + Clients_Connected3s.size());
                    for (int i = 0; i < Clients_Connected3.size(); i++) {
                        System.out.println("Size : " + usernames3p.size());
                        try {
                            Clients_Connected3.get(i).writeInt(usernames3p.size());
                            for (int j = 0; j < usernames3p.size(); j++) {

                                Clients_Connected3.get(i).writeUTF(usernames3p.get(j));
                                System.out.println("Sending: " + usernames3p.get(j));
                            }
                        } catch (Exception e) {
                            Clients_Connected3s.remove(i);
                            Clients_Connected3p.remove(i);
                            Clients_Connected3.remove(i);
                            usernames3p.remove(i);
                        }

                    }
                }
                else{
                    out.writeUTF("Alredy in");
                }
            }else {
                client.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * This class handles a remove request from a client that's in a queue for a 2 players game
     * @param in the input stream of that client
     * @param out the output stream of that client
     * @param client the socket of that client
     */
    public void Remove_2Players(DataInputStream in,DataOutputStream out,Socket client){
        try {
            int index = in.readInt();
            String username = in.readUTF();
            if(usernames2p.contains(username)){
                int port = in.readInt();
                if(Clients_Ports2.remove(new Integer(port))){
                    System.out.println("Removed port");
                }
                if((index-1)==0 || (index-1) < 0){
                    Clients_Connected2 = new ArrayList<>();
                    Clients_Connected2s = new ArrayList<>();
                    Clients_Connected2p = new ArrayList<>();
                    usernames2p = new ArrayList<>();
                }
                else {
                    System.out.println(Clients_Connected2s.remove((index - 1)));
                    System.out.println(Clients_Connected2p.remove((index - 1)));
                    System.out.println(Clients_Connected2.remove((index - 1)));
                    usernames2p.remove((index - 1));
                }
                out.writeUTF("Done");

                for (int i = 0; i < Clients_Connected2.size(); i++) {
                    String hold1 = Clients_Connected2p.get(i).readUTF();
                    System.out.println("Supposed command: " +hold1);
                    System.out.println("Size : " + usernames2p.size());
//                    Clients_Connected4.get(i).writeInt(Clients_Connected4s.size());
                    try {
                        Clients_Connected2.get(i).writeInt(usernames2p.size());
                        for (int j = 0; j < usernames2p.size(); j++) {

                            Clients_Connected2.get(i).writeUTF(usernames2p.get(j));
                            System.out.println("Sending: " + usernames2p.get(j));
                        }
                    } catch (Exception e) {
                        Clients_Connected2s.remove(i);
                        Clients_Connected2p.remove(i);
                        Clients_Connected2.remove(i);
                        usernames2p.remove(i);
                    }

                }
            }
            else{
                out.writeUTF("Failed");
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * This class handles a add request from a client for the 2 player queue
     * @param in the input stream of that client
     * @param out the output stream of that client
     * @param client the socket of that client
     */
    public void Add_2Players(DataInputStream in,DataOutputStream out,Socket client){
        try {
            if(!Clients_Connected2s.contains(client)) {
                out.writeUTF(selected_map2);
                String username = in.readUTF();
                if(!usernames2p.contains(username)) {
                    out.writeUTF("Entered Queue 2 Players");
                    usernames2p.add(username);
                    int port = in.readInt();
                    System.out.println("Port : " + port);
                    Clients_Ports2.add(new Integer(port));

                    System.out.println(client.getInetAddress().getHostAddress());
                    Clients_Connected2s.add(client);
                    Clients_Connected2p.add(in);
                    Clients_Connected2.add(out);
                    out.writeInt(Clients_Connected2s.size());
                    System.out.println("Active players: " + Clients_Connected2s.size());
                    for (int i = 0; i < Clients_Connected2.size(); i++) {
                        System.out.println("Size : " + usernames2p.size());
                        try {
                            Clients_Connected2.get(i).writeInt(usernames2p.size());
                            for (int j = 0; j < usernames2p.size(); j++) {

                                Clients_Connected2.get(i).writeUTF(usernames2p.get(j));
                                System.out.println("Sending: " + usernames2p.get(j));
                            }
                        } catch (Exception e) {
                            Clients_Connected2s.remove(i);
                            Clients_Connected2p.remove(i);
                            Clients_Connected2.remove(i);
                            usernames2p.remove(i);
                        }

                    }
                }
                else{
                    out.writeUTF("Alredy in");
                }
            }else {
                client.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * This class exchanges the IP addresses for 2 players
     */
    public void Serve_2Players(){
        DataOutputStream hold_1o = Clients_Connected2.get(0);
        DataOutputStream hold_2o = Clients_Connected2.get(1);
        Socket hold_1s = Clients_Connected2s.get(0);
        Socket hold_2s = Clients_Connected2s.get(1);
        try {
            hold_1o.writeUTF(hold_2s.getInetAddress().getHostAddress());
            hold_2o.writeUTF(hold_1s.getInetAddress().getHostAddress());
            hold_1o.writeInt(Clients_Ports2.get(1).intValue());
            hold_2o.writeInt(Clients_Ports2.get(0).intValue());
            System.out.println(Clients_Ports2.get(0).intValue());
            System.out.println(Clients_Ports2.get(1).intValue());
            Clients_Connected2 = new ArrayList<>();
            Clients_Connected2s = new ArrayList<>();
            Clients_Connected2p = new ArrayList<>();
            Clients_Ports2 = new ArrayList<>();
            usernames2p = new ArrayList<>();
            selected_map2 = maps[new Random().nextInt(maps.length)];
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
