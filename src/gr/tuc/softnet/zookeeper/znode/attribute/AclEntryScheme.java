package gr.tuc.softnet.zookeeper.znode.attribute;

/**
 * {@link AclEntryScheme} defines the scheme to use with an Acl entry
 * 
 * @author Tassos Souris
 *
 */
public enum AclEntryScheme{
	/**
	 * has a single id, anyone, that represents anyone.
	 */
	WORLD("world"),
	
	/**
	 * doesn't use any id, represents any authenticated user.
	 */
	AUTH("auth"),
	
	/**
	 * uses a username:password string to generate MD5 hash which is then used as an ACL ID identity. 
	 * Authentication is done by sending the username:password in clear text. When used in the ACL the expression will be the username:base64  encoded SHA1  password digest.
	 */
	DIGEST("digest"),
	
	/**
	 * uses the client host IP as an ACL ID identity. The ACL expression is of the form addr/bits where the most significant bits  of addr are matched against the most significant bits of the client host IP.
	 */
	IP("ip");
	
	
	// the scheme in its string representation as it is expected by ZooKeeper
	private String scheme = null;
	
	private AclEntryScheme(String scheme){
		this.scheme = scheme;
	}
	
	/**
	 * @return the string representation of this scheme as epxected by ZooKeeper
	 */
	@Override
	public String toString(){
		return scheme;
	}
}
