package gr.tuc.softnet.zookeeper.znode.attribute;

/**
 * {@link Identity} represents the id corresponding to a specific {@link AclEntryScheme} scheme.
 * 
 * The correspondence between the schemes and the ids is as follows:
 * <dl>
 * 		<dt>world</dt>
 * 		<dd>has a single id, anyone, that represents anyone.</dd>
 * 
 * 		<dt>auth</dt>
 * 		<dd>doesn't use any id, represents any authenticated user.</dd>
 * 
 * 		<dt>digest</dt>
 * 		<dd>uses a username:password string to generate MD5 hash which is then used as an ACL ID identity. 
 * 		Authentication is done by sending the username:password in clear text. When used in the ACL the expression will be the username:base64  encoded SHA1  password digest.</dd>
 * 
 * 		<dt>ip</dt>
 * 		<dd>uses the client host IP as an ACL ID identity. The ACL expression is of the form addr/bits where the most significant bits  of addr are matched against the most significant bits of the client host IP.</dd>
 * </dl>
 * 
 * @author Tassos Souris
 *
 */
public class Identity {
	private String id = null;
	
	/**
	 * Construct an empty {@link Identity} object.
	 */
	public Identity(){
		
	}
	
	/**
	 * Construct an {@link Identity} object with the passed id.
	 * 
	 * @param id
	 * 		the identity
	 * @throws NullPointerException
	 * 		if id is null
	 */
	public Identity(String id){
		if (id == null){
			throw new NullPointerException();
		}
		this.id = id;
	}
	
	
	/**
	 * Sets the given id for this Identity.
	 * 
	 * @param id
	 * 		the identity
	 * @throws NullPointerException
	 * 		if id is null
	 */
	public void setId(String id){
		if (id == null){
			throw new NullPointerException();
		}
		this.id = id;
	}
	
	/**
	 * 
	 * @return the identity
	 */
	public String getId(){
		return id;
	}
}
