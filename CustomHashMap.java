/**
 * Created by richardliu on 10/19/15.
 */
import java.util.HashMap;

public class CustomHashMap {
    /*
    Design of CustomHashMap: Array of LinkedLists with set limit of total Nodes. Each index of array is called a "bucket".
    Tests for all features of the CustomHashMap are detailed in main, at the bottom.
     */

    //-- Define K-V pair represented by "Node" for LinkedList
    class KVPairNode {
        String theKey;
        Object theValue;
        KVPairNode next;

        KVPairNode(String k, Object v){
            this.theKey = k;
            this.theValue = v;
        }
    }
    private int MY_SIZE; // Maximum number of KV Nodes
    int NUM_ELEMENTS; // Current number of KV Nodes
    private KVPairNode[] table; // Array containing KV Nodes

    //-- Constructor. Input: maximum number of Nodes (K-V pairs).
    CustomHashMap(int size){
        this.MY_SIZE=size;
        this.table = new KVPairNode[size];
    }

    //-- Creates a K-V pair. Key must be a string, Value is an object. Returns false if HashMap already full.
    boolean set(String key, Object value){
        int index = Math.abs(key.hashCode() % this.MY_SIZE);
        /*
        First, check if key exists in the appropriate bucket.
        If it does, overwrite value.
        If not, then create new K-V pair in array.
         */
        if (this.table[index] == null){
            if (this.NUM_ELEMENTS == this.MY_SIZE) { // Check if HashMap full. If full, print to console, and return false.
                System.out.println("ERROR: Trying to insert element in full CustomHashMap");
                return false;
            }
            KVPairNode temp = new KVPairNode(key, value);
            this.table[index] = temp;
            this.NUM_ELEMENTS++;
            return true;
        }
        else{
            KVPairNode start;
            start = this.table[index];
            while (start != null){
                if (start.theKey.equals(key)){ // Overwrite value. Don't increment NUM_ELEMENTS
                    start.theValue = value;
                    return true;
                }
                start = start.next;
            }
            if (this.NUM_ELEMENTS == this.MY_SIZE) { // Check if HashMap full. If full, print to console, and return false.
                System.out.println("ERROR: Trying to insert element in full CustomHashMap");
                return false;
            }
            KVPairNode temp = new KVPairNode(key, value);
            temp.next = this.table[index];
            this.table[index] = temp;
            this.NUM_ELEMENTS++;
            return true;
        }
    }

    //-- Returns value given a key. If key does not exist, returns null.
    public Object get(String key){
        //-- Look at every K-V pair in appropriate bucket.
        int index = key.hashCode() % this.MY_SIZE;
        if (index < 0) index+= this.MY_SIZE;
        KVPairNode bucket = this.table[index];
        if (bucket.theKey.equals(key)) {
            return bucket.theValue;
        }
        else{
            while(bucket != null){
                if (bucket.theKey.equals(key)) return bucket.theValue;
                bucket = bucket.next;
            }
            System.out.println("ERROR: Key does not exist.");
            return null;
        }
    }

    //-- Returns the Node given a key. If key does not exist, returns null.
    //-- Used in delete(key).
    public KVPairNode getNode(String key){
        KVPairNode bucket = this.table[key.hashCode() % this.MY_SIZE];
        if (bucket.theKey.equals(key)) return bucket;
        else{
            while(bucket.next != null){
                bucket=bucket.next;
                if (bucket.theKey.equals(key)) return bucket;
            }
            return null;
        }
    }

    //-- Deletes a K-V Pair from HashMap, returns the value deleted. Returns null and deletes nothing if key does not exist.
    public Object delete(String key) {
        KVPairNode theNode = this.getNode(key);
        Object output;
        /*
        We want to "delete" this node.
        If the next node is null, just make this current node null.
        Else, copy the next Node's information into the current node.
        */
        if (theNode != null) {
            output = theNode.theValue;
            if (theNode.next == null) {
                theNode = null;
            } else {
                theNode.theKey = theNode.next.theKey;
                theNode.theValue = theNode.next.theValue;
                theNode.next = theNode.next.next;
            }
            this.NUM_ELEMENTS--;
            return output;
        } else {
            System.out.println("ERROR: Key does not exist.");
            return null;
        }
    }

    //-- Calculates load factor of HashMap by dividing current number of elements by maximum size.
    public float load(){
        float dividend = (float)this.NUM_ELEMENTS/(float)this.MY_SIZE;
        return dividend;
    }

    public static void main(String args[]){
        //-- Declare and initialize CustomHashMap
        CustomHashMap test = new CustomHashMap(4);

        //-- Testing insertion of String-String pairs.
        String testString = " ";
        System.out.println("");
        System.out.println("--------------------");
        System.out.println("Testing: Insertion");
        System.out.println("--------------------");
        for (int i=0; i<4; i++){
            testString += "*";
            test.set(testString, testString);
            System.out.println("Expected " + Integer.toString(i+1) + " stars"+ ":" + test.get(testString));
        }

        //-- Now, testing overriding a key with a new value - String-HashMap pair.
        System.out.println("");
        System.out.println("--------------------");
        System.out.println("Testing: Override existing key");
        System.out.println("--------------------");
        test.set(testString, new HashMap<String, String>());
        System.out.println("Expected: class java.util.HashMap.");
        System.out.println(test.get(testString).getClass().toString());
        if (test.get(testString).getClass().toString().equals("class java.util.HashMap")){
            System.out.println("Test 1: PASSED.");
        }

        //-- Now, testing if the size of the HashMap is limited properly.
        //-- This should return false and print an error message to console
        System.out.println("");
        System.out.println("--------------------");
        System.out.println("Testing: Fixed limit of CustomHashMap");
        System.out.println("--------------------");
        System.out.println("Expected: ERROR message below.");
        if (!test.set(testString+"TEMP", new HashMap<String, Integer>())){
            System.out.println("Test 1: PASSED.");
        }
        System.out.println("Expected: ERROR message below.");
        if (!test.set(testString+1, new HashMap<String, Integer>())){
            System.out.println("Test 2: PASSED.");
        }

        //-- Testing if HashMap deals with invalid key working.
        System.out.println("");
        System.out.println("--------------------");
        System.out.println("Testing: Get invalid key");
        System.out.println("--------------------");
        System.out.println("Expected: ERROR message below.");
        if (test.get(testString+"TEMP")==null){
            System.out.println("Test 1: PASSED.");
        }

        //-- Testing if the load is calculated properly.
        System.out.println("");
        System.out.println("--------------------");
        System.out.println("Testing load");
        System.out.println("--------------------");
        float floater = test.load();
        System.out.println("Expected Current Load: 1.0");
        System.out.println("Observed Current Load: " + Float.toString(floater));
        if (floater == 1.0){
            System.out.println("Test 1: PASSED.");
        }

        System.out.println("");
        System.out.println("--------------------");
        System.out.println("Testing: Deletion");
        System.out.println("--------------------");
        /*
        We expect the console to print the following:
        Current load is now 0.75
        Current load is now 0.5
        Current load is now 0.25
        Current load is now 0.0
        */
        Object testRemove = test.delete(testString);
        System.out.println("Just removed: " + testRemove.toString());
        System.out.println("Current load is now: " + Float.toString(test.load()));
        for (int i=4; i>1; i--){
            System.out.println("Expected: " + Integer.toString(i-1) + " stars:" + test.delete(testString.substring(0, i)));
            System.out.println("Current load is now: " + Float.toString(test.load()));
        }

        System.out.println("");
        System.out.println("--------------------");
        System.out.println("Testing: Deleting a nonexistent key");
        System.out.println("--------------------");
        //-- test deleting nonexistent key
        Object shouldBeNull = test.delete(testString);
        System.out.println("Expected: ERROR message below.");
        if (shouldBeNull==null){
            System.out.println("Test 1: PASSED.");
        }
    }


}
