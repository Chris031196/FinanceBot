package addons.stocks.items;

import addons.TYPE;
import core.FinanceController;
import core.market.items.Item;

public class Stock extends Item {

	int number;
	double lastChange;

	public Stock(String name, double value, String description, String additionalData) {
		this.name = name;
		this.number = Integer.parseInt(additionalData.split(NEXT)[0]);
		this.value = value;
		this.lastChange = Integer.parseInt(additionalData.split(NEXT)[1]);
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
		return new Stock(name, value, description, getAdditionalData());
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
	public String getShort() {
		if(number > 1){
			return name + ": " + FinanceController.round(getValue()) +"$";
		}
		else {
			String n = lastChange > 0.0 ? "+" : "";
			return name + ": " +n+ FinanceController.round(lastChange) +"%";
		}
	}

	@Override
	public String getAdditionalData() {
		return number+NEXT+lastChange;
	}
}
