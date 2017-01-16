package core.accounts;

import core.main.MessageModel;
import core.view.MessageListener;

public class Account {

	private String name;
	private Integer iD;
	private Inventory inventory;

	private MessageListener activeListener;
	public MessageModel lastSentMsg;

	public Account(int iD, String name){
		this.iD = iD;
		this.name = name;
		this.inventory = new Inventory();
	}

	public Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inv){
		this.inventory = inv;
	}

	public String getName() {
		return name;
	}

	public Integer getID() {
		return iD;
	}

	public MessageListener getListener() {
		return activeListener;
	}
	
	public MessageListener getCurMenu() {
		return activeListener;
	}
	
	public void setListener(MessageListener menu){
		activeListener = menu;
	}
}
