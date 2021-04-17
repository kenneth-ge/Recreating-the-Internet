package util;

import dns.server.DNSServer;
import dns.server.DomainRecord;

public class Tester {

	public static void main(String[] args) {
		DNSServer server = new DNSServer();
		server.start();
//		DomainRecord test = new DomainRecord("www.test.com",(short)300,(long)1231523);
//		byte[] ip = {123 - 128,213 - 128,12 - 128,234 - 128};
//		long l = Util.fourBytesToLong(ip);
//		System.out.println(Util.longToStringIPV4(l));
	}

}
