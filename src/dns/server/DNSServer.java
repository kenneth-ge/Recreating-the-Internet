package dns.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import util.Util;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class DNSServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private int serverPort = 5000;
    
    private HashMap<String, DomainRecord> domains = new HashMap<String, DomainRecord>();

    public DNSServer() {
        try {
			socket = new DatagramSocket(serverPort);
		}catch(SocketException e) {
			System.out.println("Socket error: " + e.getMessage());
		}
    }
    
    public void readData() throws FileNotFoundException {
    	domains.clear();
    	Scanner scanner = new Scanner(new File("./data/domains.data"));
    	while(scanner.hasNextLine()) {
    		String data = scanner.nextLine();
    		DomainRecord dr = new DomainRecord(data);
    		System.out.println(dr.toReadableString());
    		domains.put(dr.getDomain(),dr);
    	}
    }
    
    public void writeData() throws IOException {
    	FileWriter writer = new FileWriter("./data/domains.data");
    	for(Entry<String, DomainRecord> dr : domains.entrySet()) {
    		writer.write(dr.toString() + "\n");
    	}
    	writer.close();
    }

    public void run() {
        running = true;
        domains.clear();
        try {
			readData();
		} catch (FileNotFoundException e1) {
			System.out.println("File Not Found error: " + e1.getMessage());
		}
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
            byte[] requestData = packet.getData();
            
            if(requestData[0] == 2) {
            	// end
                running = false;
                try {
					writeData();
				} catch (IOException e) {
					System.out.println("I/O error: " + e.getMessage());
				}
                continue;
            }else if(requestData[0] == 0) {
            	try {
					processRegistration(requestData);
				} catch (UnknownHostException e) {
					System.out.println("Unknown Host Error: " + e.getMessage());
				}
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
    	
    	DomainRecord dr = new DomainRecord(name,(short)port,Util.fourBytesToLong(addressBytes));
    	domains.put(dr.getDomain(),dr);
    }
}
