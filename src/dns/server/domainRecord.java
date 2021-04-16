package dns.server;

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
}
