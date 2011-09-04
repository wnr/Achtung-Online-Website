package core;

import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MenuSection extends MenuItem {
	private LinkedList<MenuItem> items;
	
	public MenuSection(int x, int y){
		this(x, y, 0, 0);
	}
	
	public MenuSection(int x, int y, int width, int height){
		super(x, y, width, height);
		
		items = new LinkedList<MenuItem>();
	}

	public void update(GameContainer gc) {
		for(MenuItem item : items){
			item.update(gc);
		}
	}

	public void render(Graphics g) {
		g.drawRect(x, y, width, height);
		for(MenuItem item : items){
			item.render(g);
		}
	}

	public void init(GameContainer gc) {
		for(MenuItem item : items){
			item.init(gc);
		}
	}
	
	public void add(MenuItem item){
		items.add(item);
	}
	
	public void calculateRelativeWidth(){
		int itemsWidth = 0;
		int distance = 0;
		
		for(MenuItem item : items){
			itemsWidth += item.getWidth();
		}
		
		distance = (width - itemsWidth) / (items.size()+1);
		
		int d = distance;
		
		for(MenuItem item : items){
			item.setX(d);
			d += distance + item.getWidth();
		}
	}
}