package dns.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import util.Util;
import java.util.HashMap;

public class DNSServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private int serverPort = 5000;
    
    private HashMap<String,domainRecord> data;

    public DNSServer() {
        try {
			socket = new DatagramSocket(serverPort);
		}catch(SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		}
    }

    public void run() {
        running = true;
        System.out.println("Server is running on port: " + serverPort);
        while(running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
				socket.receive(packet);
			}catch(IOException e) {
				System.out.println("I/O error: " + e.getMessage());
			}
            
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());
            
            if(received.equals("end")) {
                running = false;
                continue;
            }
            
            try {
				socket.send(packet);
			}catch(IOException e) {
				System.out.println("I/O error: " + e.getMessage());
			}
        }
        socket.close();
    }
    
    public void processRegistration(byte[] request) throws UnknownHostException {
    	//ignore first byte
    	byte operation = request[0];
    	
    	byte[] addressBytes = {request[1], request[2], request[3], request[4]};
    	InetAddress addr = InetAddress.getByAddress(addressBytes);
    	int port = Util.bytesToTwoInts(new byte[] {request[5], request[6]});
    	
    	String name = new String(request, 7, request.length - 7);
    	
    	//turn this into a java class
    }
}
