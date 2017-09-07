package gr.tuc.softnet.zookeeper.util;

import org.apache.zookeeper.ZooKeeper;

/**
 * A {@link ResilientZooKeeperOperationCommand} for the {@link ZooKeeper#delete(String, int)} method call.
 * 
 * @author Tassos Souris
 *
 */
public class DeleteZNodeResilientZooKeeperOperationCommand implements ResilientZooKeeperOperationCommand{

	/**
	 * A wrapper around the delete() method call.
	 * 
	 * @param args[0]
	 * 				The {@link ZooKeeper} client object.
	 * @param args[1]
	 * 				The path of the znode to be deleted (a String object).
	 * @param args[2]
	 * 				The version of the znode to be deleted (int).
	 * @return null
	 */
	@Override
	public Object execute(Object... args) throws Exception {
		ZooKeeper zookeeper = (ZooKeeper)args[0];
		String path = (String)args[1];
		int version = (Integer)args[2];
		
		zookeeper.delete(path, version);
		
		return null;
	}

}
