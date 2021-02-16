/**
 * RaggedArrayList.java
 * ****************************************************************************
 * revision history
 * ****************************************************************************
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

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.String.valueOf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Consumer;

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
            // TO DO IN PART 5
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

//        int l1Index = 0;
//        int l2IndexFirst = 0;
//        int l1Low = 0;
//        int l2Low = 0;
//        int l1Middle = 0;
//        int l2Middle = 0;
//        int count = 0;
//        int l1High = l1NumUsed - 1;
//        int l2High = 0;
//        //Boolean foundFirst = false;
//        ListLoc res = new ListLoc(0, 0);
//        ArrayList<ListLoc> firstL2Items = new ArrayList<>();
//
//        if (size == 0) {
//            return res;
//        }
//        try {
//            //while (l1Low <= l1High) {
//
//            //l2Index = l2Middle = (l2Low + l2High + 1) / 2;
//            ArrayList<ListLoc> lastL2Items = new ArrayList<>();
//            /**
//             * Binary search first
//             */
//            while (l1Low <= l1High) {
//                // determine middle of L1 array for inital search
//                l1Index = l1Middle = (l1Low + l1High + 1) / 2;
//                // get the L2 array object from the current L1 array position
//                L2Array l2ArrayNulls = (L2Array) l1Array[l1Middle];
//                // save numUsed
//                int numsUsed = l2ArrayNulls.numUsed;
//                // remove nulls from array
//                E[] l2Array = Arrays.copyOfRange(l2ArrayNulls.items, 0,
//                        numsUsed);
//                l2High = l2Array.length;
//
//                l2IndexFirst = l2Middle = (l2Low + l2High + 1) / 2;
//                // if the item is lesser than the current element
//                if (comp.compare(item, l2Array[l2IndexFirst]) < 0) {
//                    while (l2IndexFirst != 0) {
//                        // if current element is not equal then check next
//                        if (!l2Array[l2IndexFirst].equals(item)) {
//                            l2IndexFirst--;
//                        }
//                        // if next element is equal then add to list and
//                        // check next
//                        if (l2Array[l2IndexFirst].equals(item)) {
//                            ListLoc l2First
//                                    = new ListLoc(l1Index, l2IndexFirst);
//                            firstL2Items.add(l2First);
//                            l2IndexFirst--;
//                            /**
//                             * bug on test 3 here. Index is zero but why is it
//                             * skipping over?????
//                             */
//                            // if index is zero, check if the last element 
//                            // equal or not. Then adjust l1 array
//                        } 
//                        if (l2IndexFirst == 0) {
//                            if (!l2Array[l2IndexFirst].equals(item)) {
//                                l1High = l1Middle - 1;
//                            } else if (l2Array[l2IndexFirst].equals(item)) {
//                                ListLoc l2First = new ListLoc(l1Index,
//                                        l2IndexFirst);
//                                firstL2Items.add(l2First);
//                                l1High = l1Middle - 1;
//                            }
//                        }
//                    }
//                    // if element at first index is equal to item,
//                    // add to list
//                } else if (comp.compare(item, l2Array[l2IndexFirst]) == 0) {
//                    ListLoc l2First
//                            = new ListLoc(l1Index, l2IndexFirst);
//                    firstL2Items.add(l2First);
//                    // while the index is not zero...
//                    while (l2IndexFirst != 0) {
//                        // if the element doesnt equal the item then change
//                        // index to next element
//                        if (!l2Array[l2IndexFirst].equals(item)) {
//                            l2IndexFirst--;
//                        }
//                        // if the element does equal the item then add it to
//                        // the list and change index to next element to 
//                        // check for duplicates.
//                        if (l2Array[l2IndexFirst].equals(item)) {
//                            l2First = new ListLoc(l1Index, l2IndexFirst);
//                            firstL2Items.add(l2First);
//                            l2IndexFirst--;
//                            // if the index is zero...
//                        } else if (l2IndexFirst == 0) {
//                            // if not a match then adjust the l1 array
//                            if (!l2Array[l2IndexFirst].equals(item)) {
//                                l1High = l1Middle - 1;
//                                // if it is a match then add to list and adjust
//                                // l1 array
//                            } 
//                            if (l2Array[l2IndexFirst].equals(item)) {
//                                l2First = new ListLoc(l1Index,
//                                        l2IndexFirst);
//                                firstL2Items.add(l2First);
//                                l1High = l1Middle - 1;
//                            }
//                        }
//                    }
//                    // if the item is is "bigger" than the element at first 
//                    // index ..
//                } else if (comp.compare(item,
//                        l2Array[l2IndexFirst]) > 0) {
//                    // while index is lesser than the number of elements
//                    while (l2IndexFirst < numsUsed) {
//                        // if not a match, then move forward
//                        if (!l2Array[l2IndexFirst].equals(item)) {
//                            l2IndexFirst++;
//                        }
//                        // if still inside bounds of array and item is equal
//                        // add to list and stop because thats first element
//                        if (l2IndexFirst < numsUsed
//                                && l2Array[l2IndexFirst].equals(item)) {
//                            ListLoc l2First = new ListLoc(l1Index,
//                                    l2IndexFirst);
//                            firstL2Items.add(l2First);
//                            // if the index and total element count are 
//                            // equal, then check last element and adjust
//                            // l1 array if not equal
//                        } else if (l2IndexFirst == numsUsed) {
//                            if (!l2Array[l2IndexFirst - 1].equals(item)) {
//                                l1Low = l1Middle + 1;
//                                // otherwise add to list if equal
//                            }
//                            if (l2Array[l2IndexFirst - 1]
//                                    .equals(item)) {
//                                ListLoc l2First = new ListLoc(l1Index,
//                                        l2IndexFirst);
//                                firstL2Items.add(l2First);
//                            }
//                        }
//                    }
//                }
//            }
//            // set the result to the last list element which is the "first"    
//            res = firstL2Items.get(firstL2Items.size() - 1);
//            //count++;
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println(item.toString() + " Out of bounds error" + " "
//                    + l1Index + ", " + l2IndexFirst);
//        }
//        return res;
//    }
    /**
     * find location after the last matching entry or if no match, it finds the
     * index of the next larger item this is the position to add a new entry
     * this might be an unused slot at the end of a level 2 array
     *
     * @param item the thing we are searching for a place to put.
     * @return a ListLoc object - the location where this item should go
     */
    public ListLoc findEnd(E item) {
        /**
         * Binary search last
         */
//                while (l2Low <= l2High) {
//                    l2IndexLast = (l2Low + l2High + 1) / 2;
//                    if (comp.compare(item, l2Array[l2IndexLast]) == 0) {
//                        while (l2IndexLast < numsUsed
//                                && l2Array[l2IndexLast].equals(item)) {
//                            ListLoc l2Last = 
//                                    new ListLoc(l1Index, l2IndexLast);
//                            firstL2Items.add(l2Last);
//                            l2IndexLast++;
//                            if (l2IndexLast < numsUsed && 
//                                    !l2Array[l2IndexLast].equals(item)) {
//                                l2IndexLast = l2IndexLast - 1;
//                            } else if (l2IndexLast == 0) {
//                                l1Low = l1Middle + 1;
//                            }
//                        }
//                    }
//                }
        if (size == 0) {
            return new ListLoc(0, 0);
        }

        int i = l1NumUsed - 1;
        L2Array l2Array = (L2Array) l1Array[i];
        while (i > 0 && comp.compare(item, l2Array.items[0]) < 0) {           
            i--;
            l2Array = (L2Array) l1Array[i];
            System.out.println(i + " " + comp.compare(item, l2Array.items[0]) + " ");
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
        // TO DO in part 4

        return true;
    }

    /**
     * check if list contains a match
     *
     * @param item the thing we are looking for.
     * @return true if the item is already in the data structure
     */
    public boolean contains(E item) {
        // TO DO in part 5

        return false;
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {
        // TO DO in part 5

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
        // TO DO in part 5

        RaggedArrayList<E> result = new RaggedArrayList<E>(comp);
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
            // TO DO in part 5

            return false;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list
         */
        public E next() {
            // TO DO in part 5

            throw new IndexOutOfBoundsException();
        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
