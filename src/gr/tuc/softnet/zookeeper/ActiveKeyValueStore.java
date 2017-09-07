package gr.tuc.softnet.zookeeper;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

/**
 * {@link ActiveKeyValueStore} implements a active key-value store service for ZooKeeper.
 * 
 * <p>
 * The active key-value store is represented by a znode under which child znodes shall be created to represent the keys of the store.
 * <p>
 * In particular, each key is represented by a znode under the znode representing the active key-value store, and the value
 * associated with the key is stored as data in the znode (this places a limit on the number of bytes the value can consist of).
 * For example, if <em>/store</em> is the znode representing the active key-value store then the znode <em>/store/key</em> storing
 * as data <em>value</em>, represents the <em>(key,value)</em> pair.
 * 
 * This class is thread-safe.
 * 
 * @author Tassos Souris
 *
 */
public class ActiveKeyValueStore {
	
	// The ZooKeeper handle
	private ZooKeeperHandle handle = null;
	
	// The ActiveKeyValueStore is represented in ZooKeeper as a znode under which child znodes will be created
	// for the keys. We store the path to this special znode in activeKeyValueStoreZNodePath.
	private String activeKeyValueStoreZNodePath = null;
	
	
	/**
	 * Put the <em>(key,value)</em> pair given as parameters in the active key value store if and only if 
	 * there isn't currently a mapping for the given key stored.
	 * 
	 * @param key
	 * 			The key of the entry (must not be null).
	 * @param value
	 * 			The value associated with the key (may be null).
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws IllegalArgumentException
	 * 			If there exists an entry for the given key.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public void putIfAbsent(String key, byte [] value) throws KeeperException, IllegalArgumentException, InterruptedException{
		assert key != null;
	
		// Insert the pair in the active key-value store.
		makePair(key, value);
	
		return ;
	}
	
	/**
	 * Put all the <em>(key,value)</em> pairs stored in the {@link Map} object passed as parameter in the active key-value store. 
	 * The semantics for each of the <em>(key,value)</em> pair follow those of the {@link ActiveKeyValueStore#putIfAbsent(String, byte[])} method.
	 * 
	 * @param entries
	 * 			The entries to be stored in the active key-value store (must not be null). Note also that for each <em>(key,value)</em>
	 * 			pair the key must also be not null.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws IllegalArgumentException
	 * 			If there exists an entry for the given key in one of the entries.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public void putAll(Map<String,byte []> entries) throws KeeperException, IllegalArgumentException, InterruptedException{
		assert entries != null;
		
		Set<Map.Entry<String, byte []>> entrySet = entries.entrySet();
		
		// For each (key,value) pair
		for (Map.Entry<String, byte []> entry : entrySet){
			String key = entry.getKey();
			assert key != null;
			byte [] value = entry.getValue();
			
			
			// Insert the pair in the active key-value store.
			makePair(key, value);
		}
		
		return ;
	}
	
	/**
	 * Replace the value associated with the given key.
	 * 
	 * @param key
	 * 			The key for which we want to replace the associated value (must not be null).
	 * @param value
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws IllegalArgumentException
	 * 			If there isn't an entry for the given key.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public void replace(String key, byte [] value) throws KeeperException, IllegalArgumentException, InterruptedException{
		assert key != null;
		
		replacePair(key, value);
	}
	
	/**
	 * Retrieve the value associated with the given key (if any).
	 * 
	 * @param key
	 * 			The key for which we want to retrieve the associated value (must not be null).
	 * @return The value associated with the given key if there is a mapping for the given key; otherwise null is returned.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public byte [] get(String key) throws KeeperException, InterruptedException {
		assert key != null;
		
		return value(key);
	}
	
	/**
	 * 
	 * @param key
	 * 			The key for which we want to test if there is currently a mapping stored in the active key-value store (must not be null).
	 * @return True if there is a mapping for the given key and false otherwise.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public boolean containsKey(String key) throws KeeperException, InterruptedException {
		assert key != null;
		
		return keyExists(key);
	}
	
	/**
	 * Remove from the active key-value store the entry that has the given key.
	 * 
	 * @param key
	 * 			The key for which we want to remove the (key,value) pair (must not be null).
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws IllegalArgumentException
	 * 			If there isn't a mapping for the given key in the active key-value store.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public void remove(String key) throws KeeperException, IllegalArgumentException, InterruptedException {
		assert key != null;
		
		deletePair(key);
	}
	
	/**
	 * Retrieve all the <em>(key,value)</em> pairs currently stored in the active key-value store.
	 * 
	 * @return A {@link Set} object with all the entries in the active key-value store.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public Set<Map.Entry<String, byte []>> entrySet() throws KeeperException, InterruptedException{
		Set<Map.Entry<String, byte []>> entries = new HashSet<Map.Entry<String,byte[]>>();
		
		// Get all the keys currently in the active key-value store
		List<String> keys = keys();
		
		// For each key
		for (String key : keys){
			// Fetch the value associated with the key
			byte [] value = value(key);
			
			// we get a null from value() if no mapping was found for that key.
			// this can happen if the key is removed in between the call to the keys() method we made earlier
			// and the point above that we asked for the value. We make no entry in this case.
			if (value == null){
				continue;
			}
			
			// Create the (key,value) entry.
			Map.Entry<String, byte []> entry = new AbstractMap.SimpleEntry<String, byte []>(key,value);
							
			// add the entry to the set
			entries.add(entry);
		}
		
		return entries;
	}
	
	/**
	 * Check whether the active key-value store is empty or not.
	 * 
	 * @return True if the active key-value store is empty and false otherwise.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public boolean isEmpty() throws KeeperException, InterruptedException {
		return keys().isEmpty();
	}
	
	/**
	 * Return the number of <em>(key,value)</em> entries stored in the active key-value store.
	 * 
	 * @return The number of <em>(key,value)</em> entries stored in the active key-value store.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	public int size() throws KeeperException, InterruptedException {
		return keys().size();
	}
	
	/**
	 * This operation is currently not implemented.
	 * 
	 * @throws OperationNotSupportedException
	 * 			Always.
	 */
	public void clear() throws OperationNotSupportedException{
		throw new OperationNotSupportedException();
	}
	
	
	/**
	 * Retrieve a list of all the keys in the active key-value store.
	 * 
	 * @return A {@link List} object with all the keys stored in the active key-value store.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	private List<String> keys() throws KeeperException, InterruptedException{
		List<String> children = handle.getZooKeeper().getChildren(activeKeyValueStoreZNodePath, false);
		List<String> keysList = new LinkedList<String>();
		
		// For each child znode
		for (String child : children){
			// extract the key part of the znode path
			// it is after activeKeyValueStoreZNodePath.getPath() + "/"
			keysList.add(new String(child.substring(activeKeyValueStoreZNodePath.length() + 1)));
		}
		
		return keysList;
	}
	
	/**
	 * Retrieve the value associated with the given key.
	 * 
	 * @param key 
	 * 			The key for which we want to retrieve the value (must not be null).
	 * @return The value associated with the given key or null if there is no mapping for the key.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	private byte [] value(String key) throws KeeperException, InterruptedException{
		assert key != null;
		
		// Fetch the value associated with the key
		byte [] data = null;
		
		try{
			// The key is represented in the active key-value store as a child of the
			// znode path representing the store. We store the path in activeKeyValueStoreZNodePath
			// and we create the path to the child representing the key.
			String keyZnodePath = activeKeyValueStoreZNodePath + "/" + key;
						
			data = handle.getZooKeeper().getData(keyZnodePath, false, null);
		}
		catch(KeeperException.NoNodeException noNode){
			// the znode was not found so there isn't a mapping for the given key. no error here
		}
		
		return data;
	}
	
	/**
	 * Check whether there is an entry with the given key or not.
	 * 
	 * @param key
	 * 			The key for which we want to find whether there is an entry with it or not (must not be null).
	 * @return True if there is an entry with the given key and false otherwise.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	private boolean keyExists(String key) throws KeeperException, InterruptedException{
		assert key != null;
		
		// The key is represented in the active key-value store as a child of the
		// znode path representing the store. We store the path in activeKeyValueStoreZNodePath
		// and we create the path to the child representing the key.
		String keyZnodePath = activeKeyValueStoreZNodePath + "/" + key;
					
		// Check if the active key-value store contains a mapping for the given key
		Stat keyZnodeStat = handle.getZooKeeper().exists(keyZnodePath, false);
		boolean keyZnodeExists = keyZnodeStat != null;
		
		return keyZnodeExists;
	}
	
	
	/**
	 * Replace the value associated with the given key.
	 * 
	 * @param key
	 * 			The key for which we want to change the associated value (must not be null).
	 * @param value
	 * 			The new value for the key.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws IllegalArgumentException
	 * 			If there isn't an entry for the given key.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	private void replacePair(String key, byte [] value) throws KeeperException, IllegalArgumentException, InterruptedException{
		assert key != null;
		
		// The key is represented in the active key-value store as a child of the
		// znode path representing the store. We store the path in activeKeyValueStoreZNodePath
		// and we create the path to the child representing the key.
		String keyZnodePath = activeKeyValueStoreZNodePath + "/" + key;
		
		try{
			// change the data in the znode for the key
			handle.getZooKeeper().setData(keyZnodePath, value, -1);
		}
		catch(KeeperException.NoNodeException noNode){
			// we get a NoNode exception from setData() if no znode was found with the given path.
			// in this case the key is not there and we throw a IllegalArgumentException
			throw new IllegalArgumentException(noNode);
		}
		
		return ;
	}
	
	/**
	 * Insert the pair <em>(key,value)</em> in the active key-value store.
	 * 
	 * @param key
	 * 			The key (must not be null).
	 * @param value
	 * 			The value associated with the key.
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws IllegalArgumentException
	 * 			If there exists an entry for the given key.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	private void makePair(String key, byte [] value) throws KeeperException, IllegalArgumentException, InterruptedException{
		assert key != null;
		
		try{
			// to insert the pair means to create a znode with the name activeKeyValueStoreZNodePath.getPath() + "/" + key
			// and storing the value as data for the znode
			String keyZnodePath = activeKeyValueStoreZNodePath + "/" + key;
			
			handle.getZooKeeper().create(keyZnodePath, value, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		catch(KeeperException.NodeExistsException nodeExists){
			// we already have a entry for the key so we throw a IllegalArgumentException
			throw new IllegalArgumentException(nodeExists);
		}
		
		return ;
	}
	
	/**
	 * Remove from the active key-value store the entry for the given key.
	 * 
	 * @param key
	 * 			The key for which we want to remove the entry (must not be null).
	 * @throws KeeperException
	 * 			If the ZooKeeper server signaled an error.
	 * @throws IllegalArgumentException
	 * 			If there isn't an entry for the given key.
	 * @throws InterruptedException
	 * 			If the transaction with ZooKeeper was interrupted.
	 */
	private void deletePair(String key) throws KeeperException, IllegalArgumentException, InterruptedException{
		assert key != null;
		
		try{
			// The key is represented in the active key-value store as a child of the
			// znode path representing the store. We store the path in activeKeyValueStoreZNodePath
			// and we create the path to the child representing the key.
			String keyZnodePath = activeKeyValueStoreZNodePath + "/" + key;
			
			// delete the (key,value) pair by deleting the znode
			handle.getZooKeeper().delete(keyZnodePath, -1);
		}
		catch(KeeperException.NoNodeException noNode){
			// We get a NoNodeException from delete() if the znode does not exist.
			// This means that the (key,value) pair is not stored so we throw a IllegalArgumentException.
			throw new IllegalArgumentException(noNode);
		}
		
		return ;
	}
}