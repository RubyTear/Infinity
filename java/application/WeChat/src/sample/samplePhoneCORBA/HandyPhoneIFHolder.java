package sample.samplePhoneCORBA;

/**
* sample/samplePhoneCORBA/HandyPhoneIFHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* HandyPhone.idlから
* 2017年6月23日 11時13分38秒 JST
*/

public final class HandyPhoneIFHolder implements org.omg.CORBA.portable.Streamable
{
  public sample.samplePhoneCORBA.HandyPhoneIF value = null;

  public HandyPhoneIFHolder ()
  {
  }

  public HandyPhoneIFHolder (sample.samplePhoneCORBA.HandyPhoneIF initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = sample.samplePhoneCORBA.HandyPhoneIFHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    sample.samplePhoneCORBA.HandyPhoneIFHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return sample.samplePhoneCORBA.HandyPhoneIFHelper.type ();
  }

}
