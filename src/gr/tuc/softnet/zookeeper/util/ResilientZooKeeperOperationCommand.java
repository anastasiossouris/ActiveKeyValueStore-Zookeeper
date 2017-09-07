package gr.tuc.softnet.zookeeper.util;

/**
 * {@link ResilientZooKeeperOperationCommand} is the command object passed to {@link ResilientZooKeeperOperation#doOperation(ResilientZooKeeperOperationCommand)} method.
 * 
 * @see ResilientZooKeeperOperation
 * @author Tassos Souris
 *
 */
public interface ResilientZooKeeperOperationCommand{
	
	/**
	 * A arbitrary ZooKeeper operation that is to be passed to the {@link ResilientZooKeeperOperation#doOperation(ResilientZooKeeperOperationCommand)} method
	 * for resilience.
	 * 
	 * @param args
	 * 			Arguments needed by the command
	 * @return The result of the execution
	 * @throws Exception
	 */
	public Object execute(Object... args) throws Exception;
}
