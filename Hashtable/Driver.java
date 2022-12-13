import java.io.*;

class UCSDStudent extends Base {
	private String name;
	private long studentNum;
	private Tracker tracker;

	public UCSDStudent (String nm, long sn) {

		name = nm;
		studentNum = sn;
		
		// DO NOT CHANGE THIS PART
		tracker = new Tracker ("UCSDStudent", 
				Size.of (name)
				+ Size.of (studentNum)
				+ Size.of (tracker),
				" calling UCSDStudent Ctor");

	}

	public void jettison () {
		tracker.jettison ();
		tracker = null;
	}

	public boolean equals (Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof UCSDStudent)) {
			return false;
		}
		UCSDStudent otherStu = (UCSDStudent) object;

		return name.equals (otherStu.getName ());			
	}

	public String getName () {
		return name;
	}

	public int hashCode () {
		int retval = 0;
		int index = 0;

		while (index != name.length()) {
			retval += name.charAt (index);
			index++;
		}
		return retval;
	}

	public boolean isLessThan (Base bbb) {
		return (name.compareTo (bbb.getName()) < 0) ? true : false;
	}

	public String toString () {
		return "name: " + name + " with studentNum: " + studentNum ;
	}
}

public class Driver {
	private static final int EOF = -1;
	private static final int HASH_TABLE_SIZE = 5;

	public static void main (String [] args) {

		/* initialize debug states */
		HashTable.debugOff();

		/* check command line options */
		for (int index = 0; index < args.length; ++index) {
			if (args[index].equals("-x"))
				HashTable.debugOn();
		}

		/* The real start of the code */
		SymTab symtab = new SymTab (HASH_TABLE_SIZE, "Driver");
		String buffer = null;
		int command;
		long number = 0;

		System.out.print ("Initial Symbol Table:\n" + symtab);

		while (true) {
			command = 0;    // reset command each time in loop
			System.out.print ("Please enter a command:\n"
					 + "(c)heck memory, "
					 + "(i)nsert, (l)ookup, "
					 + "(o)ccupancy, (w)rite:  ");

			command = MyLib.getchar ();
			if (command == EOF) 
				break;
			MyLib.clrbuf ((char) command); // get rid of return

			switch (command) {			

			case 'c':	// check memory leaks
				Tracker.checkMemoryLeaks ();
				System.out.println ();
				break;

			case 'i':
				System.out.print (
				"Please enter UCSD Student name to insert:  ");
				buffer = MyLib.getline ();// formatted input

				System.out.print (
					"Please enter UCSD Student number:  ");

				number = MyLib.decin ();

				// remove extra char if there is any
				MyLib.clrbuf ((char) command);

				// create Student and place in symbol table
				if(!symtab.insert (
					new UCSDStudent (buffer, number))) {

					System.out.println ("Couldn't insert " 
							    + "student!!!"); 
				}
				break;

			case 'l':
				Base found;     // whether found or not

				System.out.print (
				"Please enter UCSD Student name to lookup:  ");

				buffer = MyLib.getline ();// formatted input

				UCSDStudent stu = new UCSDStudent (buffer, 0);
				found = symtab.lookup (stu);

				if (found != null) {
					System.out.println ("Student found!!!");
					System.out.println (found);
				}
				else
					System.out.println ("Student " 
						+ buffer
						+ " not there!");

				stu.jettison ();
				break;

			case 'o':	// occupancy
				System.out.println ("The occupancy of"
						    + " the hash table is "
						    + symtab.getOccupancy ());
				break;

			case 'w':
				System.out.print (
				"The Symbol Table contains:\n" + symtab);
			}
		}

		/* DON'T CHANGE THE CODE BELOW THIS LINE */
		System.out.print ("\nFinal Symbol Table:\n" + symtab);

		symtab.jettison ();
		Tracker.checkMemoryLeaks ();
	}
}
