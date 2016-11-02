package menus;

import main.FinanceController;
import util.Menu;

public class NoMenu extends Menu {

	@Override
	public void show(Integer userID) {}

	@Override
	public void answerReceived(String msg, Integer userID) {
		if(msg.toLowerCase().equals("/login")) {
			MainMenu menu = new MainMenu();
			FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
			menu.show(userID);
		}
	}

}
