package util;

import java.time.LocalDateTime;

import main.FinanceController;

public class CertificateBuilder {

	public static String getCertificate(Flight flight){
		Plane plane = flight.getPlane();
		int distance = flight.getDistance();
		FinanceController c = FinanceController.getInstance();
		String certificate = "URKUNDE\n";
		certificate += "Datum: " +getDate() +"\n";
		certificate += "Pilot: " +flight.getAccount().getName() +"\n";
		certificate += "Flugzeug:\n" +plane.getName() + "\nWert: "+c.round(plane.getValue()) +"$\nÜberlandchance: " +plane.getChance() +"%\n\n";
		return certificate;
	}
	
	private static String getDate(){
		LocalDateTime time = LocalDateTime.now();
		int dayNum = time.getDayOfMonth();
		int month = time.getMonth().getValue();
		int year = time.getYear();
		
		return dayNum +"." +month +"." +year;
	}
}
