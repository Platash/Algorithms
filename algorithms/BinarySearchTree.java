/**
 *
 * @author B00277179
 */
package algorithms;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinarySearchTree<E> extends AbstractSet<E> {
    
    protected Entry<E> root;
    protected int size;
    private boolean addAsRoot;
    
    /**
     *  Initializes this BinarySearchTree object to be empty, to contain only elements
     *  of type E, to be ordered by the Comparable interface, and to contain no 
     *  duplicate elements.
     *
     */ 
    public BinarySearchTree() {
        root = new Entry<>(null);
        root.makeExternal();
        size = 0; 
        addAsRoot = false;
    } // default constructor
    
    /**
     *  Initializes this BinarySearchTree object to be empty, to contain only elements
     *  of type E, to be ordered by the Comparable interface, and to contain no 
     *  duplicate elements.
     *
     */ 
    public BinarySearchTree(boolean addAsRoot) {
        root = new Entry<>(null);
        root.makeExternal();
        size = 0; 
        this.addAsRoot = addAsRoot;
    } 


    /**
     * Initializes this BinarySearchTree object to contain a shallow copy of
     * a specified BinarySearchTree object.
     * The worstTime(n) is O(n), where n is the number of elements in the
     * specified BinarySearchTree object.
     *
     * @param otherTree - the specified BinarySearchTree object that this
     *                BinarySearchTree object will be assigned a shallow copy of.
     *
     */
    public BinarySearchTree (BinarySearchTree<? extends E> otherTree) {
         root = copy (otherTree.root, null);
         size = otherTree.size;
         addAsRoot = otherTree.addAsRoot;
    } // copy constructor


    protected Entry<E> copy (Entry<? extends E> p, Entry<E> parent) {
        if (p != null) {
            Entry<E> q = new Entry<E> (parent);
            q.makeInternal(p.element);
            q.left = copy (p.left, q);
            q.right = copy (p.right, q);
            return q;
        } // if
        return null;
    } // method copy
        
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof BinarySearchTree)) {
            return false;
        }
        return equals (root, ((BinarySearchTree<? extends E>)obj).root);
    } // method 1-parameter equals
    
    
    public boolean equals (Entry<E> p, Entry<? extends E> q) {
       if (p == null || q == null) {
           return p == q;
       }      
       if (!p.element.equals (q.element)) {
           return false;
       }
       if (equals (p.left, q.left) && equals (p.right, q.right) ) {
           return true;
       }            
       return false;     
    } // method 2-parameter equals
    
    
    /**
     *  Returns the size of this BinarySearchTree object.
     *
     * @return the size of this BinarySearchTree object.
     *
     */
    @Override
    public int size() {
        return size;
    } // method size()
  

    /**
     *  Returns an iterator positioned at the smallest element in this 
     *  BinarySearchTree object.
     *
     *  @return an iterator positioned at the smallest element in this
     *                BinarySearchTree object.
     *
     */
    @Override
    public Iterator<E> iterator() {
         return new TreeIterator();
    } // method iterator

    
    /**
     *  Determines if there is at least one element in this BinarySearchTree object that
     *  equals a specified element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).  
     *
     *  @param obj - the element sought in this BinarySearchTree object.
     *
     *  @return true - if there is an element in this BinarySearchTree object that
     *                equals obj; otherwise, return false.
     *
     *  @throws ClassCastException - if obj cannot be compared to the 
     *           elements in this BinarySearchTree object. 
     *  @throws NullPointerException - if obj is null.
     *  
     */ 
    @Override
    public boolean contains (Object obj) {
        return getEntry (obj) != null;
    } // method contains

 
    /**
     *  Ensures that this BinarySearchTree object contains a specified element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     *  @param element - the element whose presence is ensured in this 
     *                 BinarySearchTree object.
     *
     *  @return true - if this BinarySearchTree object changed as a result of this
     *                method call (that is, if element was actually inserted); otherwise,
     *                return false.
     *
     *  @throws ClassCastException - if element cannot be compared to the 
     *                  elements already in this BinarySearchTree object.
     *  @throws NullPointerException - if element is null.
     *
     */
    @Override
    public boolean add (E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if(addAsRoot) {
            return addAsRoot(element);
        } else {
            return addAsLeaf(element);
        }
    } // method add
    
    private boolean addAsLeaf (E element) {
        if (root.isExternal()) {
            root = new Entry<> (null);
            root.makeInternal(element);
            size++;             
            return true; // empty tree 
        } else {
            Entry<E> temp = root;

            int comp;

            while (true) {
                comp =  ((Comparable)element).compareTo (temp.element);
                if (comp == 0) {
                    return false;
                }
                if (comp < 0) {
                    if (!temp.left.isExternal()) {
                        temp = temp.left;
                    } else {
                        temp.left = new Entry<E> (temp);
                        temp.left.makeInternal(element);
                        size++;                             
                        return true;
                    }
                } else if (!temp.right.isExternal()) {
                    temp = temp.right;
                } else {
                    temp.right = new Entry<E> (temp);
                    temp.right.makeInternal(element);
                    size++;      
                    return true;
                } // temp.right == null
            } // while
        } // root not null
    } // method add
    
    

    private boolean addAsRoot(E element) { 
        Boolean added = true;
        root = addAsRoot(root, element, added);
        root.parent = null;
        return added;
    }
    
    private Entry<E> addAsRoot(Entry<E> node, E element, Boolean added) {
        
        if(node.isExternal()) {
           node.makeInternal(element);
           size++;
           return node;
        } 
        int comp;
        comp =  ((Comparable)element).compareTo(node.element);
        if (comp < 0) {
            node.left = addAsRoot(node.left, element, added); 
            node = rotR(node);
       } else if (comp == 0) {
            added = false;
            return node;
        }
        else {
            node.right = addAsRoot(node.right, element, added); 
            node = rotL(node);
        }
        return node;
    }
    
    /**
     * Right rotation
     * @param node around which rotation must be made
     * @return node which takes place of the node given as a parameter
     */
    private Entry<E> rotR(Entry<E> node) {
        Entry<E> x = node.left;
        node.left = x.right; 
        x.right = node; 
        node.parent = x;
        if( node.left != null) {
           node.left.parent = node; 
        }
        
        return x;
    }
    
    
    /**
     * Left rotation
     * @param node around which rotation must be made
     * @return node which takes place of the node given as a parameter
     */
    private Entry<E> rotL(Entry<E> node) {
        Entry<E> x = node.right; 
        node.right = x.left; 
        x.left = node; 
        node.parent = x;
        if( node.right != null) {
           node.right.parent = node; 
        }
        return x;
    }
    

    /**
     *  Ensures that this BinarySearchTree object does not contain a specified 
     *  element.
     *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     *  @param obj - the object whose absence is ensured in this 
     *                 BinarySearchTree object.
     *
     *  @return true - if this BinarySearchTree object changed as a result of this
     *                method call (that is, if obj was actually removed); otherwise,
     *                return false.
     *
     *  @throws ClassCastException - if obj cannot be compared to the 
     *                  elements already in this BinarySearchTree object. 
     *  @throws NullPointerException - if obj is null.
     *
     */
    @Override
    public boolean remove (Object obj) {
        Entry<E> e = getEntry (obj);
        if (e == null)
            return false;
        deleteEntry (e);       
        return true;
    } // method remove


    /**
     *  Finds the Entry object that houses a specified element, if there is such an Entry.
     *  The worstTime(n) is O(n), and averageTime(n) is O(log n).
     *
     *  @param obj - the element whose Entry is sought.
     *
     *  @return the Entry object that houses obj - if there is such an Entry;
     *                otherwise, return null.  
     *
     *  @throws ClassCastException - if obj is not comparable to the elements
     *                  already in this BinarySearchTree object.
     *  @throws NullPointerException - if obj is null.
     *
     */
    protected Entry<E> getEntry (Object obj) {
        int comp;

        if (obj == null) {
            throw new NullPointerException();
        }
       
        Entry<E> e = root;
        while (!e.isExternal()) {
            comp = ((Comparable)obj).compareTo (e.element);
            if (comp == 0) {
                return e;
            }
            else if (comp < 0) {
                e = e.left;
            }
            else {
                e = e.right;
            }
        } // while
        return null;
    } // method getEntry
    
  
     /**
      *  Deletes the element in a specified Entry object from this BinarySearchTree.
      *  
      *  @param p � the Entry object whose element is to be deleted from this
      *                 BinarySearchTree object.
      *
      *  @return the Entry object that was actually deleted from this BinarySearchTree
      *                object. 
      *
      */
   protected Entry<E> deleteEntry (Entry<E> p) 
    {
        size--;

        // If p has two children, replace p's element with p's successor's
        // element, then make p reference that successor.
        if ((!p.left.isExternal()) && (!p.right.isExternal())) 
        {
            Entry<E> s = successor (p);
            p.element = s.element;
            p = s;
        } // p had two children


        // At this point, p has either no children or one child.

        Entry<E> replacement;
         
        if (!p.left.isExternal())
            replacement = p.left;
        else
            replacement = p.right;

        // If p has at least one child, link replacement to p.parent.
        if (!replacement.isExternal()) 
        {
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;
        } // p has at least one child  
        else if (p.parent.isExternal())
            root.makeExternal();
        else 
        {
            if (p == p.parent.left)
                p.parent.left.makeExternal();
            else
                p.parent.right.makeExternal();        
        } // p has a parent but no children
        return p;
    } // method deleteEntry


    /**
     *  Finds the successor of a specified Entry object in this BinarySearchTree.
     *  The worstTime(n) is O(n) and averageTime(n) is constant.
     *
     *  @param e - the Entry object whose successor is to be found.
     *
     *  @return the successor of e, if e has a successor; otherwise, return null.
     *
     */
    protected Entry<E> successor (Entry<E> e) {
        if (e == null) {
            return null;
        }
        else if (!e.right.isExternal()) {
            // successor is leftmost Entry in right subtree of e
            Entry<E> p = e.right;
            while (!p.left.isExternal()) {
                p = p.left;
            }
            return p;

        } // e has a right child
        else {

            // go up the tree to the left as far as possible, then go up
            // to the right.
            Entry<E> p = e.parent;
            Entry<E> ch = e;
            while ((!p.isExternal()) && ch == p.right) {
                ch = p;
                p = p.parent;
            } // while
            return p;
        } // e has no right child
    } // method successor
    
    
    /**
     * Displays elements of the tree in the order they would be visited 
     * in a breadth-first of the tree
     */
    
    
    protected void traversalBreadthFirst () {
        if(!root.isExternal()) {
            Queue<Entry<E>> queue = new LinkedList<>();
            Entry<E> node = this.root;
            Entry<E> nodeOld;
            queue.add(node);
            int height = getHeight();
            
            while(!queue.isEmpty()) {
                nodeOld = node;
                node = queue.remove();
               
                System.out.print(node.element + ",  "); 
                
                
                if (!node.left.isExternal()) {
                    queue.add(node.left);
                }
                if (!node.right.isExternal()) {
                    queue.add(node.right);
                }
            }
            System.out.println("");
        }
    }
    
    
    /**
     * Returns the height of the tree 
     * (including the external nodes when calculating this).
     */
    protected int getHeight() {
        if(root.isExternal()){
            System.out.println("is empty");
            return -1;
        } else {
            return getHeight(root); 
        }
    }
    
    /**
     * Returns the height of the subtree, starting from the given node. 
     * (including the external nodes when calculating this).
     */
    private int getHeight(Entry<E> node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }
    
    
    protected class TreeIterator implements Iterator<E> {

        protected Entry<E> lastReturned = null;
        protected Entry<E> next;               

        /**
         *  Positions this TreeIterator to the smallest element, according to the Comparable
         *  interface, in the BinarySearchTree object.
         *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
         *
         */
        protected TreeIterator() {             
            next = root;
            if (!next.isExternal()) {
                while (!next.left.isExternal()) {
                    next = next.left;
                }
            }
        } // default constructor


        /**
         *  Determines if there are still some elements, in the BinarySearchTree object this
         *  TreeIterator object is iterating over, that have not been accessed by this
         *  TreeIterator object.
         *
         *  @return true - if there are still some elements that have not been accessed by
         *                this TreeIterator object; otherwise, return false.
         *
         */ 
        @Override
        public boolean hasNext() {
            return !next.isExternal();
        } // method hasNext


        /**
         *  Returns the element in the Entry this TreeIterator object was positioned at 
         *  before this call, and advances this TreeIterator object.
         *  The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         *  @return the element this TreeIterator object was positioned at before this call.
         *
         *  @throws NoSuchElementException - if this TreeIterator object was not 
         *                 positioned at an Entry before this call.
         *
         */
        @Override
        public E next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = successor (next);             
            return lastReturned.element;
        } // method next

        /**
         *  Removes the element returned by the most recent call to this TreeIterator
         *  object�s next() method.
         *  The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         *  @throws IllegalStateException - if this TreeIterator�s next() method was not
         *                called before this call, or if this TreeIterator�s remove() method was
         *                called between the call to the next() method and this call.
         *
         */ 
        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
 
            if (lastReturned.left != null && lastReturned.right != null) {
                next = lastReturned;
            }
            deleteEntry(lastReturned);
            lastReturned = null; 
        } // method remove     

    } // class TreeIterator

    
    protected static class Entry<E> {
        
        protected E element;
        protected Entry<E> left = null;
        protected Entry<E> right = null;
        protected Entry<E> parent;
 
        /**
         *  Initializes this Entry object.
         *
         *  This default constructor is defined for the sake of subclasses of
         *  the BinarySearchTree class. 
         */
        public Entry() { }


         /**
         *  Initializes this Entry object from parent.
         *  Used to create leaves.
         */ 
         public Entry (Entry<E> parent) {
             this.parent = parent;
         } // constructor
         
         /**
          * returns true if this entry is an external node and false otherwise
          * @return 
          */
         public boolean isExternal() {
             //No need to check the other one, if one child is null other is null as well
             if(this.left == null) {    
                 return true;
             }
             return false;
         }
         
         
         /**
          * converts this internal node to an external node and 
          * returns the element that the internal node contained 
          * (used when deleting an internal node that has no internal nodes as children) 
          */
         
        @SuppressWarnings("unchecked")
         public E makeExternal() {
             this.left = null;
             this.right = null;
             E temp = element;
             element = null;
             return temp;
         }
        
        
        /**
         * converts this external node to an internal node 
         * containing the given element and adds two new external nodes 
         * as the left and right children of the node 
         * (used when inserting an element in to the tree)
         * @param element 
         */
        public void makeInternal(E element) {
            this.element = element;
            this.left = new Entry<>(this);
            this.right = new Entry<>(this);
        } 
        

    } // class Entry

} // class BinarySearchTree
