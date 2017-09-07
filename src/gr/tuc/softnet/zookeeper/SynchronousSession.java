package gr.tuc.softnet.zookeeper;

import gr.tuc.softnet.zookeeper.znode.Path;
import gr.tuc.softnet.zookeeper.znode.attribute.Acl;
import gr.tuc.softnet.zookeeper.znode.attribute.Acls;
import gr.tuc.softnet.zookeeper.znode.attribute.CreateOption;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * {@link SynchronousSession} wraps the synchronous versions of the {@link ZooKeeper} client object.
 * 
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * @author Tassos Souris
 *
 */
public class SynchronousSession extends Session{
	
	/**
	 * A wrapper around the {@link ZooKeeper#create(String, byte[], List, org.apache.zookeeper.CreateMode)} method.
	 * 
	 * @param path
	 * 			An absolute path object representing the path of the znode to be created. Note that only the path component will be considered.
	 * 			That is the path passed to ZooKeeper will be that returned by {@link Path#getPath()} method and not by {@link Path#toString()} method.
	 * @param data
	 * 			The data to store in the znode. May be null.
	 * @param acl
	 * 			The access control list associated with the znode.
	 * @param createOption
	 * 			Specifying whether the znode is created ephemeral and/or sequential.
	 * @return The actual path object of the znode as returned by ZooKeeper. This absolute path object has the same root component as the passed path object
	 * 			and may differ in the last name element. For example, if we pass the path "/app1/x-" with the sequential flag enabled then the path we will receive
	 * 			may be "/app1/x-0000000001"
	 * @throws KeeperException
	 * 			if the ZooKeeper server returns an error
	 * @throws InterruptedException
	 * 			if the transaction is interrupted
	 * @throws IllegalArgumentException
	 * 			if the path is a relative path object.
	 * @throws NullPointerException
	 * 			if one of path, acl or createOption is null.
	 */
	public Path create(Path path, byte [] data, Acl acl, CreateOption createOption) throws KeeperException, InterruptedException, IllegalArgumentException, NullPointerException{
		if (path == null || acl == null || createOption == null){
			throw new NullPointerException();
		}
		else if (path.isRelative()){
			throw new IllegalArgumentException();
		}
		
		CreateMode createMode = createOption.mode();
		List<ACL> acls = Acls.toZooKeeperACL(acl);
		
		return new Path(path.getRoot(), zookeeper.create(path.getPath(), data, acls, createMode));
	}
	
	/**
	 * A wrapper around the {@link ZooKeeper#delete(String, int)} method.
	 * 
	 * @param path
	 * 			An absolute path object representing the path of the znode to be deleted. Note that only the path component will be considered.
	 * 			That is the path passed to ZooKeeper will be that returned by {@link Path#getPath()} method and not by {@link Path#toString()} method.
	 * @param version
	 * 			the expected node version
	 * @throws KeeperException
	 * 			if the ZooKeeper server returns an error
	 * @throws InterruptedException
	 * 			if the transaction is interrupted
	 * @throws IllegalArgumentException
	 * 			if the path is a relative path object.
	 * @throws NullPointerException
	 * 			if path is null
	 */
	public void delete(Path path, int version) throws KeeperException, InterruptedException, IllegalArgumentException, NullPointerException{
		if (path == null){
			throw new NullPointerException();
		}
		else if (path.isRelative()){
			throw new IllegalArgumentException();
		}
		
		zookeeper.delete(path.getPath(), version);
	}
	
	/**
	 * A wrapper around the {@link ZooKeeper#exists(String, Watcher)} method.
	 * 
	 * @param path
	 * 			An absolute path object representing the path of the znode to be tested. Note that only the path component will be considered.
	 * 			That is the path passed to ZooKeeper will be that returned by {@link Path#getPath()} method and not by {@link Path#toString()} method.
	 * @param watcher
	 * 			explicit watcher set on the znode.
	 * @return the stat of the node of the given path; return null if no such a node exists. 
	 * @throws KeeperException
	 * 			if the ZooKeeper server returns an error
	 * @throws InterruptedException
	 * 			if the transaction is interrupted
	 * @throws IllegalArgumentException
	 * 			if the path is a relative path object.
	 * @throws NullPointerException
	 * 			if path is null
	 */
	public Stat exists(Path path, Watcher watcher) throws KeeperException, InterruptedException, IllegalArgumentException, NullPointerException{
		if (path == null){
			throw new NullPointerException();
		}
		else if (path.isRelative()){
			throw new IllegalArgumentException();
		}
		
		return zookeeper.exists(path.getPath(), watcher);
	}
	
	
	/**
	 * A wrapper around the {@link ZooKeeper#getData(String, Watcher, Stat)} method.
	 * 
	 * @param path
	 * 			An absolute path object representing the path of the znode for which we want to retrieve the data. Note that only the path component will be considered.
	 * 			That is the path passed to ZooKeeper will be that returned by {@link Path#getPath()} method and not by {@link Path#toString()} method.
	 * @param watcher
	 * 			explicit watcher set on the znode
	 * @param stat
	 * 			the stat of the znode will be written here
	 * @return the data of the node
	 * @throws KeeperException
	 * 			if the ZooKeeper server returns an error
	 * @throws InterruptedException
	 * 			if the transaction is interrupted
	 * @throws IllegalArgumentException
	 * 			if the path is a relative path object.
	 * @throws NullPointerException
	 * 			if path is null
	 */
	public byte [] getData(Path path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException, IllegalArgumentException, NullPointerException{
		if (path == null){
			throw new NullPointerException();
		}
		else if (path.isRelative()){
			throw new IllegalArgumentException();
		}
		
		return zookeeper.getData(path.getPath(), watcher, stat);
	}
	
	/**
	 * A wrapper around the {@link ZooKeeper#setData(String, byte[], int)} method.
	 * 
	 * @param path
	 * 			An absolute path object representing the path of the znode for which we want to set the data. Note that only the path component will be considered.
	 * 			That is the path passed to ZooKeeper will be that returned by {@link Path#getPath()} method and not by {@link Path#toString()} method.
	 * @param data
	 * 			the data to set
	 * @param version
	 * 			the expected matching version
	 * @return the state of the node
	 * @throws KeeperException
	 * 			if the ZooKeeper server returns an error
	 * @throws InterruptedException
	 * 			if the transaction is interrupted
	 * @throws IllegalArgumentException
	 * 			if the path is a relative path object.
	 * @throws NullPointerException
	 * 			if path is null
	 */
	public Stat setData(Path path, byte [] data, int version) throws KeeperException, InterruptedException, IllegalArgumentException, NullPointerException{
		if (path == null){
			throw new NullPointerException();
		}
		else if (path.isRelative()){
			throw new IllegalArgumentException();
		}
		
		return zookeeper.setData(path.getPath(), data, version);
	}
	
	/**
	 * A wrapper around the {@link ZooKeeper#getChildren(String, Watcher)} method.
	 * 
	 * @param path
	 * 			An absolute path object representing the path of the znode for which we want to retrieve its children. Note that only the path component will be considered.
	 * 			That is the path passed to ZooKeeper will be that returned by {@link Path#getPath()} method and not by {@link Path#toString()} method.
	 * @param watcher
	 * 			explicit watcher set on the node
	 * @return a list of path objects representing the children of the znode of the given path. For each child znode the path object has the same root component
	 * 		   as the passed path object and also contains the path of the passed path in front of it. For example, if the znode with the path "/app1" has a child names "a"
	 * 			then "/app1/a" is returned.
	 * @throws KeeperException
	 * 			if the ZooKeeper server returns an error
	 * @throws InterruptedException
	 * 			if the transaction is interrupted
	 * @throws IllegalArgumentException
	 * 			if the path is a relative path object.
	 * @throws NullPointerException
	 * 			if path is null
	 */
	public List<Path> getChildren(Path path, Watcher watcher) throws KeeperException, InterruptedException, IllegalArgumentException, NullPointerException{
		if (path == null){
			throw new NullPointerException();
		}
		else if (path.isRelative()){
			throw new IllegalArgumentException();
		}
		
		List<String> children = zookeeper.getChildren(path.getPath(), watcher);
		
		List<Path> paths = new ArrayList<Path>(children.size());
		
		for (String child : children){
			paths.add(path.resolveChild(child));
		}
		
		return paths;
	}
	
	/**
	 * A wrapper around the {@link ZooKeeper#getChildren(String, Watcher)} method.
	 * 
	 * @param path
	 * 			An absolute path object representing the path of the znode for which we want to retrieve its children. Note that only the path component will be considered.
	 * 			That is the path passed to ZooKeeper will be that returned by {@link Path#getPath()} method and not by {@link Path#toString()} method.
	 * @param watcher
	 * 			explicit watcher set on the node
	 * @param stat
	 * 			the stat of the znode will be written here
	 * @return a list of path objects representing the children of the znode of the given path. For each child znode the path object has the same root component
	 * 		   as the passed path object and also contains the path of the passed path in front of it. For example, if the znode with the path "/app1" has a child names "a"
	 * 			then "/app1/a" is returned.
	 * @throws KeeperException
	 * 			if the ZooKeeper server returns an error
	 * @throws InterruptedException
	 * 			if the transaction is interrupted
	 * @throws IllegalArgumentException
	 * 			if the path is a relative path object.
	 * @throws NullPointerException
	 * 			if path is null
	 */
	public List<Path> getChildren(Path path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException, IllegalArgumentException, NullPointerException{
		if (path == null){
			throw new NullPointerException();
		}
		else if (path.isRelative()){
			throw new IllegalArgumentException();
		}
		
		List<String> children = zookeeper.getChildren(path.getPath(), watcher, stat);
		
		List<Path> paths = new ArrayList<Path>(children.size());
		
		for (String child : children){
			paths.add(path.resolveChild(child));
		}
		
		return paths;
	}
}