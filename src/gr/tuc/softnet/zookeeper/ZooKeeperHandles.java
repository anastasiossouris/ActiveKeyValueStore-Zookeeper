package gr.tuc.softnet.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.ZooKeeper;


/**
 * {@link ZooKeeperHandles} contains only static methods returning or operating on {@link ZooKeeperHandle} objects.
 * 
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * @author Tassos Souris
 *
 */
public class ZooKeeperHandles {
	// The singleton instance for the ZooKeeperHandles.HandleFactory factory
	private static ZooKeeperHandles.HandleFactory handleFactorySingletonInstance = null;
	
	/**
	 * {@link HandleFactory} is a factory to create new {@link ZooKeeperHandle} objects.
	 * 
	 * <p>
	 * {@link HandleFactory} contains methods to establish a session to the ZooKeeper service and build a {@link ZooKeeperHandle} object around it.
	 * Refer to the {@link #newHandle(String, int)} and {@link #newHandle(String, int, long, byte[])} methods for more information.
	 * </p>
	 * 
	 * <p>
	 *	An example for creating a handle to the ZooKeeper service is the following (without error checking and such):
	 *	<pre>
	 *		String connectString = "127.0.0.1:4545";
	 *		int sessionTimeout = 3000;
	 *
	 *		ZooKeeperHandles.HandleFactory handleFactory = ZooKeeperHandles.getHandleFactory();
	 *		ZooKeeperHandle handle = handleFactory.newHandle(connectString, sessionTimeout);
	 *	</pre>
	 * </p>
	 *
	 * This class is thread-safe.
	 * 
	 * @see ZooKeeperHandle
	 * @see ZooKeeperHandleStates
	 * @author Tassos Souris
	 *
	 */
	public static class HandleFactory{
		/**
		 * Construct a new {@link HandleFactory} object.
		 */
		public HandleFactory(){
			
		}
		
		/**
		 * Establish a session with the ZooKeeper service and return a {@link ZooKeeperHandle} handle object around it.
		 * 
		 * <p>
		 * For the session to be established the following must be defined:
		 * <dl>
		 * 		<dt>connectString</dt>
		 * 		<dd>The list host:port pairs, each corresponding to a ZooKeeper server.</dd>
		 * 
		 * 		<dt>sessionTimeout</dt>
		 * 		<dd>Session timeout in milliseconds.</dd>
		 * 
		 * 		<dt>handleStatesManager</dt>
		 * 		<dd>Responsible for managing the session's state transitions and querying about the state of the session.</dd>
		 * </dl>
		 * </p>
		 * 
		 * @param connectString
		 * 				comma separated host:port pairs, each corresponding to a zk server. e.g. "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002" If the optional chroot suffix is used the example would look like: "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002/app/a" where the client would be rooted at "/app/a" and all paths would be relative to this root - ie getting/setting/etc... "/foo/bar" would result in operations being run on "/app/a/foo/bar" (from the server perspective).
		 * @param sessionTimeout
		 * 				session timeout in milliseconds
		 * @return The {@link ZooKeeperHandle} object.
		 * @throws IOException
		 * 				 in cases of network failure
		 * @throws IllegalArgumentException
		 * 				 if an invalid chroot path is specified
		 */
		public synchronized ZooKeeperHandle newHandle(String connectString, int sessionTimeout) throws IOException, IllegalArgumentException{
			ZooKeeperHandle handle = new ZooKeeperHandle();
			
			// Create the ZooKeeper client object
			ZooKeeper zookeeper = new ZooKeeper(connectString, sessionTimeout, handle);
			
			// Box the client object in the zookeeper handle
			handle.setZooKeeper(zookeeper);
			
			return handle;
		}
		
		/**
		 * Establish a session with the ZooKeeper service and return a {@link ZooKeeperHandle} handle object around it.
		 * 
		 * <p>
		 * For the session to be established the following must be defined:
		 * <dl>
		 * 		<dt>connectString</dt>
		 * 		<dd>The list host:port pairs, each corresponding to a ZooKeeper server.</dd>
		 * 
		 * 		<dt>sessionTimeout</dt>
		 * 		<dd>Session timeout in milliseconds.</dd>
		 * 
		 * 		<dt>sessionId</dt>
		 * 		<dd>specific session id to use if reconnecting.</dd>
		 * 
		 * 		<dt>sessionPasswd</dt>
		 * 		<dd>password for this session.</dd>
		 * </dl>
		 * </p>
		 * 
		 * @param connectString
		 * 				comma separated host:port pairs, each corresponding to a zk server. e.g. "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002" If the optional chroot suffix is used the example would look like: "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002/app/a" where the client would be rooted at "/app/a" and all paths would be relative to this root - ie getting/setting/etc... "/foo/bar" would result in operations being run on "/app/a/foo/bar" (from the server perspective).
		 * @param sessionTimeout
		 * 				session timeout in milliseconds
		 * @param sessionId
		 * 				specific session id to use if reconnecting.
		 * @param sessionPasswd
		 * 				password for this session.
		 * @return The {@link ZooKeeperHandle} object.
		 * @throws IOException
		 * 				 in cases of network failure
		 * @throws IllegalArgumentException
		 * 				 if an invalid chroot path is specified
		 */
		public synchronized ZooKeeperHandle newHandle(String connectString, int sessionTimeout, long sessionId, byte[] sessionPasswd)  throws IOException, IllegalArgumentException{
			ZooKeeperHandle handle = new ZooKeeperHandle();
			
			// Create the ZooKeeper client object
			ZooKeeper zookeeper = new ZooKeeper(connectString, sessionTimeout, handle, sessionId, sessionPasswd);
			
			// Box the client object in the zookeeper handle
			handle.setZooKeeper(zookeeper);
			
			return handle;
		}
	}
	
	/**
	 * Return the {@link ZooKeeperHandles.HandleFactory} singleton instance used as a factory to create new {@link ZooKeeperHandle} objects.
	 * 
	 * @return The {@link ZooKeeperHandles.HandleFactory} singleton instance.
	 */
	public static synchronized ZooKeeperHandles.HandleFactory getHandleFactory(){
		if (handleFactorySingletonInstance == null){
			handleFactorySingletonInstance = new ZooKeeperHandles.HandleFactory();
		}
		return handleFactorySingletonInstance;
	}
}
