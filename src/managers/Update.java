package managers;

import GameDatabase.GameAccountOperations;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Update {
    /**
     * @author Constantin Onofras
     * @date 2020/03/30 - 2020
     */
    private boolean connected = false;
    private static String gameVersion = "1.0";
    private static Socket connection;
    private final static String serverIP = "localhost";
    private static int serverPort=5555;
    private static ArrayList<Integer> ports;
    public static int position;
    private static final String file_output = "Update.zip";
    public static ArrayList<String> ip;
    public static int players;
    public static int active_players;
    public static String map;
    private static DataInputStream is;
    private static DataOutputStream os;
    public static ArrayList<String> usernames;
    public int socketIndex;
    public Update(int players){
        this.players = players;
        this.ip = new ArrayList<>();
        this.ports = new ArrayList<>();
        this.usernames =new ArrayList<>();
    }

    /**
     * This class handles the communication for the client with the server
     */
    public Update(){}

    /**
     * This method generates some random ports for our client to use for the game
     */
    public void GeneratePorts(){
        if(!MenuController.ports_generated) {
            Random r = new Random();
                ports.add(0,r.nextInt(65535));     //Generating random port for the clients
                while (ports.get(0) <= 1024) {
                    ports.add(0,r.nextInt(65535));
                }
//            if (players >= 3) {
                    ports.add(1,r.nextInt(65535));     //Generating random port for the clients
                    while (ports.get(1) <= 1024) {
                        ports.add(1,r.nextInt(65535));
                    }
//            }

//            if (players == 4) {
                    ports.add(2,r.nextInt(65535));     //Generating random port for the clients
                    while (ports.get(2) <= 1024) {
                        ports.add(2,r.nextInt(65535));
                    }
//            }
            MenuController.ports = ports;
            MenuController.ports_generated = true;
        }
        else{
            ports = MenuController.ports;
        }
    }

    /**
     * This class returns the ip's of the other players
     * @return the ip's of the other players as a string
     */
    public ArrayList<String> get_ips(){
        ArrayList<String> hold = ip;
        ip = new ArrayList<>();
        return hold;
    }

    /**
     * This class returns the ports for the other players
     * @return the ports that the other players are listening on
     */
    public ArrayList<Integer> get_ports(){
        ArrayList<Integer> hold = ports;
        ports = new ArrayList<>();
        return hold;
    }

    /**
     * This class establishes the connection with the server
     */
    public void Connect_to_Server(){
        try{
            connection = new Socket(serverIP,serverPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class checks if the connection was made
     * @return a boolean representing the answer
     */
    public boolean is_connected(){
        if(connection != null){
            return true;
        }
        else return false;
    }

    /**
     * This class checks if the client needs an update in order to play multiplayer accordingly
     * @return a boolean
     */
    public boolean Check_Update(){
        try {
            if(connection.isConnected()){
            is = new DataInputStream(connection.getInputStream());
            os = new DataOutputStream(connection.getOutputStream());
            os.writeUTF(gameVersion);
            String answer =is.readUTF();

            if(answer.equals("true")){
                System.out.println("Updating");
                Receive_Update(is);
                os.close();
                is.close();
                connection.close();
                Deploy_Update();
                Delete_Update_Zip();
                System.out.println("Done :)");
                return true;
            }
            else {
//                    System.out.println("You're up to date");
            }
            }else{
                System.out.println("Not connected");
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

    /**
     * This class add's the client to the right queue for their game
     */
    public void JoinQueue(){
        try {
            if(!connected){
            if(players == 4) {
                os.writeUTF("Ready 4");
                join4();
                connected = true;
            }
            if(players == 3){
                os.writeUTF("Ready 3");
                join3();
                connected = true;
            }
            if(players == 2){
                os.writeUTF("Ready 2");
                System.out.println("Joining 2p");
                join2();
                connected = true;
            }
            }
        }catch(EOFException e){
            System.out.println("We are sorry but we're waiting for another client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class add's the player on the 2 players queue on the server
     */
    public void join2(){
        try {
            map = is.readUTF();
            os.writeUTF(GameAccountOperations.playerUsername);
            String reply = is.readUTF();
            System.out.println("map: "+map);
            if(reply.equals("Entered Queue 2 Players")) {
                os.writeInt(ports.get(0));
                socketIndex = is.readInt();
                System.out.println("Index pos: " + socketIndex);
                active_players = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This class add's the player on the 4 players queue on the server
     */
    public void join4(){
        try {
            map=is.readUTF();
            os.writeUTF(GameAccountOperations.playerUsername);
            String reply = is.readUTF();
            if(reply.equals("Entered Queue 4 Players")) {
                os.writeInt(ports.get(0));   //Check if needed
                os.writeInt(ports.get(1));
                os.writeInt(ports.get(2));

                socketIndex = is.readInt();
                System.out.println("Index pos: " + socketIndex);
                active_players = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This class retrieves the ip addresses from the server for the 4 player mode
     */
    public void get4Players(){
        do{
            try {

                ip.add(0,is.readUTF());
                ports.add(3,is.readInt());
                ip.add(1,is.readUTF());
                ports.add(4,is.readInt());
                ip.add(2,is.readUTF());
                ports.add(5,is.readInt());
            }catch(EOFException e){
                System.out.println("We are sorry but we're waiting for another client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (ip.get(0).equals("NULL") || ip.get(1).equals("NULL") || ip.get(2).equals("NULL"));
//        System.out.println("1: " +ip.get(0) + " Port 1: " + ports.get(1) +" 2: "+ip.get(1) + " Port 2: " + ports.get(2) + " 3: "+ ip.get(2) + " Ports: " + ports.get(3) + " Received 4");
    }

    /**
     * This class closes the socket that the client uses to communicate with the server
     */
    public void Close(){
        try {
            connection.close();
            connection = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class handles a back request from a client
     */
    public void Back(){
        try {
            connection.close();
            Connect_to_Server();
            Check_Update();
            os.writeUTF("Remove "+players);
            os.writeInt(socketIndex);
            os.writeUTF(GameAccountOperations.playerUsername);
            if(players == 4) {
                os.writeInt(ports.get(0));
                os.writeInt(ports.get(1));
                os.writeInt(ports.get(2));
            }
            else if(players == 3){
                os.writeInt(ports.get(0));
                os.writeInt(ports.get(1));
            }
            else if(players == 2){
                os.writeInt(ports.get(0));
            }
            String result = is.readUTF();
            if(result.equals("Done")){
                System.out.println("Should delete");
                usernames.remove((socketIndex-1));
                socketIndex=0;
                System.out.println("Index Socket :" +  socketIndex);
                System.out.println(usernames.toString());
            }

        }catch (Exception e){

        }
    }

    /**
     * This class updates the state of the queue
     * @return an arraylist containing the username for the other players in the queue
     */
    public ArrayList<String> Update_State(){
        try {
            try {
                if (connection.isConnected()) {
                    os.writeUTF("View " + players);
                    active_players = is.readInt();
                    if(usernames.size() > active_players){
                        usernames = new ArrayList<>();
                    }
                    if (active_players == 1) {
//                        System.out.println("Assigning the first name");
                        String hold = is.readUTF();
                        if(!usernames.contains(hold)) {
                            usernames.add(0, hold);
                        }
                        if (GameAccountOperations.playerUsername == usernames.get(0)) {
                            position = active_players;
                        }
//                        System.out.println("Update 1: " + usernames.get(0));
                        return usernames;
                    }
                    else if (active_players == 2) {
                        String hold = is.readUTF();
                        if(!usernames.contains(hold)) {
                            usernames.add(0, hold);
                        }
                        String hold2 = is.readUTF();
                        if(!usernames.contains(hold2)) {
                            usernames.add(1, hold2);
                        }
                        for (int i = 0; i < 2; i++) {
                            if (GameAccountOperations.playerUsername == usernames.get(i)) {
                                position = i;
                            }
                        }
                        return usernames;
                    }
                    else if (active_players == 3) {
                        String hold = is.readUTF();
                        if(!usernames.contains(hold)) {
                            usernames.add(0, hold);
                        }
                        String hold2 = is.readUTF();
                        if(!usernames.contains(hold2)) {
                            usernames.add(1, hold2);
                        }
                        String hold3 = is.readUTF();
                        if(!usernames.contains(hold3)) {
                            usernames.add(2, hold3);
                        }
                        for (int i = 0; i < 3; i++) {
                            if (GameAccountOperations.playerUsername == usernames.get(i)) {
                                position = i;
                            }
                        }
                        return usernames;
                    }
                    else if (active_players == 4) {
                        String hold = is.readUTF();
                        if(!usernames.contains(hold)) {
                            usernames.add(0, hold);
                        }
                        String hold2 = is.readUTF();
                        if(!usernames.contains(hold2)) {
                            usernames.add(1, hold2);
                        }
                        String hold3 = is.readUTF();
                        if(!usernames.contains(hold3)) {
                            usernames.add(2, hold3);
                        }
                        String hold4 = is.readUTF();
                        if(!usernames.contains(hold4)) {
                            usernames.add(3, hold4);
                        }
                        for (int i = 0; i < 4; i++) {
                            if (GameAccountOperations.playerUsername == usernames.get(i)) {
                                position = i;
                            }
                        }
                        return usernames;
                    }
                    else if(active_players == 0){
                        usernames = new ArrayList<>();
                        return usernames;
                    }
                }
            } catch (SocketException e) {

            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This class add's the player on the 3 players queue on the server
     */
    public void join3(){
        try {
            map=is.readUTF();
            os.writeUTF(GameAccountOperations.playerUsername);
            String reply = is.readUTF();
            if(reply.equals("Entered Queue 3 Players")) {
                os.writeInt(ports.get(0));
                os.writeInt(ports.get(1));
                socketIndex = is.readInt();
                System.out.println("Index pos: " + socketIndex);
                active_players = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This class retrieves to ip addresses for the 3 players game mode
     */
    public void get3Players(){
        do{
            try {
                ip.add(0,is.readUTF());
                ports.add(3,is.readInt());
                ip.add(1,is.readUTF());
                ports.add(4,is.readInt());
//                ip.add(2,is.readUTF());
//                ports.add(5,is.readInt());
            }catch(EOFException e){
                System.out.println("We are sorry but we're waiting for another client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (ip.get(0).equals("NULL") || ip.get(1).equals("NULL"));
//        System.out.println("1: " + ip[0] +" 2: "+ip[1] + " Received IP 3");
    }

    /**
     * This class retrieves the ip address for the 2 players mod
     */
    public void get2Players(){
        do{
            try {
                ip.add(0,is.readUTF());
                ports.add(3,is.readInt());
//                System.out.println("OUT");
            }catch(EOFException e){
                System.out.println("We are sorry but we're waiting for another client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (ip.get(0).equals("NULL"));
//        System.out.println("1 :" + ip[0] + " Received IP 2  PORT : " + ports[1]);

    }

    /**
     * This class handles the receive of the update file
     * @param dis the data input stream that the update will be received from
     */
    public void Receive_Update(DataInputStream dis){
        try {
            System.out.println("Update");
            if(connection.isConnected()) {
                Files.copy(dis, Paths.get(file_output), StandardCopyOption.REPLACE_EXISTING);
            }else{
                System.out.println("Please connect to receive the update");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This class extracts an Update.zip file and replaces the exiting files with the files from it
     */
    public void Deploy_Update(){
        File downloaded = new File(file_output);
        if(downloaded.exists()) {
            String dest = System.getProperty("user.dir");
            System.out.println(dest);
            try {
                ZipInputStream zip = new ZipInputStream(new FileInputStream(file_output));
                ZipEntry entry = zip.getNextEntry();
                while (entry != null) {
                    String filePath = dest + File.separator + entry.getName();
                    if (!entry.isDirectory()) {
                        File f = new File(filePath);
                        f.delete();
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f, false));
                        byte[] bytesIn = new byte[4096];
                        int read = 0;
                        while ((read = zip.read(bytesIn)) != -1) {
                            bos.write(bytesIn, 0, read);
                        }
                        bos.close();
                    } else {
                        File dir = new File(entry.getName());
                        System.out.println("Created : " + entry.getName());
                        dir.mkdir();
                    }
                    zip.closeEntry();
                    entry = zip.getNextEntry();
                }
                zip.close();

            } catch (IOException e) {

            }
        }
    }
    public static void Clear_Names(){
        usernames = new ArrayList<>();
    }

    /**
     * This function deletes the Update.zip file if it exists
     */
    public void Delete_Update_Zip(){
        File delete = new File(file_output);
        if(delete.exists()) {
            delete.delete();
        }
    }
}
