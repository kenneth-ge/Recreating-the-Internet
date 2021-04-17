package dns.server;

import java.net.InetAddress;

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
		String[] infos = record.split(";");
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
		String res = domain + ";" + port + ";" + ip;
		return res;
	}
	
	public String toReadableString() {
		return Util.longToStringIPV4(ip) + ":" + port + ":" + domain;
	}
	
}
