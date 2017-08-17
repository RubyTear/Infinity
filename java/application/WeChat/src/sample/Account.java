package sample;

public class Account {

	int money = 100;

	synchronized public int increace(int i) {

		this.money += i;
		return this.money;
	}

}
