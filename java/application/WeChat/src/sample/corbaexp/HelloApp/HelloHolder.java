package sample.corbaexp.HelloApp;

/**
* sample/corbaexp/HelloApp/HelloHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* Hello.idlから
* 2017年6月27日 10時50分39秒 JST
*/

public final class HelloHolder implements org.omg.CORBA.portable.Streamable
{
  public sample.corbaexp.HelloApp.Hello value = null;

  public HelloHolder ()
  {
  }

  public HelloHolder (sample.corbaexp.HelloApp.Hello initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = sample.corbaexp.HelloApp.HelloHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    sample.corbaexp.HelloApp.HelloHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return sample.corbaexp.HelloApp.HelloHelper.type ();
  }

}
