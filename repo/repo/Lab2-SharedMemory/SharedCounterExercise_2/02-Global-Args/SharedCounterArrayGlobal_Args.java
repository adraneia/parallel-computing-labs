/*
 * SharedCounterArrayGlobal_Args.java
 *
 * QUESTION 1.1 solution for SharedCounterArrayGlobal.
 * Instead of static/global variables, end/numThreads/array are now
 * LOCAL variables in main(), passed explicitly to each thread through
 * its CONSTRUCTOR (arguments technique -- see reference-code/Shared/
 * MainArguments.java).
 *
 * NO mutex yet -- still produces WRONG results (errors > 0), same
 * race condition as the original, just with a different way of
 * sharing the variables.
 */
public class SharedCounterArrayGlobal_Args {

    public static void main(String[] args) {

        int end = 1000;              // (1) now LOCAL variables, not static
        int numThreads = 4;
        int[] array = new int[end];

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(end, numThreads, array);  // (2) passed as arguments
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(end, numThreads, array);   // (3) passed explicitly here too
    }

    static void check_array(int end, int numThreads, int[] array) {
        int i, errors = 0;

        System.out.println("Checking...");

        for (i = 0; i < end; i++) {
            if (array[i] != numThreads * i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, array[i], numThreads * i);
            }
        }
        System.out.println(errors + " errors.");
    }

    static class CounterThread extends Thread {

        int end, numThreads;   // (4) instance variables -- each thread has its OWN copy
        int[] array;           // (5) instance variable that HOLDS A REFERENCE -> shared

        public CounterThread(int end, int numThreads, int[] array) {
            this.end = end;
            this.numThreads = numThreads;
            this.array = array;   // reference -> shared
        }

        public void run() {
            for (int i = 0; i < end; i++) {
                for (int j = 0; j < i; j++)
                    array[i]++;
            }
        }
    }
}
