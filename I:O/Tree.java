import java.io.*;
/**
 * Name:	Pengkun Su
 * PID:		A16632888
 * USER:	cs12fa21ef
 * Description:	This programs utilize the binary tree, store UCSD students info
 * 		using insert, remove and lookup. program terminates when user
 * 		type ^D.
 */
/**
 * Class:	Tree
 * Description: It implements the binary tree structure and TNode class, 
 * 		storing TNodes with maxium 2 TNodes per TNode. It first reads
 * 		from a file,then it inserts, removes and look up TNodes in the 
 * 		binary tree with user input, finally it writes to the file.
 * Fields:	result - store the result from calling remove,insert or lookup
 * 		rootBox - store the rootposition to used as an output parameter
 *		leftBox - store the left position to used as an output parameter
 *		rightBox - store the right position to used as an output 
 *			   parameter
 * Public Function: write - it writes the root and occuapncy to the file
 * 		    Tree - constructor for tree, initialize and allocate
 * 		    jettison - jettison all nodes and the tree
 * 		    insert - call TNode insert 
 * 		    isEmpty - check if trees is empty
 * 		    lookup -  call TNode lookup
 * 		    remove - call TNode remove
 * 		    resetRoot - reset the root datafield of this tree
 */
public class Tree<Whatever extends Base> {

	private static final long BEGIN = 0;

	// data fields
	private RandomAccessFile fio;	// to write to and read from disk
	private long occupancy;		// number of TNode's in the Tree
	private long root;		// position of the root of the Tree
	private String representation;	// String representation of Tree
	private Base sample;		// copy Base object in TNode's read CTor
	private TNode sampleNode;	// to call TNode searchTree
	private Tracker tracker;	// track Tree's memory
	private long treeCount;		// which Tree it is
	private static long treeCounter;// how many Tree's are allocated

	// debug flag
	private static boolean debug;

	// number of disk reads and writes
	public static long cost = 0;

	// number of insert, remove, locate operations
	public static long operation = 0;

	// debug messages
	private static final String 
		TREE = "[Tree ",
		ALLOCATE = " - Allocating]\n",
		JETTISON = " - Jettisoning]\n",
		CLOSE = "]\n",
		COST_READ = "[Cost Increment (Disk Access): Reading ",
		COST_WRITE = "[Cost Increment (Disk Access): Writing ",
		AND = " and ",
		COMPARE = " - Comparing ",
		INSERT = " - Inserting ",
		CHECK = " - Checking ",
		UPDATE = " - Updating ",
		REPLACE = " - Replacing ";

	/*
	 * PositionBox class creates a PositionBox object to wrap a long type
	 * to be passed by reference in TNode methods.
	 */
	private class PositionBox {
		public long position;	// position value to be wrapped

		/*
		 * Constructor for PositionBox object, wraps position parameter.
		 *
		 * @param position the value to be wrapped by PositionBox
		 */
		public PositionBox (long position) {
			this.position = position;
		}
	}
	/**
	 * constructor for tree, allocate and initialize memory for object
	 *
	 * @param string for file name
	 * @param value for the copy TNode read ctor
	 * @param caller for driver
	 *
	 * @return none
	 */
	public Tree (String datafile, Whatever sample, String caller) {
		tracker = new Tracker ("Tree", Size.of (root)
			+ Size.of (occupancy)
			+ Size.of (representation)
			+ Size.of (treeCount)
			+ Size.of (tracker)
			+ Size.of (fio)
			+ Size.of (this.sample),
			caller + " calling Tree CTor");

		// DO NOT CHANGE TRACKER CODE ABOVE
		treeCounter++;
		treeCount = treeCounter;
		if (debug) {
			//debug statement
			System.err.print(TREE + treeCount + ALLOCATE);
		}

		this.sample = sample;
		sampleNode = new TNode("Tree Ctor");
		try {
			fio = new RandomAccessFile(datafile, "rw");
			if (fio.length() == 0) {
				//write the root and occupancy to the file
				fio.seek(BEGIN);
				fio.writeLong(root);
				fio.writeLong(occupancy);
				root = fio.getFilePointer();
				occupancy = 0;
			}
			else {
				fio.seek(BEGIN);
				root = fio.readLong();
				occupancy = fio.readLong();
			
			}
		}
		catch (IOException ioe) {
			System.err.println("IOException in Tree Ctor");
		}

	}

	/**
	 * Disable debug messager
	 */
	public static void debugOff () {
		debug = false;
	}

	/**
	 * Enable debug messages
	 */
	public static void debugOn () {
		debug = true;
	}

	/**
	 * Debug accessor
	 *
	 * @return true if debug is one, false otherwise
	 */
	public static boolean getDebug () {
		return debug;
	}

	/**
	 * Getter method for cost
	 *
	 * @return number of disk reads and writes of TNode
	 */
	public long getCost () {
		return cost;
	}

	/**
	 * Getter method for operation
	 *
	 * @return number of insert, lookup, remove operations
	 */
	public long getOperation () {
		return operation;
	}

	/**
	 * Count a TNode disk read or write
	 */
	public void incrementCost () {
		cost++;
	}

	/**
	 * Count an insert, lookup, or remove
	 */
	public void incrementOperation () {
		operation++;
	}

	/**
	 * insert the element by calling TNode insert
	 *
	 * @param complete element to be inserted
	 *
	 * @return the data that inserted
	 */
	public Whatever insert (Whatever element) {
		incrementOperation();
		if (debug) {
			//debug statement
			System.err.print(TREE + treeCount + INSERT
					+ element.getTrimName() + CLOSE);			}

		if (isEmpty()) {
			TNode tnode = new TNode(element,"Tree.insert");
			root = tnode.position;
			write();//write the root and occupancy to the file
			tnode.jettisonTNode();	
			return element;
		}

		PositionBox rootBox = new PositionBox(root);
		//wrapper object for root position
		Whatever result = sampleNode.searchTree 
				(element, rootBox, "insert");
		root = rootBox.position;//update the position
		write();
		return result;
	}
	
	/**
	 * Check if tree is empty
	 *
	 * @param none
	 *
	 * @return true if empty, false if not
	 */
	public boolean isEmpty () {
		return occupancy == 0;
	}

	/*
	 * jettison method for the Tree object. Untracks all memory associated
	 * with the Tree.
	 */
	public void jettison () {
		// Debug messages
		if (debug) {
			System.err.print (TREE);
			System.err.print (treeCount);
			System.err.print (JETTISON);
		}

		write (); // write the final root and occupancy to disk

		try {
			fio.close (); // close the file accessor
		} 
		catch (IOException ioe) {
			System.err.println ("IOException in Tree's jettison");
		}

		// Jettison TNodes and then tree itself
		sampleNode.jettisonTNode ();
		sampleNode = null;
		tracker.jettison ();
		tracker = null;
		treeCounter--;
	}
	
	/**
	 * write the root and occupany to the file
	 *
	 * @param none
	 *
	 * @return none
	 */
	public void write () {
		try {
			fio.seek(BEGIN);
			fio.writeLong(root);//write the root
			fio.writeLong(occupancy);//write the occupancy
		}
		catch (IOException ioe) {
			System.err.println ("IOException in Tree's write");
		}
	}

	/**
	 * look up matching data by calling TNode's lookup
	 *
	 * @param element that needed to be looked up
	 *
	 * @return pointer to the data if found, null if not
	 */
	public Whatever lookup (Whatever element) {
		incrementOperation();
		if (isEmpty()) {
			//no element found
			return null;
		}
		PositionBox rootBox = new PositionBox(root);//wrapper obejct
		Whatever result = sampleNode.searchTree
				(element,rootBox,"lookup");
		return result;
	}

	/**
	 * remove the matching data by calling TNode's remove
	 *
	 * @param incomplete element that need to be removed
	 *
	 * @return pointer to the data if removed, null if not
	 */
	public Whatever remove (Whatever element) {
		incrementOperation();
		if (isEmpty() == true) {
			//element doesn't exit
			return null;
		}
	
		PositionBox rootBox = new PositionBox(root);//wrapper object
		Whatever result = sampleNode.searchTree 
				(element, rootBox, "remove");
		root = rootBox.position;//update the position
		if (isEmpty() == true) {
			//call when the tree is empty
			resetRoot();
		}
		write();
		return result;



	}

	/**
	 * reset the root datafile of this tree
	 *
	 * @param none
	 *
	 * @return none
	 */
	private void resetRoot () {
		try {
			fio.seek(fio.length());
			root = fio.getFilePointer();//reset the root position
		}
		catch (IOException ioe) {
			System.err.println ("IOException in resetRoot");
		}
	}

	/**
	 * Creates a string representation of this tree. This method first
	 * adds the general information of this tree, then calls the
	 * recursive TNode function to add all nodes to the return string
	 *
	 * @return  String representation of this tree
	 */
	public String toString () {

		representation = "Tree " + treeCount + ":\n"
			+ "occupancy is " + occupancy + " elements.\n";

		try {
			fio.seek (fio.length ());
			long end = fio.getFilePointer ();

			long oldCost = getCost ();

			if (root != end) {
				TNode readRootNode = new TNode (root,
							"Tree's toString");
				readRootNode.writeAllTNodes ();
				readRootNode.jettisonTNode ();
				readRootNode = null;
			}

			cost = oldCost;
		} catch (IOException ioe) {
			System.err.println ("IOException in Tree's toString");
		}

		return representation;
	}

	/**
	 * Class:	TNode
	 * Description:	implmented by Tree class, it initializes TNode for the
	 * 		binary tree and jettison the TNode and resonsible for
	 * 		all the insert ,remove and lookup method and structing
	 * 		the tree
	 * Fields:	data - element of the current TNode
	 *		left - the left child positionof the current TNode
	 *		right - the right child position of the current TNode
	 *		balance - the balance of the current TNode
	 *		height - the height of the current TNode
	 *		leftBox - store the left position to used as an output
	 *		parameter
 	 *		rightBox - store the right position to used as an output
	 *		parameter
	 *		result - store the result of insert remove or lookup
	 *		copy - the copy fo the data
	 * Public Functions: TNode(read) - constructor for TNode, initialize and
	 * 		     allocate the memory for object by reading from 
	 * 		     the file
	 * 		     TNode(write) - constructor for TNode, intialize and
	 * 		     allocate the memory for object then writing to the
	 * 		     file
	 * 		     read - read the TNode datafield from the file
	 * 		     write - write the TNode datafield to the file
	 * 		     read - read the TNode datafield from the file
	 * 		     insert - insert the element into the binary tree
 	 * 		     lookup - look up matching data in the binary tree
 	 * 		     remove - remove matching data from the binary tree
	 *		     replaceAndRemoveMin - call when removing a TNode
	 *		     with 2 childrean, replaces the TNode with the
	 *		     minimum Tnode in its right subree
	 *		     setHeightAndBalance - updates the height and
	 *		     balance, check if the balance exceeds the threshold
	 *		     and restructure if it does.
	 */
	private class TNode {
		private Whatever data;	// data to be stored in the TNode
		// 1 + height of tallest child, or 0 for leaf
		private long height;
		// left child's height - right child's height
		private long balance;
		// positions of the TNode and its left and right children
		private long left, right, position;
		private Tracker tracker;// to track memory of the tree


		// threshold to maintain in the Tree
		private static final long THRESHOLD = 2;

		/*
		 * TNode constructor to create an empty TNode
		 *
		 * @param caller method object was created in
		 */
		public TNode (String caller) {
			tracker = new Tracker ("TNode", Size.of (data)
				+ Size.of (left)
				+ Size.of (right)
				+ Size.of (position)
				+ Size.of (height)
				+ Size.of (balance)
				+ Size.of (tracker),
				caller + " calling TNode CTor");
		}
		
		/**
		 * Tnode constructor and write to the file
		 *
		 * @param elements to be stored in data
		 * @param string of the caller
		 *
		 * @return none
		 */
		public TNode (Whatever element, String caller) {
			tracker = new Tracker ("TNode", Size.of (data)
				+ Size.of (left)
				+ Size.of (right)
				+ Size.of (position)
				+ Size.of (height)
				+ Size.of (balance)
				+ Size.of (tracker),
				caller + " calling TNode CTor");

			// DO NOT CHANGE TRACKER CODE ABOVE
			data = element;
			left = 0;
			right = 0;
			occupancy++;
			try {
				fio.seek(fio.length());
				position = fio.getFilePointer();
				//set the position to the end
			}
			catch (IOException ioe) {
				System.err.println("IOException in TNode");
			}
			write();

		}
		

		/**
		 * Tnode constructor from reading the file
		 *
		 * @param the offset to the corresponding TNode to be read
		 * @param string of the caller
		 *
		 * @return none
		 */
		@SuppressWarnings ("unchecked")
		public TNode (long position, String caller) {
			tracker = new Tracker ("TNode", Size.of (data)
				+ Size.of (left)
				+ Size.of (right)
				+ Size.of (position)
				+ Size.of (height)
				+ Size.of (balance)
				+ Size.of (tracker),
				caller + " calling TNode CTor");

			// DO NOT CHANGE TRACKER CODE ABOVE
			data = (Whatever) sample.copy();//read in from the file
			read(position);
			
		}
			
		/**
		 * read the TNode datafield from the file
		 *
		 * @param position of the TNode to be read
		 *
		 * @return none
		 */
		public void read (long position) {
			incrementCost();
			try {
				//read all the fields from the file
				fio.seek(position);
				data.read(fio);
				height = fio.readLong();
				balance = fio.readLong();
		       		left = fio.readLong();
				right = fio.readLong();
				this.position = fio.readLong();
			}
			catch (IOException ioe) {
				System.err.println("IOException in read");
			}
			
			if (debug) {
					System.err.print(COST_READ 
							+ data.getName()
							+ CLOSE);
				}

		}
		
		/**
		 * write the TNode datafield to the file
		 *
		 * @param none
		 *
		 * @return non
		 */
		public void write () {
			incrementCost();
			if (debug) {
				System.err.print(COST_WRITE + data.getName()
						+ CLOSE);
			}
			try {
				//write all the fields to the file
				fio.seek(position);
				data.write(fio);
				fio.writeLong(height);
				fio.writeLong(balance);
				fio.writeLong(left);
				fio.writeLong(right);
				fio.writeLong(position);
			}
			catch (IOException ioe) {
				System.err.println("IOException in write");
			}
		}
	
		/**
		 * insert the element into the binary tree
		 *
		 * @param data to be inserted into the tree
		 * @param object holds TNode position from parent TNode
		 *
		 * @return data that inserted
		 */
		private Whatever insert (Whatever element,
				PositionBox positionInParentBox) {
			if (debug) {
				//debug statement
				System.err.print(TREE + treeCount + COMPARE
						+ element.getTrimName()
						+ AND + data.getTrimName() 
						+ CLOSE);
			}
			if (element.equals(data)) {
				if (debug) {
					//debug statement
					System.err.print(TREE + treeCount 
							+ REPLACE
							+ element.getTrimName()
							+ CLOSE);
				}
				data.jettison();//jettison the old data
				data = element;
				write();
				return element;
			}

			TNode tnode = null;
			Whatever result = null;
			if (element.isLessThan(data)) {
				if (left == 0) {
					if (debug) {
						//debug statement
						System.err.print(TREE 
							     + treeCount
							     + INSERT 
							 + element.getTrimName()
							     + CLOSE);
					}
					tnode = new TNode(element,
							"TNode.insert");
					left = tnode.position;
					//update the position
					tnode.jettisonTNode();
					//jettison the node

				}
				else {
					PositionBox leftBox = 
						new PositionBox(left);
					//wrapper object for position
					result = sampleNode.searchTree
						(element,leftBox,"insert");
					left = leftBox.position;
					//update position
				}
			}
			else {
				if (right == 0) {
					if (debug) {
						//debug statement
						System.err.print(TREE
							     + treeCount
							     + INSERT
							 + element.getTrimName()
							     + CLOSE);
					}
					tnode = new TNode(element,
							"TNode.insert");
					right = tnode.position;
					//update teh position
					tnode.jettisonTNode();
					//jettison the node
				}
				else {
					PositionBox rightBox =
						new PositionBox(right);
					//wrapper object for position
					result = sampleNode.searchTree
						(element,rightBox,"insert");
					right = rightBox.position;
					//update position	
				}	
			}
			if (debug){
				//debug statement
				System.err.print(TREE + treeCount + UPDATE
						+ data.getTrimName() + CLOSE);
			}
			setHeightAndBalance(positionInParentBox);
			//update height and balance
			return element;

		}

		/*
		 * Jettison method for TNode object, untracks memory associated
		 * with the calling TNode.
		 */
		private void jettisonTNode () {
			left = right = 0; // reset left and right positions

			// jettison the data stored
			if (data != null) {
				data.jettison ();
				data = null;
			}

			// jettison tracker
			tracker.jettison ();
			tracker = null;
		}

		/*
		 * look up element from binary tree
		 *
		 * @param element to be looked up in the tree
		 *
		 * @return return found element, null if not found
		 */
		@SuppressWarnings ("unchecked")
		private Whatever lookup (Whatever element) {
			if (debug) {
				//debug statement
				System.err.print(TREE + treeCount + COMPARE
						+ element.getTrimName() + AND
						+ data.getName() + CLOSE);
			}
			
			Whatever result = null;
			if (element.equals(data)) {
				result = (Whatever) data.copy();
				//return found element
				return result;
			}
			if (element.isLessThan(data)) {
				//go left if smaller than
				if (left == 0) {
					//the element is not found
					return null;
				}
				PositionBox leftBox = new PositionBox(left);
				result = sampleNode.searchTree
					(element,leftBox,"lookup");
			}
			else {
				//go right if bigger than
				if (right == 0) {
					//the element is not found
					return null;
				}
				PositionBox rightBox = new PositionBox(right);
				result = sampleNode.searchTree
					(element, rightBox,"lookup");
			}
			return result;		
		}

		/*
		 * removes the element from the binary tree
		 *
		 * @param element that needed to be removed
		 * @param wrapper object for the pointer from parent
		 * @param true if called from SHB, false if not
		 */
		@SuppressWarnings("unchecked")
		private Whatever remove (Whatever element,
			PositionBox positionInParentBox, boolean fromSHB) {
			if (debug) {
				//debug statement
				System.err.print(TREE + treeCount + COMPARE
						+ element.getTrimName() + AND
						+ data.getTrimName() + CLOSE);
				}
			TNode tnode = null;
			if (element.equals(data)) {
				occupancy--;
				//one child or no child
				Whatever copy = (Whatever) data.copy();
				if (left == 0 && right == 0) {
					positionInParentBox.position = 0;
				}
				else if (left == 0) {
					tnode = new TNode
						(right,"TNode's remove");
					//update the position	
					positionInParentBox.position = 
						tnode.position;
					tnode.jettisonTNode();
				}
				else if (right == 0) {
					tnode = new TNode
						(left,"TNode's remove");
					//update the position
					positionInParentBox.position = 
						tnode.position;
					tnode.jettisonTNode();
				}
				//two children
				else {	
					PositionBox rightBox = 
						new PositionBox(right);
					//wrapper object for position
					Whatever result = sampleNode.searchTree
						(element, rightBox,"RARM");
					//update the position
					data.jettison();
					//jettison the data
					data = result;
					//update the data
					right = rightBox.position;
					//update the position
					if (fromSHB == false) {									//update the height and balance
				       		setHeightAndBalance
							(positionInParentBox);
					}
					else {
						write();
					}
				}
				return copy;	
			}
			else {
				Whatever result = null;
				if (element.isLessThan(data)) {
					if (left == 0) {
						//element is not found
						return null;
					}
					PositionBox leftBox =
					       new PositionBox(left);
					//wrapper object for pointer
					result = sampleNode.searchTree
								(element,
								leftBox,
								"remove");
					//update the pointer
					left = leftBox.position;
				}
				else {
					if (right == 0) {
						//element is not found
						return null;
					}
					PositionBox rightBox = 
						new PositionBox(right);
					//wrapper object for pointer
					result = sampleNode.searchTree
								(element,
								rightBox,
								"remove");
					//update the position
					right = rightBox.position;
				}
				if (fromSHB == false) {									//update the height and balance
				       setHeightAndBalance(positionInParentBox);
				}
				else {
					write();
				}
				
				return result;
			}
		}
		
		/*
		 * called from remove if TNode has two children, it replaces it
		 * with the smallest TNode from the right subtree
		 *
		 * @param TNode that wanted to be replaced with
		 * @param wrapper object for pointer from the parent
		 *
		 * @return none
		 */
		@SuppressWarnings ("unchecked")
		private Whatever replaceAndRemoveMin
				(PositionBox positionInParentBox) {
			if (debug) {
				//debug statement
				System.err.print(TREE + treeCount + CHECK
						+ data.getTrimName() + CLOSE);
			}
			if (left == 0) {
				Whatever result = (Whatever) data.copy();
				//debug statement
				System.err.print(TREE + treeCount + REPLACE 
						+ data.getTrimName()
						+ CLOSE);
				if (right == 0) {
					positionInParentBox.position = 0;
				}
				else {
					TNode tnode = new TNode(right,"RARM");
					positionInParentBox.position =
								tnode.position;
					tnode.jettisonTNode();
				}
				return result;
			}
			else {
				PositionBox leftBox = new PositionBox(left);
				//wrapper object for left position
				Whatever result = sampleNode.searchTree
						(null,leftBox,"RARM");
				left = leftBox.position;//update the position
				//update height and balance
				setHeightAndBalance(positionInParentBox);
				return result;
			}		
		}

		/*
		 * Reads in TNode from the disk at positionInParentBox.position
		 * so an action may be performed on that TNode. Centralizes the
		 * the operations needed when reading a TNode from the disk.
		 *
		 * @param element the data the action is to be performed on
		 * @param positionInParentBox the PositionBox holding the
		 *        position of the TNode to be read from the disk
		 * @param action the action to be performed
		 * @return returns the result of the action
		 */
		private Whatever searchTree (Whatever element,
			PositionBox positionInParentBox, String action) {

			Whatever result = null;
			TNode readNode = new TNode 
			 (positionInParentBox.position, "searchTree " + action);

			if (action.equals ("insert")) {
				result = readNode.insert (element,
							positionInParentBox);
			}
			else if (action.equals ("lookup"))
				result = readNode.lookup (element);
			else if (action.equals ("RARM")) {
				result = readNode.replaceAndRemoveMin
							(positionInParentBox);
			}
			else if (action.equals ("remove")) {
				result = readNode.remove (element,
						positionInParentBox, false);
			}

			readNode.jettisonTNode (); // rename to jettisonTNode
			readNode = null;

			return result;
		}

		/* The PointerInParent parameter is used to pass to Remove
		 * and to Insert as the way to restructure the Tree if
		 * the balance goes beyond the threshold.  You'll need to
		 * store the removed data in a working RTS TNode<Whatever>
		 * because the memory for the current TNode<Whatever> will be
		 * deallocated as a result of your call to Remove.
		 * Remember that this working TNode<Whatever> should not be
		 * part of the Tree, as it will automatically be dealloacted
		 * when the function ends. When calling Remove, remember to
		 * tell Remove that its being called from SetHeightAndBalance.
		 *
		 * @param positionInParentBox holds the position of the calling
		 *        TNode on the disk
		 */
		/**
		 * update the height and the balance of the tree, check if
		 * balance exceeds threshold, and resture it if does
		 *
		 * @param wrapper objecto for pointer from parent
		 *
		 * @return none
		 */
		@SuppressWarnings ("unchecked")
		private void setHeightAndBalance 
					(PositionBox positionInParentBox) {
			if (debug){
				//debug statement
				System.err.print(TREE + treeCount + UPDATE
						+ data.getTrimName()+ CLOSE);
			}

			TNode readRight = null;
			TNode readLeft = null;
			if (left == 0 && right == 0) {
				//if there are not children
				height = 0;
				balance = 0;
			}
			else if (left == 0) {
				//special case for only left side
				readRight = new TNode
					(right,"setHeightAndBalance");
				height = readRight.height + 1;
				balance = -1 - readRight.height;
				readRight.jettisonTNode();
			}
			else if (right == 0) {
				//special case for only right sidei
				readLeft = new TNode
					(left,"setHeightAndBalance");
				height = readLeft.height + 1;
				balance = readLeft.height + 1;
				readLeft.jettisonTNode();
			}
			else {
				readRight = new TNode
					(right,"setHeightAndBalance");
				//read in the right child
				readLeft = new TNode
					(left,"setHeightAndBalance");
				//read in the left child
				if (readRight.height > readLeft.height) {
					height = 1 + readRight.height;
				}
				else {
					height = 1 + readLeft.height;
				}
				balance = readLeft.height - readRight.height;
				readRight.jettisonTNode();
				readLeft.jettisonTNode();
				//jettison both children
			}

			write();

			if (Math.abs(balance) > THRESHOLD) {
				//check if it exceeds the threshold
				//remove and reinsert the element
				Whatever result = remove(data,
							positionInParentBox,
							true);
				result = sampleNode.searchTree
					(result,positionInParentBox,"insert");
			}
		}

		/**
		 * Creates a string representation of this node. Information
		 * to be printed includes this node's height, its balance,
		 * and the data its storing.
		 *
		 * @return  String representation of this node
		 */

		public String toString () {
			return "at height:  " + height + " with balance:  "
				+ balance + "  " + data + "\n";
		}

		/**
		 * Writes all TNodes to the String representation field.
		 * This recursive method performs an in-order
		 * traversal of the entire tree to print all nodes in
		 * sorted order, as determined by the keys stored in each
		 * node. To print itself, the current node will append to
		 * tree's String field.
		 */
		private void writeAllTNodes () {
			if (left != 0) {
				TNode readLeftNode = new TNode (left,
							"writeAllTNodes");
				readLeftNode.writeAllTNodes ();
				readLeftNode.jettisonTNode();
				readLeftNode = null;
			}

			representation += this;

			if (right != 0) {
				TNode readRightNode = new TNode (right,
							"writeAllTNodes");
				readRightNode.writeAllTNodes ();
				readRightNode.jettisonTNode();
				readRightNode = null;
			}
		}
	}
}
