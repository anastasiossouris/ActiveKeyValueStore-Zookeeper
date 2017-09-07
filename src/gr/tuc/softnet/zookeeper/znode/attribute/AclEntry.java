package gr.tuc.softnet.zookeeper.znode.attribute;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link AclEntry} represents an entry in an ACL.
 * 
 * @see AclEntryScheme
 * @see AclEntryPermission
 * @see Identity
 * @author Tassos Souris
 *
 */
public class AclEntry {
	// the permissions of this AclEntry
	private Set<AclEntryPermission> permissions = null;
	
	// the scheme of this AclEntry
	private AclEntryScheme scheme = null;
	
	// the identity of this AclEntry
	private Identity identity = null;
	
	
	/**
	 * Construct a new AclEntry given the permissions, the scheme and the Identity passed as parameter.
	 * Note that a copy of the permissions is stored in the AclEntry object.
	 * 
	 * @param permissions
	 * 			The set of permissions for this AclEntry
	 * @param scheme
	 * 			The scheme of this AclEntry
	 * @param identity
	 * 			The Identity of this AclEntry
	 * @throws NullPointerException
	 * 			if at least one of the parameters is null
	 */
	private AclEntry(Set<AclEntryPermission> permissions, AclEntryScheme scheme, Identity identity) throws NullPointerException{
		if (permissions == null || scheme == null || identity == null){
			throw new NullPointerException();
		}
		
		this.permissions = new HashSet<AclEntryPermission>(permissions);
		this.scheme = scheme;
		this.identity = identity;
	}
	
	/**
	 * Construct an empty AclEntry object.
	 */
	private AclEntry(){
	}
	
	/**
	 * {@link Builder} is used to build {@link AclEntry} objects.
	 * 
	 * 
	 * This class is not thread-safe.
	 * 
	 * @author Tassos Souris
	 *
	 */
	public static class Builder{
		// the acl entry
		private AclEntry aclEntry = null;
		
		// have the permissions been set?
		private boolean permissionsSet = false;
		// has the scheme been set?
		private boolean schemeSet = false;
		// has the Identity been set?
		private boolean identitySet = false;
		
		/**
		 * Construct a Builder object.
		 */
		public Builder(){
			this.aclEntry = new AclEntry();
		}
		
		/**
		 * Constructs a {@link AclEntry} from the components of this builder.
		 * 
		 * @return The acl entry.
		 * @throws IllegalStateException 
		 * 				If at least one of the components have not been set.
		 */
		public AclEntry build() throws IllegalStateException{
			if (!permissionsSet || !schemeSet || !identitySet){
				throw new IllegalStateException();
			}
			return aclEntry;
		}
		
		/**
		 * Sets the permissions component of the builder.
		 * 
		 * @param permissions
		 * 				The permissions a copy of which is used as the component.
		 * @return this builder
		 * @throws IllegalStateException
		 * 				If the permissions component has already been set for this builder.
		 * @throws NullPointerException
		 * 				if permissions is null
		 */
		public AclEntry.Builder setPermissions(Set<AclEntryPermission> permissions) throws IllegalStateException, NullPointerException{
			if (permissions == null){
				throw new NullPointerException();
			}
			else if (permissionsSet){
				throw new IllegalStateException();
			}
			
			aclEntry.setPermissions(permissions);
			
			return this;
		}
		
		/**
		 * Sets the scheme component of the builder.
		 * 
		 * @param scheme
		 * 			The acl scheme.
		 * @return this builder
		 * @throws IllegalStateException
		 * 				If the scheme component has already been set for this builder.
		 * @throws NullPointerException
		 * 				if scheme is null
		 */
		public AclEntry.Builder setScheme(AclEntryScheme scheme) throws IllegalStateException, NullPointerException{
			if (scheme == null){
				throw new NullPointerException();
			}
			else if (schemeSet){
				throw new IllegalStateException();
			}
			
			aclEntry.setScheme(scheme);
			
			return this;
		}
		
		/**
		 * Sets the Identity component of the builder.
		 * 
		 * @param identity
		 * 			The Identity of the acl entry.
		 * @return this builder
		 * @throws IllegalStateException
		 * 				If the Identity component has already been set for this builder.
		 * @throws NullPointerException
		 */
		public AclEntry.Builder setIdentity(Identity identity) throws IllegalStateException, NullPointerException{
			if (identity == null){
				throw new NullPointerException();
			}
			else if (identitySet){
				throw new IllegalStateException();
			}
			
			aclEntry.setIdentity(identity);
			
			return this;
		}
	}
	
	/**
	 * Construct a new Builder object. The elements of the AclEntry are initially empty (null).
	 * 
	 * @return a new builder
	 */
	public static AclEntry.Builder newBuilder(){
		return new Builder();
	}
	
	/**
	 * Return a copy of the permissions of this acl entry.
	 * 
	 * @return The permissions for this acl entry.
	 */
	public Set<AclEntryPermission> permissions(){
		return new HashSet<AclEntryPermission>(permissions);
	}
	
	/**
	 * Return the scheme of this acl entry.
	 * 
	 * @return The scheme of this AclEntry
	 */
	public AclEntryScheme sheme(){
		return scheme;
	}
	
	/**
	 * Return the Identity of this acl entry.
	 * 
	 * @return The Identity of this AclEntry
	 */
	public Identity identity(){
		return identity;
	}
	
	/** 
	 * Store a copy of the passed permissions in this AclEntry.
	 * 
	 * @param permissions
	 * 				The permissions of this AclEntry
	 * @throws NullPointerException
	 * 				if permissions is null
	 */
	private void setPermissions(Set<AclEntryPermission> permissions) throws NullPointerException{
		if (permissions == null){
			throw new NullPointerException();
		}
		this.permissions = new HashSet<AclEntryPermission>(permissions);
	}
	
	/**
	 * 
	 * @param scheme
	 * 				The schem for this AclEntry
	 * @throws NullPointerException
	 * 				if scheme is null.	
	 */
	private void setScheme(AclEntryScheme scheme) throws NullPointerException{
		if (scheme ==  null){
			throw new NullPointerException();
		}
		this.scheme = scheme;
	}
	
	/**
	 * 
	 * @param Identity
	 * 				The Identity for this AclEntry
	 * @throws NullPointerException
	 * 				if Identity is null
	 */
	private void setIdentity(Identity identity) throws NullPointerException{
		if (identity == null){
			throw new NullPointerException();
		}
		this.identity = identity;
	}
}
