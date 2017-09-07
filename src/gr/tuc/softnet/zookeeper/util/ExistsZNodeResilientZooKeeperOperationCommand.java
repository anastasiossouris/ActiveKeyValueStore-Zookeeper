package gr.tuc.softnet.zookeeper.util;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**]
 * A {@link ResilientZooKeeperOperationCommand} around the {@link ZooKeeper#exists(String, boolean)} method call.
 * 
 * @author Tassos Souris
 *
 */
public class ExistsZNodeResilientZooKeeperOperationCommand implements ResilientZooKeeperOperationCommand{

	/**
	 * A wrapper around the exists() method call.
	 * 
	 * @param args[0]
	 * 				The {@link ZooKeeper} client object.
	 * @param args[1]
	 * 				The path of the znode (a String object).
	 * @param args[2]
	 * 				Whether to set a watch or not (a Boolean object).
	 * @return The {@link Stat} object returned from the exists() method call.
	 */
	@Override
	public Object execute(Object... args) throws Exception {
		ZooKeeper zookeeper = (ZooKeeper)args[0];
		String path = (String)args[1];
		boolean watch = (Boolean)args[2];
		
		return zookeeper.exists(path, watch);
	}

}
