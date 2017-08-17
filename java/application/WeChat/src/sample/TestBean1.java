package sample;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * プロパティの変更を通知する仕組みのテストクラスです.<br/>
 * 
 * @author gsf_zero1
 * 
 */
public class TestBean1 {

	/** プロパティの変更通知をサポートするオブジェクト */
	private PropertyChangeSupport changes;

	/** NAME */
	private String name;

	/** AGE */
	private Integer age;

	/** Creates a new instance of TestBean1 */
	public TestBean1() {
		changes = new PropertyChangeSupport(this);
	}

	/**
	 * リスナの追加を行います.<br/>
	 * 
	 * @param l
	 *            追加するリスナ
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	/**
	 * リスナの削除を行います.<br/>
	 * 
	 * @param l
	 *            削除するリスナ
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}

	/**
	 * NAMEを取得します.<br/>
	 * 
	 * @return NAME
	 */
	public String getName() {
		return name;
	}

	/**
	 * NAMEを設定します.<br/>
	 * 
	 * @param name
	 *            NAME
	 */
	public void setName(String name) {

		String oldValue = getName();
		String newValue = name;

		this.name = name;

		this.changes.firePropertyChange("name", oldValue, newValue);
	}

	/**
	 * AGEを取得します.<br/>
	 * 
	 * @return AGE
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * AGEを設定します.<br/>
	 * 
	 * @param age
	 *            AGE
	 */
	public void setAge(Integer age) {
		Integer oldValue = getAge();
		Integer newValue = age;

		this.age = age;

		this.changes.firePropertyChange("age", oldValue, newValue);
	}

	/**
	 * アプリケーションエントリポイントです.<br/>
	 * 
	 * @param args
	 *            起動時引数
	 */
	public static void main(String[] args) {
		TestBean1 t = new TestBean1();
		TestBean1PropertyChangeListener listener = new TestBean1PropertyChangeListener();

		t.addPropertyChangeListener(listener);

		t.setName("gsf_zero1");
		t.setAge(99);

		t.setName("gsf_zero2");
		t.setAge(18);

		t.removePropertyChangeListener(listener);

		t.setName("gsf_zero3");
		t.setAge(20);
	}
}
