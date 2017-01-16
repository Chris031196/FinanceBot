package core.accounts;

import java.util.ArrayList;

import addons.TYPE;
import core.Stringable;
import core.market.items.Item;

public class Inventory {

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
