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
 * 携帯電話アプリのサーバ機能クラス。
 * 
 * @author Acroquest
 * 
 */
public class PhoneServer {
	/** ORB. */
	private org.omg.CORBA.ORB orb_;

	/**
	 * コンストラクタ。
	 * 
	 * @param orb
	 *            ORB
	 */
	public PhoneServer(ORB orb) {
		this.orb_ = orb;
	}

	/**
	 * 携帯電話アプリが接続を受信できる状態にする。
	 * 
	 * @param ownName
	 *            自分の名前
	 * @param handyPhoneImpl
	 *            携帯電話アプリクライアント
	 * @throws CorbaOperationException
	 *             処理異常
	 */
	public void startServer(String ownName, HandyPhoneImpl handyPhoneImpl)
			throws CorbaOperationException {
		org.omg.CORBA.Object handyPhoneRef = null;

		try {
			// ルートPOAを取得する
			// Throw InbalidName
			org.omg.CORBA.Object rootPoaObj = this.orb_
					.resolve_initial_references(PhoneConstant.ORB_ROOT_POA);
			POA rootPoa = POAHelper.narrow(rootPoaObj);
			rootPoa.the_POAManager().activate(); // AdapterInactive

			// サーバントからオブジェクト参照を取得する
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
	 * 携帯電話アプリの接続受信状態を解除する。
	 * 
	 * @param ownName
	 *            自分の名前
	 * @throws CorbaOperationException
	 *             操作異常
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

		// バインド解除
		try {
			nameServiceContext.unbind(nameComponents);
		} catch (NotFound nfExc) {
			throw new CorbaOperationException(nfExc);
		} catch (CannotProceed cpExc) {
			throw new CorbaOperationException(cpExc);
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName inExc) {
			throw new CorbaOperationException(inExc);
		}

		// 接続解除
		HandyPhoneIF handyPhoneIF = this.getRemoteHandyPhone(ownName);
		this.orb_.disconnect(handyPhoneIF);
	}

	/**
	 * 他携帯電話アプリに接続する。
	 * 
	 * @param senderName
	 *            送信者名
	 * @param recieverName
	 *            受信者名
	 * @return 接続成功ならばtrue, 失敗ならばfalse
	 */
	public boolean startConnection(String senderName, String recieverName) {
		HandyPhoneIF recieverIf = null;

		try {
			recieverIf = this.getRemoteHandyPhone(recieverName);
		} catch (CorbaOperationException coExc) {
			// 何もしない
			return false;
		}

		boolean returnFlg = false;

		try {
			returnFlg = recieverIf.connect(senderName, recieverName);
		} catch (COMM_FAILURE cfExc) {
			// 接続先が存在しない時にここの処理が行われる
			// 何もしない
		}

		return returnFlg;
	}

	/**
	 * 他携帯電話アプリとの接続を終了する。
	 * 
	 * @param senderName
	 *            送信者名
	 * @param recieverName
	 *            受信者名
	 * @return 接続が終了できた場合はtrue, 接続遮断が失敗した場合はfalse
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
	 * メッセージを送信する。
	 * 
	 * @param senderName
	 *            送信者名
	 * @param recieverName
	 *            受信者名
	 * @param message
	 *            送信メッセージ
	 * @return 送信結果
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
	 * ルートコンテキストを取得する。
	 * 
	 * @return ルートコンテキスト
	 * @throws InvalidName
	 *             ルートコンテキスト名が不適切な場合
	 */
	private NamingContextExt getNamingContext() throws InvalidName,
			COMM_FAILURE {
		// ルートコンテキストの取得
		org.omg.CORBA.Object nameServiceObj = this.orb_
				.resolve_initial_references(PhoneConstant.ORB_NAME_SERVICE);
		NamingContextExt nameServiceContext = NamingContextExtHelper
				.narrow(nameServiceObj);

		return nameServiceContext;
	}

	/**
	 * ネームコンポーネントを作成する。
	 * 
	 * @param compName
	 *            コンポーネント名
	 * @return ネームコンポーネント
	 */
	private NameComponent[] createNameComponent(String compName) {
		NameComponent[] returnComps = { new NameComponent(compName, "") };
		return returnComps;
	}

	/**
	 * リモートの携帯電話アプリクライアント参照を取得する。
	 * 
	 * @param remoteName
	 *            リモートの携帯電話アプリ名
	 * @return リモート参照
	 * @throws CorbaOperationException
	 *             処理異常
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
