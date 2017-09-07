package gr.tuc.softnet.zookeeper.util;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * A {@link ResilientZooKeeperOperationCommand} for the {@link ZooKeeper#getData(String, boolean, org.apache.zookeeper.data.Stat)} method call.
 * 
 * @author Tassos Souris
 *
 */
public class GetDataZNodeResilientZooKeeperOperationCommand implements ResilientZooKeeperOperationCommand{

	/**
	 * A wrapper around the getData() method.
	 * 
	 * @param args[0]
	 * 			The {@link ZooKeeper} client object.
	 * @param args[1]
	 * 			The path of the znode (a String object).
	 * @param args[2]
	 * 			Whether to set a watch or not (a Boolean object).
	 * @param args[3]
	 * 			The {@link Stat} object.
	 * @return The byte [] object returned from getData().
	 */
	@Override
	public Object execute(Object... args) throws Exception {
		ZooKeeper zookeeper = (ZooKeeper)args[0];
		String path = (String)args[1];
		boolean watch = (Boolean)args[2];
		Stat stat = (Stat)args[3];
		
		return zookeeper.getData(path, watch, stat);
	}

}
