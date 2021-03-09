/**
 * RaggedArrayList.java
 * ****************************************************************************
 * revision history
 * ****************************************************************************
 * 3.2020 -- Caleb Tracey and Jon Weiss implemented iterator, moveToNext, 
 *           subList, and toArray methods
 * 2.2020 -- Caleb Tracey implemented the add method
 * 2.2020 -- Caleb Tracey and Nick Largey implemented the findFront and FindEnd
 *           methods
 * 9.25.2020 -- AA cleaning up documentation again
 * 8/2015 - Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 * ****************************************************************************
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *
 * It keeps the items in sorted order according to the comparator. Duplicates
 * are allowed. New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should all
 * be private, however they have been made public so that the tester code can
 * set them
 *
 * @param <E> A generic data type so that this structure can be built with any
 * data type (object)
 */
package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class RaggedArrayList<E> implements Iterable<E> {

    // must be even so when split get two equal pieces
    private static final int MINIMUM_SIZE = 4;
    /**
     * The total number of elements in the entire RaggedArrayList
     */
    public int size;
    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;
    /**
     * The number of elements in the l1Array that are used.
     */
    public int l1NumUsed;
    /**
     * a Comparator object so we can use compare for Song
     */
    private Comparator<E> comp;

    /**
     * create an empty list always have at least 1 second level array even if
     * empty, makes code easier (DONE - do not change)
     *
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        // you can't create an array of a generic type
        l1Array = new Object[MINIMUM_SIZE];
        // first 2nd level array
        l1Array[0] = new L2Array(MINIMUM_SIZE);
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * ***********************************************************
     * nested class for 2nd level arrays (DONE - do not change)
     */
    public class L2Array {

        /**
         * the array of items
         */
        public E[] items;
        /**
         * number of items in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         *
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
            // you can't create an array of a generic type
            items = (E[]) new Object[capacity];
            numUsed = 0;
        }
    }// end of nested class L2Array

    // ***********************************************************
    /**
     * total size (number of entries) in the entire data structure (DONE - do
     * not change)
     *
     * @return total size of the data structure
     */
    public int size() {
        return size;
    }

    /**
     * null out all references so garbage collector can grab them but keep
     * otherwise empty l1Array and 1st L2Array (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null);
        l2Array.numUsed = 0;
    }

    /**
     * *********************************************************
     * nested class for a list position used only internally 2 parts: level 1
     * index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
        public int level1Index;

        /**
         * Level 2 index
         */
        public int level2Index;

        /**
         * Parameterized constructor DONE (Do Not Change)
         *
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
            this.level1Index = level1Index;
            this.level2Index = level2Index;
        }

        /**
         * test if two ListLoc's are to the same location (done -- do not
         * change)
         *
         * @param otherObj the other listLoc
         * @return true if they are the same location and false otherwise
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;
            return level1Index == other.level1Index
                    && level2Index == other.level2Index;
        }

        /**
         * move ListLoc to next entry when it moves past the very last entry it
         * will be one index past the last value in the used level 2 array. Can
         * be used internally to scan through the array for sublist also can be
         * used to implement the iterator.
         */
        public void moveToNext() {
            L2Array l2Array = (L2Array) l1Array[this.level1Index];
            if (this.level1Index < l1NumUsed - 1) {
                if (this.level2Index < l2Array.numUsed - 1) {
                    this.level2Index++;
                } else if (this.level2Index == l2Array.numUsed - 1) {
                    this.level1Index++;
                    this.level2Index = 0;
                }
            } else if (this.level1Index == l1NumUsed - 1) {
                if (this.level2Index < l2Array.numUsed - 1) {
                    this.level2Index++;
                } else if (this.level2Index == l2Array.numUsed - 1) {
                    this.level2Index = l2Array.numUsed + 1;
                }
            }
        }
    }

    /**
     * find 1st matching entry
     *
     * @param item the thing we are searching for a place to put.
     * @return a ListLoc object - ListLoc of 1st matching item or of 1st item
     * greater than the item if no match this might be an unused slot at the end
     * of a level 2 array
     */
    public ListLoc findFront(E item) {
        if (size == 0) {
            return new ListLoc(0, 0);
        }
        int i = 0;
        L2Array l2Array = (L2Array) l1Array[i];
        while (i < l1NumUsed - 1 && comp.compare(item,
                l2Array.items[l2Array.numUsed - 1]) > 0) {
            i++;
            l2Array = (L2Array) l1Array[i];
        }
        int l2index = 0;
        while (l2index <= l2Array.numUsed - 1
                && comp.compare(item, l2Array.items[l2index]) > 0) {
            l2index++;
        }
        return new ListLoc(i, l2index);
    }

    /**
     * find location after the last matching entry or if no match, it finds the
     * index of the next larger item this is the position to add a new entry
     * this might be an unused slot at the end of a level 2 array
     *
     * @param item the thing we are searching for a place to put.
     * @return a ListLoc object - the location where this item should go
     */
    public ListLoc findEnd(E item) {
        if (size == 0) {
            return new ListLoc(0, 0);
        }
        int i = l1NumUsed - 1;
        L2Array l2Array = (L2Array) l1Array[i];
        while (i > 0 && comp.compare(item, l2Array.items[0]) < 0) {
            i--;
            l2Array = (L2Array) l1Array[i];
        }
        int l2index = l2Array.numUsed - 1;
        while (l2index >= 0
                && comp.compare(item, l2Array.items[l2index]) < 0) {
            l2index--;
        }

        return new ListLoc(i, l2index + 1);
    }

    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     *
     * @param item the thing we are searching for a place to put.
     * @return
     */
    public boolean add(E item) {
        // use find end method to find where to add item
        ListLoc addItem = this.findEnd(item);
        int l1Index = addItem.level1Index;
        int l2Index = addItem.level2Index;
        L2Array l2Array = (L2Array) l1Array[l1Index];
        // convert l1 and l2 arrays to ArrayLists for easier inserting
        ArrayList<Object> l1ArrayList
                = new ArrayList<>(Arrays.asList(l1Array));
        ArrayList<E> l2ArrayList
                = new ArrayList<>(Arrays.asList(l2Array.items));
        l2ArrayList.add(l2Index, item);
        // set l2Array values to the same as l2ArrayList while retaining length
        System.arraycopy(l2ArrayList.toArray(), 0, l2Array.items, 0,
                l2Array.items.length);
        l2Array.numUsed++;
        this.size++; // update size  
        // check to see if l2Array is full
        if (l2Array.numUsed == l2Array.items.length) {
            // if so, and the l2Array length is smaller than l1Array length,
            // double l2's size and set the empty indexes to null
            if (l2Array.items.length < l1Array.length) {
                l2Array.items = Arrays.copyOf(l2Array.items,
                        l2Array.items.length * 2);
                Arrays.fill(l2Array.items, l2Array.numUsed,
                        l2Array.items.length, null);
                l1Array[l1Index] = l2Array;
                // if l1Array has a greater or equal size...
            } else if (l2Array.items.length >= l1Array.length) {
                // create a new L2Array of the same size
                L2Array splitL2Array = new L2Array(l2Array.items.length);
                int half = l2Array.numUsed / 2;
                // copy the second half of elements from l2Array into new one
                System.arraycopy(l2Array.items, half,
                        splitL2Array.items, 0, half);
                splitL2Array.numUsed = l2Array.numUsed - half; // set numUsed
                Arrays.fill(l2Array.items, half, // set empty indexes to null
                        l2Array.numUsed, null);
                l2Array.numUsed = l2Array.numUsed - half; //set numUsed
                // add / insert new L2Arrays into l1ArrayList
                l1ArrayList.set(l1Index, l2Array);
                l1ArrayList.add(l1Index + 1, splitL2Array);
                // update l1Array's values while retaining length
                System.arraycopy(l1ArrayList.toArray(), 0, l1Array, 0,
                        l1Array.length);
                l1NumUsed++;
                // check to see if l1Array is full
                if (l1NumUsed == l1Array.length) {
                    // if so double size
                    l1Array = Arrays.copyOf(l1Array,
                            l1Array.length * 2);
                    Arrays.fill(l1Array, l1NumUsed,
                            l1Array.length, null);
                }

            }
        }
        return true;
    }

    /**
     * check if list contains a match
     *
     * @param item the thing we are looking for.
     * @return true if the item is already in the data structure
     */
    public boolean contains(E item) {
        if (item != null) {
            ListLoc itemLoc = this.findFront(item);
            L2Array l2Array = (L2Array) l1Array[itemLoc.level1Index];
            return comp.compare(item, l2Array.items[itemLoc.level2Index]) == 0;
        } else {
            return false;
        }
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {
        int i = 0;
        Iterator<E> itr = this.iterator();
        while (itr.hasNext()) {
            a[i] = itr.next();
            i++;
        }
        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive. The original list is
     * unaffected findStart and findEnd will be useful here
     *
     * @param fromElement the starting element
     * @param toElement the element after the last element we actually want
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        RaggedArrayList<E> result = new RaggedArrayList<>(comp);
        ListLoc beginningElement = new ListLoc(0, 0);
        ListLoc lastElement = new ListLoc(0, 0);
        ListLoc currentLocation = new ListLoc(0, 0);
        E currentItem;

        beginningElement = findFront(fromElement); // location of the beginning
        lastElement = findFront(toElement); //location of the end
        L2Array l2Array = (L2Array) l1Array[beginningElement.level1Index];

        while (!currentLocation.equals(lastElement)) {
            currentLocation = beginningElement;
            currentItem = l2Array.items[currentLocation.level2Index];
            result.add(currentItem);
            currentLocation.moveToNext();
            l2Array = (L2Array) l1Array[currentLocation.level1Index];
        }
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     *
     * @return an iterator
     */
    public Iterator<E> iterator() {
        return new Itr();

    }

    /**
     * Iterator is just a list loc. It starts at (0,0) and finishes with index2
     * 1 past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check if more items
         */
        public boolean hasNext() {
            L2Array l2 = (L2Array) l1Array[loc.level1Index];
            return l2.items[loc.level2Index] != null;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list
         */
        public E next() {
            L2Array l2Array = (L2Array) l1Array[loc.level1Index];
            E nextItem = (E) l2Array.items[loc.level2Index];
            try {
                loc.moveToNext();
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
            return nextItem;
        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
