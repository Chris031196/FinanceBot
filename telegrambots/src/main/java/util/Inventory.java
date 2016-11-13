package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import main.FinanceController;
import util.Item.TYPE;

public class Inventory implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Item> items;
	private ArrayList<String> certs;
	
	public Inventory(){
		items = new ArrayList<Item>();
		certs = new ArrayList<String>();
	}
	
	public void save(Integer iD) throws IOException {
		FileOutputStream fos = new FileOutputStream(getSaveFile(iD));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.close();
	}
	
	public void load(Integer iD) throws IOException {
		FileInputStream fis = new FileInputStream(getSaveFile(iD));
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			Inventory temp = (Inventory) ois.readObject();
			this.certs = temp.certs;
			this.items = temp.items;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();
	}
	
	public ArrayList<Item> getItemsOfType(TYPE type){
		ArrayList<Item> itemsOfType = new ArrayList<Item>();
		for(Item item: items){
			if(item.getType().equals(type)){
				itemsOfType.add(item);
			}
		}
		return itemsOfType;
	}
	
	public String getSaveFile(Integer iD){
		return FinanceController.accountsFolder +"inv" +iD +".mgs";
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
}
