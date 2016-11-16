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

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(items);
			oos.close();
			builder.append(baos.toByteArray());
		}
		catch (IOException e){
			e.printStackTrace();
		}

		return builder.toString();
	}
	
	@Override
	public void stringToObject(String string) {
		String[] parts = string.split(NEXT);
		pop = Integer.parseInt(parts[0]);
		money = Double.parseDouble(parts[1]);
		
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(parts[2].getBytes());
			ObjectInputStream ois = new ObjectInputStream(bais);
			items = (ArrayList<Item>) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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
