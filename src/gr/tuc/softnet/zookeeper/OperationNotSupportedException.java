package gr.tuc.softnet.zookeeper;

/**
 * {@link OperationNotSupportedException} indicates that a operation is not supported by the current API (not implemented).
 * 
 * @author Tassos Souris
 *
 */
public class OperationNotSupportedException extends ZooKeeperException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4939247853833986518L;

	/**
	 * Construct a new {@link OperationNotSupportedException} with null as its detail message.
	 */
	public OperationNotSupportedException(){
		super();
	}
	
	/**
	 * Construct a new {@link OperationNotSupportedException} with the specified detail message.
	 * @param message
	 * 			The detail message.
	 */
	public OperationNotSupportedException(String message){
		super(message);
	}
	
	/**
	 * Construct a new {@link OperationNotSupportedException} with the specified detail message and cause.
	 * 
	 * @param message
	 * 			The detail message.
	 * @param cause
	 * 			The casue.
	 */
	public OperationNotSupportedException(String message, Throwable cause){
		super(message,cause);
	}
	
	/**
	 * Construct a new {@link OperationNotSupportedException} with null as its detail message and the specified cause.
	 * @param cause
	 * 			The cause.
	 */
	public OperationNotSupportedException(Throwable cause){
		super(cause);
	}
}
