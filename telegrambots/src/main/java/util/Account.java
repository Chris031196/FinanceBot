package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;

import main.FinanceController;
import menus.NoMenu;

public class Account {

	private String name;
	private Integer iD;
	private double money;
	private int pop;
	private Inventory inventory;

	private ArrayList<MessageListener> activeListeners = new ArrayList<MessageListener>();
	public ArrayList<Integer> lastSentMsgs = new ArrayList<Integer>();

	public Account(int iD, String name, double money, int pop, HashMap<String, Integer> stocks){
		this.iD = iD;
		this.name = name;
		this.money = money;
		this.pop = pop;
		this.inventory = new Inventory();
		activeListeners.add(new NoMenu());
	}

	public Account(int iD){
		this.inventory = new Inventory();
		this.load(iD);
		activeListeners.add(new NoMenu());
	}

	public void save(){
		Properties save = new Properties();

		save.put("money",""+money);
		save.put("name",""+name);
		save.put("pop",""+pop);

		try {
			save.store(new FileOutputStream(getSaveFile()), null);
			inventory.save(iD);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void load(int iD){
		this.iD = iD;

		Properties save = new Properties();
		try {
			save.load(new FileInputStream(getSaveFile()));
			inventory.load(iD);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.name = save.getProperty("name");
		this.money = Double.parseDouble(save.getProperty("money"));
		this.pop = Integer.parseInt(save.getProperty("pop"));
	}

	public String getSaveFile(){
		return FinanceController.accountsFolder +iD +".mgs";
	}

	public double getMoney() {
		return money;
	}

	public int getPop() {
		return pop;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void addPop(int pop){
		this.pop += pop;
	}

	public void addMoney(double money){
		this.money += money;
	}

	public String getName() {
		return name;
	}

	public Integer getID() {
		return iD;
	}

	public String toString(){
		String string =  iD +"_" +name +"_" +money +"_" +pop;
//		for(Entry<String, Integer> entry: inventory.getStocks().entrySet()){
//			string += "_" +entry.getKey() +"_" +entry.getValue();
//		}
		return string;
	}

	public MessageListener[] getListeners() {
		return activeListeners.toArray(new MessageListener[]{});
	}
	
	public Menu getCurMenu() {
		for(MessageListener ml: activeListeners){
			if(ml instanceof Menu){
				return (Menu) ml;
			}
		}
		return new NoMenu();
	}
	
	public void setMenu(Menu menu){
		activeListeners.remove(getCurMenu());
		activeListeners.add(menu);
	}

	public void addListener(MessageListener listener) {
		activeListeners.add(listener);
	}
}
