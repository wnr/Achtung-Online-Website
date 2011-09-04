/**
 * Player class
 * 
 * The class for handling a player. Each object of class Player will represent
 * one player (or one "worm").
 * 
 * 2011-06-07		OMMatte			Small changes to make AA work.
 * 2011-06-16		OMMatte			Changed setSpeed and setRadius to modifySpeed... Also removed tempSpeed and tempRadius #71
 * 2011-06-16		OMMatte			Cleaned up the Player class, created some private methods and head is now a rectangle if tronTurn
 * 2011-06-20		OMMatte			Modified how undead works and added UNDEADFATKONSTANT
 * 2011-06-23		OMMatte			Moved wormBody/wormHead to class Worm.
 * 2011-06-25		OMMatte			Added support for wallhack swithcing colors.
 * 2011-07-05		OMMatte			Worms now handle Undead
 * 2011-07-09		OMMatte			MAJOR CHANGE: Undead now saves shapes instead of using time. Also saves both worms and players bodies in lists.
 * 2011-07-11		OMMatte			Moved wallHackColor and wormHackCounter to worm.
 */

package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import table.BodyShape;

public class Player {

	private static final Color HEADCOLOR = Color.yellow;
	private static final float HEADINCREASE = 0f;
	private static final float WORMRADIUS = 2f;
	private static final float LOWESTRADIUS = 1f;
	private static final double SPEED = 0.08;
	private static final double LOWESTSPEED = 0.01;
	private static final double DEGREE = 0.03;
	private static final int JUMPLENGTH = 12;
	private static final int JUMPCHANCE = 100;
	private static final int JUMPFREQUENCY = 100;

	private Random random;
	private Engine engine;
	private String name;

	private Color bodyColor;
	private Color headColor;
	private float wormRadius;
	private double degree;
	private double speed;
	private double jumpCounter;
	private int leftKey;
	private int rightKey;
	private int score;
	private int tempScore;

	private boolean alive;
	private boolean invisible;
	private boolean keySwitched;
	private boolean tronTurn;
	private boolean wallHack;
	private boolean inWall;

	private List<PowerUp> powerups;
	private List<Worm> worms;
	private List<BodyShape> playerBodies;

	/**
	 * Player constructor. Creates the player(worm) with given attributes.
	 * 
	 * @param name
	 *            The name of the player.
	 * @param bodyColor
	 *            The color of the .main worm".
	 * @param leftKey
	 *            The key for turning left.
	 * @param rightKey
	 *            The key for turning right.
	 * @param spawnX
	 *            The X spawn location.
	 * @param spawnY
	 *            The Y spawn location.
	 */
	public Player(Engine engine, String name, Color bodyColor, int leftKey,
			int rightKey) {
		random = new Random();

		this.engine = engine;
		this.name = name;
		this.bodyColor = bodyColor;
		this.leftKey = leftKey;
		this.rightKey = rightKey;

		headColor = HEADCOLOR;
		wormRadius = WORMRADIUS;
		degree = DEGREE;
		speed = SPEED;
		double direction = (double) (random.nextInt(62831853)) / 10000000;
		jumpCounter = JUMPFREQUENCY;
		score = 0;
		tempScore = 0;

		alive = true;
		invisible = false;
		keySwitched = false;
		tronTurn = false;
		wallHack = false;
		inWall = false;

		powerups = new LinkedList<PowerUp>();
		worms = new LinkedList<Worm>();
		worms.add(new Worm(engine, this, 0, 0, direction));
		playerBodies = new ArrayList<BodyShape>();

	}

	public void setSpawn(float spawnX, float spawnY) {
		worms.get(1).setPosition(spawnX, spawnY);
	}

	/**
	 * Updates the player(worm). This update also calls the updatePU method in
	 * the beginning.
	 * 
	 * @param gc
	 *            The container for the game.
	 * @param delta
	 *            The delta-time.
	 * @throws SlickException
	 */
	public void update(GameContainer gc, int delta) throws SlickException {

		if (!alive) {
			return;
		}
		updateBodyLists();

		undeadCountDown(delta);

		updatePU(gc, delta);

		updateWorms(gc, delta);

		if (invisible == false) {
			jump(delta);
		}
	}

	private void updateBodyLists() {
		if (invisible) {
			return;
		}
		Iterator<Worm> worms = this.worms.iterator();
		while (worms.hasNext()) {
			Worm worm = worms.next();
			if (!worm.isActive()) {
				continue;
			}

			worm.updateRadius();

			Shape wormBody = new Circle(worm.getWormBody().getCenterX(), worm
					.getWormBody().getCenterY(), getWormRadius());

			BodyShape bodyShape = new BodyShape(this, wormBody);
			worm.addWormBodies(bodyShape);
			playerBodies.add(bodyShape);
			engine.getTableMap().addBodyShape(bodyShape, worm.getTileNumber(),
					new boolean[engine.getTableMap().getTiles().size()]);
			worm.addUndeadArea(bodyShape, true);
		}

	}

	private void undeadCountDown(int delta) {
		Iterator<Worm> worms = this.worms.iterator();
		while (worms.hasNext()) {
			Worm worm = worms.next();
			if (!worm.isActive()) {
				continue;
			}
			worm.checkUndeadArea();
			worm.checkSharedUndeadWorms();
		}
	}

	/**
	 * Updates the Power-Ups that are affecting the player.
	 * 
	 * @param gc
	 *            The container for the game.
	 * @param delta
	 *            The delta-time.
	 * @throws SlickException
	 */
	private void updatePU(GameContainer gc, int delta) throws SlickException {
		Iterator<PowerUp> it = powerups.iterator();
		while (it.hasNext()) {
			PowerUp pu = it.next();
			pu.update(gc, delta);
			if (pu.isActive() == false) {
				it.remove();
			}
		}

	}

	private void updateWorms(GameContainer gc, int delta) {
		double deltaDirection = 0;

		if (!tronTurn) {
			if (gc.getInput().isKeyDown(leftKey)) {
				deltaDirection = -getSpeed() * degree * delta;
			}
			if (gc.getInput().isKeyDown(rightKey)) {
				deltaDirection = getSpeed() * degree * delta;
			}
		} else {
			if (gc.getInput().isKeyPressed(leftKey)) {
				deltaDirection = -Math.PI / 2;
			}
			if (gc.getInput().isKeyPressed(rightKey)) {
				deltaDirection = Math.PI / 2;

			}
		}

		for (Worm worm : worms) {
			worm.update(gc, delta, deltaDirection);
		}
	}

	/**
	 * Method for generating a jump.
	 * 
	 * @param delta
	 *            The delta-time.
	 */
	private void jump(int delta) {
		if (random.nextInt((int) ((JUMPCHANCE / getSpeed() / delta) + 1))
				+ jumpCounter == 0) {
			this.addPowerUp(
					new powerups.Invisible(
							(int) (JUMPLENGTH
									* Math.sqrt(getWormRadius()
											* Math.sqrt(Math
													.sqrt(getWormRadius()))) / speed),
							0, 0), null);
			jumpCounter = JUMPFREQUENCY;
		}
		if (jumpCounter > 0) {
			jumpCounter -= (delta * getSpeed());
			if (jumpCounter < 0) {
				jumpCounter = 0;
			}
		}
	}

	/**
	 * Method for rendering the players(worms) body.
	 * 
	 * @param gc
	 *            The container for the game.
	 * @param g
	 *            The graphics for the game.
	 */
	public void renderBody(GameContainer gc, Graphics g) {
		for (Worm worm : worms) {
			worm.renderBody(gc, g);

		}
	}

	/**
	 * Method for rendering the players(worms) head.
	 * 
	 * @param gc
	 *            The container for the game.
	 * @param g
	 *            The graphics for the game.
	 * @throws SlickException
	 */
	public void renderHead(GameContainer gc, Graphics g) {
		for (Worm worm : worms) {
			worm.renderHead(gc, g);
		}
	}

	public void addPowerUp(PowerUp powerUp, Worm worm) {
		powerups.add(powerUp);
		powerUp.activate(this, worm);
	}

	public void addWorm(Worm worm) {
		worms.add(worm);
	}

	public void removeWorm(Worm worm) {
		Iterator<Worm> worms = this.worms.iterator();
		while (worms.hasNext()) {
			Worm i = worms.next();
			if (i.equals(worm)) {
				worms.remove();
			}
		}
	}

	public void reset() {
		powerups.clear();

		if (keySwitched) {
			int tempLeft = getLeftKey();
			setLeftKey(getRightKey());
			setRightKey(tempLeft);
			keySwitched = false;
		}

		headColor = HEADCOLOR;
		wormRadius = WORMRADIUS;
		degree = DEGREE;
		speed = SPEED;
		double direction = (double) (random.nextInt(62831853)) / 10000000;
		jumpCounter = JUMPFREQUENCY;

		alive = true;
		invisible = false;
		keySwitched = false;
		tronTurn = false;
		wallHack = false;
		inWall = false;

		tempScore = 0;

		powerups.clear();
		worms.clear();
		worms.add(new Worm(engine, this, 0, 0, direction));
		playerBodies.clear();
	}

	public void resetScore() {
		score = 0;
		tempScore = 0;
	}

	public void resetAllBodies() {
		playerBodies.clear();
		Iterator<Worm> worms = this.worms.iterator();
		while (worms.hasNext()) {
			Worm worm = worms.next();
			worm.resetAllBodies();
		}
	}

	// Getters
	public String getName() {
		return name;
	}

	public Color getColor() {
		return bodyColor;
	}

	public double getDegree() {
		return degree;
	}

	public Color getHeadColor() {
		return headColor;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSpeed() {
		if (speed <= LOWESTSPEED) {
			return LOWESTSPEED;
		} else {
			return speed;
		}
	}

	public List<Worm> getWorms() {
		return worms;
	}

	public int getLeftKey() {
		return leftKey;
	}

	public int getRightKey() {
		return rightKey;
	}

	public float getWormRadius() {
		if (wormRadius <= LOWESTRADIUS) {
			return LOWESTRADIUS;
		} else {
			return wormRadius;
		}
	}

	public float getRealWormRadius() {
		return wormRadius;
	}

	public int getScore() {
		return score;
	}

	public static Color getHeadcolor() {
		return HEADCOLOR;
	}

	public int getTempScore() {
		return tempScore;
	}

	public static float getHeadIncrease() {
		return HEADINCREASE;
	}

	public void setTempScore(int tempScore) {
		this.tempScore = tempScore;
	}

	public void setColor(Color color) {
		this.bodyColor = color;
	}

	public void setHeadColor(Color headColor) {
		this.headColor = headColor;
	}

	public void setDegree(double degree) {
		this.degree = degree;
	}

	public void modifySpeed(double deltaSpeed) {
		this.speed = this.speed + deltaSpeed;
	}

	public void modifyDirection(double deltaDirection) {
		Iterator<Worm> worms = this.worms.iterator();
		while (worms.hasNext()) {
			Worm worm = worms.next();
			if (worm.isMainWorm()) {
				worm.setDirection(worm.getDirection() + deltaDirection);
			}
		}
	}

	public void setLeftKey(int leftKey) {
		this.leftKey = leftKey;
	}

	public void setRightKey(int rightKey) {
		this.rightKey = rightKey;
	}

	public void setVisibility(boolean visible) {
		this.invisible = !visible;
	}

	public void modifyWormRadius(float wormRadius) {
		if (wormRadius > 0 && this.wormRadius + wormRadius > LOWESTRADIUS) {
			Iterator<Worm> worms = this.worms.iterator();
			while (worms.hasNext()) {
				Worm worm = worms.next();
				if (!worm.isActive()) {
					continue;
				}
				Circle circle = new Circle(worm.getWormBody().getCenterX(),
						worm.getWormBody().getCenterY(), this.wormRadius
								+ wormRadius);
				worm.incUndeadArea(circle);
			}
		}
		this.wormRadius = this.wormRadius + wormRadius;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;

	}

	public void setScore(int score) {
		this.score = score;
	}

	public void incScore(int num) {
		this.score += num;
	}

	public void incTempScore(int num) {
		this.tempScore += num;
	}

	public void decScore(int num) {
		this.score -= num;
	}

	// Checkers
	public boolean isInvisible() {
		return invisible;
	}

	public boolean isKeySwitched() {
		return keySwitched;
	}

	public void setKeySwitched(boolean keySwitched) {
		this.keySwitched = keySwitched;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isTronTurn() {
		return tronTurn;
	}

	public void setTronTurn(boolean tronTurn) {
		this.tronTurn = tronTurn;
	}

	public void setWallHack(boolean wallHack) {
		this.wallHack = wallHack;
	}

	public boolean isWallHack() {
		return wallHack;
	}

	public void setInWall(boolean inWall) {
		this.inWall = inWall;
	}

	public void setWorms(List<Worm> worms) {
		this.worms = worms;
	}

	public boolean isInWall() {
		return inWall;
	}

	public boolean hasWorm(Worm worm) {
		for (Worm w : worms) {
			if (w.equals(worm)) {
				return true;
			}
		}
		return false;
	}

	public boolean testAlive() {
		Iterator<Worm> worms = this.worms.iterator();
		while (worms.hasNext()) {
			Worm worm = worms.next();
			if (worm.isActive()) {
				return true;
			}
		}
		engine.getPlayerManager().setAlive(this, false);
		setAlive(false);
		return false;
	}
}
