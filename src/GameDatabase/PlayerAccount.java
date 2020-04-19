package GameDatabase;

import java.util.Date;

/**
 * @author WenhanWei
 * @date 2020/2/13
 */
public class PlayerAccount {

    private int id;
    private String username;
    private String password;
    private int singlePlayerScore;
    private int multiPlayerScore;
    private int highestLevel;
    private Date authenticate;


    public PlayerAccount() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSinglePlayerScore() {
        return singlePlayerScore;
    }

    public void setSinglePlayerScore(int singlePlayerScore) {
        this.singlePlayerScore = singlePlayerScore;
    }

    public int getMultiPlayerScore() {
        return multiPlayerScore;
    }

    public void setMultiPlayerScore(int multiPlayerScore) {
        this.multiPlayerScore = multiPlayerScore;
    }

    public int getHighestLevel() {
        return highestLevel;
    }

    public void setHighestLevel(int highestLevel) {
        this.highestLevel = highestLevel;
    }

    public Date getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(Date authenticate) {
        this.authenticate = authenticate;
    }

    @Override
    public String toString() {
        return "PlayerAccount{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", singlePlayerScore=" + singlePlayerScore +
                ", multiPlayerScore=" + multiPlayerScore +
                ", highestLevel=" + highestLevel +
                ", authenticate=" + authenticate +
                '}';
    }
}
