package android_serialport_api;


import android.content.Context;

import com.szfp.DataUtils;


public class PsamAPI {
	private static final byte[] OPEN_PSAM = "D&C0004010I".getBytes();
	private static final byte[] CLOSE_PSAM = "D&C0004010J".getBytes();
	private static final byte[] Reset_PASM = { (byte) 0xca, (byte) 0xdf,
			(byte) 0x05, (byte) 0x36, (byte) 0x00, (byte) 0xe3 };
	private static final byte[] Down_PSAM = { (byte) 0xca, (byte) 0xdf,
			(byte) 0x05, (byte) 0x36, (byte) 0x01, (byte) 0xe3 };
	private static final byte[] SELECT0_PSAM = { (byte) 0xca, (byte) 0xdf,
			(byte) 0x05, (byte) 0x36, (byte) 0x02, (byte) 0xe3 };
	private static final byte[] SELECT1_PSAM = { (byte) 0xca, (byte) 0xdf,
			(byte) 0x05, (byte) 0x36, (byte) 0x03, (byte) 0xe3 };
	private byte[] header = { (byte) 0xca, (byte) 0xdf, (byte) 0x05,
			(byte) 0x35, (byte) 0x03, (byte) 0x72, (byte) 0x00, (byte) 0x05,
			(byte) 0xE3 };// ��6���ֽ�72��ʾ��ʼ��7-8���ֽڱ�ʾ�ֽڳ���
	private static final byte[] end = { (byte) 0xca, (byte) 0xdf, (byte) 0x05,
			(byte) 0x35, (byte) 0x01, (byte) 0x73, (byte) 0xe3 };

	private OnPsamCallback callback;
	private byte[] buffer;

	public PsamAPI(Context context, OnPsamCallback callback) {
		this.callback = callback;
	}

	/**
	 * ��psamģ��
	 */
	public void open() {
		buffer = new byte[1024];
		SerialPortManager.getInstance().write(OPEN_PSAM);
		int length = SerialPortManager.getInstance().read(buffer, 4000, 200);
		if (length > 0 && (buffer[0] == 79)) {
			callback.OnOpenSuccess();
			buffer = null;
		} else {
			callback.OnOpenFailed();
			buffer = null;
		}
	}

	/**
	 * �ر�psamģ��
	 */
	public void close() {
		SerialPortManager.getInstance().write(CLOSE_PSAM);
	}

	/**
	 * ��λ
	 */
	public void Reset() {
		buffer = new byte[1024];
		SerialPortManager.getInstance().write(Reset_PASM);
		int length = SerialPortManager.getInstance().read(buffer, 4000, 200);
		if (length > 0) {
			callback.OnResetSuccess();
			buffer = null;
		} else {
			callback.OnResetFailed();
			buffer = null;
		}
	}

	/**
	 * psam�µ�
	 */
	public void Down() {
		SerialPortManager.getInstance().write(Down_PSAM);
	}

	/**
	 * ѡ��psam��1
	 */
	public void SELECT0() {
		buffer = new byte[1024];
		SerialPortManager.getInstance().write(SELECT0_PSAM);
		int length = SerialPortManager.getInstance().read(buffer, 4000, 200);
		if (length > 0) {
			callback.OnSelectSam0Success();
			buffer = null;
		} else {
			callback.OnSelectSam0Failed();
			buffer = null;
		}
	}

	/**
	 * ѡ��psam��2
	 */
	public void SELECT1() {
		buffer = new byte[1024];
		SerialPortManager.getInstance().write(SELECT1_PSAM);
		int length = SerialPortManager.getInstance().read(buffer, 4000, 200);
		if (length > 0) {
			callback.OnSelectSam1Success();
			buffer = null;
		} else {
			callback.OnSelectSam1Failed();
			buffer = null;
		}
	}

	/**
	 * �Զ���ָ��
	 * 
	 * @param data
	 *            ���͵�����
	 * @throws InterruptedException
	 */
	public void Custom(String data) throws InterruptedException {
		buffer = new byte[1024];
		int n = data.length() / 2;
		header[7] = (byte) n;
		SerialPortManager.getInstance().write(header);
		Thread.sleep(1000);
		DataUtils.toHexString(header);
		SerialPortManager.getInstance().write(getBytes(data));
		Thread.sleep(1000);
		DataUtils.toHexString(getBytes(data));
		SerialPortManager.getInstance().write(end);
		DataUtils.toHexString(end);
		int len = SerialPortManager.getInstance().read(buffer, 4000, 200);
		if (len > 0) {
			byte[] newdata = new byte[len];
			System.arraycopy(buffer, 0, newdata, 0, len);
			callback.OnCustomSamSuccess(newdata);
		} else {
			callback.OnCustomSamFailed();
		}
		buffer = null;
	}

	/**
	 * ��ָ����װ
	 * 
	 * @param data
	 *            ���ܵ�ԭ����
	 * @return �µ�ָ������
	 */
	private byte[] getBytes(String data) {
		int n = data.length() / 2;
		byte[] newbyte = new byte[6 + n];
		byte[] hexData = DataUtils.hexStringTobyte(data);
		byte[] header = { (byte) 0xca, (byte) 0xdf, (byte) 0x05, (byte) 0x35 };
		System.arraycopy(header, 0, newbyte, 0, header.length);
		newbyte[header.length] = (byte) n;
		System.arraycopy(hexData, 0, newbyte, 5, hexData.length);
		newbyte[newbyte.length - 1] = (byte) 0xe3;
		return newbyte;
	}

	/**
	 * psam����صĻص��ӿ�
	 * 
	 * @author user
	 * 
	 */
	public interface OnPsamCallback {
		void OnOpenSuccess();

		void OnOpenFailed();

		void OnResetSuccess();

		void OnResetFailed();

		void OnSelectSam0Success();

		void OnSelectSam0Failed();

		void OnSelectSam1Success();

		void OnSelectSam1Failed();

		void OnCustomSamSuccess(byte[] data);

		void OnCustomSamFailed();
	}
}