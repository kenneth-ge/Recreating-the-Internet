package util;

import java.util.Arrays;

public class Util {

	//turns the port number into two bytes. Note that this truncates the int. 
	public static byte[] intToTwoBytes(int port) {
		byte a = (byte) port;
		byte b = (byte) (port >>> 8);
		
		return new byte[] {a, b};
	}
	
	public static int bytesToTwoInts(byte[] bytes) {
		System.out.println("Bytes to two ints: " + Arrays.toString(bytes));
		int a = bytes[0];
		int b = bytes[1];
		
		int x = ((b & 0xff) << 8) | (a & 0xff);
		
		return x;
	}
	
	public static String longToStringIPV4(long ip) {
		String res = ((ip >> 24) & (255)) + "." + ((ip >> 16) & 255) + "." + ((ip >> 8) & 255) + "." + ((ip >> 0) & 255);
		return res;
	}
	
	public static long fourBytesToLong(byte[] ip) {
		long res = ((long)(ip[0] & 0xff) << 24) + ((long)(ip[1] & 0xff) << 16) + ((long)(ip[2] & 0xff) << 8) + ((long)(ip[3] & 0xff));
		return res;
	}
	
	public static byte[] longToFourBytes(long ip) {
		byte[] res = {
				(byte) ((ip >> 24) & (255)),
				(byte) ((ip >> 16) & 255),
				(byte) ((ip >> 8) & 255), 
				(byte) ((ip >> 0) & 255)
		};
		return res;
	}
	
	public static String[] parseDomain(String s) {
		return s.split("\\.");
	}

	public static String toDomainString(String[] domain) {
		StringBuffer sb = new StringBuffer();
		
		for(String s: domain) {
			sb.append(s);
			sb.append('.');
		}
		
		return sb.toString();
	}
	
}
