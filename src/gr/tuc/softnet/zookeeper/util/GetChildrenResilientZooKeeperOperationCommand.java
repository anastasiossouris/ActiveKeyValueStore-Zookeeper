package gr.tuc.softnet.zookeeper.util;

import org.apache.zookeeper.ZooKeeper;

/**
 * A {@link ResilientZooKeeperOperationCommand} around the {@link ZooKeeper#getChildren(String, boolean)} method call.
 * 
 * @author Tassos Souris
 *
 */
public class GetChildrenResilientZooKeeperOperationCommand implements ResilientZooKeeperOperationCommand{

	/**
	 * A wrapper around the getChildren() method call.
	 * 
	 * @param args[0]
	 * 			The {@link ZooKeeper} client object.
	 * @param args[1]
	 * 			The path of the znode (a String object).
	 * @param args[2]
	 * 			Whether to set a wath or not (a Boolean object).
	 * @return The List<String> object returned from getChildren().
	 */
	@Override
	public Object execute(Object... args) throws Exception {
		ZooKeeper zookeeper = (ZooKeeper)args[0];
		String path = (String)args[1];
		boolean watch = (Boolean)args[2];
		
		return zookeeper.getChildren(path, watch);
	}

}
