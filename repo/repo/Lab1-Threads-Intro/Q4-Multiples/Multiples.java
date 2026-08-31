/*
 * Multiples.java
 *
 * Creates 10 threads. Thread[i], i=0,1,...,9, computes and prints
 * the first 20 multiples of (i+1). E.g. thread 2 (i=2, multiplier=3)
 * prints:
 *   1 * 3 = 3
 *   2 * 3 = 6
 *   3 * 3 = 9
 *   ...
 *
 * The printouts happen concurrently, not sequentially.
 */
public class Multiples {

    public static void main(String[] args) {

        int numThreads = 10;
        int howMany = 20;   // how many multiples each thread prints

        Thread[] threads = new Thread[numThreads];

        System.out.println("In main: creating " + numThreads + " threads");

        /* create and start threads */
        for (int i = 0; i < numThreads; ++i) {
            threads[i] = new MultiplierThread(i + 1, howMany);
            threads[i].start();
        }

        /* wait for all threads to finish */
        for (int i = 0; i < numThreads; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }

        System.out.println("In main: all threads finished");
    }
}

/* thread that prints the first N multiples of a given number */
class MultiplierThread extends Thread {

    private int multiplier;   // the number whose multiples we print (i+1)
    private int howMany;      // how many multiples to print

    public MultiplierThread(int multiplier, int howMany) {
        this.multiplier = multiplier;
        this.howMany = howMany;
    }

    @Override
    public void run() {
        for (int k = 1; k <= howMany; ++k) {
            System.out.println(k + " * " + multiplier + " = " + (k * multiplier));
        }
    }
}
