package sample.samplePhoneCORBA;


/**
* sample/samplePhoneCORBA/_HandyPhoneIFStub.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* HandyPhone.idlから
* 2017年6月23日 11時13分38秒 JST
*/

public class _HandyPhoneIFStub extends org.omg.CORBA.portable.ObjectImpl implements sample.samplePhoneCORBA.HandyPhoneIF
{


  // Connect method
  public boolean connect (String senderName, String recieverName)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("connect", true);
                $out.write_wstring (senderName);
                $out.write_wstring (recieverName);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return connect (senderName, recieverName        );
            } finally {
                _releaseReply ($in);
            }
  } // connect


  // disConnect method
  public boolean disConnect (String senderName, String recieverName)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("disConnect", true);
                $out.write_wstring (senderName);
                $out.write_wstring (recieverName);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return disConnect (senderName, recieverName        );
            } finally {
                _releaseReply ($in);
            }
  } // disConnect


  // send message
  public boolean sendMessage (String senderName, String message)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("sendMessage", true);
                $out.write_wstring (senderName);
                $out.write_wstring (message);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return sendMessage (senderName, message        );
            } finally {
                _releaseReply ($in);
            }
  } // sendMessage

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:sample/samplePhoneCORBA/HandyPhoneIF:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _HandyPhoneIFStub
