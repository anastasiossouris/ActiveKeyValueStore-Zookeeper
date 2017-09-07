package gr.tuc.softnet.zookeeper.util;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * A {@link ResilientZooKeeperOperationCommand} for the {@link ZooKeeper#setData(String, byte[], int)} method call.
 * 
 * @author Tassos Souris
 *
 */
public class SetDataZNodeResilientZooKeeperOperation implements ResilientZooKeeperOperationCommand{

	/**
	 * A wrapper around the setData() method.
	 * 
	 * @param args[0]
	 * 				The {@link ZooKeeper} client object.
	 * @param args[1]
	 * 				The path of the znode (a String object).
	 * @param args[2]
	 * 				The data to set (byte []).
	 * @param args[3]
	 * 				The version of the znode (int).
	 * @return The {@link Stat} object as returned from setData().
	 */
	@Override
	public Object execute(Object... args) throws Exception {
		ZooKeeper zookeeper = (ZooKeeper)args[0];
		String path = (String)args[1];
		byte [] data = (byte [])args[2];
		int version = (Integer)args[3];
		
		return zookeeper.setData(path, data, version);
	}

}
