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
 * 		storing TNodes with maxium 2 TNodes per TNode. It inserts, 
 * 		removes and look up TNodes in the binary tree with user input.
 * Fields:	result - store the result from calling remove,insert or lookup
 * 		rootBox - store the rootpointer to used as an output parameter
 *		leftBox - store the left pointer to used as an output parameter
 *		rightBox - store the right ponter to used as an output parameter
 * Public Function: debugOn - turns on debugging for this tree
 * 		    debugOff - turns off debugging for this tree
 * 		    Tree - constructor for tree, initialize and allocate
 * 		    jettison - jettison all nodes and the tree
 * 		    insert - call TNode insert 
 * 		    isEmpty - check if trees is empty
 * 		    lookup -  call TNode lookup
 * 		    remove - call TNode remove
 */
public class Tree<Whatever extends Base> {

	/* data fields */
	private TNode root;
	private long occupancy; 
	private String representation;
	private long treeCount;
	private Tracker tracker;
	private static long treeCounter;

	/* debug flag */
	private static boolean debug;

	/* debug messages */
	private static final String ALLOCATE = " - Allocating]\n";
	private static final String JETTISON = " - Jettisoning]\n";
	private static final String AND = " and ";
	private static final String CLOSE = "]\n";
	private static final String COMPARE = " - Comparing ";
	private static final String INSERT = " - Inserting ";
	private static final String CHECK = " - Checking ";
	private static final String UPDATE = " - Updating ";
	private static final String REPLACE = " - Replacing ";
	private static final String TREE = "[Tree ";

	private class PointerBox {
		public TNode pointer;

		public PointerBox (TNode pointer) {
			this.pointer = pointer;
		}
	}
	/**
	 * constructor for tree, allocate and initialize memory for object
	 *
	 * @param string for tree name
	 * @param caller for driver
	 *
	 * @return none
	 */
	public Tree (String caller) {
		tracker = new Tracker ("Tree", Size.of (root)
		+ Size.of (occupancy)
		+ Size.of (representation)
		+ Size.of (treeCount)
		+ Size.of (treeCounter)
		+ Size.of (tracker),
		caller + " calling Tree CTor");

		// DO NOT CHANGE THIS PART ABOVE
		root = null;
		occupancy = 0;
		treeCounter++;
		treeCount = treeCounter;

		if (debug) {
			System.err.print(TREE + treeCount + ALLOCATE);
		}

	}
	
	/**
	 * jettison the tree and all the TNodes
	 *
	 * @param none
	 *
	 * @return none
	 */
	public void jettison () {
		if (isEmpty()) {//check if the tree is empty
			tracker.jettison();//jettison the tree
		}
		else {
			root.jettisonAllTNodes();//jettison all the Nodes
			tracker.jettison();//jettison the tree
		}

	}
	/**
	 * turn off debug message
	 *
	 * @param none
	 * 
	 * @return none
	 */
	public static void debugOff () {
		debug = false;
	}
	
	/**
	 * turn on debug message
	 *
	 * @param none
	 *
	 * @return none
	 */
	public static void debugOn () {
		debug = true;
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
	
	/**
	 * insert the element by calling TNode insert
	 *
	 * @param complete element to be inserted
	 *
	 * @return true if insert sucessfull, false if not
	 */
	public boolean insert (Whatever element) {
		if (isEmpty()) {
			//if root is null insert at root
			root = new TNode(element,"Tree.insert");

			if (debug ) {
				//debug message
				System.err.print(TREE + treeCount + INSERT
						+ element.getName()
						+ CLOSE);
			}
			return true;
		}

		PointerBox rootBox = new PointerBox(root);
		//set PointerBox for output parameter
		root.insert(element,rootBox);//call insert from TNode
		root = rootBox.pointer;//update pointer
		return true;

	}
	
	/**
	 * look up matching data by calling TNode's lookup
	 *
	 * @param element that needed to be looked up
	 *
	 * @return pointer to the data if found, null if not
	 */
	public Whatever lookup (Whatever element) {
		if (isEmpty()) {
			return null;
		}
		Whatever result = root.lookup(element);
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
		if (isEmpty()) {
			return null;
		}
		
		PointerBox rootBox = new PointerBox(root);
		Whatever result = root.remove(element,rootBox,false);
		root = rootBox.pointer;
		return result;
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

		if (root != null)
		root.writeAllTNodes();

		return representation;
	}
	/**
	 * Class:	TNode
	 * Description:	implmented by Tree class, it initializes TNode for the
	 * 		binary tree and jettison the TNode and resonsible for 
	 * 		all the insert ,remove and lookup method and structing
	 * 		the tree 
	 * Fields:	data - element of the current TNode
	 *		left - the left child of the current TNode
	 *		right - the right child of the current TNode
	 *		balance - the balance of the current TNode
	 *		height - the height of the current TNode
	 *		leftBox - store the left pointer to used as an output 
	 *		parameter
 	 *		rightBox - store the right ponter to used as an output 
	 *		parameter
	 *		result - store the result of insert remove or lookup
	 * Public Functions: TNode - constructor for TNode, initialize and 
	 * 		     allocate the memory for object
	 *		     jettisonTNodeOnly - jettison TNode only
	 *		     jettisonTNodeAndData - jettison TNode and its data
	 *		     jettisonAllTNodes - jetiison All TNodes
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
		private Whatever data;
		private TNode left, right;
		private Tracker tracker;

		/* left child's height - right child's height */
		private long balance;
		/* 1 + height of tallest child, or 0 for leaf */
		private long height;

		private static final long THRESHOLD = 2;

		public TNode (Whatever element, String caller) {
			tracker = new Tracker ("TNode", Size.of (data)
			+ Size.of (left)
			+ Size.of (right)
			+ Size.of (height)
			+ Size.of (balance)
			+ Size.of (tracker),
			caller + " calling Tree CTor");
			// DO NOT CHANGE THIS PART ABOVE
			data = element;
			left = null;
			right = null;
			height = 0;
			balance = 0;
			occupancy++;
		}
		
		/**
		 * jettison the TNode but not the data
		 *
		 * @param none
		 *
		 * @return none
		 */
		private void jettisonTNodeOnly () {
			tracker.jettison();//jettison TNode
		}
		
		/**
		 * jettison the TNode and the data
		 *
		 * @param none
		 *
		 * @return none
		 */
		private void jettisonTNodeAndData () {
			tracker.jettison();//jettisonTNode
			data.jettison();//jettison data
		}
		
		/**
		 * jettison all TNodes and data
		 *
		 * @param none
		 *
		 * @return none
		 */
		private void jettisonAllTNodes () {
			if (left != null) {
				//go left first
				left.jettisonAllTNodes();
			}
			if (right != null) {
				//go right second
				right.jettisonAllTNodes();
			}
			jettisonTNodeAndData();//visit there TNode
		}
		
		/** 
		 * insert the element into the binary tree
		 *
		 * @param data to be inserted into the tree
		 * @param object holds TNode pointer from parent TNode
		 *
		 * @return true if insert successfully,false if not
		 */
		private boolean insert (Whatever element,
			PointerBox pointerInParentBox) {
			if (debug) {
				//debug statement
				System.err.print(TREE + treeCount + COMPARE
						+ element.getName()
						+ AND + data.getName() + CLOSE);
			}

			if (element.equals(data)) {
				if (debug) {
					//debug statement
					System.err.print(TREE + treeCount 
							+ REPLACE
							+ element.getName()
							+ CLOSE);
				}
				data.jettison();//jettison old data
				data = element;
				return true;
			}

			if (element.isLessThan(data)) {
				if (left == null) {
					if (debug) {
						//debug statement
						System.err.print(TREE 
							     + treeCount
							     + INSERT 
							     + element.getName()
							     + CLOSE);
					}
					left = new TNode(element,
							"TNode.insert");
				}
				else {
					PointerBox leftBox = 
						new PointerBox(left);
					//wrapper object for pointer
					left.insert(element,leftBox);
					left = leftBox.pointer;//update pointer
				}
			}
			else {
				if (right == null) {
					if (debug) {
						//debug statement
						System.err.print(TREE
							     + treeCount
							     + INSERT
							     + element.getName()
							     + CLOSE);
					}
					right = new TNode(element,
							"TNode.insert");
				}
				else {
					PointerBox rightBox =
						new PointerBox(right);
					//wrapper object for pointer
					right.insert(element,rightBox);
					right = rightBox.pointer;
					//update pointer	
				}	
			}
			if (debug){
				//debug statement
				System.err.print(TREE + treeCount + UPDATE
						+ data.getName() + CLOSE);
			}
			setHeightAndBalance(pointerInParentBox);
			//update height and balance
			return true;

		}

		@SuppressWarnings("unchecked")
		/*
		 * look up element from binary tree
		 *
		 * @param element to be looked up in the tree
		 * 
		 * @return return found element, null if not found
		 */
		private Whatever lookup (Whatever element) {
			if (debug) {
				//debug statement
				System.err.print(TREE + treeCount + COMPARE
						+ element.getName() + AND
						+ data.getName() + CLOSE);
			}
			
			Whatever result = (Whatever) data.copy();
			if (element.equals(data)) {
				//return found element
				return result;
			}
			if (right == null && left == null) {
				//element is not found
				return null;
			}
			if (element.isLessThan(data)) {
				//go left if less than
				result = left.lookup(element);
			}
			else {
				//go right if bigger than
				result = right.lookup(element);
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
		private Whatever remove (Whatever element, 
		PointerBox pointerInParentBox,
		boolean fromSHB) {
			if (debug) {
				//debug statement
				System.err.print(TREE + treeCount + COMPARE
						+ element.getName() + AND
						+ data.getName() + CLOSE);
				}

			Whatever result = this.data;
			if (element.equals(data)) {
				occupancy--;
				//one child or no child
				if (left == null) {
					jettisonTNodeOnly();//jettison TNode
					//update the pointer
					pointerInParentBox.pointer = right;
				}
				else if (right == null) {
					jettisonTNodeOnly();//jettison TNode
					//update the pointer
					pointerInParentBox.pointer = left;
				}
				//two children
				else {	
					PointerBox rightBox = 
						new PointerBox(right);
					//wrapper object for pointer
					right.replaceAndRemoveMin(this,
								rightBox);
					//update the pointer
					right = rightBox.pointer;
					
				}
				return result;	
			}
			else {
				//the element is not found
				if (left == null && right == null) {
					return null;
				}
                 		
				if (element.isLessThan(data)) {
					PointerBox leftBox =
					       new PointerBox(left);
					//wrapper object for pointer
					result = left.remove(element,
								leftBox,
								fromSHB);
					//update the pointer
					left = leftBox.pointer;
					
				}
				else {
					PointerBox rightBox = 
						new PointerBox(right);
					//wrapper object for pointer
					result = right.remove(element,
								rightBox,
								fromSHB);
					//update the pointer
					right = rightBox.pointer;
				}

				if (fromSHB == false) {
					if (debug){
						//debug statement
						System.err.print(TREE 
								+ treeCount
							       	+ UPDATE
								+ data.getName()
							       	+ CLOSE);
					}
					//update the height and balance
					setHeightAndBalance(pointerInParentBox);
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
		private void replaceAndRemoveMin (TNode targetTNode,
		PointerBox pointerInParentBox) {
			if (debug) {
				//debug statement
				System.err.print(TREE + treeCount + CHECK
						+ data.getName() + CLOSE);
			}
			if (left == null) {
				//debug statement
				System.err.print(TREE + treeCount + REPLACE 
						+ data.getName()
						+ CLOSE);
				targetTNode.data = this.data;
				//update targetTNode
				pointerInParentBox.pointer = right;
				//update pointer
				jettisonTNodeOnly();
				return;
			}
			else {
				PointerBox leftBox = new PointerBox(left);
				//wrapper object for left pointer
				left.replaceAndRemoveMin(targetTNode,leftBox);
				left = leftBox.pointer;
				//update the pointer
				if (debug) {
					//debug statement
					System.err.print(TREE + treeCount 
							+ UPDATE
							+ data.getName() 
							+ CLOSE);

				}
				//update height and balance
				setHeightAndBalance(pointerInParentBox);
			}		
		}

		// The PointerInParent parameter is used to pass to Remove
		// and to Insert as the way to restructure the Tree if
		// the balance goes beyond the threshold.  You'll need to
		// store the removed data in a working RTS TNode<Whatever>
		// because the memory for the current TNode<Whatever> will be
		// deallocated as a result of your call to Remove.  
		// Remember that this working TNode<Whatever> should not be part
		// of the Tree, as it will automatically be deallocated when the
		// function ends. When calling Remove, remember to tell Remove
		// that its being called from SetHeightAndBalance.
		
		/**
		 * update the height and the balance of the tree, check if 
		 * balance exceeds threshold, and resture it if does
		 *
		 * @param wrapper objecto for pointer from parent
		 *
		 * @return none
		 */
		private void setHeightAndBalance
			(PointerBox pointerInParentBox) {
			if (left == null) {
				//special case for only left side
				height = right.height + 1;
				balance = -1 - right.height;
			}
			else if (right == null) {
				//special case for only right side
				height = left.height + 1;
				balance = left.height + 1;
			}
			else if (right == null && left == null) {
				height = 0;
				balance = 0;
			}
			else {
				if (right.height > left.height) {
					height = 1 + right.height;
				}
				else {
					height = 1 + left.height;
				}
				balance = left.height - right.height;
			}
			if (Math.abs(balance) > THRESHOLD) {
				//check if it exceeds the threshold
				//remove and reinsert the element
				Whatever result = remove(data,
							pointerInParentBox,
							true);
				pointerInParentBox.pointer.insert(result,
						pointerInParentBox);			
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
			if (left != null)
				left.writeAllTNodes ();

			representation += this;

			if (right != null)
				right.writeAllTNodes ();
		}
	}
}

