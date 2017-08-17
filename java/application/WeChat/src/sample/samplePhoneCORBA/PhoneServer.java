package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/**
 * �g�ѓd�b�A�v���̃T�[�o�@�\�N���X�B
 * 
 * @author Acroquest
 * 
 */
public class PhoneServer {
	/** ORB. */
	private org.omg.CORBA.ORB orb_;

	/**
	 * �R���X�g���N�^�B
	 * 
	 * @param orb
	 *            ORB
	 */
	public PhoneServer(ORB orb) {
		this.orb_ = orb;
	}

	/**
	 * �g�ѓd�b�A�v�����ڑ�����M�ł����Ԃɂ���B
	 * 
	 * @param ownName
	 *            �����̖��O
	 * @param handyPhoneImpl
	 *            �g�ѓd�b�A�v���N���C�A���g
	 * @throws CorbaOperationException
	 *             �����ُ�
	 */
	public void startServer(String ownName, HandyPhoneImpl handyPhoneImpl)
			throws CorbaOperationException {
		org.omg.CORBA.Object handyPhoneRef = null;

		try {
			// ���[�gPOA���擾����
			// Throw InbalidName
			org.omg.CORBA.Object rootPoaObj = this.orb_
					.resolve_initial_references(PhoneConstant.ORB_ROOT_POA);
			POA rootPoa = POAHelper.narrow(rootPoaObj);
			rootPoa.the_POAManager().activate(); // AdapterInactive

			// �T�[�o���g����I�u�W�F�N�g�Q�Ƃ��擾����
			// Throw ServantNotActive, WrongPolicy
			handyPhoneRef = rootPoa.servant_to_reference(handyPhoneImpl);
		} catch (InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		} catch (AdapterInactive aiExc) {
			throw new CorbaOperationException(aiExc);
		} catch (ServantNotActive snExc) {
			throw new CorbaOperationException(snExc);
		} catch (WrongPolicy wpExc) {
			throw new CorbaOperationException(wpExc);
		}

		NamingContextExt nameServiceContext = null;

		try {
			nameServiceContext = this.getNamingContext();
		} catch (InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		} catch (COMM_FAILURE cfExc) {
			throw new CorbaOperationException(cfExc);
		}

		NameComponent[] path = null;

		try {
			path = nameServiceContext.to_name(ownName);
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		}

		try {
			// throw NotFound, CannotProceed, InvalidName
			nameServiceContext.rebind(path, handyPhoneRef);
		} catch (NotFound nfExc) {
			throw new CorbaOperationException(nfExc);
		} catch (CannotProceed cpExc) {
			throw new CorbaOperationException(cpExc);
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		}
	}

	/**
	 * �g�ѓd�b�A�v���̐ڑ���M��Ԃ���������B
	 * 
	 * @param ownName
	 *            �����̖��O
	 * @throws CorbaOperationException
	 *             ����ُ�
	 */
	public void terminateServer(String ownName) throws CorbaOperationException {
		NameComponent[] nameComponents = this.createNameComponent(ownName);

		NamingContextExt nameServiceContext = null;

		try {
			nameServiceContext = this.getNamingContext();
		} catch (InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		} catch (COMM_FAILURE cfExc) {
			throw new CorbaOperationException(cfExc);
		}

		// �o�C���h����
		try {
			nameServiceContext.unbind(nameComponents);
		} catch (NotFound nfExc) {
			throw new CorbaOperationException(nfExc);
		} catch (CannotProceed cpExc) {
			throw new CorbaOperationException(cpExc);
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		}

		// �ڑ�����
		HandyPhoneIF handyPhoneIF = this.getRemoteHandyPhone(ownName);
		this.orb_.disconnect(handyPhoneIF);
	}

	/**
	 * ���g�ѓd�b�A�v���ɐڑ�����B
	 * 
	 * @param senderName
	 *            ���M�Җ�
	 * @param recieverName
	 *            ��M�Җ�
	 * @return �ڑ������Ȃ��true, ���s�Ȃ��false
	 */
	public boolean startConnection(String senderName, String recieverName) {
		HandyPhoneIF recieverIf = null;

		try {
			recieverIf = this.getRemoteHandyPhone(recieverName);
		} catch (CorbaOperationException coExc) {
			// �������Ȃ�
			return false;
		}

		boolean returnFlg = false;

		try {
			returnFlg = recieverIf.connect(senderName, recieverName);
		} catch (COMM_FAILURE cfExc) {
			// �ڑ��悪���݂��Ȃ����ɂ����̏������s����
			// �������Ȃ�
		}

		return returnFlg;
	}

	/**
	 * ���g�ѓd�b�A�v���Ƃ̐ڑ����I������B
	 * 
	 * @param senderName
	 *            ���M�Җ�
	 * @param recieverName
	 *            ��M�Җ�
	 * @return �ڑ����I���ł����ꍇ��true, �ڑ��Ւf�����s�����ꍇ��false
	 */
	public boolean terminateConnection(String senderName, String recieverName) {
		HandyPhoneIF recieverIf = null;

		try {
			recieverIf = this.getRemoteHandyPhone(recieverName);
		} catch (CorbaOperationException coExc) {
			return false;
		}

		boolean returnFlg = recieverIf.disConnect(senderName, recieverName);

		return returnFlg;
	}

	/**
	 * ���b�Z�[�W�𑗐M����B
	 * 
	 * @param senderName
	 *            ���M�Җ�
	 * @param recieverName
	 *            ��M�Җ�
	 * @param message
	 *            ���M���b�Z�[�W
	 * @return ���M����
	 */
	public boolean sendMessage(String senderName, String recieverName,
			String message) {
		HandyPhoneIF recieverIf = null;

		try {
			recieverIf = this.getRemoteHandyPhone(recieverName);
		} catch (CorbaOperationException coExc) {
			return false;
		}

		boolean isSent = recieverIf.sendMessage(senderName, message);

		return isSent;
	}

	/**
	 * ���[�g�R���e�L�X�g���擾����B
	 * 
	 * @return ���[�g�R���e�L�X�g
	 * @throws InvalidName
	 *             ���[�g�R���e�L�X�g�����s�K�؂ȏꍇ
	 */
	private NamingContextExt getNamingContext() throws InvalidName,
			COMM_FAILURE {
		// ���[�g�R���e�L�X�g�̎擾
		org.omg.CORBA.Object nameServiceObj = this.orb_
				.resolve_initial_references(PhoneConstant.ORB_NAME_SERVICE);
		NamingContextExt nameServiceContext = NamingContextExtHelper
				.narrow(nameServiceObj);

		return nameServiceContext;
	}

	/**
	 * �l�[���R���|�[�l���g���쐬����B
	 * 
	 * @param compName
	 *            �R���|�[�l���g��
	 * @return �l�[���R���|�[�l���g
	 */
	private NameComponent[] createNameComponent(String compName) {
		NameComponent[] returnComps = { new NameComponent(compName, "") };
		return returnComps;
	}

	/**
	 * �����[�g�̌g�ѓd�b�A�v���N���C�A���g�Q�Ƃ��擾����B
	 * 
	 * @param remoteName
	 *            �����[�g�̌g�ѓd�b�A�v����
	 * @return �����[�g�Q��
	 * @throws CorbaOperationException
	 *             �����ُ�
	 */
	private HandyPhoneIF getRemoteHandyPhone(String remoteName)
			throws CorbaOperationException {
		NameComponent[] nameComponents = this.createNameComponent(remoteName);
		NamingContextExt namingContext = null;

		try {
			namingContext = this.getNamingContext();
		} catch (InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		} catch (COMM_FAILURE cfExc) {
			throw new CorbaOperationException(cfExc);
		}

		org.omg.CORBA.Object targetObj = null;
		try {
			targetObj = namingContext.resolve(nameComponents);
		} catch (NotFound nfExc) {
			throw new CorbaOperationException(nfExc);
		} catch (CannotProceed cpExc) {
			throw new CorbaOperationException(cpExc);
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		}

		HandyPhoneIF recieverIf = HandyPhoneIFHelper.narrow(targetObj);
		return recieverIf;
	}
}
