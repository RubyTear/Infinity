package sample.corbaexp.HelloApp;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

class HelloImpl extends HelloPOA {
	private ORB orb;

	public void setORB(ORB orb_val) {
		orb = orb_val;
	}

	// implement sayHello() method
	public String sayHello() {
		return "\nHello world !!\n";
	}

	// implement shutdown() method
	public void shutdown() {
		orb.shutdown(false);
	}
}

public class HelloServer {

	public static void main(String args[]) {
		try {
			Properties prop = new Properties();
			prop.put("org.omg.CORBA.ORBInitialHost", "localhost");
			prop.put("org.omg.CORBA.ORBInitialPort", "900");

			// create and initialize the ORB
			// ORB orb = ORB.init(args, null);
			ORB orb = ORB.init(args, prop);

			// get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			HelloImpl helloImpl = new HelloImpl();
			helloImpl.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
			Hello href = HelloHelper.narrow(ref);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			// Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			String name = "Hello";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);

			System.out.println("HelloServer ready and waiting ...");

			// wait for invocations from clients
			orb.run();
		}

		catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

		System.out.println("HelloServer Exiting ...");

	}
}
