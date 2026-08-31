/*
 * SharedCounterArrayGlobalWhile_Args.java
 *
 * QUESTION 1.1 solution for SharedCounterArrayGlobalWhile.
 * end/array/numThreads are passed as constructor arguments, same
 * idea as 02-Global-Args.
 *
 * IMPORTANT DIFFERENCE from the simpler Global version: "counter" is
 * a value that MUST be shared AND modified by all threads. A plain
 * "int counter" argument would be passed BY VALUE -- each thread
 * would get its OWN independent copy starting at 0, which is wrong
 * (every thread would then process the whole array on its own).
 * To fix this we wrap it in a tiny object (Counter), which is
 * passed BY REFERENCE, so all threads truly share the same counter.
 *
 * NO mutex yet -- still produces WRONG results (errors > 0), same
 * double race condition as the original.
 */
public class SharedCounterArrayGlobalWhile_Args {

    public static void main(String[] args) {

        int end = 10000;
        int numThreads = 4;
        int[] array = new int[end];
        Counter counter = new Counter();   // object wrapper -> shared by reference

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(end, counter, array);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(end, array);
    }

    static void check_array(int end, int[] array) {
        int errors = 0;
        System.out.println("Checking...");
        for (int i = 0; i < end; i++) {
            if (array[i] != 1) {
                errors++;
                System.out.printf("%d: %d should be 1\n", i, array[i]);
            }
        }
        System.out.println(errors + " errors.");
    }

    /* wrapper -- the only way to share a mutable int by reference */
    static class Counter {
        int value = 0;
    }

    static class CounterThread extends Thread {

        int end;
        Counter counter;   // reference to the SAME object in every thread
        int[] array;

        public CounterThread(int end, Counter counter, int[] array) {
            this.end = end;
            this.counter = counter;
            this.array = array;
        }

        public void run() {
            while (true) {
                if (counter.value >= end)
                    break;
                array[counter.value]++;
                counter.value++;
            }
        }
    }
}
