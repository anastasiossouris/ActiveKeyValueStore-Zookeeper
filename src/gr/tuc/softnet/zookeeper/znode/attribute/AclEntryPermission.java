package gr.tuc.softnet.zookeeper.znode.attribute;

import org.apache.zookeeper.ZooDefs;

/**
 * {@link AclEntryPermission} defines the permissions for use with the permission component of an ACL entity.
 * 
 * @author Tassos Souris
 *
 */
public enum AclEntryPermission{
	/**
	 * Create a child znode
	 */
	CREATE("create",ZooDefs.Perms.CREATE),
	
	/**
	 * Get data from a znode and list its children
	 */
	READ("read",ZooDefs.Perms.READ),
	
	/**
	 * Set data for a znode
	 */
	WRITE("write",ZooDefs.Perms.WRITE),
	
	/**
	 * Delete a child znode
	 */
	DELETE("delete",ZooDefs.Perms.DELETE),
	
	/**
	 * Set permissions
	 */
	ADMIN("admin",ZooDefs.Perms.ADMIN);
	
	// the string representation of this permission
	private String permission = null;
	// the code of the permission as expected by ZooKeeper
	private int code = 0;
	
	private AclEntryPermission(String permission, int code){
		this.permission = permission;
		this.code = code;
	}
	
	/**
	 * @return the string representation of this permission object
	 */
	@Override
	public String toString(){
		return permission;
	}
	
	/**
	 * 
	 * @return the code of this permission object as expected by ZooKeeper
	 */
	public int code(){
		return code;
	}
}
