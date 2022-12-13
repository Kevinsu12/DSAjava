/**
 * Debugger question:
 * 1.end:Node 3
 * 2.Node 1 data:1
 * 3.Node 1 pre: 3
 * 4.Node 1 next:2
 * 5.Node 2 data:2
 * 6.Node 2 pre: 1
 * 7.Node 2 next:3
 * 8.Node 3 data:3
 * 9.Node 3 pre: 2
 * 10.NOde 3 next: 1
 */
/*
 * Name:	Pengkun Su
 * PID: 	A16632888
 * USER:	cs12fa21ov
 * File name:	List.java
 * Description:	This program uses a linked_list data straucture,take input from
 * 		the user, process the command on the list. Program ends when 
 * 		user enters ^D.
 */

import java.io.*;

public class List extends Base {

	static boolean debug = false;	// debug status
	static int listCounter = 0;	// used to number each List

	public static final int		// List controls
		END = 0,
		FRONT = 1;

	/*
	 * Class:	ListEngine
	 * Description: this class relies on the Node class. It'll be called
	 * 		for List objects. it implements commands in the
	 * 		list.
	 * Fields:	oldEnd - temporarily hold the value of end
	 * 		idx - value of index for looping
	 * Public functions: ListEngine Constructor - initialize the data
	 * 		     fields of ListEngine
	 * 		     jettisonList - stop tracking memory for everything
	 * 		     advanceNext - move the end forward by one Node
	 * 		     advancePre - move the end backward by one Node
	 * 		     checkToGoForward -  check if it's more efficeint
	 * 		     to loop forward or backward
	 * 		     isEmpty - check if the list is empty
	 * 		     insert - insert a base object into the list
	 *		     remove - remove anode from the list
	 *		     locate - locate the idea position of node for
	 *		     action
	 *		     view - return data stored at location where
	 *		     writeReverseList - write the elemnts backwards
	 */

	private class ListEngine {

		// catastrophic error messages
		static final String 
			ADNEXT_EMPTY = "Advance next from empty list!!!\n",
			ADPRE_EMPTY = "Advance pre from empty list!!!\n",
			REMOVE_EMPTY = "Remove from empty list!!!\n",
			VIEW_EMPTY = "Viewing an empty list!!!\n",
			WRITE_NONEXISTFILE 
				= "Writing to a non-existent file!!!\n";
	
		// debug messages
		static final String 
			ADNEXT = "[List %d - Advancing next]\n",
			ADPRE = "[List %d - Advancing pre]\n",
			INSERT = "[List %d - Inserting node]\n",
			REMOVE = "[List %d - Removing node]\n",
			VIEW = "[List %d - Viewing node]\n",
			LIST_ALLOCATE 
				= "[List %d has been allocated]\n",
			LIST_JETTISON
				= "[List %d has been jettisoned]\n";

		int count;		// which list is it
		Node end;		// end of the List
		long occupancy;		// how many items stored
		Base sample;		// sample object of what is stored
		Tracker tracker;	// to track memory

		/**
		 * Constructor for listEngine, it initialize every field of 
		 * listEngine
		 *
		 * @param values os sample are expected to be an empty Base
		 * 	  object of the same type that will be store in the 
		 * 	  list, or null
		 *
		 * @param values of caller and expected to be a string with a
		 * 	  class name to help debug memory issues
		 *
		 * @return none
		 */
		ListEngine (Base sample, String caller) {
			tracker = new Tracker ("ListEngine", 
				Size.of (count) 
				+ Size.of (end)
				+ Size.of (occupancy)
				+ Size.of (sample)
				+ Size.of (tracker),
				caller + " calling ListEngine Ctor");
			// ---- DO NOT CHANGE TRACKER ---- //
			listCounter ++;
			count = listCounter;
			this.sample = sample;
			occupancy = 0;
			end = null;
			
			if (debug) {
				//debug message
				System.err.print (String.format(LIST_ALLOCATE,
							count));
			}
			
		}		
		
		/**
		 * Stop tracking the memory for all nodes, data and list itself
		 *
		 * @param none
		 *
		 * @return none
		 */
		void jettisonList () {  
			if (debug) {
				System.err.print (String.format (LIST_JETTISON,
								count));
			}
			tracker.jettison();
			listCounter--;
			for (long idx = occupancy; idx > 0; idx--){
					//loop through the list
					Node oldEnd = end;
					//store the end for jettison
					advancePre();
					oldEnd.jettisonNode ();
			}
		}

		/**
		 * move the end of the list forward by one node.
		 *
		 * @param none
		 *
		 * @return none
		 */
		void advanceNext () {
			if (isEmpty()) {
				//error message
				System.err.print(ADNEXT_EMPTY);
				return;
			}

			end = end.getNext();

			if (debug) {
				//debug message
				System.err.print(String.format(ADNEXT,count));
			}
			
		}

		/**
		 * move the end of the list backward by one Node.
		 *
		 * @param none
		 *
		 * @return none
		 */
		void advancePre () {
			if (isEmpty()) {
				//error message
				System.err.print(ADPRE_EMPTY);
				return;
			}

			end = end.getPre();

			if (debug) {
				//debug message
				System.err.print(String.format(ADPRE,count));
			}
		}
		
		/**
		 * checks whether it would be more efficent to loop forward or
		 * loop backward to reach the target number
		 *
		 * @param values of where in the list to store the element
		 *
		 * @return true if go forward, false if go bakcward
		 */
		boolean checkToGoForward (long where) {
			if (occupancy / 2 >= where) {
				return true;
			}
			else {
				return false;
			}
		}
		
		/**
		 * Check wheterh the list is empty or not
		 *
		 * @param none
		 *
		 * @return none
		 */
		boolean isEmpty () {
			return occupancy == 0;
		}

		/**
		 * insert a base object into the list
		 *
		 * @param values of a base object insert to the list
		 * @param values of where are expected to store the element
		 */
		boolean insert (Base element, long where) {
			if (isEmpty()) {
				//set end if empty
				end = new Node(element);
			}
			else {
				Node oldEnd = end;//hold the end
				end = locate(where);
				end = end.insert(element);
				if (where != 0) {
					end = oldEnd;//restore the end 
				}
			}

			if (debug) {
				System.err.print(String.format(INSERT,count));
			}
			
			return true;
		}

		/** locate the Node at location where
		 *
		 * @param value of where in the list would the number be
		 *
		 * @return node before where
		 */
		Node locate (long where) {
			if (checkToGoForward(where) ){
				//check effiency of looping
				for (long idx = 0; idx < where-1; idx++) {
					//move to the node before where
					advanceNext();
				}
			}
			else {
				for (long idx = occupancy;idx >= where; idx--){
					advancePre();
				}
			}
			return end;
		}

		/**
		 * remove a node from the list
		 *
		 * @param value of where in the list of the removed elemnt
		 *
		 * @return removed data
		 */
		Base remove (long where) {
			if (isEmpty()) {
				//error message
				System.err.print(REMOVE_EMPTY);
				return null;
			}

			if (debug) {
				//debug message
				System.err.print(String.format(REMOVE,count));
			}

			Node oldEnd = end;//hold the end
			end = locate(where);

			if (where != 0) {
				//special case for 0
				advanceNext();
			}

			Base result = end.remove();

			if (where == 0) {
				end = oldEnd .getPre();//sepcial case for 0
			}
			else {
				end = oldEnd;//restore the end
			}

			return result;
		}

		/**
		 * return the data stored at location where
		 *
		 * @param values of hwhere in the list of the viewed element
		 * 
		 * @return viewd data
		 */
		Base view (long where) {
			if (isEmpty()) {
				//error message
				System.err.print(VIEW_EMPTY);
				return null;
			}

			if (debug) {
				//debug message
				System.err.print(String.format(VIEW,count));
			}

			Node oldEnd = end;//hold the end
			end = locate(where);

			if (where != 0) {
				advanceNext();//special case for 0
			}
			Base result = end.view();
			end = oldEnd;//restore the end

			return result;
		}
		
		// ListEngine WRITELIST
		void writeList (PrintStream stream) {

			if (stream == null) {
				System.err.print (WRITE_NONEXISTFILE);
				return;
			}

			// extra output if we are debugging
			if (stream == System.err) {
				stream.print ("List " 
					+ count + " has "
					+ occupancy + " items in it:\n");
			}

			// display each Node in the List
			Node oldEnd = end;  // to save prior end
			if (occupancy > 0) {
				advanceNext ();
			}
			for (long idx = 1; idx <= occupancy; idx++) {
				stream.print (" element " + idx + ": ");
				end.writeNode (stream);
				advanceNext ();
			}

			// memory tracking output if we are debugging
			if (debug) {
				System.err.print (tracker);
			}
			
			// restore end to prior value
			end = oldEnd;
		}

		/**
		 * Write the element of ListEngine backwards
		 *
		 * @param filestream stream
		 *
		 * @return none
		 */
		void writeReverseList (PrintStream stream) {
			if (stream == null) {
				System.err.print (WRITE_NONEXISTFILE);
				return;
			}

			// extra output if we are debugging
			if (stream == System.err) {
				stream.print ("List " 
					+ count + " has "
					+ occupancy + " items in it:\n");
			}

			// display each Node in the List
			Node oldEnd = end;  // to save prior end
			for (long idx = occupancy; idx >0; idx--) {
				stream.print (" element " + idx + ": ");
				end.writeNode (stream);
				advancePre ();
			}

			// memory tracking output if we are debugging
			if (debug) {
				System.err.print (tracker);
			}

			end = oldEnd;//restore the end
			

		}

		private class Node {

			/**
			 * Class:	NodeEngin
			 * Description: it's called from the Node class and it
			 * 		contains methods for commands among 
			 * 		nodes
			 * Fields: 	newNode - store the new element
			 * Public fucntions: NodeEngine - constructor method,
			 * 		     initialize all the fields
			 * 		     jettisonNodeAndData - jettison 
			 * 		     the NodeEngine and the data
			 * 		     jettisonNodeOnly - jettison the
			 * 		     NodeEngine
			 * 		     insert - create a new node object
			 * 		     and incorporated into the list
			 * 		     remove - unlins this node object
			 * 		     view - return the data stored
			 */
			private class NodeEngine {

				static final String WRITE_NONEXISTFILE 
					= "Writing to a " 
					+ "non-existent file!!!\n";

				Base data;	// the item stored
				Node next;	// to get to following item
				Node pre;	// to get to previous item
				Tracker tracker; // to track memory

				/**
				 * creates a new node to hold element
				 *
				 * @param values of a node that newly created
				 * 	  NodeEngine belongs to
				 * @param values of a base object for storing
				 * @param values of a string of constructor
				 *
				 * @return none
				 */ 
				NodeEngine (Node newNode, 
					Base element, String caller) {
					
					tracker = new Tracker ("NodeEngine", 
						Size.of (data) 
						+ Size.of (next) 
						+ Size.of (pre)
						+ Size.of (tracker),
						caller 
						+= " calling NodeEngine Ctor");
					// ---- DO NOT CHANGE TRACKER ---- //
					if (sample == null) {
						data = element;
					}
					else {
						data = sample.copy(element);
					}
						pre = newNode;
						next = newNode;
						occupancy++;
				}
							
				/**
				 * Jettison the NodeEngine and the data 
				 *
				 * @param none
				 *
				 * @return none
				 */ 
				void jettisonNodeAndData () {
					tracker.jettison();//jettison the node
					data.jettison();//jettison the data
				} 
				
				/**
				 * Jettison the NodeEngine
				 *
				 * @param none
				 *
				 * @return none
				 */
				void jettisonNodeOnly () {
					//jettison the node only	
					tracker.jettison();
				} 
				
				/**
				 * create a new node object to hold element, 
				 * which later incorporated into the list after
				 * thisNode
				 *
				 * @param value of thisNod are expected to be
				 * 	  node object that the new node is 
				 * 	  inserted after 
				 * @param values of a base object to store in
				 * 	  the new node
				 *
				 * @return new NOde that is created
				 */
				Node insert (Node thisNode, 
					Base element) {
					if (thisNode == null) {
						thisNode = new Node (element);
					}
					//create a new node
					Node newNode = new Node(element);
					//attach the node
					newNode.setPre(thisNode);
					newNode.setNext(thisNode.getNext());
					//integrate the node
					newNode.getPre().setNext(newNode);
					newNode.getNext().setPre(newNode);
						
					return newNode;
				}
				
				/**
				 *  Unlink this Node object to be removed from 
				 *  the list
				 *
				 *  @param none
				 *
				 *  @return reference to teh data is returned
				 */
				Base remove () {
					//unlink the node
					getPre().setNext(getNext());
					getNext().setPre(getPre());
					//increase the occupancy
					occupancy--;
					//jettison the node
					jettisonNodeOnly();
					return data;

				}

				/**
				 * return the data stored in the Node object
				 *
				 * @param none
				 *
				 * @return data from node object
				 */
				Base view () {
					return data;
				}

				// NodeEngine WRITENODE METHOD
				void writeNode (PrintStream stream) {
					if (stream == null) {
						System.err.print (
							WRITE_NONEXISTFILE);
						return;
					}
					stream.print (data + "\n");
				}
			}

			// -------- YOUR CODE SHOULD GO ABOVE --------
			// NOTE: 
			// READ THE CODE BELOW TO SEE WHAT METHOD YOU CAN USE
			
			static final String
				GETPRE_NONEXISTNODE
				= "Getting pre of a non-existent node!!!\n",
				GETNEXT_NONEXISTNODE
				= "Getting next of a non-existent node!!!\n",
				SETPRE_NONEXISTNODE
				= "Setting pre of a non-existent node!!!\n",
				SETNEXT_NONEXISTNODE
				= "Setting next of a non-existent node!!!\n",
				JETTISON_NONEXISTNODE 
				= "Jettisoning a non-existent node!!!\n",
				LOOKUP_NONEXISTNODE 
				= "Looking up a non-existent node!!!\n",
				INSERT_NONEXISTNODE
				= "Inserting a non-existent node!!!\n",
				REMOVE_NONEXISTNODE
				= "Removing a non-existent node!!!\n",
				VIEW_NONEXISTNODE 
				= "Viewing a non-existent node!!!\n",
				WRITE_NONEXISTNODE 
				= "Writing from a non-existent node!!!\n";

			NodeEngine nodeEngine;	// To be wrapped by a Node

			// Node CTOR METHOD 
			Node (Base element) {
				nodeEngine = new NodeEngine (
					this, element, "Node Ctor");
			}
			
			// Node GETPRE METHOD
			Node getPre () {
				if (!exist ()) {
					System.err.print (
						GETPRE_NONEXISTNODE);
					return null;
				}
				return nodeEngine.pre;
			}

			// Node GETNEXT METHOD
			Node getNext () {
				if (!exist ()) {
					System.err.print (
						GETNEXT_NONEXISTNODE);
					return null;
				}
				return nodeEngine.next;
			}
			
			// Node SETNEXT METHOD
			void setNext (Node next) {
				if (!exist ()) {
					System.err.print (
						SETNEXT_NONEXISTNODE);
					return;
				}
				nodeEngine.next = next;
			}

			void setPre (Node pre) {
				if (!exist ()) {
					System.err.print (
						SETPRE_NONEXISTNODE);
					return;
				}
				nodeEngine.pre = pre;
			}

			// Node JETTISON METHOD
			boolean jettisonNode () {
				if (!exist ()) {
					System.err.print (
						JETTISON_NONEXISTNODE);
					return false;
				}
				nodeEngine.jettisonNodeAndData ();
				nodeEngine = null;
				return true;
			} 
			
			// Node EXIST METHOD 
			boolean exist () {
				return nodeEngine != null;
			}

			// Node INSERT METHOD 
			Node insert (Base element) {
				if (!exist ()) {
					System.err.print (INSERT_NONEXISTNODE);
					return null;
				}
				return nodeEngine.insert (this, element);
			} 

			// Node REMOVE METHOD
			Base remove () {
				if (!exist ()) {
					System.err.print (REMOVE_NONEXISTNODE);
					return null;
				}
				return nodeEngine.remove ();
			}

			// Node VIEW METHOD
			Base view () {
				if (!exist ()) {
					System.err.print (
						VIEW_NONEXISTNODE);
					return null;
				}
				return nodeEngine.view ();
			}

			// Node WRITENODE METHOD
			void writeNode (PrintStream stream) {
				nodeEngine.writeNode (stream);
			}
		}
	}

	// catastrophic error messages
	static final String 
		ADNEXT_NONEXIST = "Advance next from non-existent list!!!\n",
		ADPRE_NONEXIST = "Advance pre from non-existent list!!!\n",
		EMPTY_NONEXIST = "Empyting from non-existent list!!!\n",
		ISEMPTY_NONEXIST = "Is empty check from non-existent list!!!\n",
		INSERT_NONEXIST = "Inserting to a non-existent list!!!\n",
		JETTISON_NONEXIST = "Jettisoning from non-existent list!!!\n",
		REMOVE_NONEXIST = "Removing from non-existent list!!!\n",
		OCCUPANCY_NONEXIST 
			= "Occupancy check from non-existent list!!!\n",
		VIEW_NONEXIST = "Viewing a non-existent list!!!\n",
		WRITE_NONEXISTLIST = "Writing from a non-existent list!!!\n",
		WRITE_MISSINGFUNC = "Don't know how to write out elements!!!\n";

	private ListEngine listEngine;	// The ListEngine instance
	
	public static void debugOn () {
		debug = true;
	}

	public static void debugOff () {
		debug = false;
	}

	// List CTOR METHOD
	public List (Base sample, String caller) {
		caller += "\n\tcalling List Ctor";	
		listEngine = new ListEngine (sample, caller);
	}
	
	// list JETTISON
	public void jettison () {
		jettisonList ();
	}

	// list JETTISON
	public boolean jettisonList () {
		
		if (!exist ()) {
			System.err.print (JETTISON_NONEXIST);
			return false;
		}

		listEngine.jettisonList ();
		listEngine = null;
		return true;
	}

	// List ADVANCENPRE METHOD
	public void advancePre () {
		
		if (!exist ()) {
			System.err.print (ADPRE_NONEXIST);
			return;
		}
		
		listEngine.advancePre ();
	}

	// List ADVANCENEXT METHOD
	public void advanceNext () {
		
		if (!exist ()) {
			System.err.print (ADNEXT_NONEXIST);
			return;
		}

		listEngine.advanceNext ();
	}

	// List EMPTY METHOD
	public void empty () {
		
		if (!exist ()) {
			System.err.print (EMPTY_NONEXIST);
			return;
		}
		while (!isEmpty ()) {
			listEngine.remove (0);
		}
	}

	// List EXIST METHOD
	public boolean exist () {
		
		return listEngine != null;
	}

	// List GETOCCUPANCY METHOD
	public long getOccupancy () {
		
		return listEngine.occupancy;
	}

	// List ISEMPTY METHOD
	public boolean isEmpty () {
		
		if (!exist ()) {
			System.err.print (ISEMPTY_NONEXIST);
			return false;
		}

		return listEngine.isEmpty ();
	}

	// List INSERT METHOD
	public boolean insert (Base element, long where) {
		
		if (!exist ()) {
			System.err.print (INSERT_NONEXIST);
			return false;
		}
		
		return listEngine.insert (element, where); 	
	}

	// List REMOVE METHOD
	public Base remove (long where) {
		
		if (!exist ()) {
			System.err.print (REMOVE_NONEXIST);
			return null;
		}
		
		return listEngine.remove (where);
	}

	// List TOSTRING METHOD
	public String toString () {
		writeList (System.out);
		return "";
	}
	
	// List VIEW METHOD
	public Base view (long where) {
		
		if (!exist ()) {
			System.err.print (VIEW_NONEXIST);
			return null;
		}
		
		return listEngine.view (where);
	}
	
	// List WRITELIST METHOD
	public void writeList (PrintStream stream) {
		
		if (!exist ()) {
			System.err.print (WRITE_NONEXISTLIST);
			return;
		}
		
		listEngine.writeList (stream);
	}

	// List WRITEREVERSELIST METHOD
	public void writeReverseList (PrintStream stream) {
		
		if (!exist ()) {
			System.err.print (WRITE_NONEXISTLIST);
			return;
		}
		
		listEngine.writeReverseList (stream);
	}
}

