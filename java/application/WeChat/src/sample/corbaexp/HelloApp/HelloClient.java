package sample.corbaexp.HelloApp;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class HelloClient {
	static Hello helloImpl;

	public static void main(String args[]) {
		try {
			Properties prop = new Properties();
			prop.put("org.omg.CORBA.ORBInitialHost", "localhost");
			prop.put("org.omg.CORBA.ORBInitialPort", "900");

			// create and initialize the ORB
			// ORB orb = ORB.init(args, null);
			ORB orb = ORB.init(args, prop);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "Hello";
			helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));

			System.out.println("Obtained a handle on server object: "
					+ helloImpl);
			System.out.println(helloImpl.sayHello());
			helloImpl.shutdown();

		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}
	}

}
