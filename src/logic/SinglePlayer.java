package logic;

import GameObjects.*;

public class SinglePlayer extends Multiplayer {

	public SinglePlayer() {
		super();
	}

	public GameObject getCurrentPlayer() {
		GameObject current = super.getPlayers().get(0);
		return current;
	}

	public GameObject getOpponentPlayer() { // AI player
		GameObject opponent = super.getPlayers().get(1);
		return opponent;
	}

}
