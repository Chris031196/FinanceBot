package menus.generic;

import java.util.ArrayList;

import util.Item;
import util.Menu;

public class SelectionMenu extends Menu {
	
	private ArrayList<Item> options;
	private String message;
	
	public SelectionMenu(ArrayList<Item> options, String message){
		this.options = options;
		this.message = message;
	}

	@Override
	public void show(Integer userID) {
		
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		// TODO Auto-generated method stub

	}

}
