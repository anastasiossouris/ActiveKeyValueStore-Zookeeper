package gr.tuc.softnet.zookeeper;

/**
 * {@link ZooKeeperException} and its subclasses are used to indicate error conditions regarding the 
 * various ZooKeeper operations.
 * 
 * @author Tassos Souris
 *
 */
public class ZooKeeperException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5245505496210227585L;

	/**
	 * Construct a new {@link ZooKeeperException} with null as its detail message.
	 */
	public ZooKeeperException(){
		super();
	}
	
	/**
	 * Construct a new {@link ZooKeeperException} with the specified detail message.
	 * @param message
	 * 			The detail message.
	 */
	public ZooKeeperException(String message){
		super(message);
	}
	
	/**
	 * Construct a new {@link ZooKeeperException} with the specified detail message and cause.
	 * 
	 * @param message
	 * 			The detail message.
	 * @param cause
	 * 			The casue.
	 */
	public ZooKeeperException(String message, Throwable cause){
		super(message,cause);
	}
	
	/**
	 * Construct a new {@link ZooKeeperException} with null as its detail message and the specified cause.
	 * @param cause
	 * 			The cause.
	 */
	public ZooKeeperException(Throwable cause){
		super(cause);
	}
}
