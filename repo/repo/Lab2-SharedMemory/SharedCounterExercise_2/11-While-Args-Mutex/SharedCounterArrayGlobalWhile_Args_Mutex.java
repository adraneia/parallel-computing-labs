import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * SharedCounterArrayGlobalWhile_Args_Mutex.java
 *
 * STEP 2 solution for the ARGUMENTS (1.1) "While" version.
 * Same as 08-While-Args, protected with a Lock. Same
 * single-critical-section requirement as 10-While-Original-Mutex.
 *
 * Expected output: "0 errors." every single time.
 */
public class SharedCounterArrayGlobalWhile_Args_Mutex {

    public static void main(String[] args) {

        int end = 10000;
        int numThreads = 4;
        int[] array = new int[end];
        Counter counter = new Counter();
        Lock lock = new ReentrantLock();

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(end, array, counter, lock);
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

    /* wrapper so the int counter can be shared by reference */
    static class Counter {
        int value = 0;
    }

    static class CounterThread extends Thread {

        int end;
        int[] array;
        Counter counter;
        Lock lock;

        public CounterThread(int end, int[] array, Counter counter, Lock lock) {
            this.end = end;
            this.array = array;
            this.counter = counter;
            this.lock = lock;
        }

        public void run() {
            while (true) {
                lock.lock();
                try {
                    if (counter.value >= end) {
                        break;
                    }
                    array[counter.value]++;
                    counter.value++;
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
