/**
 * PowerUpManager class
 * 
 * The manager that handles the powerups.
 * 
 * 2011-06-07		OMMatte			Changed randomSpawn() to work well with round maps. #17
 */
package core;

import java.util.HashSet;

import java.util.Iterator;
import java.util.Random;

import javax.naming.NamingException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;


public class PowerUpManager {

	public static final int BLUE = 0, GREEN = 1, RED = 2;
	public static final PowerUpSprite POWERUPSPRITE = new PowerUpSprite();
	
	private static final int SPAWNCHANCE = 200;
	private static final int SPAWNFREQUENCY = 4000;
	private static final float POWERUPDIAMETER = PowerUp.getRadius()*2;
	
	private Engine engine;
	private HashSet<PowerUp> powerUps;
	private HashSet<PowerUp> spawnedPowerUps;
	private Random random;
	private int spawnCounter;

	PowerUpManager(Engine engine) {
		this.engine = engine;
		powerUps = new HashSet<PowerUp>();
		spawnedPowerUps = new HashSet<PowerUp>();
		random = new Random();
		spawnCounter = SPAWNFREQUENCY;
	}

	private void randomPowerUp(int x, int y) {
		int randomPowerUp = 0;
		Iterator<PowerUp> i = powerUps.iterator();
		while(i.hasNext()){
			PowerUp powerUp = i.next();
			randomPowerUp += powerUp.getSpawnChance();
		}

		int spawn = random.nextInt(randomPowerUp) + 1;
		randomPowerUp = 0;
		Iterator<PowerUp> it = powerUps.iterator();
		while(it.hasNext()){
			PowerUp powerUp = it.next();
			randomPowerUp += powerUp.getSpawnChance();
			if(spawn <= randomPowerUp){
				PowerUp powerUpClone = powerUp.clone();
				powerUpClone.setShape(x,y);
				this.spawnedPowerUps.add(powerUpClone);
				return;
			}
		}
	}

	public void addPowerUp(PowerUp powerUp) throws NamingException {
		if (powerUp == null)
			throw new IllegalArgumentException();

		powerUps.add(powerUp);

	}

	public void spawnPowerUp(PowerUp powerUp) throws NamingException {
		if (powerUp == null)
			throw new IllegalArgumentException();

		if (getPowerUp(powerUp.getName()) == null)
			throw new NamingException();

		spawnedPowerUps.add(powerUp);
	}

	public void removePowerUp(String name) {
		if (name == null)
			throw new IllegalArgumentException();

		Iterator<PowerUp> it = spawnedPowerUps.iterator();

		while (it.hasNext()) {
			PowerUp p = it.next();
			if (p.getName().equals(name))
				it.remove();
		}

	}

	public void removePowerUp(PowerUp powerUp) {
		Iterator<PowerUp> it = spawnedPowerUps.iterator();

		while (it.hasNext()) {
			PowerUp p = it.next();
			if (p.equals(powerUp))
				it.remove();
		}
	}

	private void powerUpSpawn(int delta) {
		if (random.nextInt((SPAWNCHANCE / delta) + 1) + spawnCounter == 0) {
			randomSpawn();
			spawnCounter = SPAWNFREQUENCY;
		}
		if (spawnCounter > 0) {
			spawnCounter -= delta;
			if (spawnCounter < 0) {
				spawnCounter = 0;
			}
		}
	}

	private void randomSpawn() {
		int x = 0;
		int y = 0;
		int xSpawnPoints = 0;
		int ySpawnPoints = 0;
		boolean safeSpot = false;
		Map map = engine.getMapManager().getActiveMap();
		while (!safeSpot) {
		if(map.isCircle()){
			double direction = (double)(random.nextInt(62831853)) /10000000;
			xSpawnPoints = (int) (Math.abs(Math.cos(direction))*(map.getGameArea().getBoundingCircleRadius()-POWERUPDIAMETER/2)*2);
			ySpawnPoints = (int) (Math.abs(Math.sin(direction))*(map.getGameArea().getBoundingCircleRadius()-POWERUPDIAMETER/2)*2);
		}
		else{
		xSpawnPoints = (int) (map.getWidth()- map.getEdgeSize()*2 - POWERUPDIAMETER);
		ySpawnPoints = (int) (map.getHeight()- map.getEdgeSize()*2 - POWERUPDIAMETER);
		}
		PowerUp[] powerUps = new PowerUp[this.spawnedPowerUps.size()];
		this.spawnedPowerUps.toArray(powerUps);
		
		
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
			x = (int) (random.nextInt(xSpawnPoints) + map.getGameArea().getMinX()+POWERUPDIAMETER/2);
			y = (int) (random.nextInt(ySpawnPoints) + map.getGameArea().getMinY()+POWERUPDIAMETER/2);
			}
			safeSpot = true;
			for (int i = 0; i < powerUps.length; i++) {
				
				if (Math.abs(x - powerUps[i].getPowerUpShape().getCenterX()) < POWERUPDIAMETER
						&& Math.abs(y
								- powerUps[i].getPowerUpShape().getCenterY()) < POWERUPDIAMETER) {
					safeSpot = false;

				}
			}
		}
		randomPowerUp(x, y);
	}

	public void update(GameContainer gc, int delta) {
		powerUpSpawn(delta);
	}

	public void unload() {
		//TODO: Do whats suppose to be done
		spawnedPowerUps.clear();
		spawnCounter = SPAWNFREQUENCY;
	}
	
	public void reset(){
		spawnedPowerUps.clear();
		spawnCounter = SPAWNFREQUENCY;
	}

	public void render(GameContainer gc, Graphics g) {
		Iterator<PowerUp> it = spawnedPowerUps.iterator();
		while (it.hasNext()) {
			PowerUp p = it.next();

			p.render(gc, g);
		}
	}

	public PowerUp getPowerUp(String name) {
		if (name == null)
			throw new IllegalArgumentException();

		Iterator<PowerUp> it = spawnedPowerUps.iterator();

		while (it.hasNext()) {
			PowerUp p = it.next();
			if (p.getName().equals(name))
				return p;
		}

		return null;
	}

	public Iterator<PowerUp> getPowerUpsIterator() {
		return powerUps.iterator();
	}
	public Iterator<PowerUp> getPowerUpsSpawnedIterator() {
		return spawnedPowerUps.iterator();
	}
	
	public PowerUpSprite getSpriteIcon() {
		return POWERUPSPRITE;
	}

	public HashSet<PowerUp> getPowerUps() {
		return powerUps;
	}
}
