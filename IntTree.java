public class IntTree {

    

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
