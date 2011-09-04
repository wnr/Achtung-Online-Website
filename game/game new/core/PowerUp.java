/**
 * PowerUp Class
 * 
 * The class for handling the Power-ups in the game.
 * Each PowerUp will implement from this Abstract class.
 * 
 * 2011-06-16		mrWiener		Fixed issue #75
 * 2011-07-12		OMMatte			Added the Worm that hit the PU in each PU-class.
 */

package core;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

public abstract class PowerUp implements Cloneable {
	protected static final int RADIUS = 20;
	protected static final int TIME = 8000;
	
	protected double amount;
	protected String name;
	protected String description;
	protected Shape powerUpShape;
	protected boolean active;
	protected Image powerUpIcon;
	protected double time;
	protected int spawnChance;
	protected Player player;
	protected Worm worm;
	protected boolean affectSelf;
	protected boolean affectOthers;
	protected int index;
	protected int colorIndex;
	
	public PowerUp(){
		this.time = TIME;
		this.active = false;
		this.affectOthers = false;
		this.affectSelf = false;
	}

	/**
	 * Updates the PowerUp.
	 * Calculates the alive time.
	 * Calls deactivate when time is up.
	 * 
	 * @param gc
	 * @param delta
	 */
	public void update(GameContainer gc, int delta) {
		time -= delta;
		
		if (time <= 0) {
			deactivate();
		}
	}
	
	/**
	 * Renders the PowerUp.
	 */
	public void render(GameContainer gc, Graphics g) {
		g.drawImage(powerUpIcon, powerUpShape.getX(), powerUpShape.getY());

	}
	
	public void activate(Player player, Worm worm){
		this.player = player;
		this.worm = worm;
		this.active = true;
	}
	
	public void deactivate(){
		active = false;
	}
	
	public boolean isActive() {
			return active;
	}
	public Shape getPowerUpShape() {
		return powerUpShape;
	}
	
	public PowerUp clone(){
		PowerUp clone = null;
		try {
			clone = (PowerUp)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clone;
		
	}
	
	public void setShape(int x, int y){
		powerUpShape = new Circle(x,y, RADIUS);
	}
	
	public String getName() {
		return name;
	}
	public static int getRadius() {
		return RADIUS;
	}
	public int getSpawnChance() {
		return spawnChance;
	}
	public String getDescription() {
		return description;
	}
	
	public boolean affectSelf(){
		return affectSelf;
	}
	
	public boolean affectOthers(){
		return affectOthers;
	}

	public int getIndex() {
		return index;
	}
}
