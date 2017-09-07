package gr.tuc.softnet.zookeeper;   

/**
 * {@link SessionStateEvent} represents the various events regarding a session's state transitions.
 * 
 * @author Tassos Souris
 *
 */
public enum SessionStateEvent {
	/**
	 * Auth failed state
	 */
	AUTH_FAILED,
	
	/**
	 * The client is in the disconnected state - it is not connected to any server in the ensemble.
	 */
	DISCONNECTED,
	
	/**
	 * The serving cluster has expired this session.
	 */
	EXPIRED,
	
	/**
	 * The client is in the connected state - it is connected to a server in the ensemble (one of the servers specified in the host connection parameter during ZooKeeper client creation).
	 */
	SYNC_CONNECTED
}
