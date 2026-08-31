/*
 * SharedCounterArrayGlobal_Object.java
 *
 * QUESTION 1.2 solution for SharedCounterArrayGlobal.
 * Instead of passing end/array as separate arguments (1.1), we now
 * bundle them into ONE shared structure/object (SharedData), and
 * pass just that ONE object as reference to every thread
 * (see reference-code/Shared/MainArgsObj.java for the pattern).
 *
 * NOTE: SharedData is declared as a "static class" because it is
 * nested inside the main class -- without "static" it would need an
 * instance of the outer class to exist, which main() (a static
 * method) does not have.
 *
 * NO mutex yet -- still produces WRONG results (errors > 0).
 */
public class SharedCounterArrayGlobal_Object {

    public static void main(String[] args) {

        int numThreads = 4;
        SharedData data = new SharedData(1000);   // (1) one object bundles end + array

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(numThreads, data);   // (2) pass numThreads + data
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(numThreads, data);
    }

    static void check_array(int numThreads, SharedData data) {
        int errors = 0;
        System.out.println("Checking...");
        for (int i = 0; i < data.end; i++) {
            if (data.array[i] != numThreads * i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, data.array[i], numThreads * i);
            }
        }
        System.out.println(errors + " errors.");
    }

    /* The shared structure: only data, no methods (struct-style, like
       C's struct) -- as opposed to the fully-OOP version in Step 2,
       which will have an embedded increment() method. */
    static class SharedData {
        int end;
        int[] array;

        public SharedData(int end) {
            this.end = end;
            this.array = new int[end];
        }
    }

    static class CounterThread extends Thread {

        int numThreads;    // primitive -> by value, doesn't need to be shared
        SharedData data;   // object -> by reference, shared

        public CounterThread(int numThreads, SharedData data) {
            this.numThreads = numThreads;
            this.data = data;
        }

        public void run() {
            for (int i = 0; i < data.end; i++) {
                for (int j = 0; j < i; j++)
                    data.array[i]++;   // access the field directly, no method
            }
        }
    }
}
