package sample.samplePhoneCORBA;


/**
* sample/samplePhoneCORBA/HandyPhoneIFOperations.java .
* IDL-to-Java�R���p�C��(�|�[�^�u��)�A�o�[�W����"3.2"�ɂ���Đ�������܂���
* HandyPhone.idl����
* 2017�N6��23�� 11��13��38�b JST
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
