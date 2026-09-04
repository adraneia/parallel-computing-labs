public class SharedCounterArrayGlobal_sol {

    public static void main(String[] args) {
        
        int end = 1000;  // (1) ΤΩΡΑ τοπικές μεταβλητές, όχι static
        int[] array = new int[end];
        int numThreads = 4;

		CounterThread threads[] = new CounterThread[numThreads];
		
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread(end, numThreads, array); // (2) περνάμε ως ορίσματα
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		check_array (end, numThreads, array); // (3) περνάμε και εδώ ρητά

    }
     
    static void check_array (int end, int numThreads, int[] array)  { //edw
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (array[i] != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, array[i], numThreads*i);
			}         
        }
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {

        //edw
        int end, numThreads; // (4) instance variables - κάθε thread έχει ΔΙΚΟ ΤΟΥ αντίγραφο
        int[] array;        // (5) instance variable ΠΟΥ ΚΡΑΤΑΕΙ REFERENCE
  	
        //olos o kataskevastis allazei 
       public CounterThread(int end, int numThreads, int[] array) {
            this.end = end;
            this.numThreads = numThreads;
            this.array = array;   // αναφορά -> shared
       }
  	
       public void run() {
  
            for (int i = 0; i < end; i++) {
				for (int j = 0; j < i; j++)
					array[i]++;		
            } 
		}            	
    }
}
// Παρατήρησε τη λεπτή διαφορά σε σχέση με το end/numThreads: 
// αυτά είναι primitives (int), άρα περνάνε by value — 
// κάθε thread παίρνει το δικό του αντίγραφο. 
// Δεν πειράζει, γιατί δεν χρειάζεται να είναι shared — 
// κάθε thread απλά χρειάζεται να ξέρει τις τιμές τους (read-only), 
// δεν τα τροποποιεί κανείς. 
// Το μόνο που χρειάζεται να είναι πραγματικά shared είναι το array[],
//  και αυτό είναι shared γιατί περνάει by reference (είναι αντικείμενο/πίνακας).


// Οι 5 σημαντικές λεπτομέρειες, μία-μία

//(1) Οι μεταβλητές έγιναν τοπικές στο main. Δεν είναι πια static — 
// ζουν μόνο μέσα στη μέθοδο main, στο δικό της stack frame.

//(2) Ρητό πέρασμα στον constructor. Αντί το CounterThread να "μαντεύει"/κοιτάζει έξω για τα δεδομένα, 
// το main του τα δίνει ρητά όταν το φτιάχνει: new CounterThread(end, numThreads, array).

//(3) Το check_array επίσης χρειάστηκε αλλαγή.
//  Αφού το end/numThreads/array δεν είναι πια static (προσβάσιμα από παντού μέσα στην κλάση), 
// έπρεπε να τα περάσουμε σαν παραμέτρους και σε αυτή τη μέθοδο.

//(4) Το end και το numThreads είναι primitives (int) — περνάνε by value. 
// Κάθε thread αντιγράφει τη δική του τιμή στο δικό του this.end/this.numThreads. 
// Αυτό είναι εντάξει εδώ, γιατί κανένα thread δεν χρειάζεται να αλλάξει αυτές τις τιμές — 
// απλά τις διαβάζει (πόσες επαναλήψεις να κάνει, πόσα threads υπάρχουν συνολικά). 
// Δεν χρειάζεται να είναι shared.

//5) Το array είναι πίνακας (αντικείμενο) — περνάει by reference. 
// Εδώ είναι η ουσία: όλα τα threads παίρνουν αναφορά στον ίδιο, 
// ένα και μοναδικό πίνακα που δημιουργήθηκε στο main. 
// Άρα όταν το thread 1 κάνει array[5]++, επηρεάζει τον ίδιο πίνακα που βλέπει και το thread 2, 3, 4. 
// Αυτό είναι το πραγματικά "μοιραζόμενο" κομμάτι — 
// ακριβώς όπως στο πρότυπο MainArguments.java που είδαμε στο reference-code.