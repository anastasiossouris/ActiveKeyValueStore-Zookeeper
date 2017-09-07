package gr.tuc.softnet.zookeeper.util;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

/**
 * A {@link ResilientZooKeeperOperationCommand} for the {@link ZooKeeper#create(String, byte[], java.util.List, org.apache.zookeeper.CreateMode)} method call.
 * 
 * @author Tassos Souris
 *
 */
public class CreateZNodeResilientZooKeeperOperationCommand implements ResilientZooKeeperOperationCommand{

	/**
	 * Wrapper around the create() method call.
	 * 
	 * @param args[0]
	 * 				A {@link ZooKeeper} client object.
	 * @param args[1]
	 * 				The path of the znode to be created (a String object).
	 * @param args[2]
	 * 				The data to store in the znode (byte [] object).
	 * @param args[3]
	 * 				The List<ACL> object.
	 * @param args[4]
	 * 				The CreateMode object.
	 * @return The path of the created znode (must be cast to String).
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object... args) throws Exception {
		ZooKeeper zookeeper = (ZooKeeper)args[0];
		String path = (String)args[1];
		byte [] data = (byte [])args[2];
		List<ACL> acl = (List<ACL>)args[3];
		CreateMode createMode = (CreateMode)args[4];
		
		return zookeeper.create(path, data, acl, createMode);
	}

}
