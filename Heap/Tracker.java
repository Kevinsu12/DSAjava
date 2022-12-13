import java.util.*;
/**
 * name:	Pengkun Su
 * pid:		A16632888
 * user:	cs12fa21ef
 * description: this file is used to track memory of the program
 */
/**
 * class:	Tracker
 * description: track and jettison memory
 * fields:	className - the name of class
 * 		size - the size of the data
 * 		caller - the method caller
 * 		memeory - the array of memory
 * pubic function: Tracker - constructor for tracker class
 * 		   jettison - jettison the tracker
 *		   checKMemoryLeaks - check if there are unjettisoned data
 *		   toString - printout what is currently tracked
 */
public class Tracker {

	public static final long BYTES_FOR_LONG = 8;
	public static final long BYTES_FOR_INT = 4;

	private String className;
	private long size;
	private String caller;
	private static ArrayList<Tracker> memory = new ArrayList<Tracker>();
	
	/**
	 * constructor for Tracker class, allocate and initialize
	 *
	 * @param the name of the class
	 * @param the size of the data
	 * @param the caller 
	 */
	public Tracker (String className, long size, String caller) {
		this.className = className;
		this.size = size;
		this.caller = caller;
		trackObject ();
	}
	
	/**
	 * add tracked object
	 *
	 * @param none
	 *
	 * @return none
	 */
	private void trackObject () {
		memory.add(this);
	}
	
	/**
	 * unadd the tracked object
	 *
	 * @param none
	 *
	 * @return none
	 */
	private void untrackObject () {
		memory.remove (this);
	}
	
	/**
	 * jettison the object
	 *
	 * @param none
	 *
	 * @return none
	 */
	public void jettison () {
		untrackObject ();
	}
	
	/**
	 * check if there is memory leaks
	 *
	 * @param none
	 *
	 * @return none
	 */
	public static boolean checkMemoryLeaks () {
		if(memory.size () == 0) {
			System.err.println (
				"\nNo memory leaks!  All memory " 
				+ "has been correctly jettisoned.");
			return true;
		}

		System.err.print (memory.get (0));
		return false; 
	}
	
	/**
	 * printout the current tracked data
	 *
	 * @param none
	 *
	 * @return none
	 */
	// print out what is currently tracked, the array list, to debug. 
	public String toString () {
		String string = "";
		// TODO: addition of the size, 
		// and print out the total at the end (similar to valgrind)
		// number of allocations, size is, etc. 
		if(memory.size () > 0) {
			string += "\n------------ " 
				+ "TRACKED MEMORY ------------\n";
			for(Tracker tracked : memory) {
				string += tracked.size 
					+ " bytes of heap memory, created in " 
					+ tracked.caller + " for the " 
					+ tracked.className + " object.\n";
			}
		}
		return string;
	}
}
