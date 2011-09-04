/**
 * Engine class
 * 
 * Top class for handling the game. Singleton objects will live within Engine.
 * Engine will indirectly include all objects created in the game.
 * 
 * 2011-06-02		mrWiener		Added kill method. Changed name and version.
 * 2011-06-07		OMMatte			Added AA, small modifcations in most classes due to this... #64 (not working fully yet)
 * 2011-06-07		mrWiener		Added KeyRepresentation. Issue #15
 * 2011-07-31		mrWiener		Added printDebugInfo method
 */

package core;

import javax.naming.NamingException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;

import powerups.Clear;
import powerups.Drunk;
import powerups.FastTurn;
import powerups.Thick;
import powerups.Invisible;
import powerups.Slow;
import powerups.SlowTurn;
import powerups.Thin;
import powerups.Speed;
import powerups.SwitchKeys;
import powerups.Switcheroonie;
import powerups.TronTurn;
import powerups.Twin;
import powerups.Wallhack;
import table.TableMap;

public class Engine extends BasicGame{
	private static final String VERSION = "0.2";
	private static final String NAME = "Achtung Online";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static final boolean FULLSCREEN = false;

	private StateManager stateManager;
	private PlayerManager playerManager;
	private MapManager mapManager;
	private PowerUpManager powerUpManager; 
	private Settings settings;
	private Score score;
	private Collision collision;
	private KeyRepresenter keyRepresenter;
	private GameContainer gc;
	private TableMap tableMap;

	public static void main(String[] args) throws SlickException {

		try
		{
			AppGameContainer app = new AppGameContainer(new Engine());
			app.setDisplayMode(WIDTH, HEIGHT, FULLSCREEN);
			app.start();
		}
		catch(SlickException e)
		{
			System.out.println(e.toString());
		}
	}

	/**
	 * Engine constructor
	 */
	public Engine() {
		super(NAME);
	}

	/**
	 * Initializes the Engine object. Creates Singleton classes.
	 */
	public void init(GameContainer gc) throws SlickException {
		
		printDebugInfo(gc);

		this.gc = gc;
		stateManager = new StateManager();
		mapManager = new MapManager();
		playerManager = new PlayerManager(this);
		powerUpManager =  new PowerUpManager(this);
		settings = new Settings();
		score = new Score(this);
		collision = new Collision(this);
		keyRepresenter = new KeyRepresenter();
		tableMap = new TableMap(this);

		try {
			mapManager.addMap(new maps.Standard(600, 600), false, gc);
			mapManager.addMap(new maps.Round(300), true, gc);
			
			stateManager.addState(new states.Menu(gc, this), true, gc);
			stateManager.addState(new states.Game(gc, this), false, gc);
			stateManager.addState(new states.GameOver(gc, this), false, gc);
			stateManager.addState(new states.Settings(gc, this), false, gc);
			stateManager.addState(new states.Help(gc,this), false, gc);


			
			powerUpManager.addPowerUp(new Clear(this));
			powerUpManager.addPowerUp(new Switcheroonie(this));	
			powerUpManager.addPowerUp(new Twin(this));
			powerUpManager.addPowerUp(new SwitchKeys());
			powerUpManager.addPowerUp(new Speed());
			powerUpManager.addPowerUp(new Slow());
			powerUpManager.addPowerUp(new SlowTurn());
			powerUpManager.addPowerUp(new FastTurn());
			powerUpManager.addPowerUp(new Thin());
			powerUpManager.addPowerUp(new Thick());
			powerUpManager.addPowerUp(new Invisible());
			powerUpManager.addPowerUp(new TronTurn());
			powerUpManager.addPowerUp(new Drunk());
			powerUpManager.addPowerUp(new Wallhack());	
					
							
							
							
							
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the game.
	 * 
	 * @param gc
	 *            Game Container
	 * @param delta
	 *            The delta time
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		stateManager.update(gc, delta);
	}

	/**
	 * Renders the game.
	 * 
	 * @param gc
	 *            Game Container
	 * @param g
	 *            Graphics engine
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setAntiAlias(true);
		stateManager.render(gc, g);
	}

	
	/**
	 * Terminates the application.
	 */
	public void kill(){
		System.exit(0);
	}
	
	/**
	 * Represent a key code with a string.
	 */
	public String getKeyStringRepresentation(Integer keyCode){
		return keyRepresenter.representKey(keyCode);
	}

	/**
	 * @return The current version of the application.
	 */
	public String getVersion() {
		return VERSION;
	}

	/**
	 * @return The name of the game.
	 */
	public String getName() {
		return NAME;
	}

	/**
	 * @return The state manager.
	 */
	public StateManager getStateManager() {
		return stateManager;
	}

	/**
	 * @return The settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	public Score getScore() {
		return score;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public PowerUpManager getPowerUpManager() {
		return powerUpManager;
	}

	public Collision getCollision() {
		return collision;
	}

	public GameContainer getGc() {
		return gc;
	}

	public TableMap getTableMap() {
		return tableMap;
	}
	
	private void printDebugInfo(GameContainer gc){
		System.out.println("Slick build version: " + GameContainer.getBuildVersion());		
		System.out.println("Graphics adapter: " + GL11.glGetString(GL11.GL_RENDERER));
		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
		
		if(gc.isVSyncRequested())
			System.out.println("VSync requested.");
		else
			System.out.println("VSync not requested.");
		
	}
}
