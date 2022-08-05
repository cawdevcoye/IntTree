public class IntTree {

    /*
    Some Info.

    Basic info:
    n = a set of numbers
    e = encoder length
        Space Complexity:
            O(e * n)
        Time Complexities: (e=32 for int tree)
            Add / Remove / Contains / Get by Index / Remove by Index (Per element)
                Worst: O(e)
                Average: O(e)
                Best: O(e)
            EXTRACT_ARRAY / PRINT_STRUCTURE / PRINT :
                Worst:   O(e*n)) (2^32 non duplicate integers in tree)  (Full traversal of 2^32 tree) (n=2^32 unique elements)
                Average: O(e*n) ?? Use AVG as middle point ??  ( > 1 and < 2^32 non duplicate ints in tree) (Partial traversal of partially built exponential tree)
                Best:    O(e*n) (1 number in tree) (just parsing 1 leaf of tree)
        Integers are encoded using 32 bits
            Min value is -2^21
            Max value is 2^31 -1
            Encoder detailed below.

    DESCRIPTION:
    Presenting::
        IntTree. A data structure where:
            There is Constant Time ( O(e) ) accessing and removing of data via index or value.
            Constant time contains per value. ( O(e) )
            Constant time remove by index or value ( O(e) )
            Constant Space Complexity overhead. ( O(e*n) )
    Presenting::
        Inherent Sort. A sorting algorithm where:
            Incorporating a new number into IntTree takes constant time O(e). (Constant time sort for new elements)
            O(e * n) time for sorting a list of numbers.
    Where:
        e is the encoder length, which is 32 for integers.


    General:
        As an analogy, imagine an array of size 2^32 (length of numbers storable using an integer data type) where each number of a set of numbers of size n are stored each in their index location.
        Assume with this array that any unused data is collapsed, removed,  and not represented in this array.
        So for example with 2^3 (8 memory indexes), n=4, n= (1,4,6,7) -> [x,1,x,x,4,x,6,7] the final array is [1,4,6,7].
        In theory, if such a data structure existed there would be:
            O(1) access by value or index (add, remove, contains)
                Simple add/remove a number in its index location (like a regular array) or check if it exists at that location
                (Since this data structure is for numbers, the value is numeric and is equivalent to its index)
            A sort in O(1) time for each value
                Placing a number in its index location in the original (non collapsed) array puts it in a location where its value is relative to the value of all other ints
            N space Complexity
                Inserting a list of size n into this array and eliminating unused space. 2^32 becomes n units of memory used.

        When numbers are inserted into their index, ignoring unused space, the whole list is inherently sorted using this insertion technique.
        Also, accessing this array by value can be done in constant time.
        Finally, because unused space is ignored while looking through this array, but the structure is still an array, looking at values by their index is constant too.

        IntTree does all of this with constant space complexity overhead per element.

    For Inherent Sort:
        Sorting takes constant time per element and O(e * n) for a list of numbers.

    For IntTree:
        The data structure approximates a 2^32 array.
        Rather than using an array to store information, a dynamically built exponential tree is used.
        A full sized exponential tree has exponentially many leaves, but the properties of the structure lead to defined locations for each element added to it via its encoding.
        An example of those locations with k=3 for a 2^k exponential tree with a traditional binary encoding is:
        Encoding    number
        000         0
        001         1
        010         2
        011         3
        100         4
        101         5
        110         6
        111         7

        To avoid exponential space complexity in the tree that encodes these values, the tree only stores the amount of nodes needed to encode the set of added numbers.
        An example of tree of height 2 (k=3) is given.
        Encoding
        a,b,c
        0 0 0 -> 0
        0 0 1 -> 1
        ...
        1 1 1 -> 7

        Data:
        n=3
        n= (0,1,5)

        Encodings
        0 -> 0 0 0
        1 -> 0 0 1
        5 -> 1 0 1

        Left edge encodes 0, right edge encodes 1,
        a0 b0 c0 = 000 -> 0
                              a
                          /       \
                         b         b
                       /          /
                      c          c
                     / \         \

         Number:    0   1          5
         Encoding: 000  001       101

        The tree itself only contains the set of nodes necessary to store the numbers added to it (leading to constant overhead per number stored)
        Adding values is equivalent to building the nodes needed to store a number.
        Adding duplicate values are handled by adding a counter to each leaf node.
        Contains can be performed by checking if a leaf node for a number exists with count > 0.
        Sorting happens by placing a number in its respective leaf node.
        Removing values happens via decreasing the occurance counter down to 0 for a number (at a given leaf node).



    For this structure:
        Recursion happens starting at 0, then moves to 1.
            This means the order of traversal is 0 to 1
        The process of reading the encoding is Left-Right and index based (starts at 0)

        This data structure uses Inherent Sort (Discovered Inherent sort):
            This means the Left most recursive traversal needs to be the minimum value
            It also means the right most recursive traversal needs to be the maximum value
            So the tree encoding of 000...0 needs to be the minimum value, which is Min value representable with an integer.
            The encoding of 111...1 needs to be the maximum value, which is the MAX integer value .

        ENCODER:
            The integer encoder uses a single bit to represent if a number is negative or positive
                0 if Negative
                1 if Positive

            Then it has 31 bits that encode the rest of the integer
                If the number is positive then its encoded using a traditional binary number
                    I.e. 5
                         1 0..0 101 (Extended to 31 bits)
                If the number is negative then the bit values are inverted.
                    i.e.-5
                         0 1..1 010 (Extended to 31 bits)

                This creates a span from 000....0 to 111....1
                    where 000....0 is the minimum storable number, and 111....1 is the maximum storable number

        The explicit encoding of a number details where it exists in this tree, and inherently stores its relative location to other values
        Numbers can be embedded into this tree in their respective index locations.

        ***This gives rise to inherent sort.


        Total Encoder Length is 32

        EX:

            -2147483648 (INTEGER.MIN_VALUE)
                0 0000000000000000000000000000000
            -7
                0 1111111111111111111111111111000
            -1
                0 1111111111111111111111111111111
            0
                1 0000000000000000000000000000000
            1
                1 0000000000000000000000000000001
            7
                1 0000000000000000000000000000111
            2147483647 (INTEGER.MAX_VALUE)
                1 1111111111111111111111111111111
     */

    /*
        Flags / Operations
     */
    private final int CONTAINS = 0;
    private final int ADD = 1;
    private final int REMOVE = 2;
    private final int GET_IDX = 6;
    private final int REMOVE_IDX = 7;
    private final int PRINT = 3;
    private final int PRINT_STRUCTURE = 4;
    private final int EXTRACT_ARRAY = 5;

    /*
        Instance Data
     */
    private final int encoderLen = 32;
    private Node root;
    private int size = 0;
    private int[] extractedArray;
    private boolean modifiedSinceBuilt = false;
    private int parseIndex=0;

    public IntTree()
    {
        root=new Node(encoderLen);
        extractedArray=new int[0];
    }

    public void add(int num)
    {
        this.performOperations(num,ADD);
        this.size++;
        this.modifiedSinceBuilt=true;
    }
    public void remove(int num)
    {
        if(this.size==0)
            throw new RuntimeException("Tree has no values. Cannot remove anything as a result. ");
        this.performOperations(num,REMOVE);
        this.size--;
        this.modifiedSinceBuilt=true;
    }
    public boolean contains(int num)
    {
        if(this.size==0)
            return false;
        return this.performOperations(num, CONTAINS);
    }
    public int removeByIdx(int idx)
    {
        if(this.size > idx && idx >=0)
            this.performOperationsIDX(idx+1, this.REMOVE_IDX);
        else
            throw new RuntimeException("Index out of bounds");
        return 1;
    }
    public int getByIdx(int idx)
    {
        if(this.size > idx && idx >=0)
            return this.performOperationsIDX(idx+1, this.GET_IDX);
        else
            throw new RuntimeException("Index out of bounds");
    }
    public void print()
    {
        System.out.print("[ ");
        this.performOperations(PRINT);
        System.out.println("]");
    }
    public void printStructure()
    {
        this.performOperations(PRINT_STRUCTURE);
    }


    /*
    Used to interface with the Integer Tree by DFS
    Valid Operations are
        Array Extraction (EXTRACT_ARRAY)
        Printing (PRINT_STRUCTURE, PRINT)
     */
    private void performOperations(int flag)
    {
        if(flag != PRINT && flag != PRINT_STRUCTURE && flag != EXTRACT_ARRAY)
            throw new RuntimeException("Only use this method for printing or extracting array.");

        this.recursiveParse(flag, root);
        this.parseIndex=0;
    }
    /*
    Used to interface with the Integer Tree by value
    Valid operations are
        Contains
        Add
        Remove
     */
    private boolean performOperations(int num, int flag)
    {
        if(flag != ADD && flag != REMOVE && flag != CONTAINS)
            throw new RuntimeException("Only use this method for ADD, REMOVE, and CONTAINS");

        Encoder encoder = new Encoder();
        boolean status = this.recursiveParse(num, flag, root, encoder.encodeINT(num), 0) == 1 ;
        this.parseIndex = 0;
        return status;

    }
    /*
    Used to interface with the Integer Tree by index
    Valid operations are
        Get by IDX
        Remove by Idx
     */
    private int performOperationsIDX(int idx, int flag)
    {
        if ( flag != GET_IDX && flag != REMOVE_IDX)
            throw new RuntimeException("Only use this method for GET by IDX or Remove by IDX");

        Encoder encoder = new Encoder();
        int statusValue = this.recursiveParseIDX(idx, flag, root);
        this.parseIndex = 0;
        return statusValue;
    }

    /*
    Recursive parse for Parsing the Integer Tree by value
    Used
        Add
        Remove
        Contains

    Output is:
        IF ADD
            1 if successful
            Error otherwise
        IF REMOVE
            1 if successful
            Error if element is not present
            Error if something failed.
        IF CONTAINS
            1 if CONTAINS
            0 if element not present
     */
    private int recursiveParse(int integer, int flag, Node current, int[] encoding, int loc) {

        if(loc < this.encoderLen) {
            int z_or_o = encoding[loc];
            Node next;
            if (z_or_o == 0)
                next = current.buildAndOrExploreZero();
            else if (z_or_o == 1)
                next = current.buildAndOrExploreOne();
            else
                throw new RuntimeException("Encoding requires 0 or 1 values. ");

            int ret = recursiveParse(integer, flag, next, encoding, loc+1);
            return ret;
        }
        else
        {
            //loc is the last value of the encoder
            //System.out.println(current.getLevel()); // should be 31
            switch (flag)
            {
                case ADD:
                    current.addInteger(integer);
                    this.parentTraverse(current, ADD);
                    this.inherentSort(false);
                    this.assumptionSort(false);
                    break;
                case REMOVE:
                    current.removeValue();
                    this.parentTraverse(current, REMOVE);
                    break;
                case CONTAINS:
                    return (current.contains(integer) ? 1 : 0);
            }
            return 1;
        }
    }

    /*
        For printing and extracting an array from this structure.
     */
    private void recursiveParse(int flag, Node current)
    {

        if(current.isActive()) {
            if (current.hasValue()) {
                if(flag==PRINT) {
                    for (int i = 0; i < current.getCount(); i++)
                        System.out.print(current.getValue() + " ");
                }
                if(flag==EXTRACT_ARRAY)
                {
                    for(int i=0;i<current.getCount();i++)
                    {
                        this.extractedArray[this.parseIndex]=current.getValue();
                        this.parseIndex++;
                    }
                }
            }
            else
            {
                if(current.hasZero())
                    recursiveParse(flag, current.getZero());
                if(current.hasOne())
                    recursiveParse(flag, current.getOne());
            }
        }
        else
            throw new RuntimeException("Parsed an Inactive Node. Some issue occurred. ");
    }
    /*
    Operations
    GET_IDX
    REMOVE_IDX

    OUTPUT
        IF GET_IDX
            Error if index Out of bounds
            value otherwise
        IF REMOVE_IDX
            Error if idx oob
            value otherwise
     */
    public int recursiveParseIDX(int num, int flag, Node current)
    {
        if(current.isActive())
        {
            if(current.hasValue())
            {
                switch (flag)
                {
                    case GET_IDX:
                        return current.getValue();
                    case REMOVE_IDX:
                        int removeVal = current.getValue();
                        current.removeValue();
                        this.size--;
                        this.parentTraverse(current, REMOVE);
                        return removeVal;
                }
            }
            else
            {
                int sizeZero = 0;
                int sizeOne =0;
                if(current.hasZero())
                    sizeZero = current.getZero().getChildrenAssignments();
                if(current.hasOne())
                    sizeOne = current.getOne().getChildrenAssignments();
                int sizeChildren = sizeZero + sizeOne;
                if( sizeChildren  < num)
                    throw new IndexOutOfBoundsException();
                if(sizeZero  >= num && current.hasZero())
                    return recursiveParseIDX(num, flag, current.getZero());
                else if(sizeOne  + sizeZero  >= num && current.hasOne())
                    return recursiveParseIDX(num - sizeZero, flag, current.getOne());
                else
                    throw new RuntimeException("Something broke. ");
            }
        }


        throw new RuntimeException("Not done yet. ");
    }
    /*
    Traverses upward to height 0 from a node and removes one child assignment over the set of nodes visited.
    FLAGS are add, remove
     */
    private void parentTraverse(Node current, int flag)
    {
        if(current != null)
        {
            if(flag == REMOVE)
                current.removeOneChildAssignment();
            if(flag == ADD)
                current.addChildAssignment();
        }
        else
            throw new RuntimeException("Null pointer");
        if(current.getParent() != null)
            parentTraverse(current.getParent(), flag);
    }
    /*
    Extracts an array from the Int Tree
    The extracted array is located in the array extractedArray
     */
    public int[] extractArray()
    {
        if(modifiedSinceBuilt) {
            extractedArray = new int[this.size];
            this.performOperations(this.EXTRACT_ARRAY);
            this.modifiedSinceBuilt=false;
        }
        return extractedArray;
    }

    public int getSize()
    {
        return this.size;
    }

    public void inherentSort(boolean print)
    {
        if(print) {
            System.out.println("Running Inherent Sort. ");
            //dot dot dot
            System.out.println("Working very hard to sort this..... brb");
            //more dots
            System.out.println("Done. ");
        }
    }
    public void assumptionSort(boolean print)
    {
        if(print) {
            System.out.println("\nRunning Assumption sort. ");
            //Also do nothing
            System.out.println("Oh my god it actually works. \n");
        }
    }
}
