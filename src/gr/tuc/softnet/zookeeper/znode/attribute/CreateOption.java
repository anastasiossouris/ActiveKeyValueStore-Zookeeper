package gr.tuc.softnet.zookeeper.znode.attribute;

import org.apache.zookeeper.CreateMode;

/**
 * {@link CreateOption} defines how a znode is created in ZooKeeper.
 * 
 * @author Tassos Souris
 *
 */
public enum CreateOption{
	/**
	 * The znode will be deleted upon the client's disconnect
	 */
	EPHEMERAL("ephemeral",CreateMode.EPHEMERAL),
	
	/**
	 * The znode will be deleted upon the client's disconnect, and its name will be appended with a monotonically increasing number.
	 */
	EPHEMERAL_SEQUENTIAL("ephemeral-sequential",CreateMode.EPHEMERAL_SEQUENTIAL),
	
	/**
	 * The znode will not be automatically deleted upon client's disconnect.
	 */
	PERSISTENT("persistent",CreateMode.PERSISTENT),
	
	/**
	 * The znode will not be automatically deleted upon client's disconnect, and its name will be appended with a monotonically increasing number.
	 */
	PERSISTENT_SEQUENTIAL("persistent-sequential",CreateMode.PERSISTENT_SEQUENTIAL);
	
	// the string representation
	private String option = null;
	// the corresponding CreateMode object
	private CreateMode mode = null;
	
	private CreateOption(String option, CreateMode mode){
		this.option = option;
		this.mode = mode;
	}
	
	/**
	 * @return the string representation of this object
	 */
	@Override
	public String toString(){
		return option;
	}
	
	/**
	 * 
	 * @return the {@link CreateMode} object corresponing to this CreateOption.
	 */
	public CreateMode mode(){
		return mode;
	}
}
