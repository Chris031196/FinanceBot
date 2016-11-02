package util;


import java.time.LocalDateTime;

import main.FinanceController;
import main.IOController;
import menus.inventory.InventoryMenu;

public class Plane extends Item {

	public Plane(String name, double value, int chance, String description) {
		this.name = name;
		this.type = TYPE.Plane;
		this.value = value;
		this.chance = chance;
		this.description = description;
	}


	@Override
	public void use(String msg, Integer userID) {
		int hour = LocalDateTime.now().getHour();
		if(hour <= 11 && hour >= 7){
			FlyMenu menu = new FlyMenu(this);
			FinanceController.getInstance().getAccount(userID).setCurMenu(menu);
			menu.show(userID);
		}
		else {
			IOController.sendMessage("Sie sollten zwischen 09:00 Uhr und 13:00 Uhr starten. Sonst haben Sie keine Chance!", null, userID.toString(), true);
		}
	}

	public void addItem(Item item){
		if(description == ""){
			description += item.getName();
		}
		else {
			description += "\n" +item.getName();
		}
		chance += item.getChance();
		value += item.getValue();
	}

	public int getChance() {
		return chance;
	}

	private class FlyMenu extends Menu {

		Plane plane;

		public FlyMenu(Plane plane){
			this.plane = plane;
		}

		@Override
		public void show(Integer userID) {
			IOController.sendMessage("Wie viele Kilometer möchten Sie ausschreiben? (Wahrscheinlichkeiten werden mit dem Chancesummanden des Flugzeuges addiert)", new String[]{"100km FAI (90%)","100","300km FAI (70%)","300","500km FAI (50%)","500","700km FAI (30%)","700","1000km FAI (0%)","1000","🔙","cancel"}, userID.toString(), false);

		}

		@Override
		public void answerReceived(String msg, Integer userID) {
			if(msg.equals("cancel")){
				InventoryMenu menu = new InventoryMenu();
				FinanceController.getInstance().getAccount(userID).setCurMenu(menu);;
				menu.show(userID);
			}
			try{
				Account acc = FinanceController.getInstance().getAccount(userID);
				int distance = Integer.parseInt(msg);
				plane.setValue(plane.getValue() * 3/4);
				double chance = 100;
				chance -= (distance / 10);
				chance += plane.getChance();

				double random = Math.random() * 100;
				IOController.sendMessage("Flug gestartet! Der Flug dauert 6 Stunden.", new String[]{"🔙","cancel"}, userID.toString(), false);
				acc.getItems().remove(plane);
				if(random <= chance){
					Thread fly = new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(21600000);
								//								Thread.sleep(20000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							IOController.sendMessage("Sie sind rumgekommen! Sie bekommen " +distance/100 +"+ auf Ihr Punktekonto!", null, userID.toString(), true);
							acc.addPop(distance/100);
							acc.save();
							acc.addItem(plane);
						}
					});
					fly.start();
				}
				else {
					Thread fly = new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(21600000);
								//								Thread.sleep(20000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							IOController.sendMessage("Leider sind Sie nicht rumgekommen!", null, userID.toString(), true);
							acc.addItem(plane);
						}
					});
					fly.start();
				}
			}
			catch(NumberFormatException e){}
		}

	}
}
