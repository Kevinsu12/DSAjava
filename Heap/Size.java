/**
 * name:	Pengkun Su
 * pid:		A16632888
 * user:	cs12fa21ef
 * description: this file is used for reading the size of input
 */
/**
 * class:	Size
 * description: used for reading the size of input
 * fields:	of (byte b ) - return byte in bytes
 * 		of (shorts s) - resturn shorts in bytes
 * 		of (int i) - reutrn integer in bytes
 * 		of (long l ) - return long in bytes
 * 		of (long[] l) - return array in bytes
 * 		of (Object ref) - return objects in bytes
 */
public class Size {
	public static long of (byte b) { return Byte.BYTES; }
	public static long of (short s) { return Short.BYTES; }
	public static long of (int i) { return Integer.BYTES; }
	public static long of (long l) { return Long.BYTES; }
	public static long of (long[] la) { return Long.BYTES * la.length; }
	public static long of (Object ref) { return Long.BYTES; }
}
