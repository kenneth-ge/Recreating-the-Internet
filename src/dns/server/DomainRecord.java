package dns.server;

import util.Util;

public class DomainRecord {
	
	private String domain;
	private short port;
	private long ip;
	
	public DomainRecord(String domain,short port,long ip) {
		this.domain = domain;
		this.port = port;
		this.ip = ip;
	}
	
	public DomainRecord(String record) {
		String[] infos = record.split("" + (char)(0));
		this.domain = infos[0];
		this.port = Short.parseShort(infos[1]);
		this.ip = Long.parseLong(infos[2]);
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
