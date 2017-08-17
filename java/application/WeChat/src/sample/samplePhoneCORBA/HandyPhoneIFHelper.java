package sample.samplePhoneCORBA;


/**
* sample/samplePhoneCORBA/HandyPhoneIFHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* HandyPhone.idlから
* 2017年6月23日 11時13分38秒 JST
*/

abstract public class HandyPhoneIFHelper
{
  private static String  _id = "IDL:sample/samplePhoneCORBA/HandyPhoneIF:1.0";

  public static void insert (org.omg.CORBA.Any a, sample.samplePhoneCORBA.HandyPhoneIF that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static sample.samplePhoneCORBA.HandyPhoneIF extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (sample.samplePhoneCORBA.HandyPhoneIFHelper.id (), "HandyPhoneIF");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static sample.samplePhoneCORBA.HandyPhoneIF read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_HandyPhoneIFStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, sample.samplePhoneCORBA.HandyPhoneIF value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static sample.samplePhoneCORBA.HandyPhoneIF narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof sample.samplePhoneCORBA.HandyPhoneIF)
      return (sample.samplePhoneCORBA.HandyPhoneIF)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      sample.samplePhoneCORBA._HandyPhoneIFStub stub = new sample.samplePhoneCORBA._HandyPhoneIFStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static sample.samplePhoneCORBA.HandyPhoneIF unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof sample.samplePhoneCORBA.HandyPhoneIF)
      return (sample.samplePhoneCORBA.HandyPhoneIF)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      sample.samplePhoneCORBA._HandyPhoneIFStub stub = new sample.samplePhoneCORBA._HandyPhoneIFStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
