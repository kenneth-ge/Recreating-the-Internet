package util;

public class Util {

	//turns the port number into two bytes. Note that this truncates the int. 
	public static byte[] intToTwoBytes(int port) {
		byte a = (byte) port;
		byte b = (byte) (port >>> 8);
		
		return new byte[] {a, b};
	}
	
	public static int bytesToTwoInts(byte[] bytes) {
		int a = bytes[0];
		int b = bytes[1];
		
		int x = (b & 0xff << 8) | (a & 0xff);
		
		return x;
	}
	
	public static String longToStringIPV4(long ip) {
		String res = ((ip >> 24) & (255)) + "." + ((ip >> 16) & 255) + "." + ((ip >> 8) & 255) + "." + ((ip >> 0) & 255);
		return res;
	}
	
	public static long fourBytesToLong(byte[] ip) {
		long res = ((long)(ip[0] + 128) << 24) + ((long)(ip[1] + 128) << 16) + ((long)(ip[2] + 128) << 8) + ((long)(ip[3] + 128));
		return res;
	}
	
}
