/*
 * SharedCounterArrayGlobalWhile_Object.java
 *
 * QUESTION 1.2 solution for SharedCounterArrayGlobalWhile.
 * Instead of separate arguments (1.1), we bundle end, counter, AND
 * array into ONE shared structure/object (SharedData), passed as a
 * single reference to every thread -- same idea as 03-Global-Object.
 *
 * Because "counter" now lives INSIDE the shared object (as a field),
 * it is automatically shared correctly -- no extra wrapper class
 * needed this time, unlike in 08-While-Args.
 *
 * NO mutex yet -- still produces WRONG results (errors > 0), same
 * double race condition as the original.
 */
public class SharedCounterArrayGlobalWhile_Object {

    public static void main(String[] args) {

        SharedData data = new SharedData(10000);   // bundles end + counter + array

        int numThreads = 4;
        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(data);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(data);
    }

    static void check_array(SharedData data) {
        int errors = 0;
        System.out.println("Checking...");
        for (int i = 0; i < data.end; i++) {
            if (data.array[i] != 1) {
                errors++;
                System.out.printf("%d: %d should be 1\n", i, data.array[i]);
            }
        }
        System.out.println(errors + " errors.");
    }

    /* the shared structure: only data, no methods */
    static class SharedData {
        int end;
        int counter;
        int[] array;

        public SharedData(int end) {
            this.end = end;
            this.counter = 0;
            this.array = new int[end];
        }
    }

    static class CounterThread extends Thread {

        SharedData data;

        public CounterThread(SharedData data) {
            this.data = data;
        }

        public void run() {
            while (true) {
                if (data.counter >= data.end)
                    break;
                data.array[data.counter]++;
                data.counter++;
            }
        }
    }
}
