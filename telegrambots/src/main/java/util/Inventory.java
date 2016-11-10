package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
	
	private HashMap<String, Integer> stocks; //name der Firma, Menge der Aktien
	private ArrayList<Item> items;
	private ArrayList<String> certs;
	
	public Inventory(){
		stocks = new HashMap<String, Integer>();
		items = new ArrayList<Item>();
		certs = new ArrayList<String>();
	}
	
	public void save(Integer iD) throws IOException{
		FileOutputStream fos = new FileOutputStream("save/accounts/inv" +iD +".mgs");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.close();
	}
	
	public void load(Integer iD) {
		
	}
	
	public void addCertificate(String certificate){
		certs.add(certificate);
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
	
	public HashMap<String, Integer> getStocks() {
		return stocks;
	}
}
