package sample.samplePhoneCORBA;


/**
* sample/samplePhoneCORBA/HandyPhoneIFPOA.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* HandyPhone.idlから
* 2017年6月23日 11時13分38秒 JST
*/

public abstract class HandyPhoneIFPOA extends org.omg.PortableServer.Servant
 implements sample.samplePhoneCORBA.HandyPhoneIFOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("connect", new java.lang.Integer (0));
    _methods.put ("disConnect", new java.lang.Integer (1));
    _methods.put ("sendMessage", new java.lang.Integer (2));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {

  // Connect method
       case 0:  // sample/samplePhoneCORBA/HandyPhoneIF/connect
       {
         String senderName = in.read_wstring ();
         String recieverName = in.read_wstring ();
         boolean $result = false;
         $result = this.connect (senderName, recieverName);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }


  // disConnect method
       case 1:  // sample/samplePhoneCORBA/HandyPhoneIF/disConnect
       {
         String senderName = in.read_wstring ();
         String recieverName = in.read_wstring ();
         boolean $result = false;
         $result = this.disConnect (senderName, recieverName);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }


  // send message
       case 2:  // sample/samplePhoneCORBA/HandyPhoneIF/sendMessage
       {
         String senderName = in.read_wstring ();
         String message = in.read_wstring ();
         boolean $result = false;
         $result = this.sendMessage (senderName, message);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:sample/samplePhoneCORBA/HandyPhoneIF:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public HandyPhoneIF _this() 
  {
    return HandyPhoneIFHelper.narrow(
    super._this_object());
  }

  public HandyPhoneIF _this(org.omg.CORBA.ORB orb) 
  {
    return HandyPhoneIFHelper.narrow(
    super._this_object(orb));
  }


} // class HandyPhoneIFPOA
