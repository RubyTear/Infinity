package sample.corbaexp.HelloApp;


/**
* sample/corbaexp/HelloApp/HelloHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* Hello.idlから
* 2017年6月27日 10時50分39秒 JST
*/

abstract public class HelloHelper
{
  private static String  _id = "IDL:sample/corbaexp/HelloApp/Hello:1.0";

  public static void insert (org.omg.CORBA.Any a, sample.corbaexp.HelloApp.Hello that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static sample.corbaexp.HelloApp.Hello extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (sample.corbaexp.HelloApp.HelloHelper.id (), "Hello");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static sample.corbaexp.HelloApp.Hello read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_HelloStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, sample.corbaexp.HelloApp.Hello value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static sample.corbaexp.HelloApp.Hello narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof sample.corbaexp.HelloApp.Hello)
      return (sample.corbaexp.HelloApp.Hello)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      sample.corbaexp.HelloApp._HelloStub stub = new sample.corbaexp.HelloApp._HelloStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static sample.corbaexp.HelloApp.Hello unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof sample.corbaexp.HelloApp.Hello)
      return (sample.corbaexp.HelloApp.Hello)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      sample.corbaexp.HelloApp._HelloStub stub = new sample.corbaexp.HelloApp._HelloStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
