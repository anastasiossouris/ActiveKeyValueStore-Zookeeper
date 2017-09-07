package gr.tuc.softnet.zookeeper.util;

import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;

/**
 * {@link ResilientZooKeeperOperation} is a aid in making operations resilient in face of ZooKeeper failures.
 * 
 * <p>
 * Specifically, clients pass a {@link ResilientZooKeeperOperationCommand} to the {@link #doOperation(ResilientZooKeeperOperationCommand)} method, and
 * bypass certain failure modes. Refer to the {@link #doOperation(ResilientZooKeeperOperationCommand)} method for more information.
 * </p>
 * 
 * <p>
 * This class is thread-safe
 * </p>
 * 
 * @see ResilientZooKeeperOperationCommand
 * @author Tassos Souris
 *
 */
public class ResilientZooKeeperOperation {
	// maximum number of tries we attempt the command before aborting
	int max_tries = 0;
	// the number of seconds we sleep before each attempt
	long retry_period_seconds = 0;
	
	/**
	 * Construct a new ResilientZooKeeperOperation.
	 */
	public ResilientZooKeeperOperation(){
		
	}
	
	/**
	 * Construct a new ResilientZooKeeperOperation.
	 * 
	 * @param max_tries
	 * 				maximum number of tries we attempt the command before aborting
	 * @param retry_period_seconds
	 * 				the number of seconds we sleep before each attempt
	 */
	public ResilientZooKeeperOperation(int max_tries, long retry_period_seconds){
		this.max_tries = max_tries;
		this.retry_period_seconds = retry_period_seconds;
	}
	
	/**
	 * 
	 * @param max_tries
	 * 				maximum number of tries we attempt the command before aborting
	 */
	public void setMaxTries(int max_tries){
		this.max_tries = max_tries;
	}
	
	/**
	 * 
	 * @param retry_period_seconds
	 * 				the number of seconds we sleep before each attempt
	 */
	public void setRetryPeriodSeconds(long retry_period_seconds){
		this.retry_period_seconds = retry_period_seconds;
	}
	
	/**
	 * 
	 * @return maximum number of tries we attempt the command before aborting
	 */
	public int getMaxTries(){
		return max_tries;
	}
	
	/**
	 * 
	 * @return the number of seconds we sleep before each attempt
	 */
	public long getRetryPeriodSeconds(){
		return retry_period_seconds;
	}
	
	/**
	 * Execute the passed {@link ResilientZooKeeperOperationCommand} command object in a resilient manner.
	 * In case of an exception, the command is retried for a maximum number of tries (specified in the {@link #setMaxTries(int)} method)
	 * after waiting for some seconds (specified in the {@link #setRetryPeriodSeconds(int)} method).
	 * 
	 * The behavior of {@link #doOperation(ResilientZooKeeperOperationCommand)} in case of exceptions is as:
	 * <ol>
	 * 		<li>If no exception is throwned then the result of the command execution is returned</li>
	 * 		<li>If an {@link KeeperException.SessionExpiredException} is throwned then it is propagated to the client.</li>
	 * 		<li>If an {@link KeeperException.AuthFailedException} is throwned then it is propagated to the client.</li>
	 * 		<li>If an {@link KeeperException.ConnectionLossException} is throwned then the command is retried, unless not permitted by the maximum count
	 * 			in which case the exception is propagated to the client.</li>
	 * 		<li>All other exceptions are propagated to the client.</li>
	 * </ol>
	 * 
	 * @param command
	 * 				The {@link ResilientZooKeeperOperationCommand} object to execute.
	 * @return The result of the executed command.
	 * @throws Exception
	 */
	public Object doOperation(ResilientZooKeeperOperationCommand command, Object... args) throws Exception{
		int retries = 0; // how many times we tried so far
		
		while (true){
			try{
				return command.execute(args);
			}
			catch(KeeperException.SessionExpiredException sessionExpired){
				throw sessionExpired;
			}
			catch(KeeperException.AuthFailedException authFailed){
				throw authFailed;
			}
			catch(KeeperException.ConnectionLossException connectionLoss){
				// check if passed the max_tries limit
				if (retries++ == max_tries){
					throw connectionLoss;
				}
				// otherwise sleep for the given period and retry
				TimeUnit.SECONDS.sleep(retry_period_seconds);
			}
		}
	}
}
