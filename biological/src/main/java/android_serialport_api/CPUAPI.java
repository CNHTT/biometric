package android_serialport_api;


import android.os.SystemClock;
import android.util.Log;

/**
 * @author Administrator
 *
 */
public class CPUAPI {
	private static final byte[] SWITCH_COMMAND = "D&C00040104".getBytes();
	private static final byte[] CONFIGURATION_READER_MODE = "c05060102\r\n"
			.getBytes();
	private static final byte[] CONFIGURATION_PROTOCOL_MODE = "c05060c1001\r\n"
			.getBytes();
	private static final byte[] SET_CHECK_CODE = "c05060c04c1\r\n".getBytes();
	private static final byte[] FIND = "f26\r\n".getBytes();
	private static final String COLLISION_SELECT_CARD = "f9320\r\n";
	private static final String SELECT = "f9370";
	private static final byte[] RESET = "fE051\r\n".getBytes();
	private static final byte[] GET_CHALLENGE = "f0a010084000008\r\n".getBytes();

	
	private static final String ENTER = "\r\n";
	
	private static final String NO_RESPONSE = "No response from card.";

	private byte[] mBuffer = new byte[1024];


	/**
	 * ����˵�����л�ָ��
	 * @return
	 */
	public boolean switchStatus() {
		sendCommand(SWITCH_COMMAND);
		Log.i("whw", "SWITCH_COMMAND hex=" + new String(SWITCH_COMMAND));
		SystemClock.sleep(200);
		SerialPortManager.switchRFID = true;
		return true;
	}

	/**
	 * ����˵�������ö�����ģʽ
	 * @return ����  RF carrier on! ISO/IEC14443 TYPE A, 106KBPS.���óɹ����޷�����ͨѶ�쳣
	 */
	public String configurationReaderMode() {
		int length = receive(CONFIGURATION_READER_MODE, mBuffer);
		String receiveData = new String(mBuffer, 0, length);
		Log.i("whw", "configurationReaderMode   str=" + receiveData);
		if (length > 0
				&& receiveData.startsWith("RF carrier on! ISO/IEC14443 TYPE A, 106KBPS.")) {
			return receiveData;
		}
		return "";
	}

	/**
	 * ����˵�������ݿ�Ƭ�������ö���Э��ģʽ�����ز� ���� ���Ʒ�ʽ��
	 * @return ��ȷ ���� 0x01��01Ϊд���ֵ �ٴζ����Ľ�� �����ж�д���Ƿ�ɹ���
	 */
	public String configurationProtocolMode() {
		int length = receive(CONFIGURATION_PROTOCOL_MODE, mBuffer);
		String receiveData = new String(mBuffer, 0, length);
		Log.i("whw", "configurationProtocolMode   str=" + receiveData);
		if (length > 0 && receiveData.startsWith("0x01")) {
			return receiveData;
		}
		return "";
	}

	/**
	 * ����˵�������ö�������У���뷽ʽ �Զ����ճ�ʱ�б�
	 * @return ��ȷ ���� 0xc1��c1Ϊд���ֵ �ٴζ����Ľ�� �����ж�д���Ƿ�ɹ���
	 */
	public String setCheckCode() {
		int length = receive(SET_CHECK_CODE, mBuffer);
		String receiveData = new String(mBuffer, 0, length);
		Log.i("whw", "setCheckCode   str=" + receiveData);
		if (length > 0 && receiveData.startsWith("0xc1")) {
			return receiveData;
		}
		return "";
	}

	/**
	 * ����˵����Ѱ�������ձ�׼�涨��ͨѶЭ���ʽ��ʹ�ñ�׼�����Ŀ¼�������Ѱ����
	 * @return ����ֵ���ݲ�ͬ�������ж�  �޷���ֵѰ��ʧ��
	 */
	public String findCard() {
		int length = receive(FIND, mBuffer);
		String receiveData = new String(mBuffer, 0, length).trim();
		Log.i("whw", "findCard   str=" + receiveData);
		if (length > 0) {
			if(!receiveData.startsWith(NO_RESPONSE)){
				return receiveData;
			}
		}
		return "";
	}
	
	/**
	 * ����˵������ײѡ��
	 * @return ��ȷ���ؿ��� ����֮�޷���ֵ
	 */
	public String collisionSecectCard(){
		int length = receive(COLLISION_SELECT_CARD.getBytes(), mBuffer);
		String receiveData = new String(mBuffer, 0, length).trim();
		Log.i("whw", "CollisionSecectCard   str=" + receiveData);
		if (length > 0) {
			if(!receiveData.startsWith(NO_RESPONSE)){
				return receiveData;
			}
		}
		return "";
	}
	
	/**
	 * ����˵����ѡ��
	 * @param cardNum ��ȡ�Ŀ���
	 * @return �ɹ��з���ֵ ����֮ʧ��
	 */
	public String selectCard(String cardNum){
		byte[] command = (SELECT+cardNum+ENTER).getBytes();
		int length = receive(command, mBuffer);
		String receiveData = new String(mBuffer, 0, length).trim();
		Log.i("whw", "selectCard   str=" + receiveData);
		if (length > 0) {
			if(!receiveData.startsWith(NO_RESPONSE)){
				return receiveData;
			}
		}
		return "";
	}
	
	/**
	 * ����˵������λ��Ƭ
	 * @return �ɹ��з���ֵ ����֮ʧ��
	 */
	public boolean  reset(){
		int length = receive(RESET, mBuffer);
		String receiveData = new String(mBuffer, 0, length).trim();
		Log.i("whw", "reset   str=" + receiveData);
		if (length > 0) {
			if(!receiveData.startsWith(NO_RESPONSE)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ����˵������ȡ�����
	 * @return �ɹ����������
	 */
	public String getChallenge(){
		int length = receive(GET_CHALLENGE, mBuffer);
		String receiveData = new String(mBuffer, 0, length).trim();
		Log.i("whw", "getChallenge   str=" + receiveData);
		if (length > 0) {
			if(!receiveData.startsWith(NO_RESPONSE)){
				return receiveData;
			}
		}
		return "";
	}
	

	private void sendCommand(byte[] command) {
		SerialPortManager.getInstance().write(command);
	}
	
	private int receive(byte[] command, byte[] buffer) {
		int length = -1;
		if (!SerialPortManager.switchRFID) {
			switchStatus();
		}
		SerialPortManager.getInstance().write(command);
		Log.i("whw", "command hex=" + new String(command));
		length = SerialPortManager.getInstance().read(buffer, 150, 10);
		return length;
	}
}
