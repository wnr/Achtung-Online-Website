/**
 * Game class
 * 
 * This class represent that state Game. The class handle the mechanics of the
 * actual play-time.
 * 
 * 2011-06-02		OMMatte			Update går nu via MapManager och inte Map.
 * 2011-06-02		mrWiener 		Added escape key. Organized imports.
 * 2011-06-24		OMMatte			Fixed wormBodyImage height and width so it looks better with wallhack.
 * 2011-07-01		Seline			Added check isLeadingWithMargin() in isGameOver().
 * 2011-07-09		OMMatte			Ändrade så maskarna hamnar framför bilden igen (p.g.a ändringen till object-orienterad collition).
 * 2011-07-10		Seline			Removed check isLeadingWithMargin() in isGameOver(). Added origionalTargetScore variable. Used for loading and unloading games.
 */


package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import core.Engine;
import core.Map;
import core.State;

public class Game extends State {

	private Image wormBodyImage;

	private int originalTargetScore;
	private int pauseTime;
	private boolean longPause;
	private Map map;

	/**
	 * Game constructor.
	 */
	public Game(GameContainer gc, Engine engine) {
		super("Game", engine);
	}

	/**
	 * The things that should be loaded at the beginning of the game.
	 */
	public void load(GameContainer gc) {
		this.originalTargetScore = engine.getSettings().getTargetScore();
		gc.getGraphics().setAntiAlias(false);
		this.map = engine.getMapManager().getActiveMap();
		map.load(gc);
		engine.getTableMap().load(gc);
		
		reset(gc);
		engine.getPlayerManager().resetScore();
		super.load(gc);
	}

	public void unload(GameContainer gc) {
		engine.getSettings().setTargetScore(this.originalTargetScore);
		engine.getMapManager().unload();
		engine.getPowerUpManager().unload();
		super.unload(gc);
	}

	/**
	 * Updates the game.
	 */
	@Override
	public void update(GameContainer gc, int delta) {
		engine.getPlayerManager().adjustTargetScore();
		
		if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			engine.getStateManager().changeState("Menu", gc);
		}
		
		if (longPause && gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
			nextRound(gc);
		}
		if (shortPause(gc, delta) || longPause) {
			return;
		}

		try {
			engine.getPlayerManager().update(gc, delta);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		engine.getMapManager().update(gc, delta);
		engine.getPowerUpManager().update(gc, delta);
		gc.getInput().clearKeyPressedRecord();

	}

	/**
	 * Renders the game.
	 */
	@Override
	public void render(GameContainer gc, Graphics g) {
		
		g.drawImage(wormBodyImage, map.getGameArea().getMinX(), map.getGameArea().getMinX());
		engine.getPlayerManager().renderBody(gc, g);
		g.copyArea(wormBodyImage, (int)map.getGameArea().getMinX(), (int) map.getGameArea().getMinX());	
		engine.getMapManager().render(gc, g);
		engine.getPowerUpManager().render(gc, g);
		g.drawImage(wormBodyImage, map.getGameArea().getMinX(), map.getGameArea().getMinX());
		
		
		engine.getPlayerManager().renderHead(gc, g);
		engine.getScore().render(gc, g);
		map.renderImage(gc, g);

		if (!isPlayersAlive()) {
			gc.getInput().clearKeyPressedRecord();
			longPause = true;
		}
//		
	}

	private boolean shortPause(GameContainer gc, int delta) {
		if (pauseTime > 100 && pauseTime < 2500) {
			if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
				pauseTime = 2500;
			}
			pauseTime += delta;
			return true;
		}

		pauseTime += delta;
		return false;
	}

	public void nextRound(GameContainer gc) {
		if (isGameOver()) {
			engine.getStateManager().changeState("GameOver", gc);
			return;
		}

		reset(gc);
	}
	
	public void reset(GameContainer gc){
		engine.getPowerUpManager().reset();
		engine.getPlayerManager().reset();
		engine.getTableMap().reset();
		engine.getPlayerManager().randomSpawn();
		pauseTime = 0;
		longPause = false;
		resetWormBodyImage();
	}

	private boolean isPlayersAlive() {
		if (engine.getPlayerManager().getNumPlayersAlive() < 2) {
			return false;
		}

		return true;
	}

	public boolean isGameOver() {
		if (engine.getPlayerManager().getHighestScore() >= engine.getSettings().getTargetScore()) {
			return true;
		}

		return false;
	}

	public boolean isLongPause() {
		return longPause;
	}

	public Image getWormBodyImage() {
		return wormBodyImage;
	}

	public void resetWormBodyImage() {
		try {
			this.wormBodyImage = new Image((int)map.getGameArea().getWidth(), (int)map.getGameArea().getHeight());
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
