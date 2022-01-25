package dns.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DNSServer extends Thread {

    DatagramSocket socket;
    boolean running;
    byte[] buf = new byte[256];
    int serverPort = 5000;
    
    DatagramPacket responsePacket;
    byte[] response = new byte[256];
    
    DomainTree domains = new DomainTree();
    List<DomainRecord> records = new ArrayList<>();

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
    		domains.add(dr);
    	}
    }
    
    public void writeData() throws IOException {
    	FileWriter writer = new FileWriter("./data/domains.data");
    	for(var dr : records) {
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
        
        responsePacket = new DatagramPacket(response, response.length);
        
        while(running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
				socket.receive(packet);
			}catch(IOException e) {
				System.out.println("I/O error: " + e.getMessage());
			}
            byte[] requestData = packet.getData();
            
            responsePacket.setAddress(packet.getAddress());
            responsePacket.setPort(packet.getPort());
            
            switch(requestData[0]) {
            case 0:
            	try {
					byte code = processRegistration(requestData, packet.getLength());
					response[0] = code;
				} catch (UnknownHostException e) {
					System.out.println("Unknown Host Error: " + e.getMessage());
					response[0] = 1;
				}
            	responsePacket.setLength(1);
            	break;
            case 1:
            	processResponse(requestData, packet.getLength());
            	break;
            case 2:
            	running = false;
                System.out.println("Server stopping.");
                try {
					writeData();
					response[0] = 0;
				} catch (IOException e) {
					System.out.println("I/O error: " + e.getMessage());
					response[0] = 1;
				}
                responsePacket.setLength(1);
            	break;
            }
            
            try {
				socket.send(responsePacket);
			}catch(IOException e) {
				System.out.println("I/O error: " + e.getMessage());
			}
        }
        socket.close();
    }
    
    public void processResponse(byte[] request, int end) {
    	//TODO: I wonder if there's a way to speed up this program by bypassing this conversion and designing
    	//a custom hashmap that works at the byte level
    	String name = new String(request, 1, end - 1);
    	
    	var dr = domains.get(Util.parseDomain(name));
    	
    	if(dr == null) {
    		response[0] = 2;
    		responsePacket.setLength(1);
    		return;
    	}
    	
    	response[0] = 0;
    	
    	byte[] ip = Util.longToFourBytes(dr.getIp());
    	
    	response[1] = ip[0];
    	response[2] = ip[1];
    	response[3] = ip[2];
    	response[4] = ip[3];
    	
    	byte[] port = Util.intToTwoBytes(dr.getPort());
    	
    	response[5] = port[0];
    	response[6] = port[1];
    	
    	responsePacket.setLength(7);
    }
    
    /* Returns status code */
    public byte processRegistration(byte[] request, int end) throws UnknownHostException {
    	System.out.println("Request: " + Arrays.toString(request));
    	
    	//ignore first byte
    	//byte operation = request[0];
    	
    	byte type = request[1];
    	
    	byte[] addressBytes = {request[2], request[3], request[4], request[5]};
    	//InetAddress addr = InetAddress.getByAddress(addressBytes);
    	int port = Util.bytesToTwoInts(new byte[] {request[6], request[7]});
    	
    	String domainString = new String(request, 8, end - 8);
    	
    	System.out.println(port);
    	System.out.println("packet length: " + end);
    	System.out.println("domain string: " + domainString);
    	
    	String[] name = Util.parseDomain(domainString);
    	
    	System.out.println(Arrays.toString(name));
    	
    	DomainRecord dr = new DomainRecord(type, name,(short)port,Util.fourBytesToLong(addressBytes));
    	System.out.println("Registered domain: " + dr.toReadableString());
    	boolean successful = domains.add(dr);
    	
    	if(successful) {
    		records.add(dr);
    		return 0;
    	}else{
    		return 2;
    	}
    }
}
