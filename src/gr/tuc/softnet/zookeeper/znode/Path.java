package gr.tuc.softnet.zookeeper.znode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.zookeeper.common.PathUtils;

/**
 * {@link Path} represents a path of a znode in ZooKeeper.
 * 
 * 
 * <p>
 * The following are mostly taken from the ZooKeeper documentation. See the <a href="http://zookeeper.apache.org/doc/r3.3.3/zookeeperProgrammers.html">ZooKeeper's Programmer's Guide</a> for more information.
 * </p>
 * 
 * <p>
 * Paths to znodes are always expressed as canonical, absolute, slash-separated paths.
 * Paths must start with the "/" character which also serves as the default root component of the path and as a path separator. Moreover paths cannot end with the "/" character (except from the default root itself).
 * For example, "/app1/a/b" is a valid path but "/app1/a/b/" is not. Restrictions are also placed on the characters allowed in a path to a ZooKeeper node. Refer to 
 * <a href="http://zookeeper.apache.org/doc/r3.3.3/zookeeperProgrammers.html#ch_zkDataModel">The ZooKeeper Data Model</a> section of the ZooKeeper's Programmer's Guide for more information.
 * Besides the default root which is "/", a <i>root suffix</i> feature is supported by which all paths are interpreted relative to this root.
 * For example if the root suffix is "/app1/a" and the path is "/foo/bar" then the actual path in ZooKeeper is "/app1/a/foo/bar". 
 * Finally, the individual components of the path are refereed to as <i>names</i>. For example, the path "/a/b/c" has three names: "a", "b" and "c".
 * Relative paths are supported too mostly for convenience even though they are not valid paths for ZooKeeper nodes. Their primary usage is to define parent-child relationships and such.
 * Relative paths do not start with the "/" character (e.g. "app1/a/b") and they do not have a root component (the notion of names remains the same).
 * For more information about the distinction between absolute and relative paths refer to each method's description.
 * </p>
 * 
 * <p>
 * This class is not thread-safe.
 * </p>
 * 
 * @author Tassos Souris
 *
 */
public final class Path{
	// the root suffix for this path object
	private String root = null;
	// the path in its string representation
	private String path = null;
	// the names of the path. note that the array may have zero elements, e.g. in case of the root path.
	private String [] names = null;
	// is this path absolute?
	private boolean isAbsolutePath = false;
	
	/**
	 * The ZooKeeper path-separator character represented as a string. 
	 */
	public static final String pathSeparator = "/";
	
	/**
	 * The ZooKeeper default root suffix represented as a string.
	 */
	public static final String defaultRootSuffix = "/";
	
	
	/**
	 * Construct an absolute Path object with the default root suffix and as path component the root.
	 */
	public Path(){
		setRoot(Path.defaultRootSuffix);
		setPath(Path.pathSeparator);
		names = getNames();
		setAbsolute(true);
	}
	
	/**
	 * Construct a Path object and initialize it with the path passed as parameter. If the path string is absolute then
	 * an absolute Path object is created with the default root suffix. Otherwise, that is if the string path
	 * does not start with the path separator character, a relative Path object is created (with no root component).
	 *  
	 * @param path
	 * 			The path in its string representation.
	 * @throws NullPointerException
	 * 			If the path passed is null.
	 * @throws IllegalArgumentException
	 * 			If the path passed is not a valid path for a ZooKeeper znode. If the path is a relative path
	 * 			then it is interpreted as an absolute path, by concatenating the relative path to the path separator, 
	 * 			and the resulting path is tested for validity. 
	 */
	public Path(String path) throws NullPointerException, IllegalArgumentException{	
		if (path == null){
			throw new NullPointerException();
		}
		// validate the given path
		PathUtils.validatePath(path.startsWith(Path.pathSeparator)? path: Path.pathSeparator.concat(path));
		
		setRoot(path.startsWith(Path.pathSeparator) ? Path.defaultRootSuffix : null);
		setPath(path);
		names = getNames();
		setAbsolute(path.startsWith(Path.pathSeparator) ? true : false);
	}
	
	/**
	 * Construct an absolute Path object with the given root suffix and initialize it with the path passed as parameter.
	 * Note that if we want as path the root then we pass as path the path separator string and not the root string which serves as the root component.
	 * For example:
	 * <p>
	 * 		<pre>
	 * 		Path path = new Path("/app/a", Path.pathSeparator);
	 * 
	 * 		... and not
	 * 
	 * 		Path path = new Path("/app/a", "/app/a");
	 * 		</pre>
	 * </p>
	 * 
	 * The actual path to the znode in ZooKeeper will be the concatenation of the root with the path (with precautions in order to create a 
	 * valid path, e.g. if root="/" and path="/app1/a" then the actual path is "/app1/a" and not "//app1/a").
	 * 
	 * @param root
	 * 			The root of this path.
	 * @param path
	 * 			The path in its string representation.
	 * @throws NullPointerException
	 * 				If either root or path is null.
	 * @throws IllegalArgumentException
	 * 				If either root or path is not a valid absolute path. Note that if both are valid
	 * 				then their concatenation which forms the actual path to the znode is also valid.
	 */
	public Path(String root, String path) throws NullPointerException, IllegalArgumentException{
		if (root == null || path == null){
			throw new NullPointerException();
		}
		// validate the root and the path. We get IllegalArgumentException from validatePath()
		PathUtils.validatePath(root);
		PathUtils.validatePath(path);
		
		setRoot(root);
		setPath(path);
		names = getNames();
		setAbsolute(true);
	}
	
	/**
	 * Returns the root component of this path, or null if this path does not have a root component (if relative).
	 * 
	 * @return the root component of this path.
	 */
	public String getRoot(){
		return root;
	}
	
	/**
	 * Returns the path component of this path.
	 * 
	 * @return the path component of this path
	 */
	public String getPath(){
		return path;
	}
	
	/**
	 * Returns the parent path, or null if this path does not have a parent.
	 * 
	 * The parent of this path object consists of this path's root component, if any, and each element in the path except for the farthest from the root (the last name).
	 * 
	 * For example, the parent of "/app1/a/b" with the default root suffix is "/app1/a" also with the default root suffix.
	 * The parent of "/a/b" with the root suffix "/app1" is "/a" also with the root suffix "/app1".
	 * Also, for the root we define no parent (null is returned). For example, for the path "/" with the default root suffix the parent is null.
	 * For the path "/" with the root suffix "/app1/a" the parent is also null.
	 * 
	 * Note that a relative path does not have a parent path.
	 * 
	 * @return a path representing the path's parent, or null if this path does not have a parent.
	 */
	public Path getParent(){
		// we don not have a parent if we are relative or if we are the root
		if (isRelative() || getPath().equals(Path.pathSeparator)){
			return null;
		}
		
		// extract the part of the path until the last path separator
		// note that the path is valid so we cannot get -1 from lastIndexOf()
		String parent = path.substring(0, path.lastIndexOf(Path.pathSeparator));
		
		// construct the Path object with the root component as the this path and the parent we found
		// note that we are safe from exceptions here.
		// also note that in the case where the parent length is zero this means that our parent is the root (we have eliminated the case where we are the root in the beginning), 
		// so we must pass the root as the path component to pass a valid path. e.g. if path="/app1" then parent="/"
		return new Path(getRoot(),parent.length() != 0 ? parent : Path.pathSeparator);
	}
	
	/**
	 * Returns the number of name elements in the path, or zero if this path does not have any name elements.
	 * 
	 * @return the number of elements in the path.
	 */
	public int getNameCount(){
		return names.length;
	}
	
	/**
	 * Returns a name element of this path.
	 * 
	 * The index parameter is the index of the name element to return. The element that is closest to the root has index 0. The element that is farthest from the root has index count-1.
	 * 
	 * @param index
	 * 			The index of the element.
	 * @return the name element.
	 * @throws IllegalArgumentException
	 * 				if index is negative, index is greater than or equal to the number of elements, or this path has zero name elements
	 */
	public String getName(int index) throws IllegalArgumentException{
		if (index < 0 || index > getNameCount() - 1 || getNameCount() == 0){
			throw new IllegalArgumentException();
		}
		return names[index];
	}
	
	/**
	 * Returns an iterator over the name elements of this path in the proper sequence (from left to right).
	 * 
	 * @return an iterator over the name elements of this path.
	 */
	public Iterator<String> getNamesIterator(){
		List<String> namesList = new ArrayList<String>(names.length);
		
		// add the name elements in the sequence we needed in the list to get a correct iterator later
		for (int i = 0; i < getNameCount(); ++i){
			namesList.add(getName(i));
		}
		
		return namesList.iterator();
	}
	
	/**
	 * Resolve the relative Path object passed as parameter against this path. This method creates a new absolute Path by considering the other Path to be a child of the this path.
	 * For example, if this path is "/app1" and the other path is "a/b" then the result of this method is "/app1/a/b". The root component of the resulting Path object is the root component
	 * of the this object.
	 * 
	 * @param other
	 * 			the path object to resolve against this path
	 * @return the resulting path
	 * @throws NullPointerException
	 * 			if the other string is null.
	 * @throws IllegalArgumentException
	 * 			if the other path is not relative.
	 */
	public Path resolveChild(Path other) throws NullPointerException, IllegalArgumentException{
		if (other == null){
			throw new NullPointerException();
		}
		else if (!other.isRelative()){
			throw new IllegalArgumentException();
		}
		
		// to make the child path we append the path separator and then the child itself to this path
		return new Path(getRoot(), getPath().concat(Path.pathSeparator).concat(other.getPath()));
	}
	
	/**
	 * Convert the string other passed as parameter to a relative Path object and resolve it against this path in the same manner as specified in the {@link #resolveChild(Path)} method.
	 * 
	 * @param other
	 * 			the path string to resolve against this path
	 * @return the resulting path
	 * @throws NullPointerException
	 * 			if the other string is null.
	 * @throws IllegalArgumentException
	 * 			if the other string is not a valid relative path
	 */
	public Path resolveChild(String other) throws NullPointerException, IllegalArgumentException{
		if (other == null){
			throw new NullPointerException();
		}
		else if (other.startsWith(Path.pathSeparator)){ // we are not relative here
			throw new IllegalArgumentException();
		}
		
		return resolveChild(new Path(other));
	}
	
	/**
	 * Resolves the relative path object given as parameter against this path's parent path. This method creates a new absolute path by considering the relative path passed as parameter
	 * as a sibling of this path. For example, if this path is "/app1/a" and the string other is "b/c/d" then the result of this method is "/app1/b/c/d". If this path does not have a parent
	 * then null is returned. The root component of the resulting Path object is the root component of the this object.
	 * 
	 * @param other
	 * 			the path object	to resolve against this path's parent.
	 * @return the resulting path
	 * @throws NullPointerException
	 * 			if the other string is null.
	 * @throws IllegalArgumentException
	 * 			if the other string is not relative
	 */
	public Path resolveSibling(Path other) throws NullPointerException, IllegalArgumentException{
		if (other == null){
			throw new NullPointerException();
		}
		else if (!other.isRelative()){
			throw new IllegalArgumentException();
		}
		
		Path parent = getParent();
		
		if (parent == null){
			return null;
		}
		else{
			// if we have the parent then we treat the other string as a child of the parent which makes it a sibling of this string
			return parent.resolveChild(other);
		}
	}
	
	/**
	 * Convert the string other passed as parameter to a relative Path object and resolve it against this path in the same manner as specified in the {@link #resolveSibling(Path)} method.
	 * 
	 * @param other
	 * 			the path string	to resolve against this path's parent.
	 * @return the resulting path
	 * @throws NullPointerException
	 * 			if the other string is null.
	 * @throws IllegalArgumentException
	 * 			if the other string is not a valid relative path.
	 */
	public Path resolveSibling(String other) throws NullPointerException, IllegalArgumentException{
		if (other == null){
			throw new NullPointerException();
		}
		else if (other.startsWith(Path.pathSeparator)){ // we are not relative here
			throw new IllegalArgumentException();
		}
		
		return resolveSibling(new Path(other));
	}
	
	/**
	 * Constructs a relative path between this path and a given path. Relativization is the inverse of resolution. 
	 * This method attempts to construct a relative path that when resolved against this path, yields a path that equals the given path. 
	 * For example, if this path is "/a/b" and the given path is "/a/b/c/d" then the resulting relative path would be "c/d".
	 * Also, "/a/b" resolveChild "c/d" equals the other path "/a/b/c/d".
	 * Note that both path objects must have the same root component. 
	 * 
	 * @param other
	 * 			the path to relativize against this path
	 * @return the resulting relative path object
	 * @throws NullPointerException
	 * 			if other is null.
	 * @throws IllegalArgumentException
	 * 			If the this path and the other path do not have the same root component or if the relativization cannot be performed as
	 * 			described above.
	 */
	public Path relativize(Path other) throws NullPointerException, IllegalArgumentException{
		if (other == null){
			throw new NullPointerException();
		}
		else if (!getRoot().equals(other.getRoot())){
			throw new IllegalArgumentException("Root components do not match: " + getRoot() + ", " + other.getRoot());
		}
		else if (!other.getPath().startsWith(getPath())){
			// relativazation cannot be performed if the other path we are given does not start with the this path.
			throw new IllegalArgumentException("Relativization cannot be performed");
		}
		
		// e.g. if this="/a/b" and other="/a/b/c/d" then result="c/d". this means that we wipe out
		// the this from other where this must be in the beginning of other.
		return new Path(other.getPath().substring(getPath().length() + 1));
	}
	
	/**
	 * Tests whether this path is relative. 
	 * 
	 * @return true if this path is relative, false otherwise
	 */
	public boolean isRelative(){
		return !isAbsolutePath;
	}
	
	/**
	 * Tests whether this path is absolute. 
	 * 
	 * @return true if this path is absolute, false otherwise
	 */
	public boolean isAbsolute(){
		return isAbsolutePath;
	}
	
	/**
	 * Constructs a absolute path from the this relative path object by placing the path separator at the beginning.
	 * The root component of the resulted path is the default root suffix.
	 * 
	 * @return the actual absolute path from this relative path object
	 * @throws IllegalStateException
	 * 			if the this path object is not relative
	 */
	public Path toRealPath() throws IllegalStateException{
		if (!isRelative()){
			throw new IllegalStateException();
		}
		return new Path(Path.pathSeparator.concat(getPath()));
	}
	
	/**
	 * Constructs a absolute path from the this relative path object by placing the path separator at the beginning.
	 * The root component of the resulted path is the root passed as parameter.
	 * 
	 * @param root
	 * @return the actual absolute path from this relative path object
	 * @throws NullPointerException
	 * 			if root is null
	 * @throws IllegalArgumentException
	 * 			if the root is not valid
	 * @throws IllegalStateException
	 * 			if the this path object is not relative
	 */
	public Path toRealPath(String root) throws NullPointerException, IllegalArgumentException, IllegalStateException{
		if (root == null){
			throw new NullPointerException();
		}
		else if (!isRelative()){
			throw new IllegalStateException();
		}
		// validate the root
		PathUtils.validatePath(root);
		
		return new Path(root, Path.pathSeparator.concat(getPath()));
	}
	
	/**
	 * Returns the string representation of this path object. This method returns the whole path, e.g. the actual path to the ZooKeeper node ({@link #Path(String, String)).
	 * 
	 * Note that is the path is relative then no root component exists and the path returned (which equals the path component as returned by {@link #getPath()}) is not a valid
	 * ZooKeeper znode path.
	 * 
	 * @return the path in its string representation.
	 */
	@Override
	public String toString(){
		if (isAbsolute()){
			String root = getRoot();
			String path = getPath();
			
			if (root.equals(Path.defaultRootSuffix)){
				// e.g. if the root is "/" then whatever path we have ("/", "/app1/a") then it is not correct to place the root at the beginning: "//", "//app1/a"
				// so we discard the root and return only the path
				return path;
			}
			else{
				// say the root is "/app1/a"
				// if the path is the root "/" then we must return "/app1/a" and not "/app1/a/"
				if (path.equals(Path.pathSeparator)){
					return root;
				}
				// otherwise, e.g. if the path is "/b/c", we concat them and return "/app1/a/b/c"
				else{
					return root.concat(path);
				}
			}
		}
		else{
			// we have no root component to place in the beginning so we return the path component only
			return getPath();
		}
	}
	
	/**
	 * Sets the root for this Path object (assume that it is valid).
	 * 
	 * @param root
	 * 			The root.
	 */
	private void setRoot(String root){
		this.root = root;
	}
	
	/**
	 * Sets the path for this Path object (assume that it is valid).
	 * 
	 * @param path
	 * 			The path.
	 */
	private void setPath(String path){
		this.path = path;
	}
	
	/**
	 * Tokenize the path of this Path object around the path separator and returns the name elements.
	 * For example, for the path "/a/b/c", there are three name elements, "a", "b", and "c".
	 *  
	 * Note that we assume that the path has already been set for this Path object. Also, the String array object
	 * returned by getNames() may have zero elements in it.
	 * 
	 * @return The name elements of this Path object.
	 */
	private String [] getNames(){
		// get a tokenizer around the path separator
		StringTokenizer nameElementTokenizer = new StringTokenizer(path, Path.pathSeparator);
	
		// how many names we have in the path is given by countTokens().
		// note that we might get a zero result here.
		String [] names = new String[nameElementTokenizer.countTokens()];
		
		// fill the array with the names
		int i = 0;
		while (nameElementTokenizer.hasMoreElements()){
			names[i++] = nameElementTokenizer.nextToken();
		}
		
		return names;
	}
	
	/**
	 * Sets whether this path is absolute or not.
	 * 
	 * @param isAbsolutePath
	 * 				Whether or not this path is absolute or not.
	 */
	private void setAbsolute(boolean isAbsolutePath){
		this.isAbsolutePath = isAbsolutePath;
	}
}