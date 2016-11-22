package addons.stocks.items;

import addons.TYPE;
import core.FinanceController;
import core.market.items.Item;

public class Stock extends Item {

	int number;
	double lastChange;

	public Stock(String name, int number, double value, double lastChange) {
		this.name = name;
		this.number = number;
		this.value = value;
		this.lastChange = lastChange;
		this.type = TYPE.Stock;
	}

	@Override
	public double getValue(){
		return value*number;
	}

	public void add(int number) {
		this.number += number;
	}

	//	public void register() {
	//		controller.register(name, number, lastChange);
	//	}

	public int getNumber() {
		return number;
	}

	@Override
	public String print() {
		String msg = "";
		String n = lastChange > 0.0 ? "+" : "";
		if(number > 1){
			msg += "Unternehmen:"+getName() +"\nAnzahl: " +number +"\nGesamtwert:" +FinanceController.round(getValue()) +"$, " +n+FinanceController.round(getLastChange()) +"%\n";
		}
		else {
			msg += "Unternehmen:"+getName() +"\nWert:" +FinanceController.round(getValue()) +"$, " +n+FinanceController.round(getLastChange()) +"%\n";
		}
		return msg;
	}

	public void setLastChange(double change) {
		this.lastChange = change;
	}

	public double getLastChange(){
		return lastChange;
	}

	@Override
	public Item copy() {
		return new Stock(name, number, value, lastChange);
	}

	@Override
	public void setOptions(String[] options) {
		try {
			String price = options[0].replace("$", "");
			this.number = (int) (Integer.parseInt(price) / value);
		}
		catch(NumberFormatException e){}
	}

	@Override
	public String[] getOptionMessages() {
		String[] options = new String[1];
		options[0] = "Für wieviel Geld möchten Sie Aktien der Firma " +name +" kaufen? (Bitte Betrag in $ angeben!)";
		return options;
	}

	@Override
	public String toSaveString() {
		return name +NEXT+ type +NEXT+ number +NEXT+ value +NEXT+ lastChange;
	}

	@Override
	public void stringToObject(String string) {
		String[] data = string.split(NEXT);
		this.name = data[0];
		this.number = Integer.parseInt(data[2]);
		this.value = Double.parseDouble(data[3]);
		this.lastChange = Double.parseDouble(data[4]);
	}

	@Override
	public String getShort() {
		if(number > 1){
			return name + ": " + FinanceController.round(getValue()) +"$";
		}
		else {
			String n = lastChange > 0.0 ? "+" : "";
			return name + ": " +n+ FinanceController.round(lastChange) +"%";
		}

	}

}
