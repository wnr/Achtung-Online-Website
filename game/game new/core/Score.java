/**
 * Singleton class responsible for handling the game states.
 * 
 * 2011-07-10		Seline			Changed SCOREHEADER value to "Target Score". Also changed its position being rendered.
 */

package core;

import java.util.Arrays;
import java.util.Comparator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class Score {

	private static final String SCOREHEADER = "Target Score";

	private Engine engine;

	Score(Engine engine) {
		this.engine = engine;

	}

	class DescendingComparator implements Comparator<Player> {

		@Override
		public int compare(Player player1, Player player2) {
			if (player1.getScore() < player2.getScore()) {
				return 1;
			}
			return -1;
		}

	}

	/**
	 * Renders the current game state.
	 * 
	 * @param gc
	 *            The Game Container
	 * @param g
	 *            The graphics engine
	 */
	public void render(GameContainer gc, Graphics g) {
		g.setColor(Color.white);
		g.drawString(SCOREHEADER + " (" + engine.getSettings().getTargetScore()
				+ ")", gc.getWidth() - 180, 20);
		Player[] players = new Player[engine.getPlayerManager().getNumPlayers()];
		engine.getPlayerManager().getPlayers().toArray(players);
		Comparator<Player> descendingOrder = new DescendingComparator();
		Arrays.sort(players, descendingOrder);
		int s = 0;
		for (int i = 0; i < players.length; i++) {
			g.setColor(players[i].getColor());
			g.drawString(players[i].getName(), gc.getWidth() - 180, 50 + s);
			g.drawString(players[i].getScore() + "", gc.getWidth() - 80, 50 + s);
			if(players[i].getTempScore()> 0){
				g.drawString("+(" + players[i].getTempScore() + ")",gc.getWidth() - 50, 50 + s);
			}
			s += 20;
		}

	}

}
