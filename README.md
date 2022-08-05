# IntTree 

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
     
