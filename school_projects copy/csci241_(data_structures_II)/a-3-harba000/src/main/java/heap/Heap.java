package heap;
/*
 * Author: Alec Harb
 * Date: 3/4/2019
 * Purpose: to implement a min heap with a custom Alist class

  Note: My growIfNeeded/Rehash method is almost working but is not updating the length of buckets.
 */
import java.util.NoSuchElementException;

/** An instance is a min-heap of distinct values of type V with
 *  priorities of type P. Since it's a min-heap, the value
 *  with the smallest priority is at the root of the heap. */
public final class Heap<V, P extends Comparable<P>> {

    // TODO 1.0: Read and understand the class invariants given in the
    // following comment:

    /**
     * The contents of c represent a complete binary tree. We use square-bracket
     * shorthand to denote indexing into the AList (which is actually
     * accomplished using its get method. In the complete tree,
     * c[0] is the root; c[2i+1] is the left child of c[i] and c[2i+2] is the
     * right child of i.  If c[i] is not the root, then c[(i-1)/2] (using
     * integer division) is the parent of c[i].
     *
     * Class Invariants:
     *
     *   The tree is complete:
     *     1. `c[0..c.size()-1]` are non-null
     *
     *   The tree satisfies the heap property:
     *     2. if `c[i]` has a parent, then `c[i]`'s parent's priority
     *        is smaller than `c[i]`'s priority
     *
     *   In Phase 3, the following class invariant also must be maintained:
     *     3. The tree cannot contain duplicate *values*; note that dupliate
     *        *priorities* are still allowed.
     *     4. map contains one entry for each element of the heap, so
     *        map.size() == c.size()
     *     5. For each value v in the heap, its map entry contains in the
     *        the index of v in c. Thus: map.get(b[i]) = i.
     */
    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        public V value;
        public P priority;

        /** An Entry with value v and priority p*/
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

        public String toString() {
            return value.toString();
        }
    }

    /** Add v with priority p to the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  In Phase 3 only:
     *  @throws IllegalArgumentException if v is already in the heap.*/
    public void add(V v, P p) throws IllegalArgumentException {
        // TODO 1.1: Write this whole method. Note that bubbleUp is not implemented,
        // so calling it will have no effect. The first tests of add, using
        // test00Add, ensure that this method maintains the class invariant in
        // cases where no bubbling up is needed.
        // When done, this should pass Phase1Test::test00Add().

        Entry newEntry = new Entry(v, p);
        c.append(newEntry);

        map.put(v, c.size()-1);

        if ((map.containsKey(v)) != true) {
          throw new IllegalArgumentException();
        }

        bubbleUp(c.size()-1);

        map.size = size();

        //if map (v, size-1) != null, IllegalArgumentException
        /* if (map(v, size()-1) != null) {
          throw new IllegalArgumentException();
        } */

        // TODO 3.1: Update this method to maintain class invariants 3-5.
        //throw new UnsupportedOperationException();
    }

    /** Return the number of values in this heap.
     *  This operation takes constant time. */
    public int size() {
        return c.size();
    }

    /** Swap c[h] and c[k].
     *  precondition: h and k are >= 0 and < c.size() */
    protected void swap(int h, int k) {
        //TODO 1.2: When bubbling values up and down (later on), two values,
        // c[h] and c[k], will have to be swapped. In order to always get this right,
        // write this helper method to perform the swap.
        // When done, this should pass Phase1Test::test10Swap().
        //
        Entry tempH = c.get(h);
        Entry tempK = c.get(k);

        c.put(k, tempH);
        c.put(h, tempK);

        map.put(tempH.value, k);
        map.put(tempK.value, h);

        // TODO 3.2 Change this method to additionally maintain class
        // invariants 3-5 by updating the map field.
        //throw new UnsupportedOperationException();
    }

    /** Bubble c[k] up in heap to its right place.
     *  Precondition: Priority of every c[i] >= its parent's priority
     *                except perhaps for c[k] */
    protected void bubbleUp(int k) {
        // TODO 1.3 As you know, this method should be called within add in order
        // to bubble a value up to its proper place, based on its priority.
        // When done, this should pass Phase1Test::test15Add_BubbleUp()
        //throw new UnsupportedOperationException();

        int parent = getParent(k);

        while (c.get(k).priority.compareTo(c.get(parent).priority) < 0) {
          swap(k, parent);
          k = parent;
          parent = getParent(parent);
        }
    }

    /** Return the value of this heap with lowest priority. Do not
     *  change the heap. This operation takes constant time.
     *  @throws NoSuchElementException if the heap is empty. */
    public V peek() throws NoSuchElementException {
        // TODO 1.4: Do peek. This is an easy one.
        //         test20Peek() will not find errors if this is correct.
        if (c.size() > 0) {
          return c.get(0).value;
        } else {
          throw new NoSuchElementException();
        }

    }

    /** Remove and return the element of this heap with lowest priority.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws NoSuchElementException if the heap is empty. */
    public V poll() throws NoSuchElementException {
        // TODO 1.5: Do poll (1.5) and bubbleDown (1.6) together. When they
        // are written correctly, testing procedures
        // test30Poll_BubbleDown_NoDups and test40testDuplicatePriorities
        // should pass. The second of these checks that entries with equal
        // priority are not swapped.
        //
        if (c.size() == 0) {
          throw new NoSuchElementException();
        } else if (c.size() == 1) {
          map.remove(peek());
          return c.pop().value;
        }
        map.remove(peek());
        V value = peek();
        c.put(0, c.get(c.size()-1)); //puts the last entry of the alist into the first index

        bubbleDown(0);

        c.size--;
        map.size--;

        return value;


        // TODO 3.3: Update poll() to maintain class invariants 3-5.
      //  throw new UnsupportedOperationException();
    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: Each c[i]'s priority <= its childrens' priorities
     *                except perhaps for c[k] */
    protected void bubbleDown(int k) {
        // TODO 1.6: Do poll (1.5) and bubbleDown together.  We also suggest
        //         implementing and using smallerChild, though you don't
        //         have to.
        int smaller = smallerChild(k);

        if (smaller < 0) {
          return;
        }

        while (c.get(smaller).priority.compareTo(c.get(k).priority) < 0) {
          swap(k, smaller);
          k = smaller;
          //small = smallerChild(k);
        }
    }

    /** Return true if the value v is in the heap, false otherwise.
     *  The average case runtime is O(1).  */
    public boolean contains(V v) {
        // TODO 3.4: Use map to check wehther the value is in the heap.
        return map.containsKey(v);
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws IllegalArgumentException if v is not in the heap. */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        // TODO 3.5: Implement this method to change the priority of node in
        // the heap.
        /* if (map.containsKey(v)) {
          map.put(v, c.get());
        } else {
          throw new IllegalArgumentException();
        } */
        throw new IllegalArgumentException();
    }

    // Recommended helper method spec:
    /* Return the index of the child of k with smaller priority.
     * if only one child exists, return that child's index
     * Precondition: at least one child exists.*/
     private int smallerChild(int k) {

         int left = getLeftChild(k);
         int right = getRightChild(k);

         Entry leftEntry = c.get(left);
         Entry rightEntry = c.get(right);

         if (right < 0 || right > c.size()) {
             return left;
         } else if (leftEntry.priority.compareTo(rightEntry.priority) < 0) {//(c.get(left).priority.compareTo(c.get(right).priority) < 0) {
           return left;
         } else {
           return right;
         }
     }

    private int getParent(int k) {
      return (k-1)/2;
    }

    private int getLeftChild(int k) {
      return 2 * k;
    }

    private int getRightChild(int k) {
      return (2 * k) + 1;
    }

}
