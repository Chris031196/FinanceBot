package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import main.FinanceController;

public class Company {

	private String name;
	private double value;
	private double lastChange;

	public Company(String name, double value, double lastChange){
		this.name = name;
		this.value = value;
		this.lastChange = lastChange;
	}

	public void startCircleOfLife(){
		Thread life = new Thread(new Runnable() {

			@Override
			public void run() {
				
				FinanceController controller = FinanceController.getInstance();
				while(controller.isRunning()){
					double change=0, min=0, max=20, random;

					if(lastChange < 0){
						min = 0;
						max = 20 + (lastChange / 2.0);
					}
					else if(lastChange > 0){
						min = 0 + (lastChange / 2.0);
						max = 20;
					}
					random = Math.random()*(max - min) + min;
					change = random-10;
					lastChange = change;
					value = value+value*(change/100);
					
					controller.stockChanged(getName());
					
					save();
					
					long waitTime = (long) (Math.random()*16200000+1800000);
//					long waitTime = (long) (Math.random()*10000);
					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		life.start();
	}

	public String getName() {
		return name;
	}
	
	public void save(){
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(FinanceController.logFile));
			String values = properties.getProperty(name) != null ? properties.getProperty(name) +";" +value : "" +value;
			properties.put(name, values);
		} catch (IOException e) {}
		try {
			properties.store(new FileOutputStream(FinanceController.logFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getValue() {
		return value;
	}

	public double getLastChange() {
		return lastChange;
	}

	public String toString(){
		return name +"_" +value +"_" +lastChange;
	}

}



