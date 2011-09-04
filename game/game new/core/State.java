/**
 * Defines the interface of an game state.
 */

package core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import core.Engine;

public abstract class State {
	protected final static String GAME = "Game";
	protected final static String MENU = "Menu";
	
	protected String id;
	protected boolean loaded;
	protected final Engine engine;
	protected PowerUp powerUp;

	/**
	 * @param id The unique identifier for the game state.
	 */
	public State(String id, Engine engine){
		this.id = id;
		this.loaded = false;
		this.engine = engine;
	}
	
	/**
	 * Loads the game state.
	 * To be overridden.
	 * 
	 * @param gc The game container.
	 */
	public void load(GameContainer gc){
		loaded = true;
	}
	
	/**
	 * Unloads the game state.
	 * To be overridden.
	 * 
	 * @param gc The game container.
	 */
	public void unload(GameContainer gc){
		loaded = false;
	}
	
	/**
	 * Updates the game state.
	 * 
	 * @param gc The game container.
	 * @param delta The delta time.
	 */
	public abstract void update(GameContainer gc, int delta);
	
	/**
	 * Renders the game state.
	 * 
	 * @param gc The Game Container
	 * @param g The graphics engine
	 */
	public abstract void render(GameContainer gc, Graphics g);
	
	/**
	 * @return The game state identifier.
	 */
	public String getId(){
		return id;
	}
	

	/**
	 * @return <code>true</code> if the game state is loaded. <code>false</code> if not.
	 */
	public boolean isLoaded(){
		return loaded;
	}
}