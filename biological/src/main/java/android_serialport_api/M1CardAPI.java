package android_serialport_api;


import android.util.Log;

import com.szfp.DataUtils;


public class M1CardAPI {
	public static final int KEY_A = 1;
	public static final int KEY_B = 2;

	/**
	 * Ϊ�˼���No response!\r\n �� No responese!\r\n ��ֻ�ж�ǰ�沿��"No respon"
	 */
	private static final String NO_RESPONSE = "No respon";

	// �������ݰ���ǰ׺
	private static final String DATA_PREFIX = "c050605";
	private static final String FIND_CARD_ORDER = "01";// Ѱ��ָ��
	private static final String PASSWORD_SEND_ORDER = "02";// �����·�ָ��
	private static final String PASSWORD_VALIDATE_ORDER = "03";// ������֤����
	private static final String READ_DATA_ORDER = "04";// ��ָ��
	private static final String WRITE_DATA_ORDER = "05";// дָ��
	private static final String ENTER = "\r\n";// ���з�
	private static final String TURN_OFF = "c050602\r\n";// �ر����߳�

	// Ѱ����ָ���
	private static final String FIND_CARD = DATA_PREFIX + FIND_CARD_ORDER
			+ ENTER;

	// �·�����ָ���(A��B�������12����f��)
	private static final String SEND_PASSWORD = DATA_PREFIX
			+ PASSWORD_SEND_ORDER + "ffffffffffffffffffffffff" + ENTER;
	private static final String DEFAULT_PASSWORD = "ffffffffffff";
	private static final String FIND_SUCCESS = "0x00,";
	private static final String WRITE_SUCCESS = " Write Success!" + ENTER;
	public byte[] buffer = new byte[100];

	private int receive(byte[] command, byte[] buffer) {
		int length = -1;
		if (!SerialPortManager.switchRFID) {
			SerialPortManager.getInstance().switchStatus();
		}
		sendCommand(command);
		length = SerialPortManager.getInstance().read(buffer, 300, 5);
		return length;
	}

	private void sendCommand(byte[] command) {
		SerialPortManager.getInstance().write(command);
	}

	/**
	 * ����˵������ȡ�������Ͷ�Ӧ���ֽ����ݣ�Ĭ����������ΪKEYA
	 * 
	 * @param keyType
	 * @return
	 */
	private String getKeyTypeStr(int keyType) {
		String keyTypeStr = null;
		switch (keyType) {
		case KEY_A:
			keyTypeStr = "60";
			break;
		case KEY_B:
			keyTypeStr = "61";
			break;
		default:
			keyTypeStr = "60";
			break;
		}
		return keyTypeStr;
	}

	/**
	 * ����˵����ת���������ĵ�ַΪ��λ
	 * 
	 * @param block
	 *            ���
	 * @return
	 */
	private String getZoneId(int block) {
		return DataUtils.byte2Hexstr((byte) block);
	}

	/**
	 * ����˵������ȡM1������ Read the M1 card number
	 * 
	 * @return Result
	 */
	public Result readCardNum() {
		Log.i("whw", "!!!!!!!!!!!!readCard");
		Result result = new Result();
		byte[] command = FIND_CARD.getBytes();
		int length = receive(command, buffer);
		if (length == 0) {
			result.confirmationCode = Result.TIME_OUT;
			return result;
		}
		String msg = "";
		msg = new String(buffer, 0, length);
		Log.i("whw", "msg hex=" + msg);
		turnOff();
		if (msg.startsWith(FIND_SUCCESS)) {
			result.confirmationCode = Result.SUCCESS;
			result.num = msg.substring(FIND_SUCCESS.length());
		} else {
			result.confirmationCode = Result.FIND_FAIL;
		}
		return result;
	}

	/**
	 * ����˵������֤����
	 * 
	 * @param block
	 * @param keyType
	 * @param keyA
	 * @param keyB
	 * @return
	 */
	public boolean validatePassword(int block, int keyType, String keyA,
			String keyB) {
		byte[] cmd = (DATA_PREFIX + PASSWORD_SEND_ORDER + keyA + keyB + ENTER)
				.getBytes();// �·�����ָ��
		int tempLength = receive(cmd, buffer);// �·���ָ֤��
		String verifyStr = new String(buffer, 0, tempLength);
		Log.i("whw", "validatePassword verifyStr=" + verifyStr);
		byte[] command2 = (DATA_PREFIX + PASSWORD_VALIDATE_ORDER
				+ getKeyTypeStr(keyType) + getZoneId(block) + ENTER).getBytes();
		int length = receive(command2, buffer);// ��֤����
		String msg = new String(buffer, 0, length);
		Log.i("whw", "validatePassword msg=" + msg);
		String prefix = "0x00,\r\n";
		if (msg.startsWith(prefix)) {
			return true;
		}
		return false;
	}

	/**
	 * ��ȡָ����Ŵ洢�����ݣ�����һ��Ϊ16�ֽ� Reads the specified number stored data, length of
	 * 16 bytes
	 * 
	 *            block number
	 * @return
	 */
	public byte[][] read(int startPosition, int num) {
		byte[] command = { 'c', '0', '5', '0', '6', '0', '5', '0', '4', '0',
				'0', '\r', '\n' };
		byte[][] pieceDatas = new byte[num][];
		for (int i = 0; i < num; i++) {
			char[] c = getZoneId(startPosition + i).toCharArray();
			command[9] = (byte) c[0];
			command[10] = (byte) c[1];
			int readTime = 0;
			int length = 0;
			String data = "";
			while (readTime < 3) {
				readTime++;
				length = receive(command, buffer);
				data = new String(buffer, 0, length);
				if (data != null && data.startsWith(NO_RESPONSE)) {
					continue;
				} else {
					break;
				}
			}
			Log.i("whw", "read data=" + data + "   readTime=" + readTime);
			String[] split = data.split(";");
			String msg = "";
			if (split.length == 2) {
				int index = split[1].indexOf("\r\n");
				if (index != -1) {
					msg = split[1].substring(0, index);
				}

				Log.i("whw",
						"split msg=" + msg + "  msg length=" + msg.length());
			}
			pieceDatas[i] = DataUtils.hexStringTobyte(msg);
		}

		return pieceDatas;
	}

	/**
	 * ����˵��: ��ȡָ����Ŵ洢�����ݣ�����һ��Ϊ16�ֽ� Reads the specified number stored data,
	 * length of 16 bytes
	 * 
	 *            ���(S50 ����Ĭ��0-63)
	 * 
	 * @return ����ָ���������
	 */
	public byte[] read(int startPosition) {
		byte[] command = { 'c', '0', '5', '0', '6', '0', '5', '0', '4', '0',
				'0', '\r', '\n' };
		byte[] pieceDatas = null;
		char[] c = getZoneId(startPosition).toCharArray();
		command[9] = (byte) c[0];
		command[10] = (byte) c[1];
		int readTime = 0;
		int length = 0;
		String data = "";
		while (readTime < 3) {
			readTime++;
			length = receive(command, buffer);
			data = new String(buffer, 0, length);
			if (data != null && data.startsWith(NO_RESPONSE)) {
				continue;
			} else {
				break;
			}
		}
		Log.i("xuws", "read data=" + data + "   readTime=" + readTime);
		String[] split = data.split(";");
		String msg = "";
		if (split.length == 2) {
			int index = split[1].indexOf("\r\n");
			if (index != -1) {
				msg = split[1].substring(0, index);
			}
			Log.i("xuws", "split msg=" + msg + "  msg length=" + msg.length());
		}
		pieceDatas = DataUtils.hexStringTobyte(msg);
		return pieceDatas;
	}

	/**
	 * ��ָ���Ŀ��д�����ݣ�����Ϊ16�ֽ� Write data to the specified block, length is 16 bytes
	 * argument should be data[i].length == num
	 * 
	 * @param data
	 * @param startPosition
	 * @param num
	 *            the number of block
	 * @return
	 */
	public boolean write(int block, int num, String data) {
		if (data.length() == 0) {
			return false;
		}
		for (int i = 0; i < num; i++) {
			byte[] command = (DATA_PREFIX + WRITE_DATA_ORDER + getZoneId(block)
					+ data + ENTER).getBytes();
			Log.i("whw", "***write hexStr=" + DataUtils.toHexString(command));
			int length = receive(command, buffer);
			boolean isWrite = false;
			if (length > 0) {
				String writeResult = new String(buffer, 0, length);
				Log.i("whw", "write result=" + writeResult);
				isWrite = M1CardAPI.WRITE_SUCCESS.equals(writeResult);
			}
			if (!isWrite) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ����˵�����޸�����
	 * 
	 * @param block
	 * @param num
	 * @param data
	 * @param keyType
	 * @return
	 */
	public boolean updatePwd(int block, int num, String data, int keyType) {
		if (data.length() == 0) {
			return false;
		}
		for (int i = 0; i < num; i++) {
			byte[] command = (DATA_PREFIX + WRITE_DATA_ORDER + getZoneId(block)
					+ makeCompletePassword(keyType, data) + ENTER).getBytes();
			Log.i("whw", "***write hexStr=" + DataUtils.toHexString(command));
			int length = receive(command, buffer);
			boolean isWrite = false;
			if (length > 0) {
				String writeResult = new String(buffer, 0, length);
				Log.i("whw", "write result=" + writeResult);
				isWrite = M1CardAPI.WRITE_SUCCESS.equals(writeResult);
			}
			if (!isWrite) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ����˵������װ�����
	 * 
	 * @param keyType
	 * @param passwordHexStr
	 * @return
	 */
	private String makeCompletePassword(int keyType, String passwordHexStr) {
		String completePasswordHexStr = "";
		switch (keyType) {
		case KEY_A:
			completePasswordHexStr = passwordHexStr + "ff078069"
					+ DEFAULT_PASSWORD;
			break;
		case KEY_B:
			completePasswordHexStr = DEFAULT_PASSWORD + "ff078069"
					+ completePasswordHexStr;
			break;
		default:
			break;
		}
		Log.i("whw", "completePasswordHexStr == " + completePasswordHexStr);
		return completePasswordHexStr;
	}

	/**
	 * ��ָ���Ŀ��д�����ݣ�����Ϊ16�ֽ� Write data to the specified block, length is 16 bytes
	 * 
	 * @param data
	 * @param position
	 * @return
	 */
	public boolean write(byte[] data, int position) {
		String hexStr = DataUtils.toHexString(data);
		byte[] command = (DATA_PREFIX + WRITE_DATA_ORDER + getZoneId(position)
				+ hexStr + ENTER).getBytes();
		Log.i("whw", "***write hexStr=" + hexStr);
		int length = receive(command, buffer);
		if (length > 0) {
			String writeResult = new String(buffer, 0, length);
			Log.i("whw", "write result=" + writeResult);
			return M1CardAPI.WRITE_SUCCESS.equals(writeResult);
		}
		return false;
	}

	/**
	 * ����˵�����ر����߳�
	 * 
	 * @return
	 */
	public String turnOff() {
		// byte[] command = TURN_OFF.getBytes();
		// int length = receive(command, buffer);
		// String str = "";
		// if (length > 0) {
		// str = new String(buffer, 0, length);
		// }
		// return str;
		return "";
	}

	public static class Result {
		/**
		 * �ɹ� successful
		 */
		public static final int SUCCESS = 1;
		/**
		 * Ѱ��ʧ�� Find card failure
		 */
		public static final int FIND_FAIL = 2;
		/**
		 * ��֤ʧ�� Validation fails
		 */
		public static final int VALIDATE_FAIL = 3;
		/**
		 * ����ʧ�� Read card failure
		 */
		public static final int READ_FAIL = 4;
		/**
		 * д��ʧ�� Write card failure
		 */
		public static final int WRITE_FAIL = 5;
		/**
		 * ��ʱ timeout
		 */
		public static final int TIME_OUT = 6;
		/**
		 * �����쳣 other exception
		 */
		public static final int OTHER_EXCEPTION = 7;

		/**
		 * ȷ���� 1: �ɹ� 2��Ѱ��ʧ�� 3����֤ʧ�� 4:д��ʧ�� 5����ʱ 6�������쳣
		 */
		public int confirmationCode;

		/**
		 * �����:��ȷ����Ϊ1ʱ�����ж��Ƿ��н�� Results: when the code is 1, then determine
		 * whether to have the result
		 */
		public Object resultInfo;

		/**
		 * ���� The card number
		 */
		public String num;
	}

}
