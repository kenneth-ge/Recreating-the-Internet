package dns.server;

import util.Util;

public class domainRecord {
	private String domain;
	private short port;
	private long ip;
	
	public domainRecord(String domain,short port,long ip) {
		this.domain = domain;
		this.port = port;
		this.ip = ip;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public short getPort() {
		return port;
	}
	
	public long getIp() {
		return ip;
	}
	
	@Override
	public String toString() {
		String res = domain + (char)(0) + port + (char)(0) + Util.longToStringIPV4(ip);
		return res;
	}
}
