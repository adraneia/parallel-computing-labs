import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionManager {
    
    private static ArrayList<PrintWriter> clients = new ArrayList<>(); 
    private static ReentrantLock lock = new ReentrantLock();

    //ADD καλειται οταν συνδεεται νεος client 
    public static void addClient(PrintWriter out){
        lock.lock();

        try{
            clients.add(out);
        }finally{
            lock.unlock();
        }
    }

    //REMOVE οταν αποσυνδεεται client 

    public static void removeClient(PrintWriter out)
    {
        lock.lock();
        try{
            clients.remove(out);
        }finally {
            lock.unlock();
        }
    }


    //BROADCAST  στελνει το μνμα σε ολους τους συνδεμενους clients 
    public static void broadcast(String message)
    {
        lock.lock();
        try{

            for (PrintWriter out: clients){
                out.println(message);
            }

            // for (int i = 0; i < clients.size(); i++) {
            //     PrintWriter out = clients.get(i);   // παίρνουμε το i-οστό στοιχείο
            //     out.println(message);
            // }

        }finally {
            lock.unlock();
        }
    }
}
// "Πάρε τη λίστα clients (όλοι οι συνδεδεμένοι). Για τον καθένα ξεχωριστά, 
// κάλεσε .println(message) πάνω του — δηλαδή στείλε του το μήνυμα."

// Αν έχεις 3 clients στη λίστα (Α, Β, Γ), αυτό το loop εκτελείται 3 φορές:
// 1η επανάληψη: out = ο PrintWriter του Α → στέλνει το μήνυμα στον Α
// 2η επανάληψη: out = ο PrintWriter του Β → στέλνει το μήνυμα στον Β
// 3η επανάληψη: out = ο PrintWriter του Γ → στέλνει το μήνυμα στον Γ


// ArrayList<Integer> numbers = new ArrayList<>();
// numbers.add(5);
// numbers.add(10);
// numbers.add(15);

// Τώρα η λίστα numbers περιέχει: [5, 10, 15]

// Αν γράψω:

// java
// for (Integer n : numbers) {
//     System.out.println(n);
// }

// Τι συμβαίνει, κυριολεκτικά:

// Το loop παίρνει το πρώτο στοιχείο της λίστας (5), το βάζει στη μεταβλητή n, και τρέχει το σώμα → τυπώνει 5
// Μετά παίρνει το δεύτερο στοιχείο (10), το βάζει στο n, τρέχει το σώμα ξανά → τυπώνει 10
// Μετά το τρίτο (15) → τυπώνει 15
// Η λίστα τελείωσε, το loop σταματάει