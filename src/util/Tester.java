package util;

import dns.server.DNSServer;
import dns.server.DomainRecord;

public class Tester {

	public static void main(String[] args) {
		DNSServer server = new DNSServer();
		server.start();
	}

}
