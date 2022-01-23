package dns.server;

import java.util.HashMap;
import java.util.Map;

public class DomainTree {

	private Map<String, DomainTree> map;
	private DomainRecord value;
	
	public DomainTree() {
		map = new HashMap<>();
	}
	
	/* Returns whether or not this function executed successfully */
	public boolean add(DomainRecord dr) {
		return add(dr, 0);
	}
	
	/* Returns whether or not this function executed successfully */
	private boolean add(DomainRecord dr, int domainIndex) {
		if(domainIndex == dr.getDomain().length) {
			if(this.value != null) {
				return false;
			}
			
			this.value = dr;
			return true;
		}
		
		DomainTree child = new DomainTree();
		
		map.put(dr.getDomain()[domainIndex], child);
		
		return child.add(dr, domainIndex + 1);
	}
	
	public DomainRecord get(String[] domain) {
		return get(domain, 0);
	}
	
	private DomainRecord get(String[] domain, int domainIndex) {
		if(domainIndex == domain.length) {
			return this.value;
		}
		
		var child = map.get(domain[domainIndex]);
		
		if(child != null) {
			return child.get(domain, domainIndex + 1);
		}else{
			return value;
		}
	}
	
	public void clear() {
		map.clear();
	}
	
}
