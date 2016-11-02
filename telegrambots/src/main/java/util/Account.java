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
	private HashMap<String, Integer> stocks; //name der Firma, Menge der Aktien
	private ArrayList<Item> items;
	private ArrayList<String> certs;

	private Menu curMenu;
	public ArrayList<Integer> lastSentMsgs = new ArrayList<Integer>();

	public Account(int iD, String name, double money, int pop, HashMap<String, Integer> stocks){
		this.iD = iD;
		this.name = name;
		this.money = money;
		this.pop = pop;
		this.stocks = stocks;
		items = new ArrayList<Item>();
		curMenu = new NoMenu();
	}

	public Account(int iD){
		stocks = new HashMap<String, Integer>();
		items = new ArrayList<Item>();
		certs = new ArrayList<String>();
		this.load(iD);
		curMenu = new NoMenu();
	}

	public void save(){
		Properties save = new Properties();

		save.put("money",""+money);
		save.put("name",""+name);
		save.put("pop",""+pop);

		String stocksString = "";
		for(Entry<String, Integer> stock: stocks.entrySet()){
			stocksString += stock.getKey() +"_" +stock.getValue() +"_";
		}
		save.put("stocks", stocksString);

		String itemsString = "";
		for(Item item: items){
			itemsString += item.toString() + "_";
		}
		save.put("items", itemsString);
		
		String certsString = "";
		for(String cert: certs){
			certsString += cert + "_";
		}
		save.put("certs", certsString);

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
		this.money = Double.parseDouble(save.getProperty("money"));
		this.pop = Integer.parseInt(save.getProperty("pop"));

		String[] stocksString = save.getProperty("stocks").split("_");
		for(int i=0;i<stocksString.length-1;i+=2){
			System.out.println("loading " +name +"_" +stocksString[i] +"_");
			stocks.put(stocksString[i], Integer.parseInt(stocksString[i+1]));
		}

		String temp = save.getProperty("items");
		if(temp.length() > 1){
			String[] itemsString = temp.split("_");
			for(int i=0;i<itemsString.length;i++){
				String itemDesc = itemsString[i];
				Item item = Item.getNewItem(itemDesc);
				items.add(item);
			}
		}
		
		temp = save.getProperty("certs");
		if(temp.length() > 1){
			String[] certsString = temp.split("_");
			for(int i=0;i<certsString.length;i++){
				certs.add(certsString[i]);
			}
		}
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

	public void addPop(int pop){
		this.pop += pop;
	}
	
	public void addCertificate(String certificate){
		certs.add(certificate);
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

	public HashMap<String, Integer> getStocks() {
		return stocks;
	}

	public String toString(){
		String string =  iD +"_" +name +"_" +money +"_" +pop;
		for(Entry<String, Integer> entry: stocks.entrySet()){
			string += "_" +entry.getKey() +"_" +entry.getValue();
		}
		return string;
	}

	public ArrayList<String> getCerts() {
		return certs;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void addItem(Item item){
		items.add(item);
	}

	public Menu getCurMenu() {
		return curMenu;
	}

	public void setCurMenu(Menu curMenu) {
		this.curMenu = curMenu;
	}

}
