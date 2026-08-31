/*
 * SharedCounterArrayGlobalWhile.java
 *
 * ORIGINAL starting file (the "While" variant). Uses GLOBAL (static)
 * variables. 4 threads share a common "counter": each thread grabs
 * the next index, increments array[counter], then advances counter.
 *
 * NO mutex -- this will produce WRONG results (errors > 0). This
 * has a DOUBLE race condition: on "counter" itself, AND on
 * array[counter].
 */
public class SharedCounterArrayGlobalWhile {

    static int end = 10000;
    static int counter = 0;
    static int[] array = new int[end];
    static int numThreads = 4;

    public static void main(String[] args) {

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread();
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array();
    }

    static void check_array() {
        int i, errors = 0;

        System.out.println("Checking...");

        for (i = 0; i < end; i++) {
            if (array[i] != 1) {
                errors++;
                System.out.printf("%d: %d should be 1\n", i, array[i]);
            }
        }
        System.out.println(errors + " errors.");
    }

    static class CounterThread extends Thread {

        public CounterThread() {
        }

        public void run() {

            while (true) {
                if (counter >= end)
                    break;
                array[counter]++;
                counter++;
            }
        }
    }
}
