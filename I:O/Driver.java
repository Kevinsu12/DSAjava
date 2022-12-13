/**
 * name:	pengkun su
 * pid:		a16632888
 * user:	cs12fa21ef
 * description: this is the driver file for running the program tree,which 
 * 		implments many different classes. program terminates when user
 * 		type ^d.
 */
import java.io.*;

/**
 * class:	ucsdstudent
 * description: it reads input from the use for object ucsdstudent and contains
 * 		accomondate methods that can be utilize in different classes.
 * fields:	name -  a string value of name for ucsd student
 * 		studentnum -  a long for stored data for ucsd student
 * public function: ucsdstudent - constructor for ucsdstudent class
 * 		    jettison - jettison the ucsdstudent object
 * 		    copy - it copies the current object
 *	            getname - return name of ucsdstudent
 *		    equals - check if two ucsdstudent name is identical
 *		    islessthan - check which ucsdstudent name is "smaller"
 *		    getTrimName - return trim name of ucsdstudent
 *		    read - reading from the disk
 *		    write - write to the disk
 */

class UCSDStudent extends Base {

	private String name;
	private long studentNum;
	private Tracker tracker;
	private static long counter = 0;
	private long count;

	/*
	 * Default constructor for the UCSDStudent object.
	 * Tracks the memory associated with the UCSDStudent object.
	 */
	public UCSDStudent () {
		tracker = new Tracker ("UCSDStudent " + count + " " + name,
		Size.of (name) 
		+ Size.of (studentNum)
		+ Size.of (count)
		+ Size.of (tracker),
		"UCSDStudent Ctor");

		count = ++counter;
		name = String.format ("%1$-14s", "default");
	}

	/*
	 * Constructor for the UCSDStudent object given a name and student
	 * number. Tracks the memory associated with the UCSDStudent.
	 *
	 * @param nm the name of the UCSDStudent being created
	 * @param sn the student number of the UCSDStudent being created
	 */
	public UCSDStudent (String nm, long sn) {
		tracker = new Tracker ("UCSDStudent " + count + " " + nm,
		nm.length ()
		+ Size.of (studentNum)
		+ Size.of (count)
		+ Size.of (tracker),
		"UCSDStudent Ctor");

		count = ++counter;
		name = String.format ("%1$-14s", nm);
		studentNum = sn;
	}

	// TODO: YOUR UCSDSTUDENT METHODS GO HERE
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
		return new UCSDStudent(name,studentNum);
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
	 * return the trim name
	 *
	 * @param none
	 *
	 * @return none
	 */
	 public String getTrimName() {
		 return  name.trim();
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
	
	/**
	 * reading from the disk
	 *
	 * @param an access file of the disk
	 *
	 * @return none
	 */
	public void read (RandomAccessFile fio) {
		try {
			name = fio.readUTF();
			studentNum = fio.readLong();	
		}
		catch (IOException ioe) {
			System.err.println ("IOException in UCSDStudent Read");
		}
	}

	/**
	 * writing to the disk
	 *
	 * @param an acess for of the disk
	 *
	 * @return none
	 */
	public void write (RandomAccessFile fio) {
		try {
			fio.writeUTF(name);
			fio.writeLong(studentNum);
		}
		catch (IOException ioe) {
			System.err.println("IOException in Variable Write");
		}	
	}

	public String toString () {
		if (Tree.getDebug ())
			return "UCSDStudent #" + count + ":  name:  " 
				+ name.trim () + "  studentnum:  " + studentNum;

		return "name:  " + name.trim () + "  studentnum:  "
			+ studentNum;
	}
}

/**
 * class:	Driver
 * description: responsible for different cases and running and compliling all
 * 		 the programs and dealing with EOF.
 * Fields: 	NULL - assign null value to 0
 * 		FILE - assign file value to 0
 * 		KEYBOARD - assign KEYBOARD to 1
 * 		EOF - assign EOF value to -1
 * public function:	main - use different cases and runnign all 
 * 			the programs and dealing with EOF
 */
public class Driver {
	private static final int
		NULL = 0,
		FILE = 0,
		KEYBOARD = 1,
		EOF = -1;

	public static void main (String [] args) {

		/* initialize debug states */
		Tree.debugOff ();

		/* check command line options */
		for (int index = 0; index < args.length; ++index) {
			if (args[index].equals ("-x"))
				Tree.debugOn ();
		}

		UCSDStudent sample = new UCSDStudent ();
		/* The real start of the code */
		SymTab<UCSDStudent> symtab = 
			new SymTab<UCSDStudent> ("Driver.datafile",
						sample, "Driver");

		String buffer = null;
		int command;
		long number = 0;
		UCSDStudent stu = null;

		Writer os = new FlushingPrintWriter (System.out, true);
		Reader is = new InputStreamReader (System.in);
		int readingFrom = KEYBOARD;

		System.out.println ("Initial Symbol Table:\n" + symtab);

		// SUGGESTED TEST STUDENT NUMBERS FOR VIEWING IN OCTAL DUMPS
		// 255, 32767, 65535, 8388607, 16777215
		// FF	7FFF	FFFF	7FFFFF	FFFFFF
		while (true) {
		try {
			command = NULL; // reset command each time in loop
			os.write ("Please enter a command ((c)heck memory, "
				+ "(f)ile, (i)nsert, (l)ookup, (r)emove, "
				+ "(w)rite):  ");
			command = MyLib.getchar (is);

			if (command == EOF) {
				if (readingFrom == KEYBOARD) {
					break;
					//stop the program
				}
				else {
					is = new InputStreamReader(System.in);
					os = new FlushingPrintWriter 
						(System.out, true);
					readingFrom = KEYBOARD;
				}
			}

			if (command != EOF)
				MyLib.clrbuf ((char) command, is);

			switch (command) {
			case 'c':
				Tracker.checkMemoryLeaks ();
				System.out.println();

				break;

			case 'f':
				// TODO: YOUR CODE GOES HERE
				os.write
				("Please enter file name for commands:  ");
				String filename = MyLib.getline(is);
				is = new FileReader(filename);
				//set the input stream
				os = new FlushingFileWriter("/dev/null");
				//set the output stream
				readingFrom = FILE;

				break;
				

			case 'i':
				os.write
				("Please enter UCSD student name to insert:  ");

				buffer = MyLib.getline (is);

				os.write
					("Please enter UCSD student number:  ");

				number = MyLib.decin (is);
				MyLib.clrbuf ((char) command, is);

				// create student and place in symtab
				stu = new UCSDStudent (buffer, number);
				symtab.insert (stu);

				break;

			case 'l': 
				UCSDStudent found;

				os.write
					("Please enter UCSD student name to "
					+ "lookup:  ");
				buffer = MyLib.getline (is);

				stu = new UCSDStudent (buffer, 0);
				found = symtab.lookup (stu);
				stu.jettison ();
				stu = null;
					
				if (found != null) {
					System.out.println 
						("Student found!!!\n");
					System.out.println (found);

					found.jettison ();
					found = null;
				}
				else
					System.out.println 
						("student " + buffer 
						+ " not there!");
					
					break;

			case 'r':
				// data to be removed 	
				UCSDStudent removed;

				os.write 
				("Please enter UCSD student name to remove:  ");

				buffer = MyLib.getline (is);

				stu = new UCSDStudent (buffer, 0);
				removed = symtab.remove (stu);

				stu.jettison ();
				stu = null;

				if (removed != null) {
					System.out.println ("Student "
								+ "removed!!!"); 
					System.out.println (removed);

					removed.jettison ();
					removed = null;
				}
				else
					System.out.println 
					("student " + buffer + " not there!");

				break;

			case 'w':
				System.out.print ("The Symbol Table " +
				"contains:\n" + symtab);
				break;
			}
		}
		catch (IOException ioe) {
			System.err.println ("IOException in Driver main");
		}
		}


		System.out.print ("\nFinal Symbol Table:\n" + symtab);

		if (symtab.getOperation () != 0){
			System.out.print ("\nCost of operations:    ");
			System.out.print (symtab.getCost());
			System.out.print (" tree accesses");

			System.out.print ("\nNumber of operations:  ");
			System.out.print (symtab.getOperation());

			System.out.print ("\nAverage cost:          ");
			System.out.print (((float) (symtab.getCost ())) 
					  / (symtab.getOperation ()));
			System.out.print (" tree accesses/operation\n");
		}
		else{
			System.out.print ("\nNo cost information available.\n");
		}
		symtab.jettison ();
		sample.jettison ();
		Tracker.checkMemoryLeaks ();
	}
}
