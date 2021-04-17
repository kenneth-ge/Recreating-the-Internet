package dns.client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Scanner;

import util.Util;

public class DNSClient {
	
	public static void main(String[] args) throws IOException {
		//convert int to two bytes
		Scanner sc = new Scanner(System.in);
		DNSClient client = new DNSClient(InetAddress.getByName("localhost"));
		
		while(true) {
			String ip = sc.nextLine();
			
			if(ip.equals("q")) {
				client.stopServer();
			}
			
			int port = Integer.parseInt(sc.nextLine());
			String name = sc.nextLine();
			
			client.registerDomain(InetAddress.getByName(ip), port, name);
		}
	}
	
	public DatagramSocket socket;
	public InetAddress serverIP;
	
	public DNSClient(InetAddress serverIP) throws SocketException {
		this.serverIP = serverIP;
		
		socket = new DatagramSocket();
	}
	
	public void registerDomain(InetAddress ip, int port, String name) throws IOException {
		if(name.length() > 255) {
			throw new RuntimeException("Domain name too long");
		}
		
		byte operation = 0;
		byte[] address = ip.getAddress();
		byte[] portBytes = Util.intToTwoBytes(port);
		byte[] nameBytes = name.getBytes();
		
		byte[] total = new byte[1 + 4 + 2 + nameBytes.length];
		
		total[0] = operation;
		
		for(int i = 1; i <= 4; i++) {
			total[i] = address[i - 1];
		}
		
		total[5] = portBytes[0];
		total[6] = portBytes[1];
		
		for(int i = 0; i < nameBytes.length; i++) {
			total[i + 7] = nameBytes[i];
		}
		
		this.sendPacket(total);
	}
	
	public Socket request(String domain) throws IOException {
		byte[] text = domain.getBytes();
		byte[] data = new byte[text.length + 1];
		System.arraycopy(text, 0, data, 1, text.length);
		data[0] = 1;
		
		DatagramPacket request = new DatagramPacket(data, data.length);
		request.setAddress(serverIP);
		request.setPort(5000);
		socket.send(request);
		
		byte[] responseData = new byte[6];
		DatagramPacket response = new DatagramPacket(responseData, 6);
		socket.receive(response);
		
		Socket ret = new Socket();
		ret.address = InetAddress.getByAddress(new byte[] {responseData[0], responseData[1], responseData[2], responseData[3]});
		ret.port = Util.bytesToTwoInts(new byte[] {responseData[4], responseData[5]});
		
		return ret;
	}
	
	public void stopServer() throws IOException {
		sendPacket(new byte[] {2});
	}
	
	private void sendPacket(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, 0, data.length);
		packet.setAddress(serverIP);
		packet.setPort(5000);
		
		socket.send(packet);
	}
	
}
