package util;

public class Util {

	//turns the port number into two bytes. Note that this truncates the int. 
	public static byte[] portToBytes(int port) {
		byte a = (byte) port;
		byte b = (byte) (port >>> 8);
		
		return new byte[] {a, b};
	}
	
	public static int bytesToPort(byte[] bytes) {
		int a = bytes[0];
		int b = bytes[1];
		
		int x = (b & 0xff << 8) | (a & 0xff);
		
		return x;
	}
	
}
