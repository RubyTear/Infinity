package sample;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * �v���p�e�B�̕ύX��ʒm����d�g�݂̃e�X�g�N���X�ł�.<br/>
 * 
 * @author gsf_zero1
 * 
 */
public class TestBean1 {

	/** �v���p�e�B�̕ύX�ʒm���T�|�[�g����I�u�W�F�N�g */
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
	 * ���X�i�̒ǉ����s���܂�.<br/>
	 * 
	 * @param l
	 *            �ǉ����郊�X�i
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	/**
	 * ���X�i�̍폜���s���܂�.<br/>
	 * 
	 * @param l
	 *            �폜���郊�X�i
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}

	/**
	 * NAME���擾���܂�.<br/>
	 * 
	 * @return NAME
	 */
	public String getName() {
		return name;
	}

	/**
	 * NAME��ݒ肵�܂�.<br/>
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
	 * AGE���擾���܂�.<br/>
	 * 
	 * @return AGE
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * AGE��ݒ肵�܂�.<br/>
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
	 * �A�v���P�[�V�����G���g���|�C���g�ł�.<br/>
	 * 
	 * @param args
	 *            �N��������
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
