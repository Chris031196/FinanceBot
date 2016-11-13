package util;

public class Stock extends Item {
	
	int number;
	
	public Stock(Company company, int number) {
		this.name = company.getName();
		this.number = number;
		this.value = company.getValue()*number;
	}
	
	public void add(int number) {
		this.number += number;
	}

	public int getNumber() {
		return number;
	}
	
}
