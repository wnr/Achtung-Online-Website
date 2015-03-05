/**
 * Menu class
 * 
 * The class for handling the startup-menu.
 * 
 * 2011-06-02		OMMatte			Added error-messages + Cleared KeyPress
 * 2011-06-02		mrWiener		Added exit on escape press.
 * 2011-06-07		OMMatte			Added methods for selecting Maps, added method StartGame()
 * 2011-06-07		mrWiener		Fixed key representation + added settings
 * 2011-06-07		OMMatte 		Fixed "Select leftKey/RightKey" that stopped working on last change.
 * 2011-06-08		OMMatte			Fixed clear keybindings that stopped working on last change.
 * 2011-07-01		Seline			Added static variable SCOREINCREMENT.
 * 2011-07-10		Seline			Fixed score issue when removing player. removePlayer method now use SCOREINCREMENT variable.
 */

package states;

import java.util.Iterator;

import javax.naming.NamingException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.RoundedRectangle;

import core.Engine;
import core.Map;
import core.MenuButton;
import core.MenuSection;
import core.MenuSystem;
import core.Player;
import core.PowerUp;
import core.State;

public class Menu extends State {
	private MenuSystem menu;
	
	
	private static final int MAXPLAYERS = 10;
	private static final RoundedRectangle RECSTARTGAME = new RoundedRectangle(700, 550, 70, 35, 5);
	private static final RoundedRectangle RECSETTINGS = new RoundedRectangle(RECSTARTGAME.getMinX() - 100, RECSTARTGAME.getMinY(), 95, 35, 5);
	private static final RoundedRectangle RECHELP = new RoundedRectangle(RECSETTINGS.getMinX() - 60, RECSETTINGS.getMinY(), 55, 35, 5);
	private static final Color BGCOLOR = Color.black;
	private static final GradientFill RECFILLCOLOR = new GradientFill(45, 45, Color.darkGray, 46, 46, Color.darkGray);
	private static final GradientFill RECLINECOLOR = new GradientFill(45, 45, Color.yellow, 46, 46, Color.yellow);
	private static final GradientFill RECFILLCLICKED = new GradientFill(45, 45, Color.green, 46, 46, Color.green);
	private static final GradientFill RECFILLUNCLICKED = new GradientFill(45, 45, Color.red, 46, 46, Color.red);
	private static final String SELECTLEFTKEY = ("Select your" + "\n" + "left key.");
	private static final String SELECTRIGHTKEY = ("Select your" + "\n" + "right key.");
	private static final String TARGETSCORE = "Target Score:";
	private static final String BADKEY = ("That is not an acceptable key." + "\n" + "Please select another.");
	private static final String FEWPLAYERS = ("You need at least 2 players" + "\n" + "to start the game.");
	private static final int SCOREINCREMENT = 5;

	private RoundedRectangle[] recPlayers;
	private RoundedRectangle[] recMaps;
	private Circle[] recColors;
	private String[] stringPlayers;
	private GradientFill[] selectedPlayers;
	private String selectedMap;
	private String startGame;
	private String[] leftKeyBinding;
	private Integer[] leftKeyInt;
	private String[] rightKeyBinding;
	private Integer[] rightKeyInt;
	private boolean waitForInput;
	private KeyListener keyListener;
	private boolean badKey;
	private boolean fewPlayers;
	
	private int[] powerUpOrder;
	
	public Menu(final GameContainer gc, Engine engine) {
		super("Menu", engine);
		engine.getSettings().setMaxPlayers(MAXPLAYERS);

		startGame = "Start";
		
		powerUpOrder();
		recPlayers = new RoundedRectangle[MAXPLAYERS];
		recColors = new Circle[MAXPLAYERS];
		recMaps = new RoundedRectangle[engine.getMapManager().getNumMaps()];
		selectedMap = engine.getMapManager().getActiveMap().getId();

		stringPlayers = new String[MAXPLAYERS];
		selectedPlayers = new GradientFill[MAXPLAYERS];
		rightKeyBinding = new String[MAXPLAYERS];
		leftKeyBinding = new String[MAXPLAYERS];
		rightKeyInt = new Integer[MAXPLAYERS];
		leftKeyInt = new Integer[MAXPLAYERS];
		waitForInput = false;
		for (int i = 0; i < MAXPLAYERS; i++) {
			recPlayers[i] = new RoundedRectangle(60, 40 + i * 50, 95, 35, 5);
			recColors[i] = new Circle(recPlayers[i].getX() - 30, recPlayers[i].getCenterY(), 15);
			stringPlayers[i] = ("Player " + (i + 1));
			selectedPlayers[i] = RECFILLUNCLICKED;
			leftKeyBinding[i] = "";
			rightKeyBinding[i] = "";
		}
		for(int i = 0; i < recMaps.length; i++){
			recMaps[i] =  new RoundedRectangle(640, 350+i*50, 110, 35, 5);
			
		}
		

		keyListener = new KeyListener() {
			@Override
			public void inputEnded() {
			}

			@Override
			public boolean isAcceptingInput() {
				return true;
			}

			@Override
			public void keyPressed(int keyInt, char keyStr) {
				fewPlayers = false;
				badKey = false;
				if (waitForInput) {
					if (badKeys(keyInt)) {
						return;
					}
					gc.getInput().clearKeyPressedRecord();
					
					for (int i = 0; i < MAXPLAYERS; i++) {
						if (leftKeyBinding[i].equals(SELECTLEFTKEY)) {
							leftKeyBinding[i] = "";
							leftKeyInt[i] = keyInt;
							rightKeyBinding[i] = SELECTRIGHTKEY;
							return;
						}
						if (rightKeyBinding[i].equals(SELECTRIGHTKEY)) {
							rightKeyBinding[i] = "";
							rightKeyInt[i] = keyInt;
							waitForInput = false;
							addPlayer(i);
							return;
						}
					}

				}

			}

			@Override
			public void keyReleased(int arg0, char arg1) {
			}

			@Override
			public void inputStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void setInput(Input input) {
				// TODO Auto-generated method stub

			}

		};
		
		menu = new MenuSystem(3);
	}


	public void load(GameContainer gc) {
		gc.getGraphics().setBackground(BGCOLOR);
		gc.getGraphics().setColor(Color.white);
		gc.getInput().addKeyListener(keyListener);
		
		menu.setSection(new MenuSection(0, 0, Engine.WIDTH, 100), 0);
		menu.add(new MenuButton("Players", 0, 50),	0);
		menu.add(new MenuButton("Power ups", 0, 50), 0);
		menu.add(new MenuButton("Preferences", 0, 50), 0);
		
		menu.setSection(new MenuSection(0, 100, Engine.WIDTH, Engine.HEIGHT-200), 1);
		menu.setSection(new MenuSection(0, Engine.HEIGHT-100, Engine.WIDTH, 100), 2);
		
		menu.init(gc);
		
		menu.getSection(0).calculateRelativeWidth();
		
		super.load(gc);
	}

	public void unload(GameContainer gc) {
		gc.getGraphics().setBackground(Color.black);
		gc.getGraphics().setColor(Color.white);
		gc.getInput().removeKeyListener(keyListener);

		super.unload(gc);
	}

	public void update(GameContainer gc, int delta) {
		menu.update(gc);
		
		if (waitForInput) {
			return;
		}
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			buttonPressed(gc, gc.getInput().getMouseX(), gc.getInput().getMouseY());
		}
		if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
			buttonPressed(gc, (int) RECSTARTGAME.getCenterX(), (int) RECSTARTGAME.getCenterY());
		}

		if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			engine.kill();
		}
		gc.getInput().clearKeyPressedRecord();
	}

	public void render(GameContainer gc, Graphics g) {
		menu.render(g);
		
		/*g.fill(RECSTARTGAME, RECFILLCOLOR);
		g.draw(RECSTARTGAME, RECLINECOLOR);
		g.drawString(startGame, RECSTARTGAME.getMinX() + 10, RECSTARTGAME.getMinY() + 8);
		
		g.fill(RECSETTINGS, RECFILLCOLOR);
		g.draw(RECSETTINGS, RECLINECOLOR);
		g.drawString("Settings", RECSETTINGS.getMinX() + 10, RECSETTINGS.getMinY() + 8);
		
		g.fill(RECHELP, RECFILLCOLOR);
		g.draw(RECHELP, RECLINECOLOR);
		g.drawString("Help", RECHELP.getMinX() + 10, RECHELP.getMinY() + 8);
		
		g.drawString(TARGETSCORE, 620, 310);
		g.drawString(engine.getSettings().getTargetScore() + "", 740, 310);

		for (int i = 0; i < MAXPLAYERS; i++) {
			g.fill(recPlayers[i], selectedPlayers[i]);
			g.draw(recPlayers[i], RECLINECOLOR);
			g.drawString(stringPlayers[i], recPlayers[i].getMinX() + 10, recPlayers[i].getMinY() + 8);
			g.drawString(leftKeyBinding[i], recPlayers[i].getMaxX() + 15, recPlayers[i].getMinY() + 8);
			g.drawString(rightKeyBinding[i], recPlayers[i].getMaxX() + 100, recPlayers[i].getMinY() + 8);
			g.drawString(engine.getKeyStringRepresentation(leftKeyInt[i]), recPlayers[i].getMaxX() + 15, recPlayers[i].getMinY() + 8);
			g.drawString(engine.getKeyStringRepresentation(rightKeyInt[i]), recPlayers[i].getMaxX() + 100, recPlayers[i].getMinY() + 8);
			g.fill(recColors[i], new GradientFill(3, 3, engine.getSettings().getPlayerColor(i), 4, 4, engine
					.getSettings().getPlayerColor(i)));
			g.draw(recColors[i], new GradientFill(3, 3, engine.getSettings().getPlayerColor(i), 4, 4, engine
					.getSettings().getPlayerColor(i)));
		}
		renderMaps(gc,g);
		renderPowerUps(gc, g);

		if (badKey) {
			g.drawString(BADKEY, 100, 550);
		}
		if (fewPlayers) {
			g.drawString(FEWPLAYERS, 100, 550);
		}
		
		*/
	}
	
	
	
	
	
	private void powerUpOrder(){
		powerUpOrder = new int[14];
		powerUpOrder[0] = 0;
		powerUpOrder[1] = 7;
		powerUpOrder[2] = 6;
		powerUpOrder[3] = 10;
		powerUpOrder[4] = 1;
		powerUpOrder[5] = 12;
		powerUpOrder[6] = 8;
		powerUpOrder[7] = 11;
		powerUpOrder[8] = 4;
		powerUpOrder[9] = 5;
		powerUpOrder[10] = 13;
		powerUpOrder[11] = 2;
		powerUpOrder[12] = 9;
		powerUpOrder[13] = 3;
	}

	private boolean badKeys(int keyInt) {
		if (keyInt == Input.KEY_ESCAPE || keyInt == Input.KEY_SPACE) {
			badKey = true;
			return true;
		}
		return false;
	}

	private void renderPowerUps(GameContainer gc, Graphics g) {
		int x = 410;
		int y = 33;
		start: for(int i = 0; i < powerUpOrder.length; i++){
			Iterator<PowerUp> powerUps = engine.getPowerUpManager().getPowerUpsIterator();
			while(powerUps.hasNext()){
				PowerUp powerUp = powerUps.next();
				if(powerUp.getIndex() == powerUpOrder[i]){
					powerUp.setShape(x, y);
					powerUp.render(gc, g);
					g.drawString(powerUp.getDescription(), x + 30, y - 10);
					y += 41;
					continue start;
				}
			}
		}

	}
	private void renderMaps(GameContainer gc, Graphics g){
		int i = 0;
		Iterator<Map> maps = engine.getMapManager().getMaps().iterator();
		while(maps.hasNext()){
			Map map = maps.next();
			if(map.getId().equals(selectedMap)){
				g.fill(recMaps[i], RECFILLCLICKED);
			}else{g.fill(recMaps[i], RECFILLUNCLICKED);}
			g.draw(recMaps[i], RECLINECOLOR);
			g.drawString(map.getId(), recMaps[i].getMinX()+10, recMaps[i].getMinY()+8);
			i++;
		}
	}

	private void buttonPressed(GameContainer gc, int x, int y) {
		if (RECSTARTGAME.contains(x, y)) {
			if (engine.getPlayerManager().getNumPlayers() > 1) {
				startGame(gc);

			} else {
				fewPlayers = true;
			}
			return;
		} else if(RECSETTINGS.contains(x, y)){
			engine.getStateManager().changeState("Settings", gc);
		}else if(RECHELP.contains(x,y)){
			engine.getStateManager().changeState("Help", gc);
		}
		
		
		for (int i = 0; i < recPlayers.length; i++) {
			if (recPlayers[i].contains(x, y))
				changePlayerState(i);
		}
		for (int i = 0; i < recMaps.length; i++){
			if (recMaps[i].contains(x,y))
				changeMapState(i);
		}
	}
	private void startGame(GameContainer gc){
		engine.getMapManager().changeMap(selectedMap, gc);
		engine.getStateManager().changeState("Game", gc);
		
	}

	private void changePlayerState(int player) {
		if (selectedPlayers[player].equals(RECFILLUNCLICKED)) {
			selectedPlayers[player] = RECFILLCLICKED;
			leftKeyBinding[player] = SELECTLEFTKEY;
			rightKeyBinding[player] = "";
			waitForInput = true;

		} else {
			selectedPlayers[player] = RECFILLUNCLICKED;
			leftKeyBinding[player] = "";
			rightKeyBinding[player] = "";
			leftKeyInt[player] = null;
			rightKeyInt[player] = null;
			
			removePlayer(player);
		}
	}
	private void changeMapState(int map){
		String pressedMap = engine.getMapManager().getMaps().get(map).getId();
		if(pressedMap.equals(selectedMap)){
			
		}
		else{
			selectedMap = pressedMap;
		}
	}

	private void addPlayer(int player) {
		try {
			engine.getPlayerManager().addPlayer(
					new Player(engine,"Player " + (player + 1), engine.getSettings().getPlayerColor(player),
							leftKeyInt[player], rightKeyInt[player]));
		} catch (NamingException e) {
			e.printStackTrace();
		}

		engine.getSettings().incTargetScore(SCOREINCREMENT);
	}

	private void removePlayer(int player) {
		engine.getPlayerManager().removePlayer("Player " + (player + 1));

		engine.getSettings().decTargetScore(SCOREINCREMENT);
	}
}
