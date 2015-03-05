/**
 * 2011-07-10		Seline			Renamed ...MaxScore methods to ...TargetScore.
 */

package core;

import org.newdawn.slick.Color;

public class Settings {
	private int maxScore;
	private int maxPlayers;
	private Color[] playerColors;

	public Settings() {
		maxScore = 0;
		maxPlayers = 2;
		playerColors = new Color[12];
		
		populatePlayerColors();
	}

	public int getTargetScore() {
		return maxScore;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void setTargetScore(int maxScore) {
		this.maxScore = maxScore;
		
	}

	public void incTargetScore(int num) {
		this.maxScore += num;
	}
	
	public void decTargetScore(int num) {
		this.maxScore -= num;
	}
	
	public void setMaxPlayers(int maxPlayers){
		this.maxPlayers = maxPlayers; 
	}

	
	private void populatePlayerColors(){
		playerColors[0] = new Color(Color.red);
		playerColors[1] = new Color(Color.blue);
		playerColors[2] = new Color(Color.green);
		playerColors[3] = new Color(Color.white);
		playerColors[4] = new Color(Color.orange);
		playerColors[5] = new Color(Color.magenta);
		playerColors[6] = new Color(Color.cyan);
		playerColors[7] = new Color(Color.pink);
		playerColors[8] = new Color(Color.darkGray);
		playerColors[9] = new Color(Color.lightGray);
		playerColors[10] = new Color(Color.gray);
		playerColors[11] = new Color(Color.yellow);
	}
	
	public Color getPlayerColor(int num){
		return playerColors[num];
	}
}
