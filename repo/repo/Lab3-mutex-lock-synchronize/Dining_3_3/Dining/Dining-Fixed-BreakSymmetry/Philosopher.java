class Philosopher extends Thread {
  private int identity;
  private Fork left;
  private Fork right;
  private int scale;
  private int next;
  private boolean reversedOrder;   // true only for the "odd one out"

  Philosopher(int id, int n, int s, Fork l, Fork r, int numphils) {
        identity = id; next = n; scale = s; left = l; right = r;
        reversedOrder = (id == numphils - 1);   // the last philosopher breaks the symmetry
  }

  public void run() {
     while (true) {
        //thinking
		System.out.println(" Philosopher "+ identity + " is thinking");
        delay(scale);
        //hungry

        if (reversedOrder) {
            // this philosopher grabs LEFT first, then RIGHT
            System.out.println(" Philosopher "+ identity+ " is trying to get fork " + next);
            left.get();
            System.out.println(" Philosopher "+ identity+ " got fork " + next + " and is trying to get fork " + identity);
            right.get();
        } else {
            // everyone else keeps the ORIGINAL order: RIGHT first, then LEFT
            System.out.println(" Philosopher "+ identity+ " is trying to get fork " + identity);
            right.get();
            System.out.println(" Philosopher "+ identity+ " got fork " + identity + " and is trying to get fork " + next);
            left.get();
        }

        System.out.println(" Philosopher "+ identity + " is eating");
        //eating
        System.out.println(" Philosopher "+ identity + " is releasing left fork " + next);
        //delay(scale);
        left.put();
		System.out.println(" Philosopher "+ identity + " is releasing fork " + identity);
		//delay(scale);
        right.put();
     }    
  }

  public void delay(int scale) {
	try {
		 sleep((int)(Math.random()*scale));
	} catch (InterruptedException e) { }
  }
}
/*
 * Philosopher.java -- FIXED to avoid deadlock.
 *
 * DEADLOCK CAUSE in the original: every philosopher grabs the SAME
 * relative fork first ("right" then "left"). If all 5 philosophers
 * grab their right fork at roughly the same time, each one is then
 * stuck waiting forever for their left fork, which is held by their
 * neighbor -- a circular wait, i.e. a deadlock.
 *
 * FIX (resource ordering / asymmetry): make ONE philosopher (the
 * "odd one out") grab forks in the OPPOSITE order from everyone
 * else. This breaks the circular wait: there can no longer be a
 * cycle where every philosopher is waiting on the next one, because
 * at least one link in the ring goes the "other way".
 *
 * Here we pick the LAST philosopher (identity == numphils - 1) to
 * grab LEFT first, then RIGHT -- everyone else keeps the original
 * RIGHT-then-LEFT order. We need to know the total number of
 * philosophers to detect "am I the last one?", so we added a
 * "numphils" parameter to the constructor.
 */