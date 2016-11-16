package old.menus.flight;

import functions.plane.Flight;
import main.IOController;
import view.Menu;

public class DecisionMenu extends Menu {
	
	Flight flight;
	Menu last;
	
	public DecisionMenu(Flight flight, Menu last){
		this.flight = flight;
		this.last = last;
	}

	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Ihre " +flight.getPlane().getName() +" hat eine kritische Höhe erreicht! Was möchten Sie tun?", new String[]{"Weiterfliegen!","fly","Umkehren","return"}, userID.toString(), false);
	}

	@Override
	public void messageReceived(String msg, Integer userID) {
		switch(msg) {
		case "fly":
			flight.flyNextPart();
			flight.getAccount().setMenu(last);
			last.show(userID);
		case "return":
			flight.abort();
			flight.getAccount().setMenu(last);
			last.show(userID);
		}
		
	}
}
