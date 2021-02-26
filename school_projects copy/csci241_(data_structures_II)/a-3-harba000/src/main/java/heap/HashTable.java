/*
*Author: Alec Harb
* Date: 3/4/2019
* Purpose: to implement a hashtable
*/

package heap;
import java.lang.Math;

/** A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8. */
public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /** returns (k, v) String representation of the pair */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
        // TODO 2.1 - do this together with put.
        int mod = Math.abs(key.hashCode());
        int index = mod % getCapacity();

        Pair nextChain = buckets[index];

        while (nextChain != null) {
          if (nextChain.key.equals(key)) {
            return nextChain.value;
          } else {
            nextChain = nextChain.next;
          }
        }

        return null;
     }

    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Runtime: average case O(1); worst case O(size^2 + a.length)*/
    public V put(K key, V val) {
        // TODO 2.2
        //   do this together with get. For now, don't worry about growing the
        //   array and rehashing.
        //   Tips:
        //     - Use the key's hashCode method to find which bucket it belongs in.
        //     - It's possible for hashCode to return a negative integer.
        //
        int mod = Math.abs(key.hashCode());
        int index = mod % getCapacity();

        if (get(key) != null) {
          V oldKey = get(key);
          Pair nextChain = buckets[index];

          while (nextChain != null) {
            if (nextChain.key.equals(key)) {
              nextChain.value = val;
              return oldKey;
            } else {
              nextChain = nextChain.next;
            }
          }
        }

        Pair newPair = new Pair(key, val, null);

        if (buckets[index] == null) {
            buckets[index] = newPair;
            size++;
            return null;
        } else {
            Pair findEnd = buckets[index];
            while (findEnd.next != null) {
                findEnd = findEnd.next;
            }
            findEnd.next = newPair;
        }

        size++;
        return newPair.value;

        // TODO 2.5 - modify this method to grow and rehash if the load factor
        //            exceeds 0.8.

    }

    /** Return true if this map contains a mapping for the specified key.
     *  Runtime: average case O(1); worst case O(size) */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /** Remove the mapping for the specified key from this map if present.
     *  Return the previous value associated with key, or null if there was no
     *  mapping for key.
     *  Runtime: average case O(1); worst case O(size)*/
    public V remove(K key) {
      int mod = Math.abs(key.hashCode());
      int index = mod % getCapacity();

      Pair chain = buckets[index];
      V old = null;
      while (chain != null) {
        if (chain.key.equals(key)){
          old = chain.value;
          if (chain.next != null) {
            if (chain.next.next != null) {
              chain.next = chain.next.next;
            } else {
              chain.next.next = null;
            }
          }
          size--;
          return old;
        } else {
          chain = chain.next;
        }
      }
        return null;
    }


    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the array size
     * (capacity) and rehash values from the old array to the new array */
    private void growIfNeeded() {
      double load = (double) getSize() / getCapacity();

      if (load > 0.8) {
        Pair[] oldBucket = buckets;
        buckets = createBucketArray(getCapacity() * 2);

        for (int i = 0; i < oldBucket.length; i++) {
          while (oldBucket[i] != null) {
            put(oldBucket[i].key, oldBucket[i].value);
            oldBucket[i] = oldBucket[i].next;
          }
        }
      }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;
            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
