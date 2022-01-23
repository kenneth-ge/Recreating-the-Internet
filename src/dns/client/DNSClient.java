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
			String req = sc.nextLine();
			
			switch(req) {
			case "q":
				client.stopServer();
				break;
			case "r":
				byte type = Byte.parseByte(sc.nextLine());
				String ip = sc.nextLine();
				int port = Integer.parseInt(sc.nextLine());
				String name = sc.nextLine();
				
				client.registerDomain(type, InetAddress.getByName(ip), port, name);
				break;
			case "R":
				name = sc.nextLine();
				
				var socket = client.request(name);
				
				System.out.println(socket.address + " " + socket.port);
				
				break;
			}
			
		}
	}
	
	public DatagramSocket socket;
	public InetAddress serverIP;
	
	public DNSClient(InetAddress serverIP) throws SocketException {
		this.serverIP = serverIP;
		
		socket = new DatagramSocket();
	}
	
	public void registerDomain(byte type, InetAddress ip, int port, String name) throws IOException {
		if(name.length() > 255) {
			throw new RuntimeException("Domain name too long");
		}
		
		byte operation = 0;
		byte[] address = ip.getAddress();
		byte[] portBytes = Util.intToTwoBytes(port);
		byte[] nameBytes = name.getBytes();
		
		byte[] total = new byte[1 + 1 + 4 + 2 + nameBytes.length];
		
		total[0] = operation;
		
		total[1] = type;
		
		for(int i = 2; i <= 5; i++) {
			total[i] = address[i - 1];
		}
		
		total[6] = portBytes[0];
		total[7] = portBytes[1];
		
		for(int i = 0; i < nameBytes.length; i++) {
			total[i + 8] = nameBytes[i];
		}
		
		this.sendPacket(total);
	}
	
	//TODO: Fix request function
	public Socket request(String domain) throws IOException {
		byte[] text = domain.getBytes();
		byte[] data = new byte[text.length + 1];
		System.arraycopy(text, 0, data, 1, text.length);
		data[0] = 1;
		
		DatagramPacket request = new DatagramPacket(data, data.length);
		request.setAddress(serverIP);
		request.setPort(5000);
		socket.send(request);
		
		byte[] responseData = new byte[7];
		
		DatagramPacket response = new DatagramPacket(responseData, 7);
		socket.receive(response);
		
		Socket ret = new Socket();
		ret.address = InetAddress.getByAddress(new byte[] {responseData[1], responseData[2], responseData[3], responseData[4]});
		ret.port = Util.bytesToTwoInts(new byte[] {responseData[5], responseData[6]});
		
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
