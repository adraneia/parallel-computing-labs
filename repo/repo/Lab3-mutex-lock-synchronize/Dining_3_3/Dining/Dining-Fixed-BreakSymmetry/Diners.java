/*
 * Diners.java -- FIXED (only change: pass numphils to each
 * Philosopher's constructor, so it can detect whether it is the
 * "last" philosopher and reverse its fork-grabbing order --
 * see Philosopher.java for the actual deadlock fix).
 */
public class Diners {

    static final int numphils = 5;
    static final int sleeptime = 1;
    
    
     public static void main(String[] args) {
       Philosopher[] phil= new Philosopher[numphils];
       Fork[] fork = new Fork[numphils];

       for (int i =0; i<numphils; ++i)
            fork[i] = new Fork(i);
       for (int i =0; i<numphils; ++i){
            phil[i] = new Philosopher(i, (i+1)%numphils, sleeptime,
                        fork[i], fork[(i+1)%numphils], numphils);
            phil[i].start();
       }
    }

}
