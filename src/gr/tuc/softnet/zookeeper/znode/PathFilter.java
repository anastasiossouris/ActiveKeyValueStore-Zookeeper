package gr.tuc.softnet.zookeeper.znode;

/**
 * A filter for {@link Path} objects.
 * 
 * Instances of this interface may be passed to the {@link Paths#listPaths(java.util.Collection, PathFilter)} method of the {@link Paths} class.
 * 
 * @see Path
 * @see Paths
 * @author Tassos Souris
 *
 */
public interface PathFilter {
	
	/**
	 * Tests whether or not the path is accepted by this filter or not.
	 * 
	 * @param path
	 * 			the path to be tested
	 * @return true if and only if the path is accepted by this filter.
	 */
	public boolean accept(Path path);
}
