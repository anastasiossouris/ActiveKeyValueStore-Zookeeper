package gr.tuc.softnet.zookeeper.znode.attribute;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * {@link Acl} represents an Access Control List.
 * 
 * This class is thread-safe.
 * 
 * @author Tassos Souris
 *
 */
public class Acl {
	// the set of acl entries in the acl
	private Set<AclEntry> aclEntries = null;
	
	/**
	 * Construct a new Acl object.
	 */
	public Acl(){
		this.aclEntries = new HashSet<AclEntry>();
	}
	
	/**
	 * Add the given acl entry in the acl.
	 * 
	 * @param aclEntry
	 * 				An AclEntry object.
	 */
	public void addAclEntry(AclEntry aclEntry){
		aclEntries.add(aclEntry);
	}
	
	/**
	 * 
	 * @return A copy of the entries in the acl.
	 */
	public Set<AclEntry> entriesSet(){
		return new HashSet<AclEntry>(aclEntries);
	}
	
	/**
	 * 
	 * @return An iterator over a copy of the entries in the acl.
	 */
	public Iterator<AclEntry> entriesIterator(){
		return new HashSet<AclEntry>(aclEntries).iterator();
	}
}
