package gr.tuc.softnet.zookeeper.znode;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link Paths} contains only static methods and classes regarding {@link Path} objects.
 * 
 * @see Path
 * @author Tassos Souris
 *
 */
public final class Paths {
	
	/**
	 * A {@link Comparator} that compares two path objects according to their sequence number that they are assigned when the path objects
	 * are created as sequential.
	 * 
	 * The pattern followed by this comparator is that it retrieves the sequence id after the last "-" character found (this means that both
	 * absolute and relative paths are allowed for comparison and also that the path can contain more than one "-" characters, e.g. the common idiom
	 * where a znode name has the form "x-sessionid-sequenceid" is supported too).
	 * 
	 * @see Path
	 * @author Tassos Souris
	 *
	 */
	public static Comparator<Path> PathSequenceIdComparator = new Comparator<Path>(){
		/**
		 * Compare the two path objects in the manner specified in the class description.
		 * 
		 * @throws NullPointerException
		 * 				If at least one of the path objects is null
		 * @throws IllegalArgumentException
		 * 				If a sequence id is not found.
		 */
		@Override
		public int compare(Path o1, Path o2) throws NullPointerException, IllegalArgumentException{
			if (o1 == null || o2 == null){
				throw new NullPointerException();
			}
			
			try{
				// extract the sequence ids from the paths
				int o1SequenceIndex = o1.getPath().lastIndexOf("-");
				if (o1SequenceIndex == -1){
					throw new IllegalArgumentException();
				}
				int o2SequenceIndex = o2.getPath().lastIndexOf("-");
				if (o2SequenceIndex == -1){
					throw new IllegalArgumentException();
				}
				
				String o1SequenceIdString = o1.getPath().substring(o1SequenceIndex + 1);
				String o2SequenceIdString = o2.getPath().substring(o2SequenceIndex + 1);
				
				int o1SequenceId = Integer.valueOf(o1SequenceIdString);
				int o2SequenceId = Integer.valueOf(o2SequenceIdString);
				
				return o1SequenceId - o2SequenceId;
			}
			catch( NumberFormatException n){
				throw new IllegalArgumentException(n);
			}
		}
	};
	
	/**
	 * Return a list of Path objects from the collection of Path objects passed as parameter that satisfy the specified filter.
	 * 
	 * @param paths
	 * 			A collection of path objects
	 * @param pathFilter
	 * 			The path filter to filter the paths from the collection
	 * @return a list of Path objects that satisfy the specified filter.
	 * @throws NullPointerException
	 * 			if at least one of the parameters is null
	 */
	public static List<Path> listPaths(Collection<Path> paths, PathFilter pathFilter) throws NullPointerException{
		if (paths == null || pathFilter == null){
			throw new NullPointerException();
		}
		
		List<Path> pathList = new LinkedList<Path>();
		
		// for each path in the collection
		for (Path path : paths){
			// if the path satisfies the filter add it to the resulting list
			if (pathFilter.accept(path)){
				pathList.add(path);
			}
		}
		
		return pathList;
	}
}
