/**
 * name:	pengkun su
 * pid:		a16632888
 * user:	cs12fa21ef
 * description: this is the file for calling and implementing generic class
 * 		that extends from base.
 */
/**
 * class:	SymTab
 * description: calling and implementing heap class
 * fields:	none
 * public function: SymTab - constructor for SymTab calls
 */
public class SymTab<Whatever extends Base> extends Heap<Whatever> {
    /**
     * initialize the SymTab with the constructor
     *
     * @param the size of the array in Heap class
     * @param caller for tracker
     */
    public SymTab (int sz,String caller) {
        super (sz,caller);
    }
}
