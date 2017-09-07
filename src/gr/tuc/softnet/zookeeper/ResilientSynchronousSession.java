package gr.tuc.softnet.zookeeper;

import gr.tuc.softnet.zookeeper.znode.Path;
import gr.tuc.softnet.zookeeper.znode.attribute.Acl;
import gr.tuc.softnet.zookeeper.znode.attribute.CreateOption;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

/**
 * A {@link SynchronousSession} that makes each operation resilient. That is, each operation tries a maximum number of retries and sleeps for some period between each attempt
 * before returning on error. The exception that is caught for this purpose is only {@link KeeperException.ConnectionLossException}. Any other exception that occurs is re-throwned 
 * to the client.
 * 
 * @see SynchronousSession
 * @author Tassos Souris
 *
 */
public class ResilientSynchronousSession extends SynchronousSession{
	// how many retries before aborting
	private int maxRetries = 0;
	// how long to wait before giving up, in units of unit
	private long timeout = 0;
	// a TimeUnit determining how to interpret the timeout parameter
	private TimeUnit unit = null;
	
	/**
	 * A resilient {@link SynchronousSession#create(Path, byte[], Acl, CreateOption)} operation.
	 */
	@Override
	public Path create(Path path, byte[] data, Acl acl,
			CreateOption createOption) throws KeeperException,
			InterruptedException, IllegalArgumentException,
			NullPointerException {
		int retries = 0;
		while (true){
			try{
				return super.create(path, data, acl, createOption);
			}
			catch(KeeperException.ConnectionLossException e){
				if (retries++ == maxRetries){
					throw e;
				}
				// sleep then retry
				unit.sleep(timeout);
			}
		}
	}
	
	/**
	 * A resilient {@link SynchronousSession#delete(Path, int)} operation.
	 */
	@Override
	public void delete(Path path, int version) throws KeeperException,
			InterruptedException, IllegalArgumentException,
			NullPointerException {
		int retries = 0;
		while (true){
			try{
				super.delete(path, version);
			}
			catch(KeeperException.ConnectionLossException e){
				if (retries++ == maxRetries){
					throw e;
				}
				// sleep then retry
				unit.sleep(timeout);
			}
		}
	}
	
	/**
	 * A resilient {@link SynchronousSession#exists(Path, Watcher)} operation.
	 */
	@Override
	public Stat exists(Path path, Watcher watcher) throws KeeperException,
			InterruptedException, IllegalArgumentException,
			NullPointerException {
		int retries = 0;
		while (true){
			try{
				return super.exists(path, watcher);
			}
			catch(KeeperException.ConnectionLossException e){
				if (retries++ == maxRetries){
					throw e;
				}
				// sleep then retry
				unit.sleep(timeout);
			}
		}
	}
	
	/**
	 * A resilient {@link SynchronousSession#getData(Path, Watcher, Stat)} operation.
	 */
	@Override
	public byte[] getData(Path path, Watcher watcher, Stat stat)
			throws KeeperException, InterruptedException,
			IllegalArgumentException, NullPointerException {
		int retries = 0;
		while (true){
			try{
				return super.getData(path, watcher, stat);
			}
			catch(KeeperException.ConnectionLossException e){
				if (retries++ == maxRetries){
					throw e;
				}
				// sleep then retry
				unit.sleep(timeout);
			}
		}
	}
	
	/**
	 * A resilient {@link SynchronousSession#setData(Path, byte[], int)} operation.
	 */
	@Override
	public Stat setData(Path path, byte[] data, int version)
			throws KeeperException, InterruptedException,
			IllegalArgumentException, NullPointerException {
		int retries = 0;
		while (true){
			try{
				return super.setData(path, data, version);
			}
			catch(KeeperException.ConnectionLossException e){
				if (retries++ == maxRetries){
					throw e;
				}
				// sleep then retry
				unit.sleep(timeout);
			}
		}
	}
	
	/**
	 * A resilient {@link SynchronousSession#getChildren(Path, Watcher)} operation.
	 */
	@Override
	public List<Path> getChildren(Path path, Watcher watcher)
			throws KeeperException, InterruptedException,
			IllegalArgumentException, NullPointerException {
		int retries = 0;
		while (true){
			try{
				return super.getChildren(path, watcher);
			}
			catch(KeeperException.ConnectionLossException e){
				if (retries++ == maxRetries){
					throw e;
				}
				// sleep then retry
				unit.sleep(timeout);
			}
		}	
	}
	
	/**
	 * A resilient {@link SynchronousSession#getChildren(Path, Watcher, Stat)} operation.
	 */
	@Override
	public List<Path> getChildren(Path path, Watcher watcher, Stat stat)
			throws KeeperException, InterruptedException,
			IllegalArgumentException, NullPointerException {
		int retries = 0;
		while (true){
			try{
				return super.getChildren(path, watcher, stat);
			}
			catch(KeeperException.ConnectionLossException e){
				if (retries++ == maxRetries){
					throw e;
				}
				// sleep then retry
				unit.sleep(timeout);
			}
		}
	}
}
