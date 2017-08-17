package sample;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * TestBean1�p�̃e�X�g���X�i�ł�.<br/>
 * 
 * @author gsf_zero1
 */
class TestBean1PropertyChangeListener implements PropertyChangeListener {

	/**
	 * �v���p�e�B�̕ύX�C�x���g�������s���܂�.<br/>
	 * 
	 * @param evt
	 *            �C�x���g�I�u�W�F�N�g
	 */
	public void propertyChange(PropertyChangeEvent evt) {

		System.out.printf(
				"source:%s, propertyName:%s, oldValue:%s, newValue:%s\n",
				evt.getSource(), evt.getPropertyName(), evt.getOldValue(),
				evt.getNewValue());

	}

}
