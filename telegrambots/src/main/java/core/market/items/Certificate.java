package core.market.items;

import java.time.LocalDateTime;

import addons.TYPE;
import addons.planes.functions.Flight;
import addons.planes.items.Plane;
import core.FinanceController;

public class Certificate extends Item {

	public Certificate(Flight flight) {
		Plane plane = flight.getPlane();
		int distance = flight.getDistance();
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
	public String toSaveString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stringToObject(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOptions(String[] options) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getOptionMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShort() {
		// TODO Auto-generated method stub
		return null;
	}
}
