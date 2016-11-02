package menus.flight;

import main.IOController;
import util.Flight;
import util.Menu;

public class DecisionMenu extends Menu {
	
	Flight flight;
	Menu last;
	
	public DecisionMenu(Flight flight, Menu last){
		this.flight = flight;
		this.last = last;
	}

	@Override
	public void show(Integer userID) {
		IOController.sendMessage("Ihre " +flight.getPlane().getName() +" hat eine kritische Höhe erreicht! Was möchten Sie tun?", new String[]{"Weiterfliegen!","fly","Umkehren","return"}, userID.toString(), true);
	}

	@Override
	public void answerReceived(String msg, Integer userID) {
		switch(msg) {
		case "fly":
			flight.flyNextPart();
			flight.getAccount().setCurMenu(last);
			last.show(userID);
		case "return":
			flight.abort();
			flight.getAccount().setCurMenu(last);
			last.show(userID);
		}
		
	}
}
