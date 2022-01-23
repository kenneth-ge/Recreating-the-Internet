package dns.server;

import util.Util;

public class DomainRecord {
	
	private byte type;
	private String[] domain;
	private short port;
	private long ip;
	
	public DomainRecord(byte type, String[] domain, short port, long ip) {
		this.type = type;
		this.domain = domain;
		this.port = port;
		this.ip = ip;
	}
	
	public DomainRecord(String record) {
		String[] infos = record.split(";");
		this.ip = Long.parseLong(infos[0]);
		this.port = Short.parseShort(infos[1]);
		this.domain = Util.parseDomain(infos[2]);
		this.type = Byte.parseByte(infos[3]);
	}
	
	public String[] getDomain() {
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
		String res = domain + ";" + port + ";" + ip + ";" + type;
		return res;
	}
	
	public String toReadableString() {
		return type + ":" + Util.longToStringIPV4(ip) + ":" + port + ":" + domain;
	}
	
}
