import java.util.Arrays;
import java.util.Random;
public class Main {

    public static void main(String[] args) {




        //Test Params
        int numRuns=1;
        int arraySize = 10;
        int numericOriginPoint = 10;
        int numericSpanBound = 20;
        boolean printTestResults=true;

        //Run Tests
        runTests(numRuns, arraySize, numericOriginPoint, numericSpanBound, printTestResults);

    }
    public static void printArray(int[] toPrint)
    {
        if(toPrint.length>0)
            System.out.print("[ ");
        for( int i =0;i<toPrint.length;i++)
        {
            System.out.print(""+toPrint[i]);
            if(i<toPrint.length-1)
                System.out.print(", ");
        }
        System.out.println(" ]");
    }

    public static void printEncodingsTest()
    {
        Encoder e = new Encoder();
        printArray(e.encodeINT(Integer.MIN_VALUE));
        printArray(e.encodeINT(-3));
        printArray(e.encodeINT(-2));
        printArray(e.encodeINT(-1));
        printArray(e.encodeINT(0));
        printArray(e.encodeINT(1));
        printArray(e.encodeINT(2));
        printArray(e.encodeINT(3));
        printArray(e.encodeINT(Integer.MAX_VALUE));
    }

    public static void runTests(int numRuns, int size, int origin, int bound, boolean print) {

        if(size < 1 || numRuns < 1)
            throw new RuntimeException("Size value or Number of Tests isnt high enough. ");

        int numPassAddSort = 0;
        int numPassContains =0;
        int numPassRemove =0;
        int numPassRemoveIdx =0;

        Random r = new Random();
        for (int reps = 0; reps < numRuns; reps++) {
            IntTree tree = new IntTree();
            int[] original_array = new int[0];
            for (int i = 0; i < size; i++) {
                int randomInt = r.nextInt(bound);
                if (r.nextBoolean())
                    randomInt *= -1;
                randomInt += origin;
                original_array = Arrays.copyOf(original_array, original_array.length+1);
                original_array[i] = randomInt;
                tree.add(randomInt); //O(1) Sort.

                if(print) {
                    System.out.print("Added: "+ randomInt + "\nCurrent Array.\n\t ");
                    printArray(original_array);
                    System.out.print("Current IntTree: \n\t");
                    tree.print();
                    System.out.print("\n\n");
                }
            }
            if(print) {
                System.out.println("Original. Unsorted. ");
                printArray(original_array);
                System.out.println("Original. Sorted. ");
                Arrays.sort(original_array);
                printArray(original_array);
                System.out.println("IntTree. ");
                tree.print();
                System.out.println();
            }

            //Sort, Add
            if(arrayEquals(tree.extractArray(), original_array))
            {
                numPassAddSort++;
            }
            //Contains
            int numRunsContains = Math.max( size/2 , 1);
            int numP =0;
            for(int k = 0; k < numRunsContains; k++) {
                int randomSpot = r.nextInt(size);
                if(tree.contains(original_array[randomSpot]))
                    numP++;
            }
            if(numRunsContains == numP)
                numPassContains++;
            //Remove
            int randomRemoveLoc = r.nextInt(size-1);
            int removeVal = original_array[randomRemoveLoc];
            int[] cloneOriginalArray  = removeFromArray(original_array, removeVal);
            tree.remove(removeVal);
            if(arrayEquals(cloneOriginalArray, tree.extractArray()))
                numPassRemove++;
            int repsRemoveIdx = tree.getSize() /2;
            for(int l =0; l< repsRemoveIdx;l++)
                if(tree.getSize() > 0)
                {
                    int removeLoc = r.nextInt(tree.getSize());
                    int getValIdx = tree.getByIdx(removeLoc);
                    System.out.println("Removed: "+getValIdx);
                    int removeValIDX = tree.removeByIdx(removeLoc);
                    assert getValIdx == removeValIDX;
                    tree.print();
                }
        }
        if(print)
        {
            System.out.print("\n\n");
            calcAndPrintStats(numPassAddSort, numRuns, "Add and Sort");
            calcAndPrintStats(numPassContains, numRuns, "Contains");
            calcAndPrintStats(numPassRemove, numRuns, "Remove");
        }
    }
    public static boolean arrayEquals(int[] first, int[] second)
    {
        if(first.length != second.length)
            return false;
        for(int i=0;i<first.length;i++)
        {
            if(first[i] != second[i])
                return false;
        }
        return true;
    }
    public static void calcAndPrintStats(int numPass, int numRuns, String type)
    {
        System.out.println("Type of Test: "+type);
        System.out.println("\tNumber of Tests Ran: "+numRuns);
        System.out.println("\tNumber of Tests Passed: "+numPass);
        System.out.println("\tSuccess Percentage: "+((double)numPass/(double)numRuns)*100);
    }
    public static int[] removeFromArray(int[] array, int value)
    {
        int[] copy = new int[array.length-1];
        boolean found = false;
        int newIdx = 0;
        for(int i=0;i<array.length;i++)
        {
            if(array[i]==value && !found)
            {
                found =true;
            }
            else {
                copy[newIdx] = array[i];
                newIdx++;
            }
        }
        return copy;
    }
}
