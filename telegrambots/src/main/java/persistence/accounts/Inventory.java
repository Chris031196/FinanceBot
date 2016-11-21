package persistence.accounts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import controller.FinanceController;
import persistence.Stringable;
import persistence.market.items.Item;
import persistence.market.items.Item.TYPE;

public class Inventory implements Stringable{
	
	private static final String LISTCUT = ";newitem;";

	private int pop;
	private double money;
	private ArrayList<Item> items;

	public Inventory(){
		pop = 0;
		money = 10000;
		items = new ArrayList<Item>();
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

	public ArrayList<Item> getItems() {
		return items;
	}

	public void addItem(Item item){
		items.add(item);
	}

	@Override
	public String toSaveString() {

		StringBuilder builder = new StringBuilder();
		builder.append(pop+NEXT);
		builder.append(money+NEXT);
		
		for(Item item: items){
			builder.append(LISTCUT +item.toSaveString());
		}

		return builder.toString();
	}
	
	@Override
	public void stringToObject(String string) {
		String[] parts = string.split(LISTCUT);
		
		String[] data = parts[0].split(NEXT);
		pop = Integer.parseInt(data[0]);
		money = Double.parseDouble(data[1]);
		
		for(int i=1;i<parts.length;i++) {
			items.add(Item.stringToItem(parts[i]));
		}
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

	public void addMoney(double money){
		this.money += money;
	}


}
