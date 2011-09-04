/**
 * Settings menu.
 * 
 * 2011-06-07		mrWiener		Created class.
 */

package states;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.RoundedRectangle;

import core.Engine;
import core.State;

public class Settings extends State {
	private static final RoundedRectangle RECBACK = new RoundedRectangle(700, 550, 70, 35, 5);
	private static final GradientFill RECFILLCOLOR = new GradientFill(45, 45, Color.darkGray, 46, 46, Color.darkGray);
	private static final GradientFill RECLINECOLOR = new GradientFill(45, 45, Color.yellow, 46, 46, Color.yellow);
	private static final GradientFill RECFILLCLICKED = new GradientFill(45, 45, Color.green, 46, 46, Color.green);
	private int settingsLeft = 100;
	private int settingsTop = 200;
	private int settingsWidthMargin = 100;
	private int settingsHeightMargin = 50;
	private RoundedRectangle recFullScreen;
	private RoundedRectangle recVSync;
	private String fullScreenText;
	private String vSyncText;
	private boolean fullScreenEnabled;
	private boolean vSyncEnabled;
	
	public Settings(GameContainer gc, Engine engine) {
		super("Settings", engine);
		
		recFullScreen = new RoundedRectangle(settingsLeft, settingsTop, 110, 35, 5);
		recVSync = new RoundedRectangle(settingsLeft, settingsTop + settingsHeightMargin, 110, 35, 5);
		
		fullScreenEnabled = gc.isFullscreen();
		vSyncEnabled = gc.isVSyncRequested();
		
		updateFullScreenText();
		updateVSyncText();

	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			buttonPressed(gc, gc.getInput().getMouseX(), gc.getInput().getMouseY());
		}
		if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			engine.getStateManager().changeState("Menu", gc);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.fill(RECBACK, RECFILLCOLOR);
		g.draw(RECBACK, RECLINECOLOR);
		g.drawString("Back", RECBACK.getMinX() + 10, RECBACK.getMinY() + 8);
		
		if(fullScreenEnabled)
			g.fill(recFullScreen, RECFILLCLICKED);
		else
			g.fill(recFullScreen, RECFILLCOLOR);
		g.draw(recFullScreen, RECLINECOLOR);
		g.drawString("Fullscreen", recFullScreen.getMinX() + 10, recFullScreen.getMinY() + 8);
		g.drawString(fullScreenText, recFullScreen.getMaxX() + settingsWidthMargin, recFullScreen.getMinY() + 8);
		
		if(vSyncEnabled)
			g.fill(recVSync, RECFILLCLICKED);
		else
			g.fill(recVSync, RECFILLCOLOR);
		g.draw(recVSync, RECLINECOLOR);
		g.drawString("V-Sync", recVSync.getMinX() + 10, recVSync.getMinY() + 8);
		g.drawString(vSyncText, recVSync.getMaxX() + settingsWidthMargin, recVSync.getMinY() + 8);
	}
	
	private void buttonPressed(GameContainer gc, int x, int y) {
		if (RECBACK.contains(x, y)) {
			engine.getStateManager().changeState("Menu", gc);
		}else if(recFullScreen.contains(x, y)){
			changeFullScreen(gc);
		}else if(recVSync.contains(x, y)){
			changeVSync(gc);
		}
	}
	
	private void changeFullScreen(GameContainer gc){
		fullScreenEnabled = !fullScreenEnabled;
		updateFullScreenText();
		
		try {
			gc.setFullscreen(fullScreenEnabled);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(Display.getDisplayMode());
	}
	
	private void changeVSync(GameContainer gc){
		vSyncEnabled = !vSyncEnabled;
		updateVSyncText();
		
		gc.setVSync(vSyncEnabled);
	
	}
	
	private void updateFullScreenText(){
		if(fullScreenEnabled) 
			fullScreenText = "Enabled";
		else
			fullScreenText = "Disabled";
	}
	
	private void updateVSyncText(){
		
		if(vSyncEnabled)
			vSyncText = "Enabled";
		else
			vSyncText = "Disabled";
	}
}
