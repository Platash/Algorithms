package algorithms;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

/**
 *
 * @author B00277179
 */
public class Algorithms {
    private static ArrayList<BinarySearchTree<Item>> treesRoot = new  ArrayList<>();
    private static ArrayList<BinarySearchTree<Item>> treesLeaf = new ArrayList<>();
    private static ArrayList<TreeSet<Item>> treesBasic = new ArrayList<>();
    private static final int TREE_SIZES = 5;
    private static Item[][] items = new Item[TREE_SIZES][];
    private static int[] treeSizes = {20, 1024, 15788, 100345, 1000000};
        
    /**
     * @param args the command line arguments
     */ 
    public static void main(String[] args) {
        BinarySearchTree<Item> treeR = new BinarySearchTree<>(true);
        BinarySearchTree<Item> treeL = new BinarySearchTree<>(false);
        
        treeL.add(new Item(6));
        treeL.add(new Item(4));
        treeL.add(new Item(2));
        treeL.add(new Item(8));
        treeL.add(new Item(10));
        treeL.add(new Item(3));
        
        System.out.println("getEntry 4: " + treeL.getEntry(new Item(4)).element);
        System.out.println("getEntry 2: " + treeL.getEntry(new Item(2)).element);
        System.out.println("getEntry 3: " + treeL.getEntry(new Item(3)).element);
        System.out.println("element 8: " + treeL.getEntry(new Item(8)).element);
        System.out.println("element 10: " + treeL.getEntry(new Item(10)).element);
        
        treeR.add(new Item(6));
        treeR.add(new Item(4));
        treeR.add(new Item(2));
        treeR.add(new Item(8));
        treeR.add(new Item(10));
        treeR.add(new Item(3));
        
        System.out.println("element 6 parent: " + treeR.getEntry(new Item(6)).parent.element);
        System.out.println("element 4 parent: " + treeR.getEntry(new Item(4)).parent.element);
        System.out.println("element 2 parent: " + treeR.getEntry(new Item(2)).parent.element);
        System.out.println("element 8 parent: " + treeR.getEntry(new Item(8)).parent.element);
        System.out.println("element 10 parent: " + treeR.getEntry(new Item(10)).parent.element);
        
        
        System.out.println("element 4 parent: " + treeL.getEntry(new Item(4)).parent.element);
        System.out.println("element 2 parent: " + treeL.getEntry(new Item(2)).parent.element);
        System.out.println("element 8 parent: " + treeL.getEntry(new Item(8)).parent.element);
        System.out.println("element 10 parent: " + treeL.getEntry(new Item(10)).parent.element);
        System.out.println("element 3 parent: " + treeL.getEntry(new Item(3)).parent.element);
        
        
        treeR.traversalBreadthFirst();
        treeL.traversalBreadthFirst();
        
        treeL.add(new Item(4));
        treeR.add(new Item(2));
        
        treeR.traversalBreadthFirst();
        treeL.traversalBreadthFirst();
        
        treeR.remove(new Item(10));
        treeR.remove(new Item(27));
        
        treeL.remove(new Item(10));
        treeL.remove(new Item(27));
        
        treeR.traversalBreadthFirst();
        treeL.traversalBreadthFirst();
        
        System.out.println("treeR.root.parent: " + treeR.root.parent);
        System.out.println("treeL.root.parent " + treeL.root.parent);
        
        initItems();
        Random ran = new Random();
        
        for(int i = 0; i < TREE_SIZES; i++) {
            treesRoot.add(new BinarySearchTree<Item>(true));
            initTree(treesRoot.get(i), i);
            
            treesLeaf.add(new BinarySearchTree<Item>(false));
            initTree(treesLeaf.get(i), i);
            
            treesBasic.add(new TreeSet<Item>());
            initTree(treesBasic.get(i), i);
        }
        
        findElements();
        
        for(int i = 0; i < TREE_SIZES; i++) {
            for(int j = 0; j < treesRoot.get(i).size / 4; j++) {
                treesRoot.get(i).remove(items[i][ran.nextInt(items[i].length)]);
            }
            for(int j = 0; j < treesLeaf.get(i).size / 4; j++) {
                treesLeaf.get(i).remove(items[i][ran.nextInt(items[i].length)]);
            }
            for(int j = 0; j < treesBasic.get(i).size() / 4; j++) {
                treesBasic.get(i).remove(items[i][ran.nextInt(items[i].length)]);
            }
        }
        
        for(int i = 0; i < TREE_SIZES; i++) {
            for(int j = 0; j < treesRoot.get(i).size / 2; j++) {
                treesRoot.get(i).add(items[i][ran.nextInt(items[i].length)]);
            }
            for(int j = 0; j < treesLeaf.get(i).size / 2; j++) {
                treesLeaf.get(i).add(items[i][ran.nextInt(items[i].length)]);
            }
            for(int j = 0; j < treesBasic.get(i).size() / 2; j++) {
                treesBasic.get(i).add(items[i][ran.nextInt(items[i].length)]);
            }
        }
        
        findElements();
    }
    
    private static void initItems() {
        Random ran = new Random();
        for(int i = 0; i < TREE_SIZES; i++) {
            Item[] temp = new Item [treeSizes[i]];
            for(int j = 0; j < treeSizes[i]; j++) {
                temp[j] = new Item(ran.nextInt());
            }
            items[i] =  temp;
        }
    }
    
    private static void initTree(AbstractSet<Item> tree, int nodesCount) {
        for(int i = 0; i < items[nodesCount].length; i++) {
            tree.add(items[nodesCount][i]);
        }
    }
    
    private static void findElement (AbstractSet<Item> tree) {
        Random ran = new Random();
        Item.resetCompCount();
        boolean contains = tree.contains(new Item(ran.nextInt()));
        System.out.println("Tree size: " + tree.size());
        System.out.println("Comparisons:" + Item.getCompCount());
        System.out.println("Tree contains the element: " + contains);
    }
    
    private static void findExistingElement (AbstractSet<Item> tree, int nodesCount) {
        Item.resetCompCount();
        Random ran = new Random();
        int node = ran.nextInt(items[nodesCount].length);
        System.out.println(" " + items.length);
        boolean contains = tree.contains(items[nodesCount][node]);
        System.out.println("Tree size: " + tree.size());
        System.out.println("Comparisons:" + Item.getCompCount());
        System.out.println("Tree contains the element: " + contains);
    }

    private static void findElements() {
        for(int i = 0; i < TREE_SIZES; i++) {
            findElement(treesRoot.get(i));
            findElement(treesRoot.get(i));
            findElement(treesRoot.get(i));
            System.out.println("-------------------");
            findElement(treesLeaf.get(i));
            findElement(treesLeaf.get(i));
            findElement(treesLeaf.get(i));
            System.out.println("-------------------");
            findElement(treesBasic.get(i));
            findElement(treesBasic.get(i));
            findElement(treesBasic.get(i));
             System.out.println("-------------------");
            
            for(int j = 0; j < 3; j++) {
                findExistingElement(treesRoot.get(i), i);
            }
             System.out.println("-----------------------------------------------");
            for(int j = 0; j < 3; j++) {
                findExistingElement(treesLeaf.get(i), i);
            }
             System.out.println("-----------------------------------------------");
            for(int j = 0; j < 3; j++) {
                findExistingElement(treesBasic.get(i), i);
            }
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("");
            System.out.println("");
            
        }
    }
    
    private void removeElement() {
        
    }
        
}
