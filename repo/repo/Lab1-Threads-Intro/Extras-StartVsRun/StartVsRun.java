/*
 * StartVsRun.java
 *
 * Demonstrates the difference between calling thread.start()
 * and calling thread.run() directly.
 *
 * The MyThread.run() method simulates "slow work" using a loop
 * with Thread.sleep(), so the difference in timing is visible.
 */
public class StartVsRun {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("===== TEST 1: calling start() =====");
        testStart();

        System.out.println();
        System.out.println("===== TEST 2: calling run() directly =====");
        testRunDirectly();
    }

    static void testStart() throws InterruptedException {
        MyThread t = new MyThread("A");

        System.out.println("Main: about to call t.start()");
        t.start();
        System.out.println("Main: t.start() returned immediately! Main keeps going...");

        for (int i = 1; i <= 3; i++) {
            System.out.println("Main: doing its own work, step " + i);
            Thread.sleep(300);
        }

        t.join();   // wait for the thread to finish before moving on
        System.out.println("Main: thread A has now finished (we joined it)");
    }

    static void testRunDirectly() throws InterruptedException {
        MyThread t = new MyThread("B");

        System.out.println("Main: about to call t.run() DIRECTLY (no start!)");
        t.run();   // <-- NOT start(): this just calls run() as a normal method
        System.out.println("Main: t.run() returned -- but only AFTER it fully finished!");

        for (int i = 1; i <= 3; i++) {
            System.out.println("Main: doing its own work, step " + i);
            Thread.sleep(300);
        }
    }
}

class MyThread extends Thread {

    private String label;

    public MyThread(String label) {
        this.label = label;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("  Thread " + label + " running, on " +
                                Thread.currentThread().getName() +
                                ", step " + i);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {}
        }
    }
}
