package com.sansi.stellar.local.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;

import coolniu.encrypt.utils.MyLog;

public class Data_Protocol {
	
	/**
	 * create broadcast message
	 * @return
	 */
	public byte[] broadcast(){
		ByteBuffer buffer=ByteBuffer.allocate(12);
		buffer.putShort((short) 14);
		buffer.put((byte)0xFF);
		buffer.put((byte)0xFF);
		buffer.put((byte)0xFF);
		buffer.put((byte)0xFF);
		buffer.put((byte)0xFF);
		buffer.put((byte)0xFF);
		buffer.put((byte)0xFF);
		buffer.put((byte)0xFF);
		buffer.put((byte)0x80);
		buffer.put((byte)0x00);
		byte[] re =(byte[])buffer.flip().array();
		re=packageData(re);
		return re;
	}
	
	/**
	 * coordinate time
	 * @param time
	 * @param addr
	 * @return
	 */
	public byte[] corTime(String time, String addr) {
		ByteBuffer buffer=ByteBuffer.allocate(20);
		buffer.putShort((short) 22);
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x80);
		buffer.put((byte)0x01);
		int aa[]=com.sansi.stellar.local.protocol.common.getTime(time);
		buffer.putShort((short)com.sansi.stellar.local.protocol.common.getBCD2(aa[0]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(aa[1]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(aa[2]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(aa[3]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(aa[4]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(aa[5]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(aa[6]));
		byte[] re =(byte[])buffer.flip().array();
		re=packageData(re);
		return re;
	}
	
	/**
	 * set device brightness
	 * @param Lightless
	 * @param addr
	 * @return
	 */
	public byte[] sendLight(int Lightless, String addr){
		ByteBuffer buffer=ByteBuffer.allocate(13);
		buffer.putShort((short) 15);
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x00);
		buffer.put((byte)0x02);
		buffer.put((byte)Lightless);
		byte[] re =(byte[])buffer.flip().array();
		re=packageData(re);
		MyLog.i("lei", "data lightness");
		return re;
	}

	/**
	 * reset bulb
	 * @param addr
	 * @return
	 */
	public byte[] sendReset(String addr){
		ByteBuffer buffer=ByteBuffer.allocate(12);
		buffer.putShort((short) 14);
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x00);//发送码
		buffer.put((byte)0x08);
		//buffer.put((byte)Lightless);
		byte[] re =(byte[])buffer.flip().array();
		re=packageData(re);
		return re;
	}
	
	/**
	 * set device RGB value
	 * @param addr
	 * @param playList
	 * @return
	 */
	public byte[] sendDefLight_RGBW(String addr, int[] playList){		
			ByteBuffer buffer=ByteBuffer.allocate(20);
			buffer.putShort((short) 22);
			buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
			buffer.put((byte)0x00);
			buffer.put((byte)0x03);
			buffer.put((byte)0x00);
			buffer.put((byte)0x01);
			buffer.put((byte)0xFF);
			buffer.put((byte)0xFF);
			buffer.put((byte)playList[0]);
			buffer.put((byte)playList[1]);
			buffer.put((byte)playList[2]);
			buffer.put((byte)playList[3]);//10
			byte[] re =(byte[])buffer.flip().array();
			re=packageData(re);
			MyLog.i("lei", "data rgb r:"+playList[0] +" g:"+playList[1]+" b:"+playList[2]+" w:"+playList[3]
					+" addr:"+addr);
			return re;	
	}
	
	/**
	 * set device CCT value
	 * @param addr
	 * @param temper
	 * @param time
	 * @return
	 */
	public byte[] sendDefLight_CCT(String addr,int temper,int time){
		ByteBuffer buffer=ByteBuffer.allocate(16);
		buffer.putShort((short) 18);
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x00);
		buffer.put((byte)0x04);
		buffer.putShort((short)temper);
		buffer.putShort((short)time);
		byte[] re =(byte[])buffer.flip().array();
		re=packageData(re);
		return re;
	}
	
	/**
	 * inner dimming mode
	 * @param addr
	 * @param id
	 * @param Time
	 * @return
	 */
	public byte[] sendInLight(String addr, int id, int Time){
		ByteBuffer buffer=ByteBuffer.allocate(15);
		buffer.putShort((short) 17);
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x00);
		buffer.put((byte)0x05);
		buffer.put((byte)id);
		buffer.putShort((short)Time);
		byte[] re =(byte[])buffer.flip().array();
		re=packageData(re);
		return re;
	}
	
	/**
	 * set time dimming mode
	 * @param addr
	 * @param param include time and brightness values (9 items)
	 * @param mode
	 * @return
	 */
	public byte[] sendTimeLight(String addr,int[] param,int mode){
		ByteBuffer buffer=ByteBuffer.allocate(29);
		buffer.putShort((short) 31);
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x80);
		buffer.put((byte)0x06);
		buffer.put((byte)0x00);
		buffer.putShort((short)com.sansi.stellar.local.protocol.common.getBCD2(param[0]));
		buffer.putShort((short)com.sansi.stellar.local.protocol.common.getBCD2(param[1]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(param[2]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(param[3]));
		buffer.put((byte)com.sansi.stellar.local.protocol.common.getBCD1(param[4]));
		buffer.put((byte)mode);
		buffer.put((byte)param[5]);
		buffer.put((byte)param[6]);
		buffer.put((byte)param[7]);
		buffer.put((byte)param[8]);
		byte[] re =(byte[])buffer.flip().array();
		re=packageData(re);
		return re;
	}
	
	/**
	 * setting device SSID info
	 * @param addr
	 * @param ssid
	 * @param ssid_pw
	 * @return
	 */
	public byte[] sendSSID(String addr,int[] ssid,int[] ssid_pw){
		ByteBuffer buffer=ByteBuffer.allocate(14+ssid.length+ssid_pw.length);
		buffer.putShort((short)(16+ssid.length+ssid_pw.length));
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x80);
		buffer.put((byte)0x07);
		buffer.put((byte)ssid.length);
		for(int i=0;i<ssid.length;i++){
			buffer.put((byte)ssid[i]);
		}
		buffer.put((byte)ssid_pw.length);
		for(int i=0;i<ssid_pw.length;i++){
			buffer.put((byte)ssid_pw[i]);
		}
		byte[] re =(byte[])buffer.flip().array();
		re=packageData(re);
		return re;
	}
	
	/**
	 * @param addr
	 * @param data  1 byte
	 * @param time  2 byte
	 * @param delay 4 byte
	 * @return
	 */
	public byte[] sendDelayTime(String addr,int data,int time,byte[] delay){
		
		ByteBuffer buffer=ByteBuffer.allocate(19);
		buffer.putShort((short)(21));
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x00);
		buffer.put((byte)0x0C);
		buffer.put((byte)data);
		buffer.putShort((short)time);
		for(int i=0;i<4;i++){
			buffer.put(delay[i]);
		}
		byte[] re =(byte[])buffer.flip().array();
		MyLog.i("lei", "addr: "+addr+" data: "+data+" time: "+
								time+ " buffer: "+ buffer.flip().array()+ 
								" re: "+re.toString());
		
		re=packageData(re);
		return re;
	}
	
	
	
	public byte[] getDelayTime(String addr){
		
		ByteBuffer buffer = ByteBuffer.allocate(12);
		buffer.putShort((short)(14));
		buffer.put(com.sansi.stellar.local.protocol.common.stringToByte(addr));
		buffer.put((byte)0x80);//need replay
		buffer.put((byte)0x0D);
		byte[] re = (byte[])buffer.flip().array();
		re=packageData(re);
		return re;
	}
	
	
	private byte[] packageData(byte[] re)
	{
		short crc =getcrc16(re,re.length);
		ByteBuffer buffer = ByteBuffer.allocate(re.length + 4);
		buffer.put((byte)0xA5);
		buffer.put(re).putShort(crc).put((byte)0x5A);
		return (byte[]) buffer.flip().array();
	}
	
	/**
	 * 获得crc校验码
	 * @param buffer
	 * @param buffer_length
	 * @return
	 */
	private static short getcrc16(byte[] buffer, int buffer_length)
	{
		short crc = 0;
		int i;

		for (i = 0; i < buffer_length; i++)
			crc = (short)(crc_table[((crc >> 8) ^ buffer[i]) & 0xFF] ^ (crc << 8));

		return crc;
		
	}
	
	public static short caluCRC(byte[] pByte) {
		int len = pByte.length;
		char crc;
		byte da;
		crc = 0x0;
		int i = 0;
		while (len-- != 0) {
			da = (byte) (crc / 256);
			crc <<= 8;
			int num = da ^ pByte[i];
			if (num < 0)
				num += 256;
			crc ^= crc_table[num];
			++i;
		}
		return (short)crc;
	}
	
	public static boolean isValidFrame(byte[] data, int nLength) {
	    if (nLength < 16) {
	        return false;
	    }
	    
	    // verify frame start and end
	    if (data[0] != 0x35 || data[nLength-1] != 0x53) {
	        return false;
	    }
	    
	    // verify CRC
	    short crc = getcrc16(Arrays.copyOfRange(data, 1, nLength), nLength-4);	    
	    short crc_ori = (short)( ((data[nLength-3]&0xff) << 8) + (data[nLength-2] & 0xff));	    
	    if (crc != crc_ori) {
	        return false;
	    }
	    
	    return true;
	}
	
	public static String retrieveMac(byte[] data) {
	    byte[] mac = Arrays.copyOfRange(data, 3, 11);
	    return com.sansi.stellar.local.protocol.common.bytes2HexString(mac, "").toUpperCase();
	}
	
	public static int retrieveSoftVer(byte[] data) {
		return ((int)data[14]) & 0xFF;
	}
	
	public static int retrieveHardVer(byte[] data) {
		return ((int)data[15]) & 0xFF;
	}

	/**
	 * Crc数据
	 */
	static short crc_table[] =
		{
			0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50A5, 0x60C6, 0x70E7,
			(short) 0x8108, (short) 0x9129, (short) 0xA14A, (short) 0xB16B, (short) 0xC18C, (short) 0xD1AD, (short) 0xE1CE, (short) 0xF1EF,
			0x1231, 0x0210, 0x3273, 0x2252, 0x52B5, 0x4294, 0x72F7, 0x62D6,
			(short) 0x9339, (short) 0x8318, (short) 0xB37B, (short) 0xA35A, (short) 0xD3BD, (short) 0xC39C, (short) 0xF3FF, (short) 0xE3DE,
			0x2462, 0x3443, 0x0420, 0x1401, 0x64E6, 0x74C7, 0x44A4, 0x5485,
			(short) 0xA56A, (short) 0xB54B, (short) 0x8528, (short) 0x9509, (short) 0xE5EE, (short) 0xF5CF, (short) 0xC5AC, (short) 0xD58D,
			0x3653, 0x2672, 0x1611, 0x0630, 0x76D7, 0x66F6, 0x5695, 0x46B4,
			(short) 0xB75B, (short) 0xA77A, (short) 0x9719, (short) 0x8738, (short) 0xF7DF, (short) 0xE7FE, (short) 0xD79D, (short) 0xC7BC,
			0x48C4, 0x58E5, 0x6886, 0x78A7, 0x0840, 0x1861, 0x2802, 0x3823,
			(short) 0xC9CC, (short) 0xD9ED, (short) 0xE98E, (short) 0xF9AF, (short) 0x8948, (short) 0x9969, (short) 0xA90A, (short) 0xB92B,
			0x5AF5, 0x4AD4, 0x7AB7, 0x6A96, 0x1A71, 0x0A50, 0x3A33, 0x2A12,
			(short) 0xDBFD, (short) 0xCBDC, (short) 0xFBBF, (short) 0xEB9E, (short) 0x9B79, (short) 0x8B58, (short) 0xBB3B, (short) 0xAB1A,
			0x6CA6, 0x7C87, 0x4CE4, 0x5CC5, 0x2C22, 0x3C03, 0x0C60, 0x1C41,
			(short) 0xEDAE, (short) 0xFD8F, (short) 0xCDEC, (short) 0xDDCD, (short) 0xAD2A, (short) 0xBD0B, (short) 0x8D68, (short) 0x9D49,
			0x7E97, 0x6EB6, 0x5ED5, 0x4EF4, 0x3E13, 0x2E32, 0x1E51, 0x0E70,
			(short) 0xFF9F, (short) 0xEFBE, (short) 0xDFDD, (short) 0xCFFC, (short) 0xBF1B, (short) 0xAF3A, (short) 0x9F59, (short) 0x8F78,
			(short) 0x9188, (short) 0x81A9, (short) 0xB1CA, (short) 0xA1EB, (short) 0xD10C, (short) 0xC12D, (short) 0xF14E, (short) 0xE16F,
			0x1080, 0x00A1, 0x30C2, 0x20E3, 0x5004, 0x4025, 0x7046, 0x6067,
			(short) 0x83B9, (short) 0x9398, (short) 0xA3FB, (short) 0xB3DA, (short) 0xC33D, (short) 0xD31C, (short) 0xE37F, (short) 0xF35E,
			0x02B1, 0x1290, 0x22F3, 0x32D2, 0x4235, 0x5214, 0x6277, 0x7256,
			(short) 0xB5EA, (short) 0xA5CB, (short) 0x95A8, (short) 0x8589, (short) 0xF56E, (short) 0xE54F, (short) 0xD52C, (short) 0xC50D,
			0x34E2, 0x24C3, 0x14A0, 0x0481, 0x7466, 0x6447, 0x5424, 0x4405,
			(short) 0xA7DB, (short) 0xB7FA, (short) 0x8799, (short) 0x97B8, (short) 0xE75F, (short) 0xF77E, (short) 0xC71D, (short) 0xD73C,
			0x26D3, 0x36F2, 0x0691, 0x16B0, 0x6657, 0x7676, 0x4615, 0x5634,
			(short) 0xD94C, (short) 0xC96D, (short) 0xF90E, (short) 0xE92F, (short) 0x99C8, (short) 0x89E9, (short) 0xB98A, (short) 0xA9AB,
			0x5844, 0x4865, 0x7806, 0x6827, 0x18C0, 0x08E1, 0x3882, 0x28A3,
			(short) 0xCB7D, (short) 0xDB5C, (short) 0xEB3F, (short) 0xFB1E, (short) 0x8BF9, (short) 0x9BD8, (short) 0xABBB, (short) 0xBB9A,
			0x4A75, 0x5A54, 0x6A37, 0x7A16, 0x0AF1, 0x1AD0, 0x2AB3, 0x3A92,
			(short) 0xFD2E, (short) 0xED0F, (short) 0xDD6C, (short) 0xCD4D, (short) 0xBDAA, (short) 0xAD8B, (short) 0x9DE8, (short) 0x8DC9,
			0x7C26, 0x6C07, 0x5C64, 0x4C45, 0x3CA2, 0x2C83, 0x1CE0, 0x0CC1,
			(short) 0xEF1F, (short) 0xFF3E, (short) 0xCF5D, (short) 0xDF7C, (short) 0xAF9B, (short) 0xBFBA, (short) 0x8FD9, (short) 0x9FF8,
			0x6E17, 0x7E36, 0x4E55, 0x5E74, 0x2E93, 0x3EB2, 0x0ED1, 0x1EF0
		};
}
