/**
 * Name:	Pengkun Su
 * PID:		A16632888
 * USER:	cs12fa21ef
 * Description: THis is the driver file for running the program Tree,which 
 * 		implments many different classes. program terminates when user
 * 		type ^D.
 */
import java.io.*;

/**
 * Class:	UCSDStudent
 * Description: It reads input from the use for object UCSDStudent and contains
 * 		accomondate methods that can be utilize in different classes.
 * Fields:	name -  a string value of name for UCSD student
 * 		studentnum -  a long for stored data for UCSD student
 * Public Function: UCSDStudent - constructor for UCSDStudent class
 * 		    jettison - jettison the UCSDStudent object
 * 		    copy - it copies the current object
 *	            getName - return name of UCSDStudent
 *		    equals - check if two UCSDSTudent name is identical
 *		    isLessThan - check which UCSDSTudent name is "smaller"
 */
class UCSDStudent extends Base {
	
	public String name;
	public long studentnum;
	private Tracker tracker;
	/**
	 * Constructor for UCSDSTudent, initialize and allocate memory for the 
	 * object
	 *
	 * @param string value for name
	 * @param long value of StudentNum
	 * @param string value of caller for tracker
	 *
	 * @return none
	 */
	public UCSDStudent (String nm, long sn) {
		tracker = new Tracker ("UCSDStudent" + nm,
				nm.length()
				+ Size.of (sn)
				+ Size.of (tracker),
				" calling UCSDStudent Ctor");
		name = new String (nm);
		studentnum = sn;
	}
	
	/**
	 * jettison UCSDStudent
	 *
	 * @param none
	 *
	 * @return none
	 */
	public void jettison() {
		tracker.jettison();
		tracker = null;
	}
	
	/**
	 * copy the UCSD student
	 *
	 * @param none
	 *
	 * @return none
	 */
	public Base copy () {
		return new UCSDStudent(name,studentnum);
	}

	/**
	 * return the name of UCSDStudnet
	 *
	 * @param none
	 *
	 * @return none
	 */
	public String getName () {
		return name;
	}
	
	/**
	 * check if two UCSDStudent objects have identical names
	 *
	 * @param UCSDStudent for comparing
	 *
	 * @return true if they are the same, false if not
	 */
	public boolean equals (Object object) {
		if (this == object) {
			//return true if same
			return true;
		}

		if (!(object instanceof UCSDStudent)) {
			//return false for different type
				return false;
		}

		UCSDStudent otherStu = (UCSDStudent) object;

		return name.equals (otherStu.getName());
	}
	
	/**
	 * compare which UCSDStudent is "smaller"
	 *
	 * @param UCSDStudent object for comparing
	 * 
	 * @return true if current object is smaller, false if not
	 */
	public boolean isLessThan (Base base) {
		return (name.compareTo (base.getName ()) < 0) ? true : false;
	}

	public String toString () {
		return "name:  " + name + " with studentnum:  " + studentnum;
	}
}

public class Driver {
private static final short NULL = 0;
private static final int EOF = -1;

public static void main (String [] args) {

	/* initialize debug states */
	Tree.debugOff ();

	/* check command line options */
	for (int index = 0; index < args.length; ++index) {
		if (args[index].equals ("-x"))
		Tree.debugOn ();
	}


	/* The real start of the code */
	SymTab<UCSDStudent> symtab = 
		new SymTab<UCSDStudent> ("Driver");

	String buffer = null;
	int command;
	long number = 0;
	UCSDStudent stu = null;

	System.out.println ("Initial Symbol Table:\n" + symtab);

	while (true) {
		command = NULL; // reset command each time in loop
		System.out.print ("Please enter a command:\n  "
		+ "(c)heck memory, is(e)mpty, "
		+ "(i)nsert, (l)ookup, "
		+ "(r)emove, (w)rite:  ");

		command = MyLib.getchar ();
		if (command == EOF) 
			break;

		MyLib.clrbuf ((char) command); // get rid of return

		switch (command) {
			case 'c':
				Tracker.checkMemoryLeaks ();
				System.out.println ();
				break;

			case 'e': 
				if (symtab.isEmpty ()) 
					System.out.println ("Tree is empty.");
				else
					System.out.println (
						"Tree is not empty.");
				break;

			case 'i':
				System.out.print
				("Please enter UCSD student name to insert:  ");

				buffer = MyLib.getline ();// formatted input

				System.out.print
					("Please enter UCSD student number:  ");

				number = MyLib.decin ();

				// get rid of return
				MyLib.clrbuf ((char) command); 

				// create student and place in symbol table
				stu = new UCSDStudent (buffer, number);

				symtab.insert (stu);
				stu = null;
				break;

			case 'l':  
				UCSDStudent found;      // whether found or not

				System.out.print
				("Please enter UCSD student name to lookup:  ");
				buffer = MyLib.getline ();// formatted input

				stu = new UCSDStudent (buffer, 0);
				found = symtab.lookup (stu);

				if (found != null) {
					System.out.println ("Student found!!!");
					System.out.println (found);

					found.jettison ();
					found = null;
				}
				else
					System.out.println ("student " + buffer
						+ " not there!");

				stu.jettison ();
				stu = null;
				break;

			case 'r':  
				UCSDStudent removed; // data to be removed

				System.out.print
				("Please enter UCSD student name to remove:  ");

				buffer = MyLib.getline ();

				stu = new UCSDStudent (buffer, 0);

				removed = symtab.remove (stu);

				if (removed != null) {
					System.out.println (
						"Student removed!!!"); 
					System.out.println (removed);

					removed.jettison ();
					removed = null;
				}
				else
					System.out.println ("student "
						+ buffer
						+ " not there!");

				stu.jettison ();
				stu = null;
				break;

			case 'w':
				System.out.print ("The Symbol Table " +
					"contains:\n" + symtab);
			}
		}

		System.out.print ("\nFinal Symbol Table:\n" + symtab);
		symtab.jettison ();

		Tracker.checkMemoryLeaks ();
	}
}

