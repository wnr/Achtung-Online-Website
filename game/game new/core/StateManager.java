/**
 * Singleton class responsible for handling the game states.
 */

package core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.naming.NamingException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import states.Game;

public class StateManager {
	private State active;
	private LinkedList<State> states;
	
	StateManager(){
		active = null;
		states = new LinkedList<State>();
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
	public void addState(State s, boolean changeTo, GameContainer gc) throws NamingException, IllegalArgumentException{
		if(s == null)
			throw new IllegalArgumentException();
		
		if(this.getState(s.getId()) != null)
			throw new NamingException();
		
		states.add(s);
		
		if(changeTo)
			this.changeState(s.getId(), gc);
	}
	
	/**
	 * Changes the active game state.
	 * 
	 * @param id The identifier for the game state to be activated.
	 * @param gc The current GameContainer
	 */
	public void changeState(String id, GameContainer gc) throws IllegalArgumentException, NullPointerException, NoSuchElementException{
		State s = this.getState(id);
		
		if(s == null)
			throw new NoSuchElementException();
		
		changeState(s, gc);
	}
	
	/**
	 * Changes the active Game State.
	 * 
	 * @param to The State to change to.
	 * @param gc The current GameContainer
	 */
	private void changeState(State to, GameContainer gc){
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
		if(states == null)
			return 0;
		
		return states.size();
	}
	
	/**
	 * @return The active game state.
	 */
	public State getActiveState(){
		return active;
	}
	public Game getGame(){
		return (Game) getState("Game");
		
	}
	
	public State getState(String id){
		if(id == null)
			throw new IllegalArgumentException();
		
		if(states == null)
			throw new NullPointerException();
		
		Iterator<State> it = states.iterator();
		
		while(it.hasNext()){
			State s = it.next();
			if(s.getId().equals(id))
				return s;
		}
		
		return null;
	}
	
}