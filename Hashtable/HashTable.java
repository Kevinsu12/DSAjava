/**
 * Name:	Pengkun Su
 * PID:		A16632888
 * USER:	cs12fa21ef
 * File name:	HashTable.java
 * Description:	This programs take a Base object with a key and key value and 
 * 		insert it into an table based on a probe sequence. Program 
 * 		terminates when user enters ^D.
 */

/**
 * Class:	HashTable
 * Description:	Class initializes the hashtable and will insert and lookup
 * 		wanted infomation based on a probe sequence and bully 
 * 		algorithm.
 * Fields:	idx - index for looping through the table
 * 		increment - increment on the probesequence
 * 		locate - return of locate method
 * Public function: debugOn -  turns on debugging
 * 		    debugOff - turns off debugging
 * 		    Hashtable - allocates and initialize a hashtable
 * 		    HashTable jettison -  called when jettisoning the HashTable
 *		    insert - inserts the element in the hash table
 *		    locate - locate the index in the table for wanted element
 *		    lookup - look up an element in the hash table
 */
public class HashTable extends Base {

	// counters, flags and constants 
	private static int counter = 0;         // number of HashTables so far
	private static boolean debug;           // allocation of debug states

	// data fields
	private long occupancy;     // how many elements are in the Hash Table
	private int size;           // size of Hash Table
	private Base table[];       // the Hash Table itself ==> array of Base
	private int tableCount;     // which hash table it is
	private Tracker tracker;    // to track memory

	// initialized by Locate function
	private int index;      // last location checked in hash table
        
	// set in insert/lookup, count of location in probe sequence
        private int count = 0;

	// messages
	private static final String DEBUG_ALLOCATE = " - Allocated]\n";
	private static final String DEBUG_LOCATE = " - Locate]\n";
	private static final String DEBUG_LOOKUP = " - Lookup]\n";
	private static final String AND = " and ";
	private static final String BUMP = "[Bumping To Next Location...]\n";
	private static final String COMPARE = " - Comparing ";
	private static final String FULL = " is full...aborting...]\n";
	private static final String FOUND_SPOT = " - Found Empty Spot]\n";
	private static final String HASH = "[Hash Table ";
	private static final String HASH_VAL = "[Hash Value Is ";
	private static final String INSERT = " - Inserting ";
	private static final String PROCESSING = "[Processing ";
	private static final String TRYING = "[Trying Index ";
	
	/**
	 * turn on debug for this HashTable
	 *
	 * @param none
	 *
	 * @return none
	 */
	public static void debugOn () {
		
		debug = true;
	}
	
	/**
	 * turn off debug for this HashTable
	 *
	 * @param none
	 *
	 * @return none
	 */
	public static void debugOff () {
		
		debug = false;
	}
	
	/**
	 * Allocates and initialize the memeory associated with a hash table
	 *
	 * @param expected to be any postive prime number
	 * @param called of the tracker
	 *
	 * @return none
	 */
	public HashTable (int sz, String caller) {		
		occupancy = 0;
		size = sz;
		table = new Base[sz];
		for (int idx = 0; idx < sz; idx++) {
			//loop through the array to initialize every element	
			table[idx] = null;
		}
		counter++;
		tableCount = counter;
		if (debug) {
			System.err.print(HASH + tableCount + DEBUG_ALLOCATE);
		}

		// DO NOT CHANGE THIS PART
		tracker = new Tracker ("HashTable", 
				Size.of (index)
				+ Size.of (occupancy)
				+ Size.of (size)
				+ Size.of (table)
				+ Size.of (tableCount)
				+ Size.of (tracker),
				caller + " calling HashTable Ctor");
	}
	
	/**
	 * JEttison each Based object stored in the Hashtable and the tracker
	 * object
	 *
	 * @param none
	 *
	 * @return none
	 */
	public void jettison () {
		for (int idx = 0; idx < size; idx++) {

			if (table[idx] != null) {
				//loop through table to jettison
				table[idx].jettison();
			}
		}
		tracker.jettison();
	}
	
	/**
	 * get the current occupancy
	 *
	 * @param none
	 *
	 * @return occupancy
	 */
	public long getOccupancy () {
		
		return occupancy;
	}

	/**
	 * Performs insertion into the table via delegation to the
	 * private insert method.
	 *
	 * @param   element       The element to insert.
	 *
	 * @return  true or false indicating success of insertion
	 */
	public boolean insert (Base element) {
		return insert (element, false);
	}
	
	/**
	 * inserts the element in the hash table
	 *
	 * @param a complete elements to insert
	 * @param whetehr or not this method was called recursively
	 *
	 * @return true if insert sucessfuly, false if not
	 */
	private boolean insert (Base element, boolean recursiveCall) {
		if (recursiveCall == false) {
			count = 0;
			occupancy++;
		}
		else {
			//find the count of bumped resursive called element 
			int increment = element.hashCode() % (size - 1) + 1;
			count = (index - element.hashCode() % size) / increment;
			count = count + 1;
		}


		if (debug) {
			//debug message
			System.err.print (HASH + tableCount + INSERT +
						element.getName() + "]\n");

			System.err.print (HASH + tableCount + DEBUG_LOCATE);
		}

		Base locate = locate(element);//locate the element

		if (locate == null) {
			if (count == size) {
				//table is exhausted
				return false;
			}
			else { 	
				//spot is empty
				table[index] = element;
				return true;
			}

		}
		else {	
			//bump the elemnt due to the bully system
			table[index] = element;
			insert(locate,true);//put the bumped element in insert
			return true;
		}
				
	}
	
	/**
	 * locates the index in the table of the wanted element when it's 
	 * getting called
	 *
	 * @param a complete and incompletes elements depending on whether it's
	 * 	  called from insert or lookup
	 *
	 * @return return the object if there is one, null if it's empty.
	 */
	private Base locate (Base element) {
		index = element.hashCode() % size; //initial location
		//increment for index
		int increment = element.hashCode() % (size - 1) + 1;
		if (debug) {
			//debug message
			System.err.print (PROCESSING + element.getName()
						+ "]\n");
			System.err.print (HASH_VAL + element.hashCode()
						+ "]\n");
		while (count < size) {
		// break when loop runs more than the size of the table
			if (debug) {
				//debug message
				System.err.print(TRYING + index + "]\n");		
			}

			if (table[index] == null) {
			//found a empty spot
				if (debug) {
					//debug message
					System.err.print(HASH + tableCount +
							FOUND_SPOT);
				}

				break;
			}	
			
			if (debug) {
				//debug message
				System.err.print(HASH + tableCount + COMPARE +
						element.getName() + AND + 
						table[index].getName() + "]\n");
			}

			if (table[index].isLessThan(element)){
			//compare and bully the element if can
				if (debug) {
					//debug message
					System.err.print(BUMP);
				}
				break;
			}

			index = (index + increment) % size;
			//next index
			count++;
			}
		}

		if (count == size){
		//table exhausted
			if (debug) {
				//debug message
				System.err.print(HASH + tableCount + FULL);
			}
			return null;
		}
		else {
			return table[index]; 
		}
	}

	/**
	 * look up the element in the hash table
	 *
	 * @param values of elements are expected to be incomplete elements
	 *
	 * @return references to the elements if found
	 */
	public Base lookup (Base element) {
		if (debug) {
			//debug statement
			System.err.print(HASH + tableCount + DEBUG_LOOKUP);
			System.err.print (HASH + tableCount + DEBUG_LOCATE);
		}
		
		return locate(element);
	}


	/**
	 * Creates a string representation of the hash table. The method 
	 * traverses the entire table, adding elements one by one ordered
	 * according to their index in the table. 
	 *
	 * @return  String representation of hash table
	 */
	public String toString () {
		String string = "Hash Table " + tableCount + ":\n";
		string += "size is " + size + " elements, "; 
		string += "occupancy is " + occupancy + " elements.\n";

		/* go through all table elements */
		for (int index = 0; index < size; index++) {

			if (table[index] != null) {
				string += "at index " + index + ": ";
				string += "" + table[index];
				string += "\n";
			}
		}

		string += "\n";

		if(debug)
			System.err.println(tracker);

		return string;
	}
}
