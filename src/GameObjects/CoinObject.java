package GameObjects;

import components.*;

/**
 * Coins that can be collected from the game space to add to the player's totalScore
 */

public class CoinObject extends BaseBooster {
    static final int coinSize = 30;
    static String[] coinAnimation = new String[11];

    /**
     * Creates the animated sprite file paths for a coin object
     * @return array of file paths for the images in the animation
     */
    public static String[] initialiseAnimation() {
        int j = 0;
        for (int i = 0; i < coinAnimation.length; i++) {
            j = i + 1;
            coinAnimation[i] = "src/resources/sprites/Gold/gold" + j + ".png";
        }

        return coinAnimation;
    }

    /**
     * Default constructor to create a coin object with a money value of 100
     */
    public CoinObject() {
        super(initialiseAnimation(), "Coin", coinSize);
        AddComponent(MoneyComponent.class, MoneyComponent.COMPONENT_NAME);
    }

    public CoinObject (int moneyValue) {
        super();
        MoneyComponent money = new MoneyComponent(moneyValue);
        AddComponent(money, MoneyComponent.COMPONENT_NAME);
    }

    @Override
    public boolean useBooster(GameObject gameObject) {
        int value = this.GetComponent(MoneyComponent.class).value;
        gameObject.GetComponent(PlayerController.class).currency += value;
        return true;
    }

    @Override
    public String toString() {
        return "This coin will boost your total score by " + this.GetComponent(MoneyComponent.class).value + " ! Keep collecting these coins to jump up the leaderboard.";
    }
}
