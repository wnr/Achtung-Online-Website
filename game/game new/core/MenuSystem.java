/**
 * 2011-07-31		mrWiener		Created class.
 */

package core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MenuSystem {
	MenuSection[] sections;
	
	public MenuSystem(int sections){
		this.sections = new MenuSection[sections];
	}
	
	public void setSection(MenuSection section, int index){
		sections[index] = section;
	}
	
	public void add(MenuItem item, int section){
		sections[section].add(item);
	}
	
	public void init(GameContainer gc){
		for(MenuSection section : sections){
			section.init(gc);
		}
	}
	
	public void update(GameContainer gc){
		for(MenuSection section : sections){
			section.update(gc);
		}
	}
	
	public void render(Graphics g){
		for(MenuSection section : sections){
			section.render(g);
		}
	}
	
	public MenuSection getSection(int index){
		return sections[index];
	}
	
}
