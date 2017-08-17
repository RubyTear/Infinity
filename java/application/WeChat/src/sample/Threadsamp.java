package sample;

public class Threadsamp implements Runnable {

	int a, b;
	String name;
	Account acc;

	public Threadsamp(String name, Account acc) {
		a = 1;
		b = 2;
		this.name = name;
		this.acc = acc;
	}

	public static void main(String[] args) throws InterruptedException {
		Account acc = new Account();
		Threadsamp ths_1_ = new Threadsamp("heyhey", acc);
		Threadsamp ths_2_ = new Threadsamp("heyhey2222222", acc);
		Thread th_1_ = new Thread(ths_1_);
		Thread th_2_ = new Thread(ths_2_);
		th_1_.start();
		th_2_.start();
	}

	@Override
	public void run() {
		System.out.println(this.name);
		// try {
		// Thread.sleep(1000 * 5);
		// } catch (InterruptedException e) {
		// // TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
		// e.printStackTrace();
		// }
		this.a = this.acc.increace(sam(a, b));
		System.out.println(this.name + ":" + a);
	}

	public int sam(int a, int b) {
		return a + b;
	}

}
