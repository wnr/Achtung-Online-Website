/**
 * PlayerManager class
 * 
 * The manager that handles the players.
 * 
 * 2011-06-07		OMMatte			Changed randomSpawn() to work well with round maps. #17
 * 2011-06-16		OMMatte			Simplified getNumAlivePlayers().
 * 2011-16-19		mrWiener		Implemented getWinner() method.
 * 2011-07-01		Seline			Implemented getSecondHighestScore() and isLeadingWithMargin(). Added static variable WINMARGIN.`
 * 2011-07-09		Seline			Remade getHighScore() method. Added static variables LEADER & RUNNERUP Removed getSecondHighestScore(). Fixed formating (except randomSpawn()).
 * 2011-07-10		Seline			Added adjustTargetScore() method. Removed isLeadingWithMargin() method.
 */
package core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.naming.NamingException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class PlayerManager {

	// player indexes
	private static final int LEADER = 0;
	private static final int RUNNERUP = 1;

	private static final int DISTANCEFROMWALL = 70;
	private static final int DISTANCEFROMWORMS = 40;
	private static final int WINMARGIN = 2;

	private Engine engine;
	private HashSet<Player> players;
	private HashSet<Player> alivePlayers;

	PlayerManager(Engine engine) {
		this.engine = engine;

		players = new HashSet<Player>();
		alivePlayers = new HashSet<Player>();
	}

	public void addPlayer(Player player) throws NamingException {
		if (player == null)
			throw new IllegalArgumentException();

		if (getPlayer(player.getName()) != null)
			throw new NamingException();

		players.add(player);
	}

	public void removePlayer(String name) {
		if (name == null)
			throw new IllegalArgumentException();

		Iterator<Player> it = players.iterator();

		while (it.hasNext()) {
			Player p = it.next();
			if (p.getName().equals(name))
				it.remove();
		}
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p.isAlive()) {
				p.update(gc, delta);
			}
		}
	}

	public void renderHead(GameContainer gc, Graphics g) {
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();

			p.renderHead(gc, g);
		}
	}

	public void renderBody(GameContainer gc, Graphics g) {
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p.isAlive()) {
				if (!((states.Game) engine.getStateManager().getActiveState())
						.isLongPause() && p.isAlive()) {
					engine.getCollision().collisionTest(p, g);
				}
				p.renderBody(gc, g);
			}
		}
	}

	public void givePointsToAlive() {
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p.isAlive()) {
				p.incTempScore(1);
				p.incScore(1);
			}
		}
	}

	public Player getPlayer(String name) {
		if (name == null)
			throw new IllegalArgumentException();

		Iterator<Player> it = players.iterator();

		while (it.hasNext()) {
			Player p = it.next();
			if (p.getName().equals(name))
				return p;
		}

		return null;
	}

	public void unload() {
		players.clear();
		alivePlayers.clear();
	}

	@SuppressWarnings("unchecked")
	public void reset() {
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			p.reset();
		}
		alivePlayers = (HashSet<Player>) players.clone();
	}

	public void resetScore() {
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			p.resetScore();
		}
	}

	public void randomSpawn(){
		int[] badX = new int[getNumPlayers()];
		int[] badY = new int[getNumPlayers()];
		Map map = engine.getMapManager().getActiveMap();
		int x = 0;
		int y = 0;
		int xSpawnPoints = 0;
		int ySpawnPoints = 0;
		Random random = new Random();
		Iterator<Player> it = getPlayersIterator();
		for (int i = 0; i < getNumPlayers(); i++) {
			Player player = it.next();
			if(map.isCircle()){
				double direction = (double)(random.nextInt(62831853)) /10000000;
				xSpawnPoints = (int) (Math.abs(Math.cos(direction))*(map.getGameArea().getBoundingCircleRadius()-player.getWormRadius()-DISTANCEFROMWALL))*2;
				ySpawnPoints = (int) (Math.abs(Math.sin(direction))*(map.getGameArea().getBoundingCircleRadius()-player.getWormRadius()-DISTANCEFROMWALL))*2;
			}
			else{
			xSpawnPoints = (int) (map.getWidth()-map.getEdgeSize()*2-player.getWormRadius()*2-DISTANCEFROMWALL*2);
			ySpawnPoints = (int) (map.getHeight()- map.getEdgeSize()*2-player.getWormRadius()*2-DISTANCEFROMWALL*2);
			}

			boolean safeSpot = false;
			while (!safeSpot) {
				if(map.isCircle()){
					if (xSpawnPoints <= 0){
						x = (int) map.getGameArea().getCenterX();
					}
					else{
					x = (int) (random.nextInt(xSpawnPoints) + map.getGameArea().getCenterX()-(xSpawnPoints/2));
					}
					if(ySpawnPoints <= 0){
						y = (int) map.getGameArea().getCenterY();
					}
					else{
					y = (int) (random.nextInt(ySpawnPoints) + map.getGameArea().getCenterY()-(ySpawnPoints/2));
					}
				}
				else{
				x = (int) (random.nextInt(xSpawnPoints) + map.getGameArea().getMinX()+player.getWormRadius()+DISTANCEFROMWALL);
				y = (int) (random.nextInt(ySpawnPoints) + map.getGameArea().getMinY()+player.getWormRadius()+DISTANCEFROMWALL);
				}
				safeSpot = true;
				for (int u = 0; u < i; u++) {
					if (Math.abs(x - badX[u]) < DISTANCEFROMWORMS && Math.abs(y - badY[u]) < DISTANCEFROMWORMS) {
						safeSpot = false;

					}
				}
			}

			badX[i] = x;
			badY[i] = y;
			player.setSpawn(x, y);
		}
	}

	public int getNumPlayers() {
		return players.size();
	}

	public Iterator<Player> getPlayersIterator() {
		return players.iterator();
	}

	public HashSet<Player> getPlayers() {
		return players;
	}

	public int getNumPlayersAlive() {
		return alivePlayers.size();
	}

	public int getHighestScore() {
		int highest = 0;
		Iterator<Player> it = this.players.iterator();

		while (it.hasNext()) {
			Player p = it.next();
			if (p.getScore() > highest) {
				highest = p.getScore();
			}
		}

		return highest;
	}

	private int[] getHighScore() {
		int[] highScore = new int[2];
		Iterator<Player> it = this.players.iterator();

		while (it.hasNext()) {
			Player p = it.next();
			if (p.getScore() >= highScore[LEADER]) {
				highScore[RUNNERUP] = highScore[LEADER];
				highScore[LEADER] = p.getScore();
			} else if (p.getScore() > highScore[RUNNERUP]) {
				highScore[RUNNERUP] = p.getScore();
			}
		}

		return highScore;
	}

	public void adjustTargetScore() {
		int[] highScore = getHighScore();

		if (engine.getSettings().getTargetScore() < WINMARGIN + highScore[RUNNERUP])
			engine.getSettings().incTargetScore(1);
	}

	public Player getWinner() {
		int highest = 0;
		Player player = null;
		Iterator<Player> it = this.players.iterator();

		while (it.hasNext()) {
			Player p = it.next();
			if (p.getScore() > highest) {
				highest = p.getScore();
				player = p;
			}
		}

		return player;
	}

	public void setAlive(Player player, boolean alive) {
		player.setAlive(alive);
		if (!alive) {
			alivePlayers.remove(player);
		} else {
			alivePlayers.add(player);
		}
	}

	public HashSet<Player> getAlivePlayers() {
		return alivePlayers;
	}

}
