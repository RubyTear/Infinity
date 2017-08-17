package sample.samplePhoneCORBA;


/**
* sample/samplePhoneCORBA/HandyPhoneIFOperations.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* HandyPhone.idlから
* 2017年6月23日 11時13分38秒 JST
*/

public interface HandyPhoneIFOperations 
{

  // Connect method
  boolean connect (String senderName, String recieverName);

  // disConnect method
  boolean disConnect (String senderName, String recieverName);

  // send message
  boolean sendMessage (String senderName, String message);
} // interface HandyPhoneIFOperations
