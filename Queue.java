/* Queue.java
 *
 *  Version
 *  $Id$
 *
 *  Revisions:
 * 		$Log$
 *
 */

import java.util.Vector;

public class Queue {
    private Vector vector;

    /** Queue()
     *
     * creates a new queue
     */
    public Queue() {
        vector = new Vector();
    }

    public Object next() {
        return vector.remove(0);
    }

    public void add(final Object obj) {
        vector.addElement(obj);
    }

    public boolean hasMoreElements() {
        return !vector.isEmpty();
    }

    public Vector asVector() {
        return vector;
    }

}
