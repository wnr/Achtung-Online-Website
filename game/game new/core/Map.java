/**
 * Map class
 * 
 * The map-class.
 * 
 * 2011-06-07		OMMatte			Modified to work with round and rectangle maps. #17
 * 2011-06-25		OMMatte			Added a picture to render for the circle-map class.
 */

package core;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public abstract class Map {
	protected final static String STANDARD = "Square Map";
	protected final static String ROUND = "Round Map";
	
	protected Image mapImage;
	boolean loaded;
	private int width;
	private int height;
	private Shape gameArea;
	private Shape gameBorder;
	private int edgeSize;
	
	protected String id;
	private Color gameBorderColor;
	private Color bgColor;
	private boolean circle;
	
	public Map(int width, int height){
		this.width = width;
		this.height = height;
		this.gameBorderColor = Color.yellow;
		this.bgColor = new Color(0,0,0,0);
		loaded = false;
	}
	
	public void load(GameContainer gc){
		gc.getGraphics().setBackground(bgColor);
		loaded = true;
	}
	
	public void unload(){
		loaded = false;
	}
	
	public abstract void update(GameContainer gc, int delta);
	
	public void render(GameContainer gc, Graphics g){

		g.fill(gameBorder,new GradientFill(45, 45, gameBorderColor, 46, 46, gameBorderColor));
		g.draw(gameBorder,new GradientFill(45, 45, gameBorderColor, 46, 46, gameBorderColor));
		g.fill(gameArea,new GradientFill(45, 45, Color.black, 46, 46, Color.black));
		g.draw(gameArea,new GradientFill(45, 45, Color.black, 46, 46, Color.black));

	}
	public void renderImage(GameContainer gc, Graphics g) {
		g.drawImage(mapImage, 0, 0);
	}
	public boolean isLoaded(){
		return loaded;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Color getBGColor(){
		return bgColor;
	}
	
	public void setGameArea(boolean circle, int width, int height, int edgeSize){
		this.edgeSize = edgeSize;
		if(circle) {
			this.setCircle(true);
			gameBorder = new Circle(width, width, width);
			gameArea = new Circle(width, width, width-edgeSize);
		}
		else{
			this.setCircle(false);
			gameBorder = new Rectangle(0,0, width, height);
			gameArea = new Rectangle(edgeSize, edgeSize, width-edgeSize*2, height-edgeSize*2);
		}
	}

	public void setBGColor(GameContainer gc, Color color){
		bgColor = color;
		
		gc.getGraphics().setBackground(bgColor);
	}
	
	public void setGameAreaColor(Color color){
		gameBorderColor = color;
	}

	public String getId() {
		return id;
	}

	public void unload(GameContainer gc) {
		loaded = false;
		
	}

	public Shape getGameArea() {
		return gameArea;
	}

	public void setCircle(boolean circle) {
		this.circle = circle;
	}

	public boolean isCircle() {
		return circle;
	}

	public void setEdgeSize(int edgeSize) {
		this.edgeSize = edgeSize;
	}

	public int getEdgeSize() {
		return edgeSize;
	}

	}
	

