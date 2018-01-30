package android_serialport_api;


import android.os.SystemClock;
import android.util.Log;

import com.szfp.DataUtils;


public class QRScannerAPI {
	private byte[] buffer;
	
	/**
	 * ��ȡ�豸��Ϣ
	 * @return true �ɹ���false ʧ��
	 */
	public synchronized boolean queryDeviceInfo(){
		buffer = new byte[256];
		byte[] sendCmd = {(byte)0x55,(byte)0xAA,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0xFE};
		SerialPortManager.getInstance().write(sendCmd);
		SystemClock.sleep(200);
		int length = SerialPortManager.getInstance().read(buffer, 500, 50);
		Log.d("jokey", "length:  "+length);
		if(length>0){
			byte[] rcvData = new byte[length];
			System.arraycopy(buffer, 0, rcvData, 0, length);
			Log.d("jokey", "queryDeviceInfo--->rcvData:  "+ DataUtils.toHexString(rcvData));
			if(rcvData[3] == 0x00){
				return true;
			}
		}
		return false;
	}
	/**
	 * ������ͨģʽ
	 * @return true �ɹ���false ʧ��
	 */
	public synchronized boolean setNormal(){
		buffer = new byte[256];
		byte[] sendCmd = {(byte)0x55,(byte)0xAA,(byte)0x22,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0xDD};
		SerialPortManager.getInstance().write(sendCmd);
		SystemClock.sleep(200);
		int length = SerialPortManager.getInstance().read(buffer, 500, 50);
		if(length>0){
			byte[] rcvData = new byte[length];
			System.arraycopy(buffer, 0, rcvData, 0, length);
			Log.d("jokey", "setNormal--->rcvData:  "+DataUtils.toHexString(rcvData));
			if(rcvData[3] == 0x00){
				return true;
			}
		}
		return false;
	}
	/**
	 * ���õ���ģʽ
	 * @return true �ɹ���false ʧ��
	 */
	public synchronized boolean setOnce(){
		buffer = new byte[256];
		byte[] sendCmd = {(byte)0x55,(byte)0xAA,(byte)0x22,(byte)0x01,(byte)0x00,(byte)0x02,(byte)0xDE};
		SerialPortManager.getInstance().write(sendCmd);
		SystemClock.sleep(200);
		int length = SerialPortManager.getInstance().read(buffer, 500, 50);
		if(length>0){
			byte[] rcvData = new byte[length];
			System.arraycopy(buffer, 0, rcvData, 0, length);
			Log.d("jokey", "setOnce--->rcvData:  "+DataUtils.toHexString(rcvData));
			if(rcvData[3] == 0x00){
				return true;
			}
		}
		return false;
	}
	/**
	 * ���ļ��ʱ��
	 * @param time ���ʱ�䣨ms����Χ0~60000
	 * @return true �ɹ���false ʧ��
	 */
	public synchronized boolean updataIntervalTime(long time){
		buffer = new byte[256];
		byte[] interval = DataUtils.int2Byte((int)time);
		byte[] sendCmd = {(byte)0x55,(byte)0xAA,(byte)0x23,(byte)0x02,(byte)0x00,(byte)interval[0],(byte)interval[1]};
		byte end = DataUtils.xorCheck(sendCmd);
		byte[] cmd = {(byte)0x55,(byte)0xAA,(byte)0x23,(byte)0x02,(byte)0x00,(byte)interval[0],(byte)interval[1],(byte)end};
		SerialPortManager.getInstance().write(cmd);
		SystemClock.sleep(200);
		int length = SerialPortManager.getInstance().read(buffer, 500, 50);
		Log.d("jokey", "length:  "+length);
		if(length>0){
			byte[] rcvData = new byte[length];
			System.arraycopy(buffer, 0, rcvData, 0, length);
			Log.d("jokey", "updataIntervalTime--->rcvData:  "+DataUtils.toHexString(rcvData));
			if(rcvData[3] == 0x00){
				return true;
			}
		}
		return false;
	}
	/**
	 * ���ü��ģʽ
	 * @param second ���ʱ�䣨s����Χ1~10
	 * @return true �ɹ���false ʧ��
	 */
	public synchronized boolean setInterval(int second){
		buffer = new byte[256];
		byte[] time= DataUtils.int2Byte(second);
		byte[] sendCmd = {(byte)0x55,(byte)0xAA,(byte)0x22,(byte)0x03,(byte)0x00,(byte)0x03,(byte)time[0],(byte)time[1]};
		byte end = DataUtils.xorCheck(sendCmd);
		byte[] cmd = {(byte)0x55,(byte)0xAA,(byte)0x22,(byte)0x03,(byte)0x00,(byte)0x03,(byte)time[0],(byte)time[1],end};
		SerialPortManager.getInstance().write(cmd);
		SystemClock.sleep(200);
		int length = SerialPortManager.getInstance().read(buffer, 500, 50);
		if(length>0){
			byte[] rcvData = new byte[length];
			System.arraycopy(buffer, 0, rcvData, 0, length);
			Log.d("jokey", "setInterval--->rcvData:  "+DataUtils.toHexString(rcvData));
			if(rcvData[3] == 0x00){
				return true;
			}
		}
		return false;
	}
	/**
	 * ����ɨ������
	 * @param type ����
	 * @return true �ɹ���false ʧ��
	 */
	public synchronized boolean setScanType(int type){
		buffer = new byte[256];
		byte[] sendCmd = {(byte)0x55,(byte)0xAA,(byte)0x21,(byte)0x01,(byte)0x00,(byte)type};
		byte end = DataUtils.xorCheck(sendCmd);
		byte[] cmd = {(byte)0x55,(byte)0xAA,(byte)0x21,(byte)0x01,(byte)0x00,(byte)type,end};
		SerialPortManager.getInstance().write(cmd);
		SystemClock.sleep(200);
		int length = SerialPortManager.getInstance().read(buffer, 500, 50);
		if(length>0){
			byte[] rcvData = new byte[length];
			System.arraycopy(buffer, 0, rcvData, 0, length);
			Log.d("jokey", "setScanType--->rcvData:  "+DataUtils.toHexString(rcvData));
			if(rcvData[3] == 0x00){
				return true;
			}
		}
		return false;
	}
	public byte[] readCode(){
		SerialPortManager.getInstance().clearBuffer();
		buffer = new byte[256];
		int length = SerialPortManager.getInstance().read(buffer, 2000, 100);
		if(length>0){
			byte[] rcvData = new byte[length];
			System.arraycopy(buffer, 0, rcvData, 0, length);
			Log.d("jokey", "readCode--->rcvData:  "+DataUtils.toHexString(rcvData));
			return rcvData;
		}
		return null;
	}
}
