/**
 * Singleton class responsible for handling the game states.
 * 
 * 2011-06-07		OMMatte		Added numMaps, might get removed since you can just check the list-size instead.
 */

package core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.naming.NamingException;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MapManager {
	private Map active;
	private LinkedList<Map> maps;
	private int numMaps;
	
	MapManager(){
		active = null;
		maps = new LinkedList<Map>();
		numMaps = 0;
	}
	
	/**
	 * Updates the current game state.
	 * Also performs game state changes.
	 * 
	 * @param gc The game container.
	 * @param delta The delta time.
	 */
	public void update(GameContainer gc, int delta){
		if(active == null)
			return;
		
		active.update(gc, delta);
	}
	
	/**
	 * Renders the current game state.
	 *
	 * @param gc The Game Container
	 * @param g The graphics engine
	 */
	public void render(GameContainer gc, Graphics g){
		if(active == null)
			return;
		
		active.render(gc, g);
	}
	
	/**
	 * Adds a state to the state manager.
	 * 
	 * @param s The state to be added
	 * @param changeTo <code>true</code> if state manager should change to this state directly. <code>false</code> otherwise.
	 * @param gc GameContainer if the State is to be loaded. Can be <code>null</code> if changeTo is <code>false</code>.
	 */
	public void addMap(Map m, boolean changeTo, GameContainer gc) throws NamingException, IllegalArgumentException{
		if(m == null)
			throw new IllegalArgumentException();
		
		if(this.getMap(m.getId()) != null)
			throw new NamingException();
		
		maps.add(m);
		numMaps +=1;
		
		if(changeTo)
			this.changeMap(m.getId(), gc);
	}
	
	/**
	 * Changes the active game state.
	 * 
	 * @param id The identifier for the game state to be activated.
	 * @param gc The current GameContainer
	 */
	public void changeMap(String id, GameContainer gc) throws IllegalArgumentException, NullPointerException, NoSuchElementException{
		Map m = this.getMap(id);
		
		if(m == null)
			throw new NoSuchElementException();
		
		changeMap(m, gc);
	}
	
	/**
	 * Changes the active Game State.
	 * 
	 * @param to The State to change to.
	 * @param gc The current GameContainer
	 */
	private void changeMap(Map to, GameContainer gc){
		if(active != null && active.isLoaded())
			active.unload(gc);
		
		active = to;
		
		if(!active.isLoaded())
			active.load(gc);
	}
	
	/**
	 * @return The number of game states stored in the object.
	 */
	public int getNumStates(){
		if(maps == null)
			return 0;
		
		return maps.size();
	}
	
	public LinkedList<Map> getMaps() {
		return maps;
	}

	/**
	 * @return The active game state.
	 */
	public Map getActiveMap(){
		return active;
	}
	
	public Map getMap(String id){
		if(id == null)
			throw new IllegalArgumentException();
		
		if(maps == null)
			throw new NullPointerException();
		
		Iterator<Map> it = maps.iterator();
		
		while(it.hasNext()){
			Map m = it.next();
			if(m.getId().equals(id))
				return m;
		}
		
		return null;
	}

	public void unload() {
		active.unload();
		
	}


	public int getNumMaps() {
		return numMaps;
	}
	
}