package gr.tuc.softnet.zookeeper.znode;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class PathsTest {

	@Test
	public void testPathSequenceIdComparator(){
		// the sequence id assigned has 10 characters
		// we create all the numbers from 100 to 999 create the path of the form "x-sequenceid"
		String prefix = "0000000"; // the first 7 characters, we append the id at the end as a string
		String [] numbers = new String[899];
		
		// create the string
		for (int i = 0; i < 899; ++i){
			numbers[i] = String.valueOf(i + 100);
		}
		
		// create the paths node
		Path [] paths = new Path[899];
		
		for (int i = 0; i < 899; ++i){
			paths[i] = new Path("x" + "-" + prefix + numbers[i]);
		}
		
		// add the paths to a collection
		List<Path> list =  new LinkedList<Path>();
		
		for (int i = 0; i < paths.length; ++i){
			list.add(paths[i]);
		}
		
		Collections.shuffle(list);
		
		Collections.sort(list, Paths.PathSequenceIdComparator);
	}

	@Test( expected=IllegalArgumentException.class )
	public void testPathSequenceIdComparatorExc(){
		// the sequence id assigned has 10 characters
		// we create all the numbers from 100 to 999 create the path of the form "x-sequenceid"
		String prefix = "0000000"; // the first 7 characters, we append the id at the end as a string
		String [] numbers = new String[899];
		
		// create the string
		for (int i = 0; i < 899; ++i){
			numbers[i] = String.valueOf(i + 100);
		}
		
		// create the paths node
		Path [] paths = new Path[899];
		
		for (int i = 0; i < 899; ++i){
			paths[i] = new Path("x" + prefix + numbers[i]); // error is here.. no "-" there
		}
		
		// add the paths to a collection
		List<Path> list =  new LinkedList<Path>();
		
		for (int i = 0; i < paths.length; ++i){
			list.add(paths[i]);
		}
		
		Collections.shuffle(list);
		
		Collections.sort(list, Paths.PathSequenceIdComparator);
	}
	
	@Test( expected=IllegalArgumentException.class )
	public void testPathSequenceIdComparatorExc2(){
		// the sequence id assigned has 10 characters
		// we create all the numbers from 100 to 999 create the path of the form "x-sequenceid"
		String prefix = "x000000"; // the first 7 characters, we append the id at the end as a string.. note that we make it invalid.. so correct number will be formed
		String [] numbers = new String[899];
		
		// create the string
		for (int i = 0; i < 899; ++i){
			numbers[i] = String.valueOf(i + 100);
		}
		
		// create the paths node
		Path [] paths = new Path[899];
		
		for (int i = 0; i < 899; ++i){
			paths[i] = new Path("x" + prefix + numbers[i]);
		}
		
		// add the paths to a collection
		List<Path> list =  new LinkedList<Path>();
		
		for (int i = 0; i < paths.length; ++i){
			list.add(paths[i]);
		}
		
		Collections.shuffle(list);
		
		Collections.sort(list, Paths.PathSequenceIdComparator);
	}
	
	@Test
	public void testPathFilter(){
		String [] numbers = new String[899];
		
		// create the string
		for (int i = 0; i < 899; ++i){
			numbers[i] = String.valueOf(i + 100);
		}
		
		// create the paths node
		Path [] paths = new Path[899];
		
		for (int i = 0; i < 899; ++i){
			paths[i] = new Path(numbers[i]);
		}
		
		// add the paths to a collection
		List<Path> list =  new LinkedList<Path>();
		
		for (int i = 0; i < paths.length; ++i){
			list.add(paths[i]);
		}
		
		Collections.shuffle(list);
		
		List<Path> pathList = Paths.listPaths(list, new NumberPathFilter());
	
		for (Path path: pathList){
			assertTrue(Integer.valueOf(path.toString()) <= 200);
		}
	}
	
	public static class NumberPathFilter implements PathFilter{

		@Override
		public boolean accept(Path path) {
			// TODO Auto-generated method stub
			return Integer.valueOf(path.toString()) <= 200;
		}
		
	}
}
