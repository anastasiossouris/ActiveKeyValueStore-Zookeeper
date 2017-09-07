package gr.tuc.softnet.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * {@link ZooKeeperHandle} is effectively a wrapper around a {@link ZooKeeper} client object.
 * 
 * <p>
 * A handle is created when establishing a session with the ZooKeeper service. The {@link ZooKeeperHandle} class represents such a handle by being a proxy
 * to the {@link ZooKeeper} client object. Note that session establishment is not the responsibility of the {@link ZooKeeperHandle} class.
 * To establish a session and retrieve a {@link ZooKeeperHandle} representing that session use the {@link ZooKeeperHandles.HandleFactory} class which serves
 * as a factory to create a {@link ZooKeeperHandle} object for which the associated sessions have been established.
 * </p>
 * 
 * <p>
 * {@link ZooKeeperHandle} is also responsible for keeping track of the session's state transitions. Of particular interest is the {@link #waitUntilConnected()} method
 * which should be used by the clients after they have received the {@link ZooKeeperHandle} from the handle factory to ensure that the session has been established correctly,
 * that is that the ZooKeeper client object is connected to a ZooKeeper server. The following code snippet illustrates this (without error checking and such):
 * 	<pre>
 * 		// parameters for the session
 * 		String connectString = "127.0.0.1:4545";
 * 		int sessionTimeout = 3000;
 * 
 * 		// get the handle factory
 * 		ZooKeeperHandles.HandleFactory handleFactory = ZooKeeperHandles.getHandleFactory();
 * 		
 * 		// get a new handle from the factory
 * 		ZooKeeperHandle handle = handleFactory.newHandle(connectString,sessionTimeout);
 * 
 * 		// wait until the connection has been established before using the handle...
 * 		handle.waitUntilConnected();
 * 	</pre>
 * </p>
 * 
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * @see ZooKeeperHandles.HandleFactory
 * @author Tassos Souris
 *
 */
public class ZooKeeperHandle implements Watcher{
	// The ZooKeeper client object for the session
	private ZooKeeper zookeeper = null;
	
	/**
	 * Construct a new {@link ZooKeeperHandle} object without a {@link ZooKeeper} client object attached to it.
	 */
	public ZooKeeperHandle(){
	}
	
	/**
	 * Construct a new {@link ZooKeeperHandle} object with the given {@link ZooKeeper} client object for the session.
	 * 
	 * @param zookeeper
	 * 			The {@link ZooKeeper} client object for the session.
	 */
	public ZooKeeperHandle(ZooKeeper zookeeper){
		this.zookeeper = zookeeper;
	}
	
	/**
	 * Set the {@link ZooKeeper} client object for the session.
	 * 
	 * @param zookeeper
	 * 			The {@link ZooKeeper} client object for the session.
	 */
	public void setZooKeeper(ZooKeeper zookeeper){
		this.zookeeper = zookeeper;
	}
	
	/**
	 * 
	 * @return The {@link ZooKeeper} client object.
	 */
	public ZooKeeper getZooKeeper(){
		return zookeeper;
	}
	
	@Override
	public synchronized void process(WatchedEvent event) {		
	}
}