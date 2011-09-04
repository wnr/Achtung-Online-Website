/**
 * Hanterar representationen av en tangent på skrivbordet.
 * 
 * Just nu stöds endast textrepresentationer, men senare kanske man kan använda bilder eller symboler också.
 * 
 * 2011-06-07		mrWiener		Created class.
 */

package core;

import org.newdawn.slick.Input;

public class KeyRepresenter {
	private class Key{
		private String text;
		
		public Key(int code){
			this.text = Input.getKeyName(code);
		}
		
		public String getStringRepresentation(){
			return this.text;
		}
	}
	
	public KeyRepresenter(){
		
	}
	
	public String representKey(Integer code){
		if(code == null)
			return "";
		
		Key key = new Key((int)code);
		return key.getStringRepresentation();
	}
}
