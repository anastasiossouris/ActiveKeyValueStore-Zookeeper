package gr.tuc.softnet.zookeeper.znode;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

/**
 * Test the {@link Path} class.
 * 
 * @see Path
 * @author Tassos Souris
 *
 */
public class PathTest {

	@Test
	public void testConstructor1(){
		Path relativePath =  new Path("app1/a/b");
		Path absolutePath = new Path("/app1/a/b");
		
		assertTrue(relativePath.getPath().equals("app1/a/b"));
		assertTrue(relativePath.getRoot() == null);
		
		assertTrue(absolutePath.getPath().equals("/app1/a/b"));
		assertTrue(absolutePath.getRoot().equals(Path.pathSeparator));
	}
	
	@Test( expected=NullPointerException.class )
	public void testConstructor1NullPointerException(){
		Path path = new Path(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructor1IllegalArgumentException(){
		Path path = new Path("/app1/a/b/");
	}
	
	@Test
	public void testConstructor2(){
		Path path1 = new Path(Path.defaultRootSuffix, "/app1/a");
		Path path2 = new Path("/app1/a", Path.defaultRootSuffix);
		
		assertTrue(path1.toString().equals(path2.toString()));
	}
	
	@Test(expected=NullPointerException.class)
	public void testConstructor2NullPointerException(){
		Path path = new Path(null,null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructor2IllegalArgumentException(){
		Path path = new Path(Path.defaultRootSuffix,"/app1/a/b/");
	}
	
	@Test 
	public void testName(){
		Path path = new Path("/app1/a/b/c");
		
		assertTrue(path.getNameCount() == 4);
		assertTrue(path.getName(0).equals("app1"));
		assertTrue(path.getName(1).equals("a"));
		assertTrue(path.getName(2).equals("b"));
		assertTrue(path.getName(3).equals("c"));
	}
	
	@Test( expected=IllegalArgumentException.class )
	public void testNameIllegalArgumentException1(){
		Path path = new Path("/app1/a/b/c");
		
		path.getName(4);
	}
	
	@Test( expected=IllegalArgumentException.class )
	public void testNameIllegalArgumentException2(){
		Path path = new Path("/app1/a/b/c");
		
		path.getName(-1);
	}
	
	@Test( expected=IllegalArgumentException.class )
	public void testNameIllegalArgumentException3(){
		Path path = new Path(Path.pathSeparator);
		
		path.getName(0);
	}
	
	@Test
	public void testRelativeAndAbsolute(){
		Path path1 =  new Path("app1/a/b");
		Path path2 = new Path("/app1/a/b");
		Path path3 = new Path(Path.defaultRootSuffix, "/a/b/c");
		Path path4 = new Path("/app1", "/a/b/c");
		
		assertTrue(path1.isRelative() && !path1.isAbsolute());
		assertTrue(path2.isAbsolute() && !path2.isRelative());
		assertTrue(path3.isAbsolute() && !path3.isRelative());
		assertTrue(path4.isAbsolute() && !path4.isRelative());
	}
	
	@Test
	public void testGetParent(){
		Path path1 = new Path("/","/");
		Path path2 = new Path("/","/app1");
		Path path3 = new Path("/","/app1/a/b");
		Path path4 = new Path("/app1/a","/");
		Path path5 = new Path("/app1/a","/app1");
		Path path6 = new Path("/app1/a","/app1/a/b");
		Path path7 = new Path("b/c/d");
		
		assertTrue(path1.getParent() == null);
		assertTrue(path2.getParent().getPath().equals(Path.pathSeparator));
		assertTrue(path3.getParent().getPath().equals("/app1/a"));
		assertTrue(path4.getParent() == null);
		assertTrue(path5.getParent().getPath().equals(Path.pathSeparator));
		assertTrue(path6.getParent().getPath().equals("/app1/a"));
		assertTrue(path7.getParent() == null);
	}
	
	@Test
	public void testResolveChild(){
		Path path = new Path("/app1");
		Path path2 = new Path("b/c");
		
		assertTrue(path.resolveChild(path2).toString().equals("/app1/b/c"));
		assertTrue(path.resolveChild("b/c").toString().equals("/app1/b/c"));
	}
	
	@Test( expected=IllegalArgumentException.class )
	public void testResolveChildExc1(){
		Path path = new Path("/app1");
		
		path.resolveChild("/b/c");
	}
	
	@Test
	public void testResolveSibling(){
		Path path = new Path("/");
		Path path2 = new Path("/app1/a");
		
		assertTrue(path.resolveSibling("b/c/d") ==  null);
		assertTrue(path2.resolveSibling("b/c/d").toString().equals("/app1/b/c/d"));
		
		Path path3 = new Path("b/c/d");
		
		assertTrue(path.resolveSibling(path3) ==  null);
		assertTrue(path2.resolveSibling(path3).toString().equals("/app1/b/c/d"));
	}
	
	@Test( expected=IllegalArgumentException.class )
	public void testResolveSiblingExc(){
		Path path = new Path("/");
		
		path.resolveSibling(new Path("/a"));
	}
	
	@Test( expected=IllegalArgumentException.class )
	public void testResolveSiblingExc2(){
		Path path = new Path("/");
		
		path.resolveSibling("/a");
	}
	
	@Test
	public void testRelativize(){
		Path path1 = new Path("/a/b");
		Path path2 = new Path("/a/b/c/d");
		
		assertTrue(path1.relativize(path2).toString().equals("c/d"));
	}
	
	@Test( expected=NullPointerException.class )
	public void testRelativizeExc(){
		Path path1 = new Path("/a/b");
		
		path1.relativize(null);
	}
	
	@Test( expected=IllegalArgumentException.class )
	public void testRelativizeExc2(){
		Path path1 = new Path("/a/b");
		
		path1.relativize(new Path("/c/d/e"));
	}
	
	@Test
	public void testToRealPath(){
		Path path = new Path("a/b");
		
		Path path2 = path.toRealPath();
		
		assertTrue(path2.toString().equals("/a/b"));
		
		Path path3 = path.toRealPath("/");
		
		assertTrue(path3.toString().equals("/a/b"));
		
		Path path4 = path.toRealPath("/a/b");
		
		assertTrue(path4.toString().equals("/a/b/a/b"));
	}
	
	@Test( expected=IllegalStateException.class)
	public void testToRealPathExc(){
		Path path = new Path("/a/b");
		
		Path path2 = path.toRealPath();
	}
}
