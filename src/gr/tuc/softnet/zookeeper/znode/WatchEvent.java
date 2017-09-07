package gr.tuc.softnet.zookeeper.znode;

public final class WatchEvent {
	
	/**
	 * {@link Modifier} defines additional attributes needed for a {@link WatchEvent}.
	 * 
	 * @author Tassos Souris
	 *
	 */
	public static enum Modifier implements Comparable<Modifier>{
		/**
		 * Be notified for the watch once and then not again. This is the default behavior
		 * of ZooKeeper watches.
		 */
		ONE_SHOT
	}
	
	/**
	 * {@link Kind} defines the possible events we expect for a znode.
	 * 
	 * @author Tassos Souris
	 *
	 */
	public static enum Kind implements Comparable<Kind>{
		NODE_CREATED,
		NODE_DELETED,
		NODE_DATA_CHANGED,
		NODE_CHILDREN_CHANGED
	}
	
	public WatchEvent.Kind kind(){
		return null;
	}
	
	public WatchEvent.Modifier modifier(){
		return null;
	}
}
