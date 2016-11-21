package persistence.accounts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import view.Menu;
import view.MessageListener;

public class Account {
	
	private static String accountsPath = "save/accounts/";

	private String name;
	private Integer iD;
	private Inventory inventory;

	private MessageListener activeListener;
	public ArrayList<Integer> lastSentMsgs = new ArrayList<Integer>();

	public Account(int iD, String name){
		this.iD = iD;
		this.name = name;
		this.inventory = new Inventory();
	}
	
	public static Account loadAccount(int iD){
		return new Account(iD);
	}

	private Account(int iD){
		this.load(iD);
	}

	public void save(){
		Properties save = new Properties();
		save.put("name",""+name);
		save.put("inv", inventory.toSaveString());

		try {
			save.store(new FileOutputStream(getSaveFile()), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void load(int iD){
		this.iD = iD;

		Properties save = new Properties();
		try {
			save.load(new FileInputStream(getSaveFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.name = save.getProperty("name");
		this.inventory = new Inventory();
		this.inventory.stringToObject(save.getProperty("inv"));
	}

	public String getSaveFile(){
		return Account.accountsPath +iD +".mgs";
	}

	public Inventory getInventory() {
		return inventory;
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
