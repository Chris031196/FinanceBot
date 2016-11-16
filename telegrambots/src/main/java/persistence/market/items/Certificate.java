package persistence.market.items;

import java.time.LocalDateTime;

import controller.FinanceController;
import functions.plane.Flight;

public class Certificate extends Item {
	
	private String certificate;
	
	public Certificate(Flight flight) {
		Plane plane = flight.getPlane();
		int distance = flight.getDistance();
		FinanceController c = FinanceController.getInstance();
		certificate = "URKUNDE\n";
		certificate += "Datum: " +getDate() +"\n";
		certificate += "Pilot: " +flight.getAccount().getName() +"\n";
		certificate += "Flugzeug:\n" +plane.getName() + "\nWert: "+c.round(plane.getValue()) +"$\nÜberlandchance: " +plane.getChance() +"%\n\n";
	}

	@Override
	public String print() {
		return certificate;
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
}
