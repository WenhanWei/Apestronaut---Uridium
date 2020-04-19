package components.Network;

import java.io.Serializable;

/**
 * @author Constantin Onofras and Wenhan Wei
 * @date 2020/03/30 - 2020
 * Based on PlayerController.java
 */


public class Command implements Serializable {
    String command;

    /**
     * This class is used to send commands over the network
     * @param key is the command represented as a String
     */

    public Command(String key){
        command = key;
    }

    @Override
    public String toString() {
        return command;
    }
}
