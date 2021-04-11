package dns.client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class DNSClient {
	
	public static DatagramSocket socket;

	public static void main(String[] args) throws IOException {
		socket = new DatagramSocket();
		
		//convert int to two bytes
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			int port = sc.nextInt();
			
			byte a = (byte) port;
			byte b = (byte) (port >>> 8);
			
			System.out.println(a);
			System.out.println(b);
			
			int x = (b & 0xff << 8) | (a & 0xff);
			
			System.out.println(x);
		}
	}
	
	public static void registerDomain(InetAddress ip, int port, String name) {
		if(name.length() > 255) {
			throw new RuntimeException("Domain name too long");
		}
		
		byte[] nameBytes = name.getBytes();
		
		byte operation = 0;
		byte[] address = ip.getAddress();
		
	}
	
}
