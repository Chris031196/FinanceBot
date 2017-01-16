package addons.planes.items;

import java.time.LocalDateTime;

import addons.TYPE;
import addons.planes.functions.Flight;
import core.FinanceController;
import core.market.items.Item;

public class Certificate extends Item {
	
	public Certificate(){
		this.type = TYPE.Certificate;
	}
	
	public Certificate(String name, double value, String description, String additionalData){
		this.type = TYPE.Certificate;
		
		this.name = name;
		this.value = value;
		this.description = description;
	}

	public Certificate(Flight flight) {
		Plane plane = flight.getPlane();
		int distance = flight.getDistance();
		this.value = 0.0;
		description = "URKUNDE\n";
		description += "Datum: " +getDate() +"\n";
		description += "Pilot: " +flight.getAccount().getName() +"\n";
		description += "Flugzeug:\n" +plane.getName() + "\nWert: "+FinanceController.round(plane.getValue()) +"$\nÜberlandchance: " +plane.getChance() +"%\n\n";
		description += "Distanz:\n" +distance +"km";
		
		this.type = TYPE.Certificate;
	}

	@Override
	public String print() {
		return description;
	}
	
	private static String getDate(){
		LocalDateTime time = LocalDateTime.now();
		int dayNum = time.getDayOfMonth();
		int month = time.getMonth().getValue();
		int year = time.getYear();
		
		return dayNum +"." +month +"." +year;
	}

	@Override
	public void setOptions(String[] options) {}

	@Override
	public String[] getOptionMessages() {
		return new String[]{};
	}

	@Override
	public Item copy() {
		return null;
	}

	@Override
	public String getShort() {
		String[] parts = description.split("Datum:");
		parts = parts[1].split("\n");
		return parts[0];
	}

	@Override
	public String getAdditionalData() {
		return "";
	}
}
