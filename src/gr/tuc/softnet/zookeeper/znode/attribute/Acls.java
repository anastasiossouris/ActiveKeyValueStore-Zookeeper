package gr.tuc.softnet.zookeeper.znode.attribute;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

/**
 * {@link Acls} contains classes, methods and objects regarding access control lists.
 * 
 * @author Tassos Souris
 *
 */
public class Acls {	
	/**
	 * A {@link Acl} object which represents an open access control list. That is, it grants all permissions to anyone.
	 */
	public static final Acl OPEN_ACL_UNSAFE = new Acl();
	
	static{
		// Construct the OPEN_ACL_UNSAFE access control list
		AclEntryScheme openAclUnsafeScheme = AclEntryScheme.WORLD;
		Identity openAclUnsafeIdentity = new Identity("anyone");
		Set<AclEntryPermission> openAclUnsafePermissions = new HashSet<AclEntryPermission>();
		openAclUnsafePermissions.add(AclEntryPermission.ADMIN);
		openAclUnsafePermissions.add(AclEntryPermission.CREATE);
		openAclUnsafePermissions.add(AclEntryPermission.DELETE);
		openAclUnsafePermissions.add(AclEntryPermission.READ);
		openAclUnsafePermissions.add(AclEntryPermission.WRITE);
		
		AclEntry.Builder builder = AclEntry.newBuilder();
		
		builder.setIdentity(openAclUnsafeIdentity).setPermissions(openAclUnsafePermissions).setScheme(openAclUnsafeScheme);
		
		AclEntry openAclUnsafeEntry = builder.build();
		
		OPEN_ACL_UNSAFE.addAclEntry(openAclUnsafeEntry);
	}
	
	
	/**
	 * Convert a {@link Acl} object to the acl format accepted by ZooKeeper.
	 * 
	 * @param acl
	 * 			the acl to convert
	 * @return the access control list as accepted by the ZooKeeper.
	 * @throws NullPointerException
	 * 			if the acl parameter is null
	 */
	public static List<ACL> toZooKeeperACL(Acl acl) throws NullPointerException{
		if (acl == null){
			throw new NullPointerException();
		}
		
		List<ACL> aclList = new LinkedList<ACL>();
		
		for (AclEntry entry : acl.entriesSet()){
			ACL aclListEntry = new ACL();
			Identity identity = entry.identity();
			Set<AclEntryPermission> permissions = entry.permissions();
			AclEntryScheme scheme = entry.sheme();
			
			aclListEntry.setId(new Id(scheme.toString(), identity.getId()));
			
			int perms = 0;
			for(AclEntryPermission perm : permissions){
				perms |= perm.code();
			}
			
			aclListEntry.setPerms(perms);
		}
		
		return aclList;
	}
}
