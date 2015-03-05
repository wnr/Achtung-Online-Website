/**
 * 2011-07-31		mrWiener		Created class.
**/

package core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class MenuItem{
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	MenuItem(int x, int y){
		this(x, y, 0, 0);
	}
	
	MenuItem(int x, int y, int width, int height){
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	
	abstract public void update(GameContainer gc);
	abstract public void render(Graphics g);
	abstract public void init(GameContainer gc);
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return x;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
}