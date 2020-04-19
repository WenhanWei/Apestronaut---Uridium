package logic;

import java.util.ArrayList;

import components.*;
import GameObjects.*;

public class Multiplayer extends GameObject { 
	
		private ArrayList<GameObject> players;
		public int playersTotal;
		
		GameObject playerObject = new PlayerObject();
		GameObject AIPlayerObject = new AIPlayerObject();
		
		/**
		 * A base Multiplayer GameObject, which has one player playing against an AI player
		 */
		public Multiplayer() {
			players.add(playerObject);
			players.add(AIPlayerObject);
		}
		
		/**
		 * A Multiplayer GameObject with x players
		 */
		public Multiplayer(int x) {
			this.playersTotal = x;
			for (int i = 0; i < x; i++) {
				players.add(playerObject); 
			}
			
		}
		
		public int getPlayerNum() {
			return playersTotal;
		}
		
		public void setPlayerNum(int x) {
			this.playersTotal = x;
		}
			
		public ArrayList<GameObject> getPlayers() {
			return this.players;
		}
		
		public void setPlayers(ArrayList<GameObject> players) {
			this.players = players;
		}
		
		/**
		 * Determines if the current game being played has been won
		 * @return boolean  true if the game is over, false if the game is ongoing
		 */
		
	/*	public boolean gameOver() {
			for (int i = 0; i < playersTotal; i++) {
				int playerHealth = players.get(i).GetComponent(PlayerController.class).health;
				if (playerHealth <= 0) {
					return true;
				}
			}
			return false;
		}
		*/
		/**
		 * The remaining alive players in a  multiplayer game
		 * @return ArrayList<GameObject>  player(s) that are still alive by the end of the game, returns null if 
		 */
		
		public ArrayList<GameObject> alivePlayers() {
			for (int i = 0; i < playersTotal; i++) {
				if (players.get(i).GetComponent(PlayerController.class).health <= 0) {
					players.remove(i);
				}
			}
		return players;
		}
		
		/**
		 * The winning player in a multiplayer game
		 * @return GameObject winning player
		 */
		public GameObject winner() {
			if (alivePlayers().size() == 1) {
				return alivePlayers().get(0);
			}
			return null;
		}
			
		@Override
		public String toString() {
			String stringRep = "";
				for (int i = 0; i < players.size(); i++) {
					stringRep += players.get(i).toString();
				}
			return stringRep;
		}
}
