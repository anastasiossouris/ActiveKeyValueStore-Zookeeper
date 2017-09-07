package gr.tuc.softnet.zookeeper;

import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * This thread is not thread-safe.
 * 
 * @author Tassos Souris
 *
 */
public abstract class Session{
	// the zookeeper client object.
	protected ZooKeeper zookeeper = null;


	/**
	 * The session id for this ZooKeeper client instance. The value returned is not valid until the client connects to a server and may change after a re-connect.
	 * 
	 * @return the session id
	 */
	public long getSessionId(){
		return zookeeper.getSessionId();
	}
	
	/**
	 * The session password for this ZooKeeper client instance. The value returned is not valid until the client connects to a server and may change after a re-connect.
	 * 
	 * @return the session's password
	 */
	public byte [] getSessionPasswd(){
		return zookeeper.getSessionPasswd();
	}
	
	/**
	 * The negotiated session timeout for this ZooKeeper client instance. The value returned is not valid until the client connects to a server and may change after a re-connect.
	 * 
	 * @return the session's timeout
	 */
	public int getSessionTimeout(){
		return zookeeper.getSessionTimeout();
	}
	
	/**
	 * The session's state.
	 * 
	 * @return the session's state.
	 */
	public ZooKeeper.States getSessionState(){
		return zookeeper.getState();
	}
	
	/**
	 * Close this client object. Once the client is closed, its session becomes invalid. All the ephemeral nodes in the ZooKeeper server associated with the session will be removed. The watches left on those nodes (and on their parents) will be triggered. 
	 * 
	 * @throws InterruptedException
	 */
	public void close() throws InterruptedException{
		zookeeper.close();
	}
}
