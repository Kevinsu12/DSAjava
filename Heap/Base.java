/**
 * name:	pengkun su
 * pid:		a16632888
 * user:	cs12fa21ef
 * description: this is the file for creating the generic class that utilized
 * 		in heap and UCSDstudent class.
 * 		
 */

/**
 * class:	Base
 * description: it creates the generic container used later in heap and 
 * 		UCSDStudent class.
 * fields:	none
 * public function:	copy - empty generic method
 * 			getName - empty generic method
 *			isLessThan - empty generic method
 *			jettison - empty generic method
 */
public abstract class Base {

        public Base copy () {
                return null;
        }
        public String getName () {
                return null;
        }
        public boolean isLessThan (Base base) {
                return true;
        }
	public void jettison () {};
}
