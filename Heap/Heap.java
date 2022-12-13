/**
 * Name:	Pengkun Su
 * PID:		A16632888
 * USER:	cs12fa21ef
 * Description:	This programs utilize the Heap strucutre, store UCSD students 
 * 		info using insert, remove by utilizing a array based system. 
 * 		program terminates when user type ^D.
 */
/**
 * Class:	Heap
 * Description: It implements the binary tree structure and generic array, 
 * 		storing elemnts  with in the input size aaray. It inserts, 
 * 		removes in the binary tree with user input.
 * Fields:	result - store the result from calling remove
 * 		current - a copy of current index
 * 		idx - used to loop through arrays
 *		left - store the left child
 *		right - store the right child
 *		leftIdx - store the index of left child
 *		rightIdx - store the index of right child
 *		element - store the element on the current index
 *		element1 - store the first element for swap
 *		elemtn2 - store teh second element for swap
 * Public Function: debugOn - turns on debugging for this heap
 * 		    debugOff - turns off debugging for this heap
 * 		    debugToggle - swithch the status of debug
 * 		    Heap - constructor for heap, initialize and allocate
 * 		    jettison - jettison all elemnt and the heap
 * 		    insert - insert the element
 * 		    isEmpty - check if heap is empty
 * 		    isFull - check if heap is full
 * 		    remove - remove the first element
 * 		    reHeapUp - adjust the element to the right order from 
 * 		    		bottome to top
 * 		    reHeapDown - adjust the elements to the right order from
 * 		    		top to bottomw
 * 		    elementSwap - switch two elements in the array
 * 		    getParent - return the parent of the current element
 * 		    getParentIdx - return the index of parent
 * 		    getRight - return the right child
 * 		    getLeft - return the left child
 * 		    getRightIdx - return the index of right child
 * 		    getLeftIdx - return the index of left child
 * 		    toString - a string repsentations of this class
 *
 */
public class Heap <Whatever extends Base> {

	//data fields
	private long occupancy;// number of elements in heap
	private Tracker tracker;//track memeory
	private Base[] list; //generic for data storing
	private int size;//size of the array
	private String representation;//string representation of the class
	private static boolean debug;//control debug statemetn
	private static final int TWO = 2;//avoid magic number
	private int index;//index of the last used array space

	/**
	 * constructor for heap,intialize and allocate
	 *
	 * @param size of the array
	 * @param caller for tracker
	 *
	 */
	public Heap (int sz,String caller) {
		tracker = new Tracker ("Heap",Size.of (index)
			+ Size.of (occupancy)
			+ Size.of (list)
			+ Size.of (size)
			+ Size.of (representation)
			+ Size.of (tracker),
			caller + "calling Heap CTor");
		if (debug) {
			//debug statement
			System.err.print("[Heap - Allocated]\n");
		}
		occupancy = 0;
		size = sz;
		list = new Base[size];	//initalize array
		index = -1;
	}

	/**
	 * turn off debug message
	 *
	 */	
	public static void debugOff() {
		debug = false;
	}	
	
	/**
	 * turn on debug message
	 *
	 */
	public static void debugOn() {
		debug = true;
	}
	
	/**
	 * switch the debug message
	 *
	 */
	public static void debugToggle() {
		if (debug == false) {//check the current debug status
			debug = true;
		}
		else {
			debug = false;			
		}
	}
	
	/**
	 * Check if Heap is empty
	 *
	 * @param none
	 *
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return occupancy == 0;
	}
	
	/**
	 * Check if Heap is full
	 *
	 * @param none
	 *
	 * @return true if full, false if not
	 */
	public boolean isFull() {
		//full if the last element on array is filled
		return  list[size-1] != null;
	}
	/**
	 * jettison the tree and all the TNodes
	 *
	 * @param none
	 *
	 * @return none
	 */
	public void jettison () {
		for (int idx = 0; idx < size; idx++) {
			//loop through the array
			if (list[idx] != null) {
				list[idx].jettison();
			}
		}
		tracker.jettison();//jettison the heap
	}	
	/**
	 * insert the element by calling TNode insert
	 *
	 * @param complete element to be inserted
	 *
	 * @return true if insert sucessfull, false if not
	 */
	public boolean insert (Whatever element) {
		if (debug) {
			//debug statement
			System.err.print("[Heap - inserting " 
					+ element.getName() +"]\n");
		}
		occupancy++;
		index++;//increase index
		list[index] = element;
		reHeapUp(element);//restructure the heap
		return true;
	}
	
	/**
	 * get the parent of the element
	 *
	 * @param the index location of the element we want find parent for
	 *
	 * @return return the parent element
	 */
	public Whatever getParent(int current) {
		//calculate parent index
		return (Whatever) list[ (current - 1) / TWO];
	}

	/**
	 * get the index of the parent element
	 *
	 * @param the index location of the element we want find parent for
	 *
	 * @return the index of the parent element
	 */
	public int getParentIdx(int current) {
		return (current - 1) / TWO;
	}

	/**
	 * swap the two elements in the array
	 *
	 * @param the index of the element we want to swap
	 * @param the index of the element we want to swap
	 *
	 * @return none
	 */
	public void elementSwap (int first, int second) {
		Whatever element1 = (Whatever) list[first];
		//store the fist element
		Whatever element2 = (Whatever) list[second];
		//store the second element
		if (debug) {
			//debug statement
			System.err.print("[Heap - swapping"
					+ element1.getName()
					+ " and "
					+ element2.getName()
					+ "]\n");
		}
		//switch element
		list[first] = element2;
		list[second] = element1;
	}

	/**
	 * fix the location of element based on their priority
	 *
	 * @param the element that was inserted
	 *
	 * @return none
	 */
	public void reHeapUp (Whatever element) {
		int current = index;//store the current index
		Whatever parent = null;
		while (true) {
			parent = getParent(current);
			if (debug) {
				//debug statement
			       System.err.print("[Heap - comparing "
						+ element.getName()
				 		+ " and " + parent.getName()
						+ "]\n");
			}	       
			if (element.isLessThan(parent)) {
				//switch  parent is less imporatnat
				int parentIdx = getParentIdx(current);
				elementSwap(current,parentIdx);
				current = parentIdx;//update the current index

			}
			else {
				break;
			}
		}	

	}

	/** 
	 * remove the first element
	 *
	 * @param none
	 *
	 * @return true if remove successfuly false if not
	 */
	public Whatever remove () {
		if (isEmpty()){
			//return null if empty
			return null;
			}

		Whatever result = (Whatever) list[0];//store the removed 

		if(debug) {
			//debug stetement
			System.err.print("[Heap - removing "
					+ result.getName()
					+ "]\n");
		}
		occupancy--;
		list[0].jettison();//jettison the removed element
		if (size >  1 && list[1] != null) {
			list[0] = list[index];//replace with the last element
			list[index] = null;
			index--;
			reHeapDown();//restructure the heap
		}
		else {
			//special case for size one array
			list[0] = null;
			index--;
		}
		return result;
	}
	
	/**
	 * return the left child
	 *
	 * @param index of the parent
	 *
	 * @return the value of left child
	 */
	public Whatever getLeft(int current){
		return (Whatever) list[getLeftIdx(current)];
	}

	/**
	 * return the index of left child
	 *
	 * @param index of parent
	 *
	 * @return the index of left child
	 */
	public int getLeftIdx(int current){
		if ((current * TWO + 1) > (index)) {
			//special case for index bigger than occuapncy
			return current;
		}
		else {
			return current * TWO + 1;
		}
	}

	/**
	 * return the right child
	 *
	 * @param index of parent
	 *
	 * @return the value of right child
	 */
	public Whatever getRight(int current){
		return (Whatever) list[getRightIdx(current)];
	}

	/**
	 * return the right child's index
	 *
	 * @param index of parent
	 *
	 * @return index of right child
	 */
	public int getRightIdx(int current) {
		if ((current * TWO + TWO) > (index)) {
			//special case for index bigger than occupancy
			return current;
		}
		else {
			return current * TWO + TWO;
		}
	
	}
	
	/**
	 * reset the order of the array from up to down becasue of removing
	 *
	 * @param none
	 *
	 * @return none
	 */
	public void reHeapDown() {
		int current = 0;
		while (true){
			Whatever element = (Whatever) list[current];
			//get the current element for comparing
			Whatever left = getLeft(current);
			//get the left of the element
			Whatever right = getRight(current);
			//get the right of the element
			int leftIdx = getLeftIdx(current);
			//reutrn the left index
			int rightIdx = getRightIdx(current);
			// return the right index
			if (debug) {
				System.err.print("[Heap - comparing "
						+ element.getName()
						+ " and its child/children"
						+ " ]\n");
			}
			if (left.isLessThan(element)
			&& right.isLessThan(element)) {
				if (debug) {
					System.err.print("[Heap - comparing "
							+ left.getName()
							+ " and "
							+ right.getName()
							+ " ]\n");
				}
				//if both children are more important
				if (left.isLessThan(right)){
					elementSwap(current,leftIdx);
					current = leftIdx;//update current
					}
				else {
					elementSwap(current,rightIdx);
					current = rightIdx;//update current
				}
			}
			else if (left.isLessThan(element)) {
				//only the left
				elementSwap(current,leftIdx);
				current = leftIdx;//update current
			}
			else if (right.isLessThan(element)) {
				//only the right
				elementSwap(current,rightIdx);
				current = rightIdx;//update current
			}
			else {
				break;
			}
		}
	}
	/**
	 * create the representation of the current class to over ride
	 * the defautl toString method
	 */	
	public String toString() {
		representation = "The Heap has " + occupancy + " items:\n";
		for (int idx = 0; idx < size; idx++) {
			if(list[idx] != null) {
				representation += "at index " + idx +": ";
				representation += list[idx] + "\n";
			}
		}
		return representation;
	}
}


