package sample;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * TestBean1用のテストリスナです.<br/>
 * 
 * @author gsf_zero1
 */
class TestBean1PropertyChangeListener implements PropertyChangeListener {

	/**
	 * プロパティの変更イベント処理を行います.<br/>
	 * 
	 * @param evt
	 *            イベントオブジェクト
	 */
	public void propertyChange(PropertyChangeEvent evt) {

		System.out.printf(
				"source:%s, propertyName:%s, oldValue:%s, newValue:%s\n",
				evt.getSource(), evt.getPropertyName(), evt.getOldValue(),
				evt.getNewValue());

	}

}
