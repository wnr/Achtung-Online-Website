/**
 * Collision class
 * 
 * The class for handling collisions between players(worms), walls and
 * power-ups.
 * 
 * 2011-06-07		OMMatte			Changed wallcheck to work with the new maps.
 * 2011-06-17		OMMatte			wallcheck and powerupcheck now uses wormHead instead of wormBody.
 * 2011-06-19		OMMatte			Moved checkPowerup after color-collition to make switcharoonie work again.
 * 2011-06-23		OMMatte			Modified to work with Worm-class.
 * 2011-06-23		OMMatte			Now works with Wall-Hack
 * 2011-07-03		OMMatte			Added Contains-method, contains now works much better.
 * 2011-07-05		OMMatte			Modified how Undead works.
 * 2011-07-09		OMMatte			MAJOR CHANGE: Object-oriented collision instead of Color.
 * 2011-07-12		OMMatte			Modified Crash to work with Twin-PU;
 */
package core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Set;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import table.BodyShape;

//import org.newdawn.slick.geom.Shape;

public class Collision {

	private Engine engine;

	/**
	 * Collision constructor.
	 * 
	 * @param bgColor
	 *            The "safe color" that,s always allowed to paint on.
	 */
	public Collision(Engine engine) {

		this.engine = engine;
	}

	/**
	 * @param player
	 *            The player to be checked.
	 * @param g
	 *            The graphics of the game.
	 */
	public void collisionTest(Player player, Graphics g) {
		@SuppressWarnings("unchecked")
		Iterator<Worm> worms = ((LinkedList<Worm>) ((LinkedList<Worm>) player
				.getWorms()).clone()).iterator();
		while (worms.hasNext()) {
			Worm worm = worms.next();
			if (!player.hasWorm(worm)) {
				continue;
			}

			powerUpCheck(worm, player);

			wallCheck(worm, player);

			bodyCheck(worm, player);

		}
	}

	private void bodyCheck(Worm worm, Player player) {
		if (player.isInvisible() || !worm.isActive()) {
			return;
		}

		Set<BodyShape> undeadShapes = worm.getUndeadArea();
		int tileNumber = worm.getTileNumber();
		Shape wormBody = worm.getWormBody();

		ArrayList<LinkedList<BodyShape>> bodyList = engine.getTableMap()
				.getTouchedBodyShapes(wormBody, tileNumber,
						new boolean[engine.getTableMap().getTiles().size()],
						new ArrayList<LinkedList<BodyShape>>());

		Iterator<LinkedList<BodyShape>> bodyListIt = bodyList.iterator();

		while (bodyListIt.hasNext()) {
			LinkedList<BodyShape> bodies = bodyListIt.next();
			Iterator<BodyShape> bodiesIt = bodies.iterator();

			 while (bodiesIt.hasNext()) {
				BodyShape oldBody = bodiesIt.next();

				if (undeadShapes.contains(oldBody)) {
					continue;
				}

				if (wormBody.intersects(oldBody.getWormBody())) {
					crash(player,worm);
					return;
				}
			}
		}
	}

	private void wallCheck(Worm worm, Player player) {
		if (!worm.isMainWorm() || !worm.isActive()) {
			return;
		}
		Shape gameArea = engine.getMapManager().getActiveMap().getGameArea();
		Shape wormHead = worm.getWormHead();
		if (!contains(gameArea, wormHead)) {
			if (!player.isWallHack()) {
				crash(player,worm);
			} else if (wormHead.intersects(gameArea)) {
				worm.wallHack();

			} else {
				worm.removeMain();
			}
		} else {
			if (worm.hasMirrorWorm()) {
				worm.removeMirror();
			}
		}
	}

	private boolean contains(Shape holder, Shape captive) {
		if (holder.getClass().getName().equals(Rectangle.class.getName())) {
			return contains((Rectangle) holder, captive);
		}
		if (holder.getClass().getName().equals(Circle.class.getName())
				&& captive.getClass().getName()
						.equals(Rectangle.class.getName())) {
			return contains((Circle) holder, (Rectangle) captive);
		}
		if (holder.getClass().getName().equals(Circle.class.getName())
				&& captive.getClass().getName().equals(Circle.class.getName())) {
			return contains((Circle) holder, (Circle) captive);
		}
		return false;
	}

	private boolean contains(Rectangle holder, Shape captive) {
		if (captive.getMinX() < holder.getMinX()
				|| captive.getMaxX() > holder.getMaxX()
				|| captive.getMinY() < holder.getMinY()
				|| captive.getMaxY() > holder.getMaxY()) {
			return false;
		}
		return true;
	}

	private boolean contains(Circle holder, Rectangle captive) {
		double longestLine;
		double testLine;

		longestLine = Math.sqrt(Math.pow(
				holder.getCenterY() - captive.getMaxY(), 2)
				+ Math.pow(holder.getCenterX() - captive.getMaxX(), 2));

		testLine = Math.sqrt(Math.pow(holder.getCenterY() - captive.getMaxY(),
				2) + Math.pow(holder.getCenterX() - captive.getMinX(), 2));

		if (testLine > longestLine) {
			longestLine = testLine;
		}

		testLine = Math.sqrt(Math.pow(holder.getCenterY() - captive.getMinY(),
				2) + Math.pow(holder.getCenterX() - captive.getMaxX(), 2));

		if (testLine > longestLine) {
			longestLine = testLine;
		}

		testLine = Math.sqrt(Math.pow(holder.getCenterY() - captive.getMinY(),
				2) + Math.pow(holder.getCenterX() - captive.getMinX(), 2));

		if (testLine > longestLine) {
			longestLine = testLine;
		}

		if (longestLine > holder.getBoundingCircleRadius()) {
			return false;
		}

		return true;
	}

	private boolean contains(Circle holder, Circle captive) {
		if (Math.sqrt(Math.pow(holder.getCenterY() - captive.getCenterY(), 2)
				+ Math.pow(holder.getCenterX() - captive.getCenterX(), 2))
				+ captive.getBoundingCircleRadius() > holder
				.getBoundingCircleRadius()) {
			return false;
		}
		return true;
	}

	private void powerUpCheck(Worm worm, Player player) {
		Iterator<PowerUp> powerUps = engine.getPowerUpManager()
				.getPowerUpsSpawnedIterator();

		while (powerUps.hasNext()) {
			PowerUp powerUp = powerUps.next();
			if (worm.getWormHead().intersects(powerUp.getPowerUpShape())) {
				powerUps.remove();

				if (powerUp.affectSelf()) {
					player.addPowerUp(powerUp, worm);
				}

				if (powerUp.affectOthers()) {
					Iterator<Player> players = engine.getPlayerManager()
							.getAlivePlayers().iterator();
					while (players.hasNext()) {
						Player p = players.next();
						if (!p.equals(player)) {
							p.addPowerUp(powerUp.clone(), worm);
						}
					}
				}
			}
		}
	}

	private void crash(Player player, Worm worm) {
		worm.die();
		if(!player.testAlive()){
		engine.getPlayerManager().givePointsToAlive();
		}
	}

}
