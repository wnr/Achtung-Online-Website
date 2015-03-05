/**
 * 2011-07-31		mrWiener		Created class.
**/

package core;

import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;


public class MenuButton extends MenuItem{
	public static int HEIGHT = 35;
	
	private String text;
	private RoundedRectangle rectangle;
	private boolean focused;
	private Color currentColor;

	
	public MenuButton(String text, int x, int y) {
		this(text, x, y, 0, HEIGHT);
	}
	
	public MenuButton(String text, int x, int y, int height, int width){
		super(x, y, height, width);
		
		this.text = text;
		this.focused = false;
	}
	
	public void init(GameContainer gc){
		if(width == 0){
			setWidth(gc.getGraphics().getFont().getWidth(text) + 20);
		}
		if(height == 0){
			setHeight(HEIGHT);
		}
		
		rectangle = new RoundedRectangle(x, y, width, height, 5);
		currentColor = Color.darkGray;
	}
	
	public void update(GameContainer gc){
		
	}
	
	public void render(Graphics g){
		g.fill(rectangle, new GradientFill(45, 45, currentColor, 46, 46, currentColor));
		g.draw(rectangle, new GradientFill(45, 45, Color.yellow, 46, 46, Color.yellow));
		g.drawString(text, x+((width-g.getFont().getWidth(text)) / 2), y+((height-g.getFont().getHeight(text)) / 2));
	}
	
	public void setFocus(boolean focused){
		this.focused = focused;
		
		if(focused)
			currentColor = Color.green;
		else
			currentColor = Color.darkGray;
	}
	
	public void setX(int x){
		super.setX(x);
		
		if(rectangle != null)
			rectangle.setX(x);
	}
	
	public void setY(int y){
		super.setY(y);
		
		if(rectangle != null)
			rectangle.setY(y);
	}
}